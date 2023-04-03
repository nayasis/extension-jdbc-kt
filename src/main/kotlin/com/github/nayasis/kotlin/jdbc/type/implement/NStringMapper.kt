package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

class NStringMapper: TypeMapper<String> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: String) {
        statement.setString(index, param)
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.NVARCHAR.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): String? {
        return resultSet.getString(columnName)
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): String? {
        return resultSet.getString(columnIndex)
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): String? {
        return statement.getString(columnIndex)
    }

}