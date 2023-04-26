package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

object LongMapper: TypeMapper<Long> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Long) =
        statement.setLong(index, param)
    override fun getResult(resultSet: ResultSet, columnIndex: Int): Long? =
        resultSet.getLong(columnIndex)
    override fun getResult(statement: CallableStatement, columnIndex: Int): Long? =
        statement.getLong(columnIndex)
}