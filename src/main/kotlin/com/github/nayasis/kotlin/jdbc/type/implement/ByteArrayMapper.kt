package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

object ByteArrayMapper: TypeMapper<ByteArray> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: ByteArray) =
        statement.setBytes(index, param)
    override fun getResult(resultSet: ResultSet, columnIndex: Int): ByteArray? =
        resultSet.getBytes(columnIndex)
    override fun getResult(statement: CallableStatement, columnIndex: Int): ByteArray? =
        statement.getBytes(columnIndex)
}