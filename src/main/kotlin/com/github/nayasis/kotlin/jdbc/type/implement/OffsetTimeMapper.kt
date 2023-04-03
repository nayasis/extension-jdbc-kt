package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Time
import java.time.OffsetTime
import java.util.*

class OffsetTimeMapper: TypeMapper<OffsetTime> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: OffsetTime) {
        statement.setObject(index, param)
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.TIME.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): OffsetTime? {
        return resultSet.getObject(columnName, OffsetTime::class.java)
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): OffsetTime? {
        return resultSet.getObject(columnIndex, OffsetTime::class.java)
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): OffsetTime? {
        return statement.getObject(columnIndex, OffsetTime::class.java)
    }
}