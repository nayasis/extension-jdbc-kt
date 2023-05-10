package com.github.nayasis.kotlin.jdbc.query

import com.github.nayasis.kotlin.basica.reflection.Reflector
import com.github.nayasis.kotlin.jdbc.type.JdbcType

open class Query(
    private val queryBase: QueryBase
) {

    // key: value = String : null or BindParam or List<BindParam>
    private val bindParams = HashMap<Int,Any?>()

    private val paramIndices = queryBase.paramIndices

    constructor(sql: String) : this(QueryBase(sql))

    fun setParam(vo: Any): Query {
        val param = try {
            Reflector.toMap(vo)
        } catch (e: Exception) {
            throw TypeCastException("Parameter muse be value object")
        }
        return setParam(param)
    }

    fun setParam(param: Map<String,Any?>): Query {
        param.forEach { (key, value) -> setParam(key,value) }
        return this
    }

    fun setParam(key: String, param: Any?): Query {
        paramIndices[key]?.forEach { i ->
            val struct = queryBase.paramStructs[i]
            bindParams[i] = if(param is Array<*> && struct.jdbcType != JdbcType.ARRAY) {
                param.map { BindParam(it,struct) }
            } else if( param is Collection<*>  && struct.jdbcType != JdbcType.ARRAY ) {
                param.map { BindParam(it,struct) }
            } else {
                BindParam(param,struct)
            }
        }
        return this
    }

    fun reset(): Query {
        bindParams.clear()
        return this
    }

    val preparedQuery: String
        get() {
            val sb = StringBuilder()
            for(i in 0 until queryBase.queries.size) {
                sb.append(queryBase.queries[i])
                queryBase.paramStructs.getOrNull(i)?.let { struct ->
                    if(bindParams.containsKey(i)) {
                        val value = bindParams[i]
                        if (value is List<*>) {
                            value.joinToString(",") { "?" }
                        } else "?"
                    } else if(struct.out) {
                        "?"
                    } else "$struct"
                }?.let { sb.append(it) }
            }
            return sb.toString()
        }

    @Suppress("UNCHECKED_CAST")
    val preparedParams: Map<Int,BindParam>
        get() {
            val ans = HashMap<Int,BindParam>()
            var idx = 0
            for(i in 0 until queryBase.queries.size) {
                queryBase.paramStructs.getOrNull(i)?.let { struct ->
                    if(bindParams.containsKey(i)) {
                        val value = bindParams[i]
                        if (value is List<*>) {
                            (value as List<BindParam>).forEach { e -> ans[idx++] = e }
                        } else if(value is BindParam) {
                            ans[idx++] = value
                        }
                    } else if(struct.out) {
                        ans[idx++] = BindParam(struct)
                    }
                }
            }
            return ans
        }

    override fun toString(): String {
        val sb = StringBuilder()
        for(i in queryBase.queries.indices) {
            sb.append(queryBase.queries[i])
            queryBase.paramStructs.getOrNull(i)?.let { struct ->
                if(bindParams.containsKey(i)) {
                    val value = bindParams[i]
                    if (value is List<*>) {
                        value.joinToString(",")
                    } else "$value"
                } else "$struct"
            }?.let { sb.append(it) }
        }
        return sb.toString()
    }

}

fun String.toQuery(): Query = Query(this)

