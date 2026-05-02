package com.PUB.db;

import java.sql.*;

public class DB {

    private static final String URL  = "jdbc:mysql://localhost:3306/PUB_ems?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "101048mtm"; 

    private static Connection conn;

    public static Connection get() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(URL, USER, PASS);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL Driver not found!", e);
            }
        }
        return conn;
    }

    public static void close() {
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException ignored) {}
    }
}