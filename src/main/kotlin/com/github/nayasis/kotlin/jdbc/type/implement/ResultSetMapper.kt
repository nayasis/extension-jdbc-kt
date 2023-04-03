package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

class ResultSetMapper: TypeMapper<ResultSet> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: ResultSet) {
        statement.setObject(index, param)
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.JAVA_OBJECT.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): ResultSet {
        return resultSet.getObject(columnName) as ResultSet
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): ResultSet {
        return resultSet.getObject(columnIndex) as ResultSet
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): ResultSet {
        return statement.getObject(columnIndex) as ResultSet
    }
}