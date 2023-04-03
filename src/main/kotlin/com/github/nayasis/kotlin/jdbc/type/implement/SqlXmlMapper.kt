package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLXML

class SqlXmlMapper: TypeMapper<String> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: String) {
        val sqlxml = statement.connection.createSQLXML()
        try {
            sqlxml.string = param
            statement.setSQLXML(index, sqlxml)
        } finally {
            sqlxml.free()
        }
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.SQLXML.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): String? {
        return resultSet.getSQLXML(columnName)?.let { toString(it) }
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): String? {
        return resultSet.getSQLXML(columnIndex)?.let { toString(it) }
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): String? {
        return statement.getSQLXML(columnIndex)?.let { toString(it) }
    }

    private fun toString(sqlxml: SQLXML?): String? {
        try {
            return sqlxml?.string
        } finally {
            sqlxml?.free()
        }
    }
}