package com.example.model;

import java.time.LocalDateTime;

public class Image {
    private int id;
    private String supabaseUrl;
    private String fileName;
    private LocalDateTime uploadedAt;
    
    // Constructors
    public Image() {}
    
    public Image(String supabaseUrl, String fileName) {
        this.supabaseUrl = supabaseUrl;
        this.fileName = fileName;
        this.uploadedAt = LocalDateTime.now();
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getSupabaseUrl() { return supabaseUrl; }
    public void setSupabaseUrl(String supabaseUrl) { this.supabaseUrl = supabaseUrl; }
    
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
    
    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", uploadedAt=" + uploadedAt +
                '}';
    }
}
