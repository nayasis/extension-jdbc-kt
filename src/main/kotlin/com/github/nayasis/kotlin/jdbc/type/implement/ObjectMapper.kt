package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

class ObjectMapper: TypeMapper<Any> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Any) {
        statement.setObject(index, param)
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.JAVA_OBJECT.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): Any? {
        return resultSet.getObject(columnName)
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): Any? {
        return resultSet.getObject(columnIndex)
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): Any? {
        return statement.getObject(columnIndex)
    }
}