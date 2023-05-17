package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

object FloatMapper: TypeMapper<Float> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Float) =
        statement.setFloat(index, param)
    override fun getResult(resultSet: ResultSet, columnIndex: Int): Float? =
        resultSet.getFloat(columnIndex)
    override fun getResult(statement: CallableStatement, columnIndex: Int): Float? =
        statement.getFloat(columnIndex)
}