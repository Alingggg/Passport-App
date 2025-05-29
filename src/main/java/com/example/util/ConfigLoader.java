package com.example.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();
    private static boolean loaded = false;
    
    public static void loadProperties() {
        if (loaded) return;
        
        try {
            // Try to load from project root
            Path envPath = Paths.get(".env.properties");
            
            // If not found in root, try in user home directory
            if (!Files.exists(envPath)) {
                envPath = Paths.get(System.getProperty("user.home"), ".env.properties");
            }
            
            // If still not found, check classpath
            if (!Files.exists(envPath)) {
                try (InputStream in = ConfigLoader.class.getClassLoader().getResourceAsStream(".env.properties")) {
                    if (in != null) {
                        properties.load(in);
                        loaded = true;
                        return;
                    }
                }
                System.err.println("Could not find .env.properties file. Using system environment variables.");
                return;
            }
            
            // Load from file if found
            try (FileInputStream fis = new FileInputStream(envPath.toFile())) {
                properties.load(fis);
                loaded = true;
            }
        } catch (IOException e) {
            System.err.println("Error loading properties: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static String get(String key) {
        if (!loaded) {
            loadProperties();
        }
        
        // First try to get from properties file
        String value = properties.getProperty(key);
        
        // If not found, try system environment variables
        if (value == null) {
            value = System.getenv(key);
        }
        
        return value;
    }
    
    public static String get(String key, String defaultValue) {
        String value = get(key);
        return (value != null) ? value : defaultValue;
    }
}