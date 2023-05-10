package com.github.nayasis.kotlin.jdbc.runner

import com.github.nayasis.kotlin.jdbc.query.toBatchQuery
import com.github.nayasis.kotlin.jdbc.query.toQuery
import io.kotest.core.spec.style.StringSpec
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Types

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
            CREATE TABLE IF NOT EXISTS ITEMS (
                seq        INT         PRIMARY KEY,
                customer   VARCHAR(10), 
                `key`      VARCHAR(10),
                valid_from VARCHAR(10),
                valid_to   VARCHAR(10)
            )
        """.trimIndent().toQuery().runner(connection).execute()

        """
            INSERT INTO ITEMS( seq, customer, `key`, valid_from, valid_to ) VALUES (
                #{seq},
                #{customer},
                #{key},
                #{validFrom},
                #{validTo}
            )
        """.trimIndent().toBatchQuery().addParams(
            Item(1, "A", "key1", "2023-01-01", "2023-03-01"),
            Item(2, "B", "key2", "2023-01-02", "2023-03-02"),
            Item(3, "C", "key3", "2023-01-03", "2023-03-03"),
            Item(4, "D", "key4", "2023-01-04", "2023-03-04"),
        ).runner(connection).execute()

        """
            CREATE TABLE IF NOT EXISTS ORDERS (
                order_id   INT PRIMARY KEY,
                item_seq   INT
            )
        """.trimIndent().toQuery().runner(connection).execute()

        """
            INSERT INTO ORDERS( order_id, item_seq ) VALUES (
                #{orderId},
                #{itemSeq}
            )
        """.trimIndent().toBatchQuery().addParams(
            Order(1,1),
            Order(2,1),
            Order(3,1),
            Order(4,1),
            Order(21,2),
            Order(22,2),
            Order(23,2),
            Order(24,2),
            Order(31,3),
            Order(32,3),
            Order(33,3),
            Order(34,3),
            Order(41,4),
            Order(42,4),
            Order(43,4),
            Order(44,4),
        ).runner(connection).execute()


        """
            CREATE ALIAS GET_ORDERS_WITH_VALID_ITEMS AS $$
            import java.sql.Connection;
            import java.sql.PreparedStatement;
            import java.sql.ResultSet;
            import java.sql.SQLException;
            import java.util.Collection;
            import java.util.HashSet;
            import java.util.stream.Collectors;
            @CODE
            ResultSet getOrdersWithValidItems(final Connection conn, final String searchKey, final String customer, final String date) throws SQLException
            {
              final Collection<String> recordsWithoutValidity = new HashSet<String>();
              final Collection<String> validRecords = new HashSet<String>();
              // get records without validity information
              StringBuffer sql = new StringBuffer();
              sql.append("SELECT DISTINCT ITEM FROM ITEMS ");
              sql.append("WHERE CUSTOMER='" + customer + "' ");
              sql.append("AND `key`='" + searchKey + "' ");
              ;
              PreparedStatement ps = conn.prepareStatement(sql.toString());
              ResultSet results = ps.executeQuery();
              while (results.next())
                recordsWithoutValidity.add(results.getString("ITEM"));
              // get valid records
              sql = new StringBuffer();
              sql.append("SELECT TOP 1 ITEM FROM ITEMS ");
              sql.append("WHERE CUSTOMER='" + customer + "' ");
              sql.append("AND `key`='" + searchKey + "' ");
              sql.append("AND " + date + " BETWEEN VALID_FROM AND VALID_TO ");
              sql.append("ORDER BY VALID_FROM DESC;");
              ps = conn.prepareStatement(sql.toString());
              results = ps.executeQuery();
              while (results.next())
                validRecords.add(results.getString("ITEM"));
              final Collection<String> allRecords = new HashSet<String>();
              allRecords.addAll(recordsWithoutValidity);
              allRecords.addAll(validRecords);
              final String allRecordsString = allRecords.stream().map(item -> "'" + item + "'").collect(Collectors.joining(
               ","));
              sql = new StringBuffer();
              sql.append("SELECT ORDER_ID FROM ORDERS ");
              sql.append("WHERE ITEM_SEQ IN (" + allRecordsString + ")");
              ps = conn.prepareStatement(sql.toString());
              return ps.executeQuery();
            }
            $$;
        """.trimIndent().toQuery().runner(connection).execute()

        """
            CALL GET_ORDERS_WITH_VALID_ITEMS(#{key},#{customer},#{date})
        """.trimIndent().toQuery()
            .setParam("key","key1")
            .setParam("customer","A")
            .setParam("date","2023-02-01")
            .runner(connection)
                .call().let {
                    println(it)
                }

    }

    "out parameter" {
        """
            CREATE SEQUENCE SEQ
        """.trimIndent().toQuery().runner(connection).execute()

        val query = """
            { #{seq:Number:out} = CALL NEXT VALUE FOR SEQ }
        """.trimIndent().toQuery()

        for(i in 1..20) {
            val rs = query.runner(connection).call()
            println(rs)
        }
    }

    "out parameter sample" {
        connection.createStatement().execute("CREATE SEQUENCE SEQ")
        for (i in 1..19) {
            val cs = connection.prepareCall("{ ? = CALL NEXT VALUE FOR SEQ}")
            cs.registerOutParameter(1, Types.BIGINT)
            cs.execute()
            val id: Long = cs.getLong(1)
            println(id)
            cs.close()
        }

    }

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