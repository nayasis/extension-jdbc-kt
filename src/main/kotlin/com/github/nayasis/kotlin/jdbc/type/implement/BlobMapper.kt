package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.io.ByteArrayInputStream
import java.sql.Blob
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

object BlobMapper: TypeMapper<ByteArray> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: ByteArray) =
        statement.setBinaryStream(index, ByteArrayInputStream(param), param.size)
    override fun getResult(resultSet: ResultSet, columnIndex: Int): ByteArray? =
        getBytes(resultSet.getBlob(columnIndex))
    override fun getResult(statement: CallableStatement, columnIndex: Int): ByteArray? =
        getBytes(statement.getBlob(columnIndex))
    private fun getBytes(blob: Blob?): ByteArray? =
        blob?.getBytes(1, blob.length().toInt())
}