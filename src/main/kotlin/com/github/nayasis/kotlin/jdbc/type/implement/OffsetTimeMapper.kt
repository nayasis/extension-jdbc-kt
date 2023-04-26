package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.OffsetTime

object OffsetTimeMapper: TypeMapper<OffsetTime> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: OffsetTime) =
        statement.setObject(index, param)
    override fun getResult(resultSet: ResultSet, columnIndex: Int): OffsetTime? =
        resultSet.getObject(columnIndex, OffsetTime::class.java)
    override fun getResult(statement: CallableStatement, columnIndex: Int): OffsetTime? =
        statement.getObject(columnIndex, OffsetTime::class.java)
}