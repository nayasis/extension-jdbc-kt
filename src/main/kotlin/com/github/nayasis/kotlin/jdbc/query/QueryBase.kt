package com.github.nayasis.kotlin.jdbc.query

class QueryBase(sql: String) {

    val queries = ArrayList<String>()
    val paramStructs = ArrayList<BindStruct>()

    val paramIndices: Map<String,List<Int>>

    private val paramStructsByKey: Map<String,BindStruct>

    init {
        val query       = sql.trimIndent()
        val quotChecker = QuotChecker()
        val buffer      = StringBuilder()
        var i           = 0
        while(i < query.length) {
            val curr = sql[i]
            val next = sql.getOrNull(i+1)
            if(quotChecker.read(curr, next).ignorable()) {
                buffer.append(curr)
            } else if(curr == '#' && next == '{') {
                val start = i
                val end   = sql.indexOf('}', i + 1)
                if(end < 0) {
                    buffer.append(sql.substring(i))
                    break
                } else {
                    val definition = sql.substring(start + 2, end)
                    paramStructs.add(BindStruct(definition))
                    queries.add("$buffer")
                    buffer.clear()
                    i = end
                }
            } else {
                buffer.append(curr)
            }
            i++
        }
        if(buffer.isNotEmpty())
            queries.add("$buffer")

        paramIndices = paramStructs.mapIndexed { index, bindStruct ->
            bindStruct.key to index
        }.groupBy({ it.first }, {it.second})

        paramStructsByKey = paramStructs.associateBy { it.key }
    }

    fun containsKey(key: String?): Boolean {
        return paramStructsByKey.contains(key)
    }

    fun getParamStruct(key: String?): BindStruct? {
        return paramStructsByKey[key]
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

