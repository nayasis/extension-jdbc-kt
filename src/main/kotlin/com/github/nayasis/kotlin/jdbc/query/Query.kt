package com.github.nayasis.kotlin.jdbc.query

class Query(
    private val queryBase: QueryBase
) {

    // key : BindParam or List<BindParam>
    val params = HashMap<String,Any>()

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
            queryBase.paramStructs.getOrNull(i)?.let { struct ->
                if(params.containsKey(struct.key)) {
                    val value = params[struct.key]
                    if(value is List<*>) {
                        value.joinToString(",") { "?" }
                    } else "?"
                } else "$struct"
            }
        }
        return sb.toString()
    }

    fun toPreparedParams(): List<BindParam> {
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