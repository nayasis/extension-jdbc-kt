package com.github.nayasis.kotlin.jdbc.runner

import com.github.nayasis.kotlin.jdbc.query.BatchQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import mu.KotlinLogging
import java.sql.Connection
import kotlin.coroutines.CoroutineContext

private val logger = KotlinLogging.logger {}

class BatchQueryRunner(
    private val query: BatchQuery,
    private val connection: Connection,
    override val coroutineContext: CoroutineContext = Dispatchers.IO,
): CoroutineScope {

    suspend fun execute(commitCount: Int? = null): Long {
        logger.trace { query }
        return async {
            var count = 0L
            connection.prepareStatement(query.preparedQuery).use { statement ->
                query.params.forEach { param ->
                    count++
                    setParameter(statement, param)
                    statement.addBatch()
                    statement.clearParameters()
                    commitCount?.let {
                        if(count % it == 0L) {
                            statement.executeLargeBatch()
                            statement.clearBatch()
                            connection.commit()
                        }
                    }
                }
                if(commitCount != null) {
                    if(count % commitCount != 0L) {
                        statement.executeLargeBatch()
                        statement.clearBatch()
                        connection.commit()
                    }
                } else {
                    statement.executeLargeBatch()
                    statement.clearBatch()
                }
            }
            count
        }.await()
    }

}

fun BatchQuery.batchRunner(
    connection: Connection,
    coroutineContext: CoroutineContext = Dispatchers.IO,
): BatchQueryRunner = BatchQueryRunner(this, connection, coroutineContext)