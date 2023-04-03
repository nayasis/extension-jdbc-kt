package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

class IntMapper: TypeMapper<Int> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Int) {
        statement.setInt(index, param)
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.INTEGER.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): Int? {
        return resultSet.getInt(columnName)
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): Int? {
        return resultSet.getInt(columnIndex)
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): Int? {
        return statement.getInt(columnIndex)
    }
}