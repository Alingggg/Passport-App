package com.example.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.UUID;

/**
 * Utility for interacting with Supabase Storage
 */
public class supabaseUtil {
    // Get Supabase connection details from configuration
    private static String getSupabaseUrl() {
        return ConfigLoader.get("SUPABASE_URL");
    }
    
    private static String getSupabaseApiKey() {
        return ConfigLoader.get("SUPABASE_API_KEY");
    }
    
    private static String getStorageBucket() {
        return ConfigLoader.get("SUPABASE_STORAGE_BUCKET");
    }
    
    private static final HttpClient client = HttpClient.newHttpClient();
    
    /**
     * Upload a file to Supabase Storage
     * 
     * @param file The file to upload
     * @return The URL of the uploaded file or null if upload failed
     * @throws IOException If file reading fails
     */
    public static String uploadFile(File file) throws IOException {
        // Generate a unique filename to prevent collisions
        String fileName = file.getName()
            .replaceAll("\\s+", "_")           // Replace spaces with underscores
            .replaceAll("[^a-zA-Z0-9._-]", "") // Keep only safe characters
            .toLowerCase();
        
        String uniqueFileName = UUID.randomUUID().toString() + "-" + fileName;
        
        byte[] fileContent = Files.readAllBytes(file.toPath());
        String contentType = Files.probeContentType(file.toPath());
        if (contentType == null) {
            // Default to image/jpeg if type can't be determined
            contentType = "image/jpeg";
        }
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(getSupabaseUrl() + "/storage/v1/object/" + getStorageBucket() + "/" + uniqueFileName))
            .header("apikey", getSupabaseApiKey())
            .header("Authorization", "Bearer " + getSupabaseApiKey())
            .header("Content-Type", contentType)
            .header("Cache-Control", "3600")
            .PUT(HttpRequest.BodyPublishers.ofByteArray(fileContent))
            .build();
        
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                // Return the public URL for the uploaded file
                return getSupabaseUrl() + "/storage/v1/object/public/" + getStorageBucket() + "/" + uniqueFileName;
            } else {
                System.err.println("Error uploading file: " + response.body());
                System.err.println("Status code: " + response.statusCode());
                return null;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Upload interrupted: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get a list of files in the storage bucket
     * 
     * @return JSON string containing file information
     */
    public static String listFiles() {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(getSupabaseUrl() + "/storage/v1/object/list/" + getStorageBucket()))
            .header("apikey", getSupabaseApiKey())
            .header("Authorization", "Bearer " + getSupabaseApiKey())
            .GET()
            .build();
            
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Delete a file from Supabase Storage
     * 
     * @param fileUrl The full URL of the file to delete
     * @return true if deletion was successful, false otherwise
     */
    public static boolean deleteFile(String fileUrl) {
        // Extract filename from URL
        String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(getSupabaseUrl() + "/storage/v1/object/" + getStorageBucket() + "/" + fileName))
            .header("apikey", getSupabaseApiKey())
            .header("Authorization", "Bearer " + getSupabaseApiKey())
            .DELETE()
            .build();
            
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}