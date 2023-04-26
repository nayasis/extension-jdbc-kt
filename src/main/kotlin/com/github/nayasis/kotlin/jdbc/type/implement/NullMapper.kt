package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.JdbcType
import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

object NullMapper: TypeMapper<Nothing> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Nothing) =
        statement.setNull(index, JdbcType.NULL.code)
    override fun getResult(resultSet: ResultSet, columnIndex: Int): Nothing? = null
    override fun getResult(statement: CallableStatement, columnIndex: Int): Nothing? = null
}