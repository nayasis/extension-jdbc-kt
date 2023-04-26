package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.OffsetDateTime

object OffsetDateTimeMapper: TypeMapper<OffsetDateTime> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: OffsetDateTime) =
        statement.setObject(index, param)
    override fun getResult(resultSet: ResultSet, columnIndex: Int): OffsetDateTime? =
        resultSet.getObject(columnIndex, OffsetDateTime::class.java)
    override fun getResult(statement: CallableStatement, columnIndex: Int): OffsetDateTime? =
        statement.getObject(columnIndex, OffsetDateTime::class.java)
}