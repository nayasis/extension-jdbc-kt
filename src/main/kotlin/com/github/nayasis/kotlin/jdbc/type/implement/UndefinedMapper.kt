package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

class UndefinedMapper: TypeMapper<List<*>> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: List<*>) {
        val klass = param.first()?.let { it::class }
        val type = JdbcType.of(klass) ?: JdbcType.VARCHAR
        val sqlArray = statement.connection.createArrayOf(type.name, param.toTypedArray())
        statement.setArray(index, sqlArray)
    }

    override fun setOutParameter(statement: CallableStatement, index: Int) {
        statement.registerOutParameter(index, JdbcType.ARRAY.code)
    }

    override fun getResult(resultSet: ResultSet, columnName: String): List<*> {
        return getList(resultSet.getArray(columnName))
    }

    override fun getResult(resultSet: ResultSet, columnIndex: Int): List<*> {
        return getList(resultSet.getArray(columnIndex))
    }

    override fun getResult(statement: CallableStatement, columnIndex: Int): List<*> {
        return getList(statement.getArray(columnIndex))
    }

    private fun getList(array: java.sql.Array?): List<*> {
        val result = ArrayList<Any?>()
        if (array == null) return result
        for (e in array.array as Array<*>) {
            result.add(e)
        }
        return result
    }
}