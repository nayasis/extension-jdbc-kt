package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

object BooleanMapper: TypeMapper<Boolean> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Boolean) =
        statement.setBoolean(index, param)
    override fun getResult(resultSet: ResultSet, columnIndex: Int): Boolean =
        resultSet.getBoolean(columnIndex)
    override fun getResult(statement: CallableStatement, columnIndex: Int): Boolean =
        statement.getBoolean(columnIndex)
}