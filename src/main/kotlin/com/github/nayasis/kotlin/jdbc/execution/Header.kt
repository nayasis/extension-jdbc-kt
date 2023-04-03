package com.github.nayasis.kotlin.jdbc.execution

import JdbcType
import java.sql.ResultSetMetaData

class Header(rsmeta: ResultSetMetaData) {

    private val meta = ArrayList<HeaderMeta>()

    val size: Int
        get() = meta.size

    val keySet: Set<String>
        get() = meta.map { it.name }.toSet()

    init {
        for( i in 0..rsmeta.columnCount) {
            meta.add(HeaderMeta(
                rsmeta.getColumnName(i),
                JdbcType.of(rsmeta.getColumnType(i))
            ))
        }
    }

    operator fun get(index: Int): HeaderMeta {
        return meta[index]
    }

}

data class HeaderMeta (
    val name: String,
    val type: JdbcType,
)