package com.github.nayasis.kotlin.jdbc.type.implement

import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.RowId

object RowIdMapper: TypeMapper<RowId> {
    override fun setParameter(statement: PreparedStatement, index: Int, param: RowId) =
        statement.setRowId(index, param)
    override fun getResult(resultSet: ResultSet, columnIndex: Int): RowId? =
        resultSet.getRowId(columnIndex)
    override fun getResult(statement: CallableStatement, columnIndex: Int): RowId? =
        statement.getRowId(columnIndex)
}