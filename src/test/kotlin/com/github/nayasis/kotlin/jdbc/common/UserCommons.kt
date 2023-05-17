package com.github.nayasis.kotlin.jdbc.common

import com.github.nayasis.kotlin.jdbc.query.toQuery
import com.github.nayasis.kotlin.jdbc.runner.User
import com.github.nayasis.kotlin.jdbc.runner.runner
import kotlinx.coroutines.flow.Flow
import java.sql.Connection
import java.time.LocalDateTime

class UserCommons { companion object {

    suspend fun createTable(connection: Connection) {
        """
            CREATE TABLE IF NOT EXISTS TB_USER (
                name VARCHAR(10) PRIMARY KEY, 
                age  INT, 
                depart VARCHAR(200), 
                reg_dt TIMESTAMP
            )
        """.trimIndent().toQuery().runner(connection).execute()
    }

    suspend fun deleteAll(connection: Connection) {
        """
            DELETE FROM TB_USER
        """.trimIndent().toQuery().runner(connection).execute()
    }

    inline fun <reified T> getAll(connection: Connection): Flow<T> {
        return """
            SELECT *
            FROM   TB_USER
        """.trimIndent().toQuery().runner(connection).getAllAs<T>()
    }

    suspend fun insertUser(user: User, connection: Connection) {
        val sqlInsert = """
            INSERT INTO TB_USER (
                name, age, depart, reg_dt
            ) VALUES (
                #{name}, #{age}, #{department}, #{regDt}
            )
        """.trimIndent().toQuery()
        sqlInsert.reset().setParam(user).runner(connection).execute()
    }

    suspend fun insertDummies(connection: Connection) {
        val now = LocalDateTime.now()
        (1..1000).map { User("name-$it", 20 + it, "dev", now.plusHours(it.toLong())) }.forEach {
            insertUser(it, connection)
        }
    }

}}

