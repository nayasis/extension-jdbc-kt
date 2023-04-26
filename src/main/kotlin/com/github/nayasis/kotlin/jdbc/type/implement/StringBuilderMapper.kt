package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

object StringBuilderMapper: TypeMapper<StringBuilder> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: StringBuilder) =
        statement.setString(index, param.toString())
    override fun getResult(resultSet: ResultSet, columnIndex: Int): StringBuilder? =
        resultSet.getString(columnIndex)?.let{ StringBuilder(it)}
    override fun getResult(statement: CallableStatement, columnIndex: Int): StringBuilder? =
        statement.getString(columnIndex)?.let{ StringBuilder(it)}
}