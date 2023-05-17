package com.github.nayasis.kotlin.jdbc.runner

import com.github.nayasis.kotlin.jdbc.query.toQuery
import io.kotest.core.spec.style.StringSpec
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.sql.Connection
import java.sql.DriverManager

class ProcedureTest: StringSpec({

    Class.forName("org.h2.Driver")
    lateinit var connection: Connection

    beforeTest {
        connection = DriverManager.getConnection("jdbc:h2:mem:test")
    }

    afterTest {
        connection.close()
    }

    "procedure call" {

        """
            CREATE ALIAS ADD_USER FOR "com.github.nayasis.kotlin.jdbc.runner.H2TestProcedure.addUser"
        """.trimIndent().toQuery().runner(connection).execute()

        val query = """
            { CALL ADD_USER(#{name},#{age}) }
        """.trimIndent().toQuery()

        val rs = query
            .setParam("name","John Doe")
            .setParam("age",20)
            .runner(connection).call()

        assertEquals(1, rs.returns.size)
        assertEquals(2, rs.returns[0].size)
        assertEquals("John Doe", rs.returns[0][0]["name"])
        assertEquals(20, rs.returns[0][0]["age"])

    }

    "simple" {
        val rs = H2TestProcedure.addUser("test", 10)
        assertTrue(rs.next())
        assertEquals("test", rs.getString("name"))
        assertEquals(10, rs.getInt("age"))
    }

    "out parameter" {
        """
            CREATE SEQUENCE SEQ
        """.trimIndent().toQuery().runner(connection).execute()

        val query = """
            { #{seq:Int:out} = CALL NEXT VALUE FOR SEQ }
        """.trimIndent().toQuery()

        for(i in 1..20) {
            val rs = query.runner(connection).call()
            assertEquals(i, rs.out["seq"])
        }
    }

//    "out parameter sample" {
//        connection.createStatement().execute("CREATE SEQUENCE SEQ")
//        for (i in 1..19) {
//            val cs = connection.prepareCall("{ ? = CALL NEXT VALUE FOR SEQ}")
//            cs.registerOutParameter(1, Types.BIGINT)
//            cs.execute()
//            val id: Long = cs.getLong(1)
//            println(id)
//            cs.close()
//        }
//
//    }

})

data class Item(
    val seq: Int,
    val customer: String,
    val key: String,
    val validFrom: String,
    val validTo: String,
)

data class Order(
    val orderId: Int,
    val itemSeq: Int,
)