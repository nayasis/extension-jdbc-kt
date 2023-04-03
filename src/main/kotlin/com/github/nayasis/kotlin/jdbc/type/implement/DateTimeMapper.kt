package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.*

class DateTimeMapper: TypeMapper<Date> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Date) {
        statement.setDate(index, java.sql.Date(param.time))
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.DATETIME.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): Date? {
        return resultSet.getDate(columnName)
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): Date? {
        return resultSet.getDate(columnIndex)
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): Date? {
        return statement.getDate(columnIndex)
    }
}