package com.github.nayasis.kotlin.jdbc.runner

import com.github.nayasis.kotlin.basica.core.collection.toJson
import com.github.nayasis.kotlin.jdbc.query.toQuery
import io.kotest.common.runBlocking
import io.kotest.core.Tuple2
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import kotlinx.coroutines.flow.toCollection
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDateTime

class QueryRunnerTest: FreeSpec() {

    lateinit var connection: Connection

    override fun afterTest(f: suspend (Tuple2<TestCase, TestResult>) -> Unit) {
        connection.close()
    }

    init {

        Class.forName("org.h2.Driver")
        connection = DriverManager.getConnection("jdbc:h2:mem:test;mode=mysql")

        "simple" {


            """
                CREATE TABLE IF NOT EXISTS TB_USER (
                    name VARCHAR(10), 
                    age  INT, 
                    depart VARCHAR(200), 
                    reg_dt TIMESTAMP
                )
            """.trimIndent().toQuery().runner(connection).update()

            val sqlInsert = """
                INSERT INTO TB_USER (
                    name, age, depart, reg_dt
                ) VALUES (
                    {name}, {age}, {department}, {regDt}
                )
            """.trimIndent().toQuery()

            val now = LocalDateTime.now()

            sqlInsert.reset()
                .addParam("name", "jjake")
                .addParam("age", 13)
                .addParam("department", "HR")
                .addParam("regDt", now.plusDays(1))
                .runner(connection).update()

            connection.commit()

            val sqlSelect = """
                SELECT *
                FROM   TB_USER
            """.trimIndent().toQuery()

            val result = ArrayList<Map<String,Any?>>()

            runBlocking {
                sqlSelect.runner(connection).getAll().toCollection(result)
            }

            println(result)

        }
    }

}

data class User (
    val name: String,
    val age: Int,
    val department: String,
    val regDt: LocalDateTime,
)