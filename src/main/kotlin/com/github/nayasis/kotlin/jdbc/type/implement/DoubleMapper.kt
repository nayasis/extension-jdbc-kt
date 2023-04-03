package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

class DoubleMapper: TypeMapper<Double> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Double) {
        statement.setDouble(index, param)
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.DOUBLE.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): Double? {
        return resultSet.getDouble(columnName)
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): Double? {
        return resultSet.getDouble(columnIndex)
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): Double? {
        return statement.getDouble(columnIndex)
    }
}