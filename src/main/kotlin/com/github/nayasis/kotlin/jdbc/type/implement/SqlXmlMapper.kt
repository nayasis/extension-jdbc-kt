package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLXML

object SqlXmlMapper: TypeMapper<String> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: String) {
        val sqlxml = statement.connection.createSQLXML()
        try {
            sqlxml.string = param
            statement.setSQLXML(index, sqlxml)
        } finally {
            sqlxml.free()
        }
    }
    override fun getResult(resultSet: ResultSet, columnIndex: Int): String? =
        resultSet.getSQLXML(columnIndex)?.let { toString(it) }
    override fun getResult(statement: CallableStatement, columnIndex: Int): String? =
        statement.getSQLXML(columnIndex)?.let { toString(it) }
    private fun toString(sqlxml: SQLXML?): String? =
        try { sqlxml?.string } finally { sqlxml?.free() }
}