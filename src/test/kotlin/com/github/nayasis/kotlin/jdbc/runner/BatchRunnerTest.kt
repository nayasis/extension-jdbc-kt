package com.github.nayasis.kotlin.jdbc.runner

import com.github.nayasis.kotlin.jdbc.common.UserCommons
import com.github.nayasis.kotlin.jdbc.common.UserCommons.Companion.createTable
import com.github.nayasis.kotlin.jdbc.query.toBatchQuery
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.toList
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDateTime

class BatchRunnerTest: StringSpec({

    Class.forName("org.h2.Driver")
    lateinit var connection: Connection

    beforeTest {
        connection = DriverManager.getConnection("jdbc:h2:mem:test")
    }

    afterTest {
        connection.close()
    }

    "batch insert" {

        createTable(connection)

        val sqlInsert = """
            INSERT INTO TB_USER (
                name, age, depart, reg_dt
            ) VALUES (
                #{name}, #{age}, #{department}, #{regDt}
            )
        """.trimIndent().toBatchQuery()

        val now = LocalDateTime.now()

        for(i in 1..50000) {
            sqlInsert.addParam(User(
                "name-$i",
                30 + i,
                "depart-$i",
                now
            ))
        }

        sqlInsert.runner(connection).execute()

        val res = UserCommons.getAll<User>(connection).toList()

        res.size shouldBe 50000

    }

})