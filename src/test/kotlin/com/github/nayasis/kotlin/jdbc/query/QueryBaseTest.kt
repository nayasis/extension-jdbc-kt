package com.github.nayasis.kotlin.jdbc.query

import io.kotest.core.spec.style.StringSpec
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class QueryBaseTest: StringSpec() { init {
"basic" {
    val sql = """
        SELECT *
        FROM   TABLE
        WHERE  name   = #{name:VARCHAR}
        AND    age    = #{age:NUMERIC}
        AND    depart = #{depart}
    """.trimIndent().toQuery()

    assertEquals("""
        SELECT *
        FROM   TABLE
        WHERE  name   = #{name:VARCHAR}
        AND    age    = #{age:NUMERIC}
        AND    depart = #{depart}
    """.trimIndent(), "$sql")

    assertEquals(3, sql.paramStructs.size)

    // add parameters

    sql.addParam("name", "Microsoft")
    sql.addParam("age", 13)

    assertEquals("""
        SELECT *
        FROM   TABLE
        WHERE  name   = 'Microsoft'
        AND    age    = 13
        AND    depart = #{depart}
    """.trimIndent(), "$sql")

    assertEquals("""
        SELECT *
        FROM   TABLE
        WHERE  name   = ?
        AND    age    = ?
        AND    depart = #{depart}
    """.trimIndent(), "${sql.preparedQuery}")

    sql.addParam("depart", "HR")

    assertEquals("""
        SELECT *
        FROM   TABLE
        WHERE  name   = 'Microsoft'
        AND    age    = 13
        AND    depart = 'HR'
    """.trimIndent(), "$sql")

    assertEquals("""
        SELECT *
        FROM   TABLE
        WHERE  name   = ?
        AND    age    = ?
        AND    depart = ?
    """.trimIndent(), "${sql.preparedQuery}")

    // reset parameters

    sql.params.clear()

    assertEquals("""
        SELECT *
        FROM   TABLE
        WHERE  name   = #{name:VARCHAR}
        AND    age    = #{age:NUMERIC}
        AND    depart = #{depart}
    """.trimIndent(), "$sql")

    assertEquals("""
        SELECT *
        FROM   TABLE
        WHERE  name   = #{name:VARCHAR}
        AND    age    = #{age:NUMERIC}
        AND    depart = #{depart}
    """.trimIndent(), "${sql.preparedQuery}")

    // add map parameters
    sql.params.clear()
    sql.addParam(mapOf(
        "name" to "J.R.R.Tolkien",
        "age" to 57,
        "depart" to "academic",
    ))

    assertEquals("""
        SELECT *
        FROM   TABLE
        WHERE  name   = 'J.R.R.Tolkien'
        AND    age    = 57
        AND    depart = 'academic'
    """.trimIndent(), "$sql")

    // add VO parameter
    sql.params.clear()
    sql.addParam(ParamVo(
        name = "Jake",
        age = 13,
        depart = "elementary"
    ))

    assertEquals("""
        SELECT *
        FROM   TABLE
        WHERE  name   = 'Jake'
        AND    age    = 13
        AND    depart = 'elementary'
    """.trimIndent(), "$sql")
}

"list param" {
    val sql = """
        SELECT *
        FROM   TABLE
        WHERE  age IN (#{ages})
    """.trimIndent().toQuery()

    sql.addParam("ages", listOf(12,17,34,92) )

    assertEquals(4, sql.preparedParams.size)

    assertEquals("""
        SELECT *
        FROM   TABLE
        WHERE  age IN (12,17,34,92)
    """.trimIndent(), "$sql")

    assertEquals("""
        SELECT *
        FROM   TABLE
        WHERE  age IN (?,?,?,?)
    """.trimIndent(), "${sql.preparedQuery}")
}
}}

data class ParamVo(
    val name: String,
    val age: Int,
    val depart: String
)