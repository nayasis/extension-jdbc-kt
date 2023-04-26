package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

object ShortMapper: TypeMapper<Short> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Short) =
        statement.setShort(index, param)
    override fun getResult(resultSet: ResultSet, columnIndex: Int): Short? =
        resultSet.getShort(columnIndex)
    override fun getResult(statement: CallableStatement, columnIndex: Int): Short? =
        statement.getShort(columnIndex)
}