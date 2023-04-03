package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

class ArrayMapper: TypeMapper<List<*>> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: List<*>) {
        val klass = param::class.java.componentType.kotlin
        val type = JdbcType.of(klass) ?: JdbcType.JAVA_OBJECT
        val sqlArray = statement.connection.createArrayOf(type.name, param.toTypedArray())
        try {
            statement.setArray(index, sqlArray)
        } finally {
            sqlArray.free()
        }
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.ARRAY.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): List<*>? {
        return extract(resultSet.getArray(columnName))
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): List<*>? {
        return extract(resultSet.getArray(columnIndex))
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): List<*>? {
        return extract(statement.getArray(columnIndex))
    }

    private fun extract(array: java.sql.Array?): List<*>? {
        try {
            return (array?.array as Array<*>?)?.toList()
        } finally {
            array?.free()
        }
    }
}