package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

class BooleanMapper: TypeMapper<Boolean> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Boolean) {
        statement.setBoolean(index, param)
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.BOOLEAN.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): Boolean {
        return resultSet.getBoolean(columnName)
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): Boolean {
        return resultSet.getBoolean(columnIndex)
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): Boolean {
        return statement.getBoolean(columnIndex)
    }
}