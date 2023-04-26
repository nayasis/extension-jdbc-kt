package com.github.nayasis.kotlin.jdbc.core

import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement
import javax.sql.DataSource
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class CoroutineDataSource(
    val dataSource: DataSource
): AbstractCoroutineContextElement(Key) {
    companion object Key: CoroutineContext.Key<CoroutineDataSource>
    override fun toString(): String = "DataSource in coroutine context($dataSource)"
}

class CoroutineConnection(
    val connection: Connection
): AbstractCoroutineContextElement(Key) {
    companion object Key: CoroutineContext.Key<CoroutineConnection>
    override fun toString(): String = "Connection in coroutine context($connection)"
}

class CoroutineStatement(
    val statement: Statement
): AbstractCoroutineContextElement(Key) {
    companion object Key: CoroutineContext.Key<CoroutineStatement>
    override fun toString(): String = "Statement in coroutine context($statement)"
}

class CoroutineResultSet(
    val resultSet: ResultSet
): AbstractCoroutineContextElement(Key) {
    companion object Key: CoroutineContext.Key<CoroutineResultSet>
    override fun toString(): String = "ResultSet in coroutine context($resultSet)"
}

val CoroutineContext.dataSource: DataSource
    get() = get(CoroutineDataSource.Key)?.dataSource ?: error("No data source in context")

val CoroutineContext.connection: Connection
    get() = get(CoroutineConnection.Key)?.connection ?: error("No connection in context")

val CoroutineContext.statement: Statement
    get() = get(CoroutineStatement.Key)?.statement ?: error("No statement in context")

val CoroutineContext.resultSet: ResultSet
    get() = get(CoroutineResultSet.Key)?.resultSet ?: error("No result set in context")
