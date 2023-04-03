package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

class ByteMapper: TypeMapper<Byte> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Byte) {
        statement.setByte(index, param)
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.TINYINT.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): Byte? {
        return resultSet.getByte(columnName)
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): Byte? {
        return resultSet.getByte(columnIndex)
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): Byte? {
        return statement.getByte(columnIndex)
    }
}