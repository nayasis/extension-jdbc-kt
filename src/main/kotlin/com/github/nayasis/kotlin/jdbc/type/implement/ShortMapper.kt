package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

class ShortMapper: TypeMapper<Short> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Short) {
        statement.setShort(index, param)
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.BIGINT.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): Short? {
        return resultSet.getShort(columnName)
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): Short? {
        return resultSet.getShort(columnIndex)
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): Short? {
        return statement.getShort(columnIndex)
    }
}