package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.net.URL
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

object UrlMapper: TypeMapper<URL> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: URL) =
        statement.setURL(index, param)
    override fun getResult(resultSet: ResultSet, columnIndex: Int): URL? =
        resultSet.getURL(columnIndex)
    override fun getResult(statement: CallableStatement, columnIndex: Int): URL? =
        statement.getURL(columnIndex)
}