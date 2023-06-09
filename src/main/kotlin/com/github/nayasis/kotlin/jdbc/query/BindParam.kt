package com.github.nayasis.kotlin.jdbc.query

import com.github.nayasis.kotlin.jdbc.type.JdbcType
import com.github.nayasis.kotlin.basica.core.localdate.format
import com.github.nayasis.kotlin.basica.core.localdate.toDate
import com.github.nayasis.kotlin.basica.core.localdate.toLocalDate
import com.github.nayasis.kotlin.basica.core.localdate.toLocalDateTime
import com.github.nayasis.kotlin.basica.core.localdate.toLocalTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date

class BindParam {

    var key: String
        private set
    var value: Any? = null
    var out: Boolean = false
    var jdbcType: JdbcType = JdbcType.VARCHAR
        set(type) {
            field = type
            if(value == null && !out) {
                field = JdbcType.NULL
                return
            }
            if(value is StringBuffer || value is StringBuilder)
                value = "$value"
            if(value is String) {
                when (jdbcType) {
                    JdbcType.CHAR      -> this.value = value.toString().firstOrNull()
                    JdbcType.INTEGER   -> this.value = value.toString().toInt()
                    JdbcType.BIGINT    -> this.value = value.toString().toBigInteger()
                    JdbcType.BOOLEAN   -> this.value = value.toString().toBoolean()
                    JdbcType.BIT       -> this.value = value.toString().toBoolean()
                    JdbcType.TINYINT   -> this.value = value.toString().toByte()
                    JdbcType.SMALLINT  -> this.value = value.toString().toShort()
                    JdbcType.FLOAT     -> this.value = value.toString().toFloat()
                    JdbcType.DOUBLE    -> this.value = value.toString().toDouble()
                    JdbcType.NUMERIC   -> this.value = value.toString().toDouble()
                    JdbcType.DECIMAL   -> this.value = value.toString().toBigDecimal()
                    JdbcType.DATE      -> this.value = value.toString().toDate()
                    JdbcType.DATETIME  -> this.value = value.toString().toLocalDateTime().let { it.withNano(0) }
                    JdbcType.TIME      -> this.value = value.toString().toLocalTime()
                    JdbcType.TIMESTAMP -> this.value = value.toString().toLocalDateTime()
                    else -> return
                }
            }
        }

    constructor(struct: BindStruct) {
        this.key = struct.key
        this.out   = struct.out
        this.jdbcType = struct.jdbcType ?: JdbcType.VARCHAR
    }

    constructor(value: Any?, struct: BindStruct) {
        this.key = struct.key
        this.value = value
        this.out   = struct.out
        this.jdbcType = struct.jdbcType ?: value?.let{ JdbcType.of(it::class) } ?: JdbcType.VARCHAR
    }

    private fun isString(value: Any?): Boolean {
        return if(value == null) false else
            (value is String || value is StringBuffer || value is StringBuilder)
    }

    override fun toString(): String {
        return try { when {
            value == null -> "null"
            isString(value) -> "'$value'"
            value is LocalDate -> getDateFormat()?.let { "TO_DATE('${(value as LocalDate).format("$it")}','$it')" } ?: "$value"
            value is LocalDateTime -> getDateFormat()?.let { "TO_DATE('${(value as LocalDateTime).format("$it")}','$it')" } ?: "$value"
            value is Date -> getDateFormat()?.let { "TO_DATE('${(value as Date).format("$it")}','$it')" } ?: "$value"
            value is OffsetTime -> getDateFormat()?.let { "TO_DATE('${(value as OffsetTime).format(DateTimeFormatter.ofPattern("hh:mm:ss"))}','$it')" } ?: "$value"
            value is OffsetDateTime -> getDateFormat()?.let { "TO_DATE('${(value as OffsetTime).format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))}','$it')" } ?: "$value"
            else -> "$value"
        }} catch (e: Exception) {
            "$value"
        }
    }

    private fun getDateFormat(): String? {
        return when(jdbcType) {
            JdbcType.DATE -> "YYYY-MM-DD"
            JdbcType.DATETIME -> "YYYY-MM-DD HH:MI:SS"
            JdbcType.TIME -> "HH:MI:SS"
            JdbcType.TIMESTAMP -> "HH:MI:SS.FFF"
            else -> null
        }
    }

}