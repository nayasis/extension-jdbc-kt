package com.github.nayasis.kotlin.jdbc.execution

import java.sql.Statement

class StatementHandler(
    val statement: Statement,
    val query: String,
) {
}