package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

object ResultSetMapper: TypeMapper<ResultSet> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: ResultSet) =
        statement.setObject(index, param)
    override fun getResult(resultSet: ResultSet, columnIndex: Int): ResultSet =
        resultSet.getObject(columnIndex) as ResultSet
    override fun getResult(statement: CallableStatement, columnIndex: Int): ResultSet =
        statement.getObject(columnIndex) as ResultSet
}