package com.github.nayasis.kotlin.jdbc.type.implement

import JdbcType
import com.github.nayasis.kotlin.basica.core.string.decodeBase64
import com.github.nayasis.kotlin.basica.core.string.encodeBase64
import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.RowId

class RowIdMapper: TypeMapper<String> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: String) {
        statement.setBytes(index, param.decodeBase64())
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.ROWID.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): String? {
        return resultSet.getRowId(columnName)?.let { toString(it) }
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): String? {
        return resultSet.getRowId(columnIndex)?.let { toString(it) }
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): String? {
        return statement.getRowId(columnIndex)?.let { toString(it) }
    }

    private fun toString(rowid: RowId?): String? {
        return rowid?.bytes?.encodeBase64()
    }
}