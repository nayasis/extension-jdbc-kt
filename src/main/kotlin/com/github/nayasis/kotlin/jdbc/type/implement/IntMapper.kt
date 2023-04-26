package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

object IntMapper: TypeMapper<Int> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Int) =
        statement.setInt(index, param)
    override fun getResult(resultSet: ResultSet, columnIndex: Int): Int? =
        resultSet.getInt(columnIndex)
    override fun getResult(statement: CallableStatement, columnIndex: Int): Int? =
        statement.getInt(columnIndex)
}