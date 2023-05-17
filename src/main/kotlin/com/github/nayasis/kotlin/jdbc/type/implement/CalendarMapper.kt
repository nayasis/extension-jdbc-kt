package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.basica.core.localdate.toCalendar
import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.*

object CalendarMapper: TypeMapper<Calendar> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Calendar) =
        statement.setDate(index, java.sql.Date(param.timeInMillis))
    override fun getResult(resultSet: ResultSet, columnIndex: Int): Calendar? =
        resultSet.getDate(columnIndex)?.let { it.toCalendar() }
    override fun getResult(statement: CallableStatement, columnIndex: Int): Calendar? =
        statement.getDate(columnIndex)?.let { it.toCalendar() }
}