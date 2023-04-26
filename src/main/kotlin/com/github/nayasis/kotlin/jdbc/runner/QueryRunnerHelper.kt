package com.github.nayasis.kotlin.jdbc.runner

import com.github.nayasis.kotlin.jdbc.query.BindParam
import com.github.nayasis.kotlin.jdbc.type.JdbcType
import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import com.github.nayasis.kotlin.jdbc.type.implement.NullMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.runBlocking
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.SQLSyntaxErrorException
import java.sql.Statement

fun get(resultSet: ResultSet): Map<String,Any?> {
    return resultSet.use { rset ->
        val header = Header(rset)
        val row = LinkedHashMap<String, Any?>()
        if(rset.next()) {
            for (i in 0 until header.size) {
                row[header[i].name] = convertResult(rset, i, header)
            }
        }
        row
    }
}

fun monoFirst(resultSet: ResultSet): Any? {
    return resultSet.use { rset ->
        val header = Header(rset)
        if(rset.next()) {
            convertResult(rset, 0, header)
        } else null
    }
}

fun getAll(resultSet: ResultSet): Flow<Map<String,Any?>> {
    val header = Header(resultSet)
    return flow { resultSet.use { rset ->
        while (rset.next()) {
            val row = LinkedHashMap<String,Any?>()
            for (i in 0 until header.size) {
                row[header[i].name] = convertResult(rset, i, header)
            }
            emit(row)
        }
    }}
}

fun <T> fluxFirst(resultSet: ResultSet): Flow<T?> {
    return flow { resultSet.use { rset ->
        val header = Header(rset)
        while (rset.next()) {
            emit(convertResult(rset, 0, header) as T?)
        }
    }}
}

fun toList(rset: ResultSet): List<Map<String,Any?>> {
    return runBlocking {
        getAll(rset).toCollection(ArrayList())
    }
}

private fun convertResult(rset: ResultSet, index: Int, header: Header): Any? {
    val meta = header[index]
    try {
        val result = meta.type.mapper.getResult(rset, index)
        return if(result is ResultSet) {
            runBlocking {
                getAll(result).toCollection(ArrayList())
            }
        } else {
            result
        }
    } catch (e: Exception) {
        throw SQLException("Type mapper is not valid. (column:${meta.name}, mapper:${meta.type.mapper::class.simpleName}, rsValue:${rset.getString(meta.name)}")
    }
}

fun setParameter(statement: Statement, params: List<BindParam>): Map<Int,BindParam>? {
    var outParams: HashMap<Int,BindParam>? = null
    when (statement) {
        is CallableStatement -> {
            params.forEachIndexed { i, param ->
                try {
                    if(param.out) {
                        if(outParams == null)
                            outParams = HashMap()
                        statement.registerOutParameter(i+1, param.jdbcType.code)
                        outParams?.put(i+1, param)
                    } else {
                        setParameter(statement, i, param)
                    }
                } catch (e: Exception) {
                    throw SQLSyntaxErrorException("error on binding parameter (index:$i, parameter:[$param])")
                }
            }
        }
        is PreparedStatement -> {
            params.forEachIndexed { i, param ->
                setParameter(statement, i, param)
            }
        }
        else -> throw UnsupportedOperationException("Not supported statement. (${statement::class.simpleName})")
    }
    return outParams
}

private fun setParameter(statement: PreparedStatement, i: Int, param: BindParam) {
    if (param.value == null) {
        NullMapper.setParameter(statement, i + 1, null as Nothing)
    } else {
        try {
            val mapper = param.jdbcType.mapper as TypeMapper<Any>
            mapper.setParameter(statement, i + 1, param.value!!)
        } catch (e: Exception) {
            val mapper = JdbcType.mapper(param.value!!::class) as TypeMapper<Any>
            mapper.setParameter(statement, i + 1, param.value!!)
        }
    }
}