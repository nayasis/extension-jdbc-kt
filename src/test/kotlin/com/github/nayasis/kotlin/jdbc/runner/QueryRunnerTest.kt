package com.github.nayasis.kotlin.jdbc.runner

import com.github.nayasis.kotlin.basica.annotation.NoArg
import com.github.nayasis.kotlin.basica.core.collection.toObject
import com.github.nayasis.kotlin.jdbc.query.toQuery
import io.kotest.core.Tuple2
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import java.math.BigInteger
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDateTime

class QueryRunnerTest: StringSpec() {

private lateinit var connection: Connection

override suspend fun beforeSpec(spec: Spec) {
    super.beforeSpec(spec)
    connection = DriverManager.getConnection("jdbc:h2:mem:test")
}

override fun afterTest(f: suspend (Tuple2<TestCase, TestResult>) -> Unit) {
    connection.close()
}

init {
    Class.forName("org.h2.Driver")
}

 init {

"simple" {

    """
        CREATE TABLE IF NOT EXISTS TB_USER (
            name VARCHAR(10) PRIMARY KEY, 
            age  INT, 
            depart VARCHAR(200), 
            reg_dt TIMESTAMP
        )
    """.trimIndent().toQuery().runner(connection).update()

    """
        DELETE FROM TB_USER
    """.trimIndent().toQuery().runner(connection).update()

    val sqlInsert = """
        INSERT INTO TB_USER (
            name, age, depart, reg_dt
        ) VALUES (
            #{name}, #{age}, #{department}, #{regDt}
        )
    """.trimIndent().toQuery()

    val now = LocalDateTime.now()

    sqlInsert.reset()
        .addParam("name", "jake")
        .addParam("age", 13)
        .addParam("department", "HR")
        .addParam("regDt", now.plusDays(1))
        .runner(connection).update()

    sqlInsert.reset().addParam(User(
        "nathen", 45, "Dev", now.plusDays(2)
    )).runner(connection).update()

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

    sqlSelect.runner(connection).getAll().map { it.toObject<User>() }.collect{
        r1.append(it)
    }
    sqlSelect.runner(connection).getAllAs<User>().collect{
        r2.append(it)
    }

    "$r1" shouldBe "$r2"

    val firstRowName = sqlSelect.runner(connection).getValue<String>()
    firstRowName shouldBe "jake"

    val sqlCount = """
        SELECT COUNT(1)
        FROM   TB_USER
    """.trimIndent().toQuery()

    var cnt = sqlCount.runner(connection).getValue<Int>()
    cnt shouldBe 2

    val sqlDelete = """
        DELETE
        FROM   TB_USER
        WHERE  name = #{name}
    """.trimIndent().toQuery()

    sqlDelete
        .addParam("name", "jake")
        .runner(connection)
        .update()

    cnt = sqlCount.runner(connection).getValue<Int>()
    cnt shouldBe 1

}

}}

@NoArg
data class User (
    var name: String? = null,
    var age: Int? = null,
    var department: String? = null,
    var regDt: LocalDateTime? = null,
)