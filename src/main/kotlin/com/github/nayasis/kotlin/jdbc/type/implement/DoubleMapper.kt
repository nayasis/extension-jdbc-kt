package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

object DoubleMapper: TypeMapper<Double> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Double) =
        statement.setDouble(index, param)
    override fun getResult(resultSet: ResultSet, columnIndex: Int): Double? =
        resultSet.getDouble(columnIndex)
    override fun getResult(statement: CallableStatement, columnIndex: Int): Double? =
        statement.getDouble(columnIndex)
}