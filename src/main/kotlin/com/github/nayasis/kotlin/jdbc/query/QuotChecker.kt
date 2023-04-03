package com.github.nayasis.kotlin.jdbc.query

import JdbcType

class QuotChecker {

    private var inQuot         = false
    private var inDoubleQuot   = false
    private var inBlockComment = false
    private var inLineComment  = false

    fun read( c: Char, next: Char? = null ): QuotChecker {
        when {
            inQuot         -> if(c == '\'') inQuot = false
            inDoubleQuot   -> if(c == '"') inDoubleQuot = false
            inBlockComment -> if(c == '*' && next == '/') inBlockComment = false
            inLineComment  -> if(c == '\n' || c == '\r') inLineComment = false
            else -> {
                when {
                    c == '\'' -> inQuot = true
                    c == '"'  -> inDoubleQuot = true
                    c == '/' && next == '*' -> inBlockComment = true
                    c == '-' && next == '-' -> inLineComment = true
                }
            }
        }
        return this
    }

    fun ignorable(): Boolean {
        return inQuot || inDoubleQuot || inBlockComment || inLineComment
    }

}