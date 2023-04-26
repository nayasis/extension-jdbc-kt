package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

object StringMapper: TypeMapper<String> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: String) =
        statement.setString(index, param)
    override fun getResult(resultSet: ResultSet, columnIndex: Int): String? =
        resultSet.getString(columnIndex)
    override fun getResult(statement: CallableStatement, columnIndex: Int): String? =
        statement.getString(columnIndex)
}