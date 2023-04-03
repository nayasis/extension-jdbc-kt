package com.github.nayasis.kotlin.jdbc.type.implement

import JdbcType
import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.io.ByteArrayInputStream
import java.sql.Blob
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

class BlobMapper: TypeMapper<ByteArray> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: ByteArray) {
        statement.setBinaryStream(index, ByteArrayInputStream(param), param.size )
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.BLOB.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): ByteArray? {
        return getBytes(resultSet.getBlob(columnName))
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): ByteArray? {
        return getBytes(resultSet.getBlob(columnIndex))
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): ByteArray? {
        return getBytes(statement.getBlob(columnIndex))
    }

    private fun getBytes(blob: Blob?): ByteArray? {
        return blob?.getBytes(1, blob.length().toInt())
    }
}