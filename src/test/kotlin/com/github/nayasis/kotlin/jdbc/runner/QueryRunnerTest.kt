package com.github.nayasis.kotlin.jdbc.runner

import com.github.nayasis.kotlin.basica.annotation.NoArg
import com.github.nayasis.kotlin.basica.core.collection.toObject
import com.github.nayasis.kotlin.jdbc.common.UserCommons
import com.github.nayasis.kotlin.jdbc.query.toQuery
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import java.math.BigDecimal
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

    "simple" {

        UserCommons.createTable(connection)
        UserCommons.deleteAll(connection)

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

    "key in multiple reference" {

        """
            CREATE TABLE IF NOT EXISTS TB_USER (
                name VARCHAR(10) PRIMARY KEY, 
                age1  INT, 
                age2  BIGINT, 
                age3  NUMERIC(20,2)
            )
        """.trimIndent().toQuery().runner(connection).execute()

        UserCommons.createTable(connection)

        val sqlInsert = """
            INSERT INTO TB_USER (
                name, age1, age2, age3
            ) VALUES (
                #{name}, #{name:Int}, #{name:Long}, #{name:Decimal}
            )
        """.trimIndent().toQuery()

        for(i in 1..9) {
            sqlInsert.setParam("name", i * 0.5).runner(connection).execute()
        }

        val res = UserCommons.getAll<UserMultipleReference>(connection).toList()

        println(res.joinToString("\n"))

        res.size shouldBe 9

        res.sumOf { it.age1 ?: 0 } shouldBe 20
        res.sumOf { it.age2 ?: 0 } shouldBe 25L
        res.sumOf { it.age3 ?: BigDecimal.ZERO } shouldBe BigDecimal.valueOf( 22.5)

        res.first().name shouldBe "0.5"
        res.last().name shouldBe "4.5"

    }

})

@NoArg
data class User (
    var name: String? = null,
    var age: Int? = null,
    var department: String? = null,
    var regDt: LocalDateTime? = null,
)

@NoArg
data class UserMultipleReference (
    var name: String? = null,
    var age1: Int? = null,
    var age2: Long? = null,
    var age3: BigDecimal? = null,
)