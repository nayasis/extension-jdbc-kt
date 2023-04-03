package com.github.nayasis.kotlin.jdbc.query

class QueryBase(sql: String) {

    val queries = ArrayList<String>()

    private val paramStructs = ArrayList<BindStruct>()
    private val paramStructsByKey: Map<String,BindStruct>

    init {
        val query       = sql.trimIndent()
        val quotChecker = QuotChecker()
        val buffer      = StringBuilder()
        var i           = 0
        while( i < query.length ) {
            val curr = sql[i]
            val next = sql.getOrNull(i + 1)
            if(quotChecker.read(curr, next).ignorable()) {
                buffer.append(curr)
            } else if( curr == '{' ) {
                val start = i
                val end   = sql.indexOf('}', i + 1)
                if(end < 0) {
                    buffer.append(sql.substring(i))
                    break
                } else {
                    val definition = sql.substring(start + 1, end - 1)
                    paramStructs.add(BindStruct(definition))
                    i = end
                }
            } else {
                buffer.append(curr)
            }
            i++
        }
        if(buffer.isNotEmpty())
            queries.add("$buffer")
        paramStructsByKey = paramStructs.associateBy { it.key }
    }

    fun containsKey(key: String?): Boolean {
        return paramStructsByKey.contains(key)
    }

    fun getParamStruct(key: String?): BindStruct? {
        return paramStructsByKey[key]
    }

    fun getParamStruct(index: Int): BindStruct? {
        return paramStructs.getOrNull(index)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for(i in queries.indices) {
            sb.append(queries[i])
            paramStructs.getOrNull(i)?.let { sb.append(it) }
        }
        return sb.toString()
    }

}

