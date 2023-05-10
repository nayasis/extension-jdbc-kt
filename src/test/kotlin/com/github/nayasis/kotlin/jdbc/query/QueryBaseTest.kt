package com.github.nayasis.kotlin.jdbc.query

import io.kotest.core.spec.style.StringSpec
import org.junit.jupiter.api.Assertions.*

class QueryBaseTest: StringSpec() { init {
"basic" {

    val sqlRaw =  """
            SELECT *
            FROM   TABLE
            WHERE  name   = #{name:VARCHAR}
            AND    age    = #{age:NUMERIC}
            AND    depart = #{depart}
        """.trimIndent()

    assertEquals(3, QueryBase(sqlRaw).paramStructs.size)

    val sql = sqlRaw.toQuery()

    assertEquals("""
        SELECT *
        FROM   TABLE
        WHERE  name   = #{name:VARCHAR}
        AND    age    = #{age:NUMERIC}
        AND    depart = #{depart}
    """.trimIndent(), "$sql")



    // add parameters

    sql.setParam("name", "Microsoft")
    sql.setParam("age", 13)

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

    sql.setParam("depart", "HR")

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

    sql.reset()

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
    sql.reset()
    sql.setParam(mapOf(
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
    sql.reset()
    sql.setParam(ParamVo(
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

    sql.setParam("ages", listOf(12,17,34,92) )

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