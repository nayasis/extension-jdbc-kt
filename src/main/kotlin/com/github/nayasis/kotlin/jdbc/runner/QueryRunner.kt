package com.github.nayasis.kotlin.jdbc.runner

import com.github.nayasis.kotlin.basica.core.collection.toObject
import com.github.nayasis.kotlin.basica.core.validator.Types
import com.github.nayasis.kotlin.basica.model.NGrid
import com.github.nayasis.kotlin.basica.reflection.Reflector
import com.github.nayasis.kotlin.jdbc.query.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import mu.KotlinLogging
import java.sql.Connection
import java.sql.ResultSet
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

private val logger = KotlinLogging.logger {}
private var supportOracleLobPreFetcher = true

typealias ResultSetHandler<T> = (rset: ResultSet, header: Header) -> T

class QueryRunner(
    private val query: Query,
    private val connection: Connection,
    override val coroutineContext: CoroutineContext = Dispatchers.IO,
): CoroutineScope  {

    suspend fun execute(): Long {
        logger.trace { query }
        return async { connection.prepareStatement(query.preparedQuery).use { statement ->
            setParameter(statement, query.preparedParams)
            try {
                statement.executeLargeUpdate()
            } catch (e: Exception) {
                logger.error { "Binding Parameters\n  - ${query.preparedParams.map { it.value }}" }
                throw e
            }
        }}.await()
    }

    suspend fun call(): CallResult {
        logger.trace { query }
        return async { connection.prepareCall(query.preparedQuery).use { statement ->
            val outParams = setParameter(statement, query.preparedParams)
            val outs      = mutableMapOf<String,Any?>()
            val returns   = mutableListOf<List<Map<String,Any?>>>()
            if(statement.execute()) {
                outParams?.forEach { (idx, param ) ->
                    val rs = param.jdbcType.mapper.getResult(statement,idx)
                    outs[param.key] = if(rs is ResultSet) toList(rs) else rs
                }
                statement.resultSet?.let {
                    returns.add( toList(statement.resultSet) )
                }
                while(statement.moreResults) {
                    returns.add( toList(statement.resultSet) )
                }
            }
            CallResult(outs,returns)
        }}.await()
    }

    /**
     * retrieve for handling **ResultSet** directly
     *
     * @param fetchSize Int?    row fetch size
     * @param lobFetchSize Int? LOB data prefetch size (works on ORACLE only)
     */
    fun <T: Any?> asFlow(fetchSize: Int? = null, lobFetchSize: Int? = null, maxRows: Int? = null, headerHandler: ((header: Header) -> Unit)? = null, resultSetHandler: ResultSetHandler<T>): Flow<T> {
        logger.trace { query }
        return flow { connection.prepareStatement(query.preparedQuery).use { statement ->
            fetchSize?.let { statement.fetchSize = it }
            lobFetchSize?.let {
                if (supportOracleLobPreFetcher) {
                    try {
                        OracleLobPreFetcher().setPrefetchSize(statement, it)
                    } catch (e: Exception) {
                        supportOracleLobPreFetcher = false
                    }
                }
            }
            maxRows?.let { statement.maxRows = it }
            setParameter(statement, query.preparedParams)
            statement.executeQuery().use { rset ->
                try {
                    val header = Header(rset)
                    headerHandler?.invoke(header)
                    while (rset.next()) {
                        emit(resultSetHandler.invoke(rset, header))
                    }
                } finally {
                    runCatching { if (!rset.isClosed) rset.close() }
                }
            }
        }}
    }

    suspend fun get(): Map<String,Any?>? {
        return asFlow(maxRows = 1) { rset, header -> toMap(rset, header)  }.firstOrNull()
    }

    suspend inline fun <reified T> getAs(): T? {
        @Suppress("UNCHECKED_CAST")
        val typeKlass = T::class as KClass<Any>
        return if(isSupportedPrimitive(typeKlass)) {
            asFlow(maxRows = 1) { rset, header -> getFirstColumnValue(rset, header)  }.firstOrNull()?.let {
                Types.cast(it, typeKlass) as T?
            }
        } else {
            get()?.toObject()
        }
    }

    fun getAll(fetchSize: Int? = null, lobFetchSize: Int? = null): Flow<Map<String,Any?>> {
        return asFlow(fetchSize, lobFetchSize) { rset, header -> toMap(rset, header)  }
    }

    inline fun <reified T> getAllAs(fetchSize: Int? = null, lobFetchSize: Int? = null): Flow<T> {
        @Suppress("UNCHECKED_CAST")
        val typeKlass = T::class as KClass<Any>
        return if(isSupportedPrimitive(typeKlass)) {
            asFlow(fetchSize, lobFetchSize)  { rset, header -> getFirstColumnValue(rset, header).let { Types.cast(it, typeKlass) } as T }
        } else {
            getAll(fetchSize, lobFetchSize).map {Reflector.toObject(it, typeKlass) as T }
        }
    }

    suspend fun getAllToNGrid(fetchSize: Int? = null, lobFetchSize: Int? = null): NGrid {
        val grid = NGrid()
        asFlow(fetchSize, lobFetchSize, headerHandler = { header -> grid.header.addAll(header.keys) }) { rset, header ->
            toMap(rset, header)
        }.collect {
            grid.addRow(it)
        }
        return grid
    }

}

data class CallResult(
    val out: Map<String,Any?>,
    val returns: List<List<Map<String,Any?>>>,
)

fun Query.runner(
    connection: Connection,
    coroutineContext: CoroutineContext = Dispatchers.IO,
): QueryRunner = QueryRunner(this, connection, coroutineContext)