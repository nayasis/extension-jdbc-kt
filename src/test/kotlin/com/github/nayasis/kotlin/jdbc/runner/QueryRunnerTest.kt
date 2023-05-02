package com.github.nayasis.kotlin.jdbc.runner

import com.github.nayasis.kotlin.basica.annotation.NoArg
import com.github.nayasis.kotlin.basica.core.collection.toObject
import com.github.nayasis.kotlin.jdbc.query.toQuery
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDateTime

class QueryRunnerTest: StringSpec({

    Class.forName("org.h2.Driver")
    lateinit var connection: Connection

    beforeTest {
        connection = DriverManager.getConnection("jdbc:h2:mem:test")
    }

    afterTest {
        connection.close()
    }

    suspend fun createTable() {
        """
            CREATE TABLE IF NOT EXISTS TB_USER (
                name VARCHAR(10) PRIMARY KEY, 
                age  INT, 
                depart VARCHAR(200), 
                reg_dt TIMESTAMP
            )
        """.trimIndent().toQuery().runner(connection).execute()
    }

    suspend fun deleteAll() {
        """
            DELETE FROM TB_USER
        """.trimIndent().toQuery().runner(connection).execute()
    }

    suspend fun insertUser(user: User) {
        val sqlInsert = """
            INSERT INTO TB_USER (
                name, age, depart, reg_dt
            ) VALUES (
                #{name}, #{age}, #{department}, #{regDt}
            )
        """.trimIndent().toQuery()
        sqlInsert.reset().setParam(user).runner(connection).execute()
    }

    suspend fun insertDummies() {
        val now = LocalDateTime.now()
        (1..100).map { User("name-$it", 20 + it, "dev", now.plusHours(it.toLong())) }

    }

    "simple" {

        createTable()
        deleteAll()

        val sqlInsert = """
            INSERT INTO TB_USER (
                name, age, depart, reg_dt
            ) VALUES (
                #{name}, #{age}, #{department}, #{regDt}
            )
        """.trimIndent().toQuery()

        val now = LocalDateTime.now()

        sqlInsert.reset()
            .setParam("name", "jake")
            .setParam("age", 13)
            .setParam("department", "HR")
            .setParam("regDt", now.plusDays(1))
            .runner(connection).execute()

        sqlInsert.reset().setParam(
            User( "nathen", 45, "Dev", now.plusDays(2) )
        ).runner(connection).execute()

        connection.commit()

        val sqlSelect = """
            SELECT *
            FROM   TB_USER
        """.trimIndent().toQuery()

        val res = sqlSelect.runner(connection).getAll().toList()
        println(res)

        res.size shouldBe 2

        val r1 = StringBuilder()
        val r2 = StringBuilder()

        sqlSelect.runner(connection).getAll().map { it.toObject<User>() }.collect {
            r1.append(it)
        }
        sqlSelect.runner(connection).getAllAs<User>().collect {
            r2.append(it)
        }

        "$r1" shouldBe "$r2"

        val values = sqlSelect.runner(connection).getAllAs<String?>().toList()
        println(values)
        values.size shouldBe 2

        val firstRowName = sqlSelect.runner(connection).getAs<String>()
        firstRowName shouldBe "jake"

        val sqlCount = """
            SELECT COUNT(1)
            FROM   TB_USER
        """.trimIndent().toQuery()

        var cnt = sqlCount.runner(connection).getAs<Int>()
        cnt shouldBe 2

        val sqlDelete = """
            DELETE
            FROM   TB_USER
            WHERE  name = #{name}
        """.trimIndent().toQuery()

        sqlDelete
            .setParam("name", "jake")
            .runner(connection)
            .execute()

        cnt = sqlCount.runner(connection).getAs<Int>()
        cnt shouldBe 1

    }

})

@NoArg
data class User (
    var name: String? = null,
    var age: Int? = null,
    var department: String? = null,
    var regDt: LocalDateTime? = null,
)