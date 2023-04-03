package com.github.nayasis.kotlin.jdbc.type

import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

interface TypeMapper<T> {

    @Throws(SQLException::class)
    fun setParameter(statement: PreparedStatement, index: Int, param: T)

    @Throws(SQLException::class)
    fun setOutParameter(statement: CallableStatement, index: Int)

    @Throws(SQLException::class)
    fun getResult(resultSet: ResultSet, columnName: String): T?

    @Throws(SQLException::class)
    fun getResult(resultSet: ResultSet, columnIndex: Int): T?

    @Throws(SQLException::class)
    fun getResult(statement: CallableStatement, columnIndex: Int): T?

}