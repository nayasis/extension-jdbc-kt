package com.github.nayasis.kotlin.jdbc.runner

import com.github.nayasis.kotlin.basica.core.collection.toObject
import com.github.nayasis.kotlin.basica.model.NGrid
import com.github.nayasis.kotlin.jdbc.query.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import mu.KotlinLogging
import java.sql.Connection
import java.sql.ResultSet
import kotlin.coroutines.CoroutineContext

private val logger = KotlinLogging.logger {}
private var supportOracleLobPreFetcher = true

class QueryRunner(
    private val connection: Connection,
    private val query: Query,
    override val coroutineContext: CoroutineContext = Dispatchers.IO,
): CoroutineScope  {

    suspend fun update(): Long {
        logger.trace { query }
        connection.prepareStatement(query.preparedQuery).use { statement ->
            setParameter(statement, query.preparedParams)
            statement.executeLargeUpdate()
        }
        return connection.prepareStatement(query.preparedQuery).use { statement ->
            setParameter(statement, query.preparedParams)
            async {
                try {
                    statement.executeLargeUpdate()
                } catch (e: Exception) {
                    logger.error { "Binding Parameters\n  - ${query.preparedParams.map { it.value }}" }
                    throw e
                }
            }.await()
        }
    }

    suspend fun call(): CallResult {
        logger.trace { query }
        connection.prepareCall(query.preparedQuery).use { statement ->
            val outParams = setParameter(statement, query.preparedParams)
            return async {
                val ans  = mutableMapOf<String,Any?>()
                val rtns = mutableListOf<List<Map<String,Any?>>>()
                if(statement.execute()) {
                    outParams?.forEach { (idx, param ) ->
                        val rs = param.jdbcType.mapper.getResult(statement,idx)
                        ans[param.key] = if(rs is ResultSet) toList(rs) else rs
                    }

                    while(statement.moreResults) {
                        rtns.add( toList(statement.resultSet) )
                    }
                }
                CallResult(ans,rtns)
            }.await()
        }
    }

    /**
     * retrieve for handling **ResultSet** directly
     *
     * @param fetchSize Int?    row fetch size
     * @param lobFetchSize Int? LOB data prefetch size (works on ORACLE only)
     */
    suspend fun retrieve(fetchSize: Int? = null, lobFetchSize: Int? = null, maxRows: Int? = null): ResultSet {
        logger.trace { query }
        connection.prepareStatement(query.preparedQuery).use { statement ->
            fetchSize?.let { statement.fetchSize = it }
            lobFetchSize?.let {
                if( supportOracleLobPreFetcher ) {
                    try {
                        OracleLobPreFetcher().setPrefetchSize(statement, it)
                    } catch (e: Exception) {
                        supportOracleLobPreFetcher = false
                    }
                }
            }
            maxRows?.let { statement.maxRows = it }
            setParameter(statement, query.preparedParams)
            return async {
                statement.executeQuery()
            }.await()
        }
    }

    suspend fun get(): Map<String, Any?> {
        return retrieve(null, null).let { get(it) }
    }

    suspend inline fun <reified T> getAs(): T {
        return get().toObject()
    }

    suspend inline fun <reified T> getValue(): T? {
        return retrieve(null, null).let { monoFirst(it) as T? }
    }

    suspend fun getAll(fetchSize: Int? = null, lobFetchSize: Int? = null): Flow<Map<String,Any?>> {
        return retrieve(fetchSize, lobFetchSize).let {
            getAll(it)
        }
    }

    suspend inline fun <reified T> getAllAs(fetchSize: Int? = null, lobFetchSize: Int? = null): Flow<T> {
        return getAll(fetchSize, lobFetchSize).map { it.toObject() }
    }

    suspend inline fun <reified T> getValues(fetchSize: Int? = null, lobFetchSize: Int? = null): Flow<T?> {
        return retrieve(fetchSize, lobFetchSize).let { fluxFirst(it) }
    }

    suspend fun getAllToNGrid(fetchSize: Int? = null, lobFetchSize: Int? = null): NGrid {
        return async {
            val grid = NGrid()
            retrieve(fetchSize, lobFetchSize).let { rset ->
                val header = Header(rset)
                grid.header.addAll(header.keys)
                getAll(rset).collect {
                    grid.addRow(it)
                }
            }
            grid
        }.await()
    }

}

data class CallResult(
    val out: Map<String,Any?>,
    val returns: List<List<Map<String,Any?>>>,
)

fun Query.runner(
    connection: Connection,
    coroutineContext: CoroutineContext = Dispatchers.IO,
): QueryRunner = QueryRunner(connection, this,coroutineContext)