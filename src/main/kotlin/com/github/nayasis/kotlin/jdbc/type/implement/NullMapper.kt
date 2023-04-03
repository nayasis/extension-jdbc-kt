package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

class NullMapper: TypeMapper<Nothing> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Nothing) {
        statement.setNull(index, JdbcType.NULL.code)
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.NULL.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): Nothing? {
        return null
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): Nothing? {
        return null
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): Nothing? {
        return null
    }
}