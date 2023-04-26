package com.github.nayasis.kotlin.jdbc.query

import com.github.nayasis.kotlin.jdbc.type.JdbcType
import com.github.nayasis.kotlin.basica.reflection.Reflector

class Query(
    private val queryBase: QueryBase
) {

    // key : BindParam or List<BindParam>
    val params = HashMap<String,Any>()

    val paramStructs: List<BindStruct>
        get() = queryBase.paramStructs

    constructor(sql: String) : this(QueryBase(sql))

    fun addParam(vo: Any): Query {
        return addParam(Reflector.toMap(vo))
    }

    fun addParam(params: Map<String,Any?>): Query {
        params.forEach { (key, value) -> addParam(key,value) }
        return this
    }

    fun addParam(key: String, param: Any?): Query {
        val struct = queryBase.getParamStruct(key) ?: BindStruct(key)
        if(param is Array<*> && struct.jdbcType != JdbcType.ARRAY) {
            params[key] = param.map { BindParam(it,struct) }
        } else if( param is Collection<*>  && struct.jdbcType != JdbcType.ARRAY ) {
            params[key] = param.map { BindParam(it,struct) }
        } else {
            params[key] = BindParam(param,struct)
        }
        return this
    }

    fun reset(): Query {
        params.clear()
        return this
    }

    val preparedQuery: String
        get() {
            val sb = StringBuilder()
            for(i in 0 until queryBase.queries.size) {
                sb.append(queryBase.queries[i])
                queryBase.paramStructs.getOrNull(i)?.let { struct ->
                    if(params.containsKey(struct.key)) {
                        val value = params[struct.key]
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

    val preparedParams: List<BindParam>
        get() {
            val ans = ArrayList<BindParam>()
            for( struct in queryBase.paramStructs ) {
                params[struct.key]?.let { param ->
                    if(param is List<*>) {
                        param.filterIsInstance<BindParam>().forEach { e -> ans.add(e) }
                    } else if( param is BindParam) {
                        ans.add(param)
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
                if(params.containsKey(struct.key)) {
                    val value = params[struct.key]
                    if(value is List<*>) {
                        value.joinToString(",")
                    } else {
                        "$value"
                    }
                } else {
                    "$struct"
                }.let { sb.append(it) }
            }
        }
        return sb.toString()
    }

}

fun String.toQuery(): Query = Query(this)

