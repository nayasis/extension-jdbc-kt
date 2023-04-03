package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.basica.core.extention.ifNotNull
import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

class CharacterMapper: TypeMapper<Char> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: Char) {
        statement.setString(index, param.toString())
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.TINYINT.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): Char? {
        return resultSet.getString(columnName).ifNotNull { it[0] }
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): Char? {
        return resultSet.getString(columnIndex).ifNotNull { it[0] }
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): Char? {
        return statement.getString(columnIndex).ifNotNull { it[0] }
    }
}