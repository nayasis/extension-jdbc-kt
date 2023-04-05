package com.github.nayasis.kotlin.jdbc.execution

import com.github.nayasis.kotlin.jdbc.query.Query
import mu.KotlinLogging
import java.sql.Connection

private val logger = KotlinLogging.logger {}

class QueryExecutor(
    val connection: Connection,
    val query: Query,
) {

    fun retrieve(fetchSize: Int? = null) {

        logger.trace { query.toString() }

        val statement = connection.prepareStatement(query.toPreparedString())

        for(param in query.toPreparedParams()) {
            param.jdbcType.mapper.setParameter(statement, 0, param.value)
        }

        statement.executeQuery()


    }


}