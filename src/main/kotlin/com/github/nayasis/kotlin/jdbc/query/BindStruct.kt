package com.github.nayasis.kotlin.jdbc.query

import com.github.nayasis.kotlin.jdbc.type.JdbcType

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
            0 -> key = ""
            1 -> key = words[0]
            2 -> {
                key = words[0]
                if(words[1].equals("out",true)) {
                    out = true
                } else {
                    jdbcType = JdbcType.of(words[1])
                }
            }
            else -> {
                key      = words[0]
                jdbcType = JdbcType.of(words[1])
                out      = words[2].equals("out",true)
            }
        }

    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append('#').append('{').append(key)
        if(jdbcType != null) sb.append(":$jdbcType")
        if(out) sb.append(":out")
        sb.append('}')
        return sb.toString()
    }

}