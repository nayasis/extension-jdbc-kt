package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Time
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.util.*

class OffsetDateTimeMapper: TypeMapper<OffsetDateTime> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: OffsetDateTime) {
        statement.setObject(index, param)
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.DATETIME.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): OffsetDateTime? {
        return resultSet.getObject(columnName, OffsetDateTime::class.java)
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): OffsetDateTime? {
        return resultSet.getObject(columnIndex, OffsetDateTime::class.java)
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): OffsetDateTime? {
        return statement.getObject(columnIndex, OffsetDateTime::class.java)
    }
}