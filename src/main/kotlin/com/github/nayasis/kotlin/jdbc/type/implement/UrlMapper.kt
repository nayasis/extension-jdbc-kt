package com.github.nayasis.kotlin.jdbc.type.implement

import JdbcType
import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.net.URL
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

class UrlMapper: TypeMapper<URL> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: URL) {
        statement.setURL(index, param)
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.DATALINK.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): URL? {
        return resultSet.getURL(columnName)
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): URL? {
        return resultSet.getURL(columnIndex)
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): URL? {
        return statement.getURL(columnIndex)
    }
}