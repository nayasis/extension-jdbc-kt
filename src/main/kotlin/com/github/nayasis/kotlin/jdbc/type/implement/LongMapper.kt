package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

class LongMapper: TypeMapper<Long> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Long) {
        statement.setLong(index, param)
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.BIGINT.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): Long? {
        return resultSet.getLong(columnName)
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): Long? {
        return resultSet.getLong(columnIndex)
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): Long? {
        return statement.getLong(columnIndex)
    }
}