package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.io.StringReader
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

class NClobMapper: TypeMapper<String> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: String) {
        statement.setCharacterStream(index, StringReader(param), param.length)
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.TINYINT.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): String? {
        return resultSet.getClob(columnName)?.let{ it.getSubString(1, it.length().toInt())}
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): String? {
        return resultSet.getClob(columnIndex)?.let{ it.getSubString(1, it.length().toInt())}
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): String? {
        return statement.getClob(columnIndex)?.let{ it.getSubString(1, it.length().toInt())}
    }
}