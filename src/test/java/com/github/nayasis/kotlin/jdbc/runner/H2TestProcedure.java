package com.github.nayasis.kotlin.jdbc.runner;

import org.h2.tools.SimpleResultSet;

import java.sql.ResultSet;
import java.sql.Types;

public class H2TestProcedure {

    public static ResultSet addUser(String name, Integer age) {
        SimpleResultSet rs = new SimpleResultSet();
        rs.addColumn("name", Types.VARCHAR, 255, 0);
        rs.addColumn("age", Types.INTEGER, 10, 0);
        rs.addRow(name, age);
        rs.addRow(name, age);
        return rs;
    }

}
