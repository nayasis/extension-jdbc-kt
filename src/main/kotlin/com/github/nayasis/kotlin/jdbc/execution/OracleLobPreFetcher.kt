package com.github.nayasis.kotlin.jdbc.execution

import oracle.jdbc.OracleStatement
import java.sql.Statement

class OracleLobPreFetcher {
    fun setPrefetchSize(statement: Statement, size: Int) {
        if( statement is OracleStatement) {
            statement.lobPrefetchSize = size
        }
    }
}