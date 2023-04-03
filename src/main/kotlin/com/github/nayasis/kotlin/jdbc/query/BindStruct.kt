package com.github.nayasis.kotlin.jdbc.query

import JdbcType

class BindStruct {

    var key: String
        private set
    var jdbcType: JdbcType? = null
        private set
    var out: Boolean? = null
        private set

    constructor(s: String) {
        val words = s.split(":")
        key = words[0]
        jdbcType = words.getOrNull(1)?.let { JdbcType.of(it) }
        out = words.getOrNull(2)?.toBoolean()
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append('{')
        sb.append(key)
        if(jdbcType != null)
            sb.append(':').append(jdbcType)
        if(out == true)
            sb.append(':').append("out")
        sb.append('}')
        return sb.toString()
    }

}