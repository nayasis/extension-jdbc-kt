package com.github.nayasis.kotlin.jdbc.query

import JdbcType

class BindStruct {

    var key: String
        private set
    var jdbcType: JdbcType? = null
        private set
    var out: Boolean = false
        private set

    constructor(s: String) {
        val words = s.split(":")
        when(words.size) {
            0 -> {
                key = ""
            }
            1 -> {
                key = words[0]
                out = (words.getOrNull(1) == "out")
            }
            else -> {
                key = words[0]
                jdbcType = words.getOrNull(1)?.let { JdbcType.of(it) }
                out = (words.getOrNull(2) == "out")
            }
        }

    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append('{')
        sb.append(key)
        if(jdbcType != null) sb.append(":$jdbcType")
        if(out) sb.append(":out")
        sb.append('}')
        return sb.toString()
    }

}