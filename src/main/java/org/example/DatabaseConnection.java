package org.example;
import java.sql.*;

public class DatabaseConnection {

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5433/employee_db";
        String username = "postgres";
        String password = "a1g@";

        return DriverManager.getConnection(url, username, password);
    }
}
