package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.ZonedDateTime

class ZonedDateTimeMapper: TypeMapper<ZonedDateTime> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: ZonedDateTime) {
        statement.setObject(index, param)
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.TIMESTAMP.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): ZonedDateTime? {
        return resultSet.getObject(columnName, ZonedDateTime::class.java)
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): ZonedDateTime? {
        return resultSet.getObject(columnIndex, ZonedDateTime::class.java)
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): ZonedDateTime? {
        return statement.getObject(columnIndex, ZonedDateTime::class.java)
    }
}