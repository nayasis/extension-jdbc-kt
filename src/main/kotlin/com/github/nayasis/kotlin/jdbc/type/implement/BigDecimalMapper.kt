package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.math.BigDecimal
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

class BigDecimalMapper: TypeMapper<BigDecimal> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: BigDecimal) {
        statement.setBigDecimal(index, param)
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.NUMERIC.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): BigDecimal? {
        return resultSet.getBigDecimal(columnName)
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): BigDecimal? {
        return resultSet.getBigDecimal(columnIndex)
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): BigDecimal? {
        return statement.getBigDecimal(columnIndex)
    }
}