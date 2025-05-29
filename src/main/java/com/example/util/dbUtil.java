package com.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbUtil {
    // Get connection details from configuration
    private static String getUrl() {
        return ConfigLoader.get("DB_URL", "jdbc:postgresql://localhost:5432/passport_app");
    }
    
    private static String getUser() {
        return ConfigLoader.get("DB_USER", "postgres");
    }
    
    private static String getPassword() {
        return ConfigLoader.get("DB_PASSWORD", "postgres");
    }
    
    private static Connection connection = null;
    
    /**
     * Get a database connection (creates one if it doesn't exist)
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(getUrl(), getUser(), getPassword());
        }
        return connection;
    }
    
    /**
     * Close the database connection
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}