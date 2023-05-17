package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.*

object DateMapper: TypeMapper<Date> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Date) =
        statement.setDate(index, java.sql.Date(param.time))
    override fun getResult(resultSet: ResultSet, columnIndex: Int): Date? =
        resultSet.getDate(columnIndex)
    override fun getResult(statement: CallableStatement, columnIndex: Int): Date? =
        statement.getDate(columnIndex)
}