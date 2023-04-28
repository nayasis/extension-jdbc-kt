package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

object CharSequenceMapper: TypeMapper<CharSequence> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: CharSequence) =
        statement.setString(index, param.toString())
    override fun getResult(resultSet: ResultSet, columnIndex: Int): CharSequence? =
        resultSet.getString(columnIndex)
    override fun getResult(statement: CallableStatement, columnIndex: Int): CharSequence? =
        statement.getString(columnIndex)
}