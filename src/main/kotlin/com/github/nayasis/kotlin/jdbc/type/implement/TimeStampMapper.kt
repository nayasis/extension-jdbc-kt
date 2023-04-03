package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.util.*

class TimeStampMapper: TypeMapper<Date> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Date) {
        statement.setTimestamp(index, Timestamp(param.time))
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.TIMESTAMP.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): Date? {
        return resultSet.getTimestamp(columnName)?.let { Date(it.time) }
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): Date? {
        return resultSet.getTimestamp(columnIndex)?.let { Date(it.time) }
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): Date? {
        return statement.getTimestamp(columnIndex)?.let { Date(it.time) }
    }
}