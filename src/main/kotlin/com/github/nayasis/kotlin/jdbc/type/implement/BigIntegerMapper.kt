package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

class BigIntegerMapper: TypeMapper<BigInteger> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: BigInteger) {
        statement.setBigDecimal(index, BigDecimal(param))
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.BIGINT.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): BigInteger? {
        return resultSet.getBigDecimal(columnName).toBigInteger()
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): BigInteger? {
        return resultSet.getBigDecimal(columnIndex).toBigInteger()
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): BigInteger? {
        return statement.getBigDecimal(columnIndex).toBigInteger()
    }
}