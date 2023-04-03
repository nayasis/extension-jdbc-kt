package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

class ByteArrayMapper: TypeMapper<ByteArray> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: ByteArray) {
        statement.setBytes(index, param)
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.BLOB.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): ByteArray? {
        return resultSet.getBytes(columnName)
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): ByteArray? {
        return resultSet.getBytes(columnIndex)
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): ByteArray? {
        return statement.getBytes(columnIndex)
    }
}