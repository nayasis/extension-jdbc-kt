package com.github.nayasis.kotlin.jdbc.query

class Query(
    private val queryBase: QueryBase
) {

    private val params = HashMap<String,Any?>()

    constructor(sql: String) : this(QueryBase(sql))

    fun addParams(params: Map<String,Any?>) {
        params.forEach { (key, value) -> addParam(key,value) }
    }

    fun addParam(key: String, param: Any?) {
        val struct = queryBase.getParamStruct(key) ?: BindStruct(key)
        if(param is Array<*> && struct.jdbcType != JdbcType.ARRAY) {
            params[key] = param.map { BindParam(it,struct) }
        } else if( param is Collection<*>  && struct.jdbcType != JdbcType.ARRAY ) {
            params[key] = param.map { BindParam(it,struct) }
        } else {
            params[key] = BindParam(param,struct)
        }
    }

    fun reset() {
        params.clear()
    }

    fun toPreparedString(): String {
        val sb = StringBuilder()
        for(i in 0 until queryBase.queries.size) {
            sb.append(queryBase.queries[i])
            queryBase.getParamStruct(i)?.let { struct ->
                if(params.containsKey(struct.key)) {
                    val value = params[struct.key]
                    if(value is List<*>) {
                        value.joinToString(",") { "?" }

                    } else {
                        "?"
                    }
                } else {
                    "$struct"
                }
            }
        }
        return sb.toString()
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for(i in 0 until queryBase.queries.size) {
            sb.append(queryBase.queries[i])
            queryBase.getParamStruct(i)?.let { struct ->
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