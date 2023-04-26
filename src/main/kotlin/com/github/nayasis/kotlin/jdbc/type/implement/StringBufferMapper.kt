package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

object StringBufferMapper: TypeMapper<StringBuffer> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: StringBuffer) =
        statement.setString(index, param.toString())
    override fun getResult(resultSet: ResultSet, columnIndex: Int): StringBuffer? =
        resultSet.getString(columnIndex)?.let{ StringBuffer(it)}
    override fun getResult(statement: CallableStatement, columnIndex: Int): StringBuffer? =
        statement.getString(columnIndex)?.let{ StringBuffer(it)}
}