package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

object BigIntegerMapper: TypeMapper<BigInteger> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: BigInteger) =
        statement.setBigDecimal(index, BigDecimal(param))
    override fun getResult(resultSet: ResultSet, columnIndex: Int): BigInteger? =
        resultSet.getBigDecimal(columnIndex).toBigInteger()
    override fun getResult(statement: CallableStatement, columnIndex: Int): BigInteger? =
        statement.getBigDecimal(columnIndex).toBigInteger()
}