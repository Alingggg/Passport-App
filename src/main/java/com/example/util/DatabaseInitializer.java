package com.example.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseInitializer {
    
    public static void initializeDatabase() {
        try {
            // Check if tables already exist
            if (!tablesExist()) {
                System.out.println("Database tables not found. Creating schema...");
                createSchema();
                System.out.println("Database schema created successfully!");
            } else {
                System.out.println("Database schema already exists.");
            }
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static boolean tablesExist() {
        try (Connection conn = dbUtil.getConnection()) {
            // Check if the main 'account' table exists
            String checkTableSQL = "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'account')";
            try (Statement stmt = conn.createStatement()) {
                var rs = stmt.executeQuery(checkTableSQL);
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking if tables exist: " + e.getMessage());
        }
        return false;
    }
    
    private static void createSchema() throws SQLException, IOException {
        // Read schema.sql file from resources
        String schemaSQL = readSchemaFile();
        
        try (Connection conn = dbUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Execute the schema SQL
            stmt.execute(schemaSQL);
        }
    }
    
    private static String readSchemaFile() throws IOException {
        InputStream inputStream = DatabaseInitializer.class.getResourceAsStream("/com/example/db/schema.sql");
        if (inputStream == null) {
            throw new IOException("Schema file not found: /com/example/db/schema.sql");
        }
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
