package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.ZonedDateTime

object ZonedDateTimeMapper: TypeMapper<ZonedDateTime> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: ZonedDateTime) =
        statement.setObject(index, param)
    override fun getResult(resultSet: ResultSet, columnIndex: Int): ZonedDateTime? =
        resultSet.getObject(columnIndex, ZonedDateTime::class.java)
    override fun getResult(statement: CallableStatement, columnIndex: Int): ZonedDateTime? =
        statement.getObject(columnIndex, ZonedDateTime::class.java)
}