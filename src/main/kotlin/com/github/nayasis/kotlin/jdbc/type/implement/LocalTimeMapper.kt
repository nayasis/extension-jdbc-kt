package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Time
import java.time.LocalTime

object LocalTimeMapper: TypeMapper<LocalTime> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: LocalTime) =
        statement.setTime(index, Time.valueOf(param))
    override fun getResult(resultSet: ResultSet, columnIndex: Int): LocalTime? =
        resultSet.getTime(columnIndex)?.toLocalTime()
    override fun getResult(statement: CallableStatement, columnIndex: Int): LocalTime? =
        statement.getTime(columnIndex)?.toLocalTime()
}