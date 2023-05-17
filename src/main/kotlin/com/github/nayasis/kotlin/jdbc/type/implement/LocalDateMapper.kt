package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.basica.core.localdate.toLong
import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.LocalDate

object LocalDateMapper: TypeMapper<LocalDate> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: LocalDate) =
        statement.setDate(index, java.sql.Date(param.toLong()))
    override fun getResult(resultSet: ResultSet, columnIndex: Int): LocalDate? =
        resultSet.getDate(columnIndex)?.toLocalDate()
    override fun getResult(statement: CallableStatement, columnIndex: Int): LocalDate? =
        statement.getDate(columnIndex)?.toLocalDate()
}