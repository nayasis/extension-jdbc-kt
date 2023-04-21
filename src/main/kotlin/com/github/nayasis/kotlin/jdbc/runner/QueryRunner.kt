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

    fun update(): Deferred<Long> {
        logger.trace { query }
        connection.prepareStatement(query.preparedQuery).use { statement ->
            setParameter(statement, query.preparedParams)
            statement.executeLargeUpdate()
        }
        return connection.prepareStatement(query.preparedQuery).use { statement ->
            setParameter(statement, query.preparedParams)
            async {
                statement.executeLargeUpdate()
            }
        }
    }

    fun call(): Deferred<CallResult> {
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
            }
        }
    }

    /**
     * retrieve for handling **ResultSet** directly
     *
     * @param fetchSize Int?    row fetch size
     * @param lobFetchSize Int? LOB data prefetch size (works on ORACLE only)
     */
    fun retrieve(fetchSize: Int? = null, lobFetchSize: Int? = null): Deferred<ResultSet> {
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
            setParameter(statement, query.preparedParams)
            return async {
                statement.executeQuery().use { rset -> rset.use { rset } }
            }
        }
    }

    fun get(): Deferred<Map<String,Any?>> {
        return async {
            retrieve(null, null).await().let { get(it) }
        }
    }

    inline fun <reified T> getAs(): Deferred<T> {
        return async { get().await().toObject() }
    }

    inline fun <reified T> getValue(): Deferred<T?> {
        return async {
            retrieve(null, null).await().let { monoFirst(it) as T? }
        }
    }

    fun getAll(fetchSize: Int? = null, lobFetchSize: Int? = null): Flow<Map<String,Any?>> {
        return flow {
            retrieve(fetchSize, lobFetchSize).await().let { getAll(it) }
        }
    }

    inline fun <reified T> getAllAs(fetchSize: Int? = null, lobFetchSize: Int? = null): Flow<T> {
        return getAll(fetchSize, lobFetchSize).map { it.toObject() }
    }

    inline fun <reified T> getValues(fetchSize: Int? = null, lobFetchSize: Int? = null): Flow<T?> {
        return flow {
            retrieve(fetchSize, lobFetchSize).await().let { fluxFirst(it) }
        }
    }

    fun getAllToNGrid(fetchSize: Int? = null, lobFetchSize: Int? = null): Deferred<NGrid> {
        return async {
            val grid = NGrid()
            retrieve(fetchSize, lobFetchSize).await().let { rset ->
                val header = Header(rset)
                grid.header.addAll(header.keys)
                getAll(rset).collect {
                    grid.addRow(it)
                }
            }
            grid
        }
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