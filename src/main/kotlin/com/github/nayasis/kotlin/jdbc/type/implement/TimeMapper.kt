package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Time
import java.util.*

class TimeMapper: TypeMapper<Date> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Date) {
        statement.setTime(index, Time(param.time))
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.TIME.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): Date? {
        return resultSet.getTime(columnName)?.let { Date(it.time) }
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): Date? {
        return resultSet.getTime(columnIndex)?.let { Date(it.time) }
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): Date? {
        return statement.getTime(columnIndex)?.let { Date(it.time) }
    }
}