package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

object ObjectMapper: TypeMapper<Any> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Any) =
        statement.setObject(index, param)
    override fun getResult(resultSet: ResultSet, columnIndex: Int): Any? =
        resultSet.getObject(columnIndex)
    override fun getResult(statement: CallableStatement, columnIndex: Int): Any? =
        statement.getObject(columnIndex)
}