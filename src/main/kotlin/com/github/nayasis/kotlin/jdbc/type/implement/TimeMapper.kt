package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Time
import java.util.*

object TimeMapper: TypeMapper<Date> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Date) =
        statement.setTime(index, Time(param.time))
    override fun getResult(resultSet: ResultSet, columnIndex: Int): Date? =
        resultSet.getTime(columnIndex)?.let { Date(it.time) }
    override fun getResult(statement: CallableStatement, columnIndex: Int): Date? =
        statement.getTime(columnIndex)?.let { Date(it.time) }
}