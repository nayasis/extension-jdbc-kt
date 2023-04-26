package com.github.nayasis.kotlin.jdbc.runner

import com.github.nayasis.kotlin.jdbc.type.JdbcType
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.SQLException

class Header(resultSetMeta: ResultSetMetaData) {

    constructor(resultSet: ResultSet): this(resultSet.metaData)

    private val meta = ArrayList<HeaderMeta>()

    private val keyMap = LinkedHashMap<String,Int>()

    val size: Int
        get() = meta.size

    val keys: List<String>
        get() = keyMap.keys.toList()

    init {
        val dupKeys = ArrayList<String>()
        for( i in 0..resultSetMeta.columnCount) {
            val header = HeaderMeta(
                resultSetMeta.getColumnName(i),
                JdbcType.of(resultSetMeta.getColumnType(i))
            )
            if(keyMap.containsKey(header.name))
                dupKeys.add(header.name)
            meta.add(header)
            keyMap[header.name] = i
        }
        if(dupKeys.size > 0)
            throw SQLException("Result has duplicated key${if(dupKeys.size >1) "s" else ""}. ($dupKeys)")
    }

    operator fun get(index: Int): HeaderMeta = meta[index]

    operator fun get(key: String): HeaderMeta? = keyMap[key]?.let{ get(it) }

}

data class HeaderMeta (
    val name: String,
    val type: JdbcType,
)