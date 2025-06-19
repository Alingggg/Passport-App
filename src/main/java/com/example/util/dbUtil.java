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
    
    // Get a new database connection
    public static Connection getConnection() throws SQLException {
        // Always return a new connection
        return DriverManager.getConnection(getUrl(), getUser(), getPassword());
    }
    
    /**
     * Close the database connection - This method is no longer appropriate
     * for a utility that doesn't manage a static connection.
     * Connections should be closed by the code that acquires them.
     */
    public static void closeConnection() {
        // This method should ideally be removed or log a warning.
        // For now, let's make it a no-op or log.
        System.err.println("Warning: dbUtil.closeConnection() called. Connections should be managed and closed by the calling code (e.g., using try-with-resources).");
    }
}