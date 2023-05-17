package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.math.BigDecimal
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

object BigDecimalMapper: TypeMapper<BigDecimal> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: BigDecimal) =
        statement.setBigDecimal(index, param)
    override fun getResult(resultSet: ResultSet, columnIndex: Int): BigDecimal? =
        resultSet.getBigDecimal(columnIndex)
    override fun getResult(statement: CallableStatement, columnIndex: Int): BigDecimal? =
        statement.getBigDecimal(columnIndex)
}