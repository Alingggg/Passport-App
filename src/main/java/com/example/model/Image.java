package com.example.model;

import java.time.LocalDateTime;

public class Image {
    private Integer id;
    private Integer userId;
    private String filename;
    private String fileType;
    private String supabaseUrl;
    private LocalDateTime uploadedAt;
    
    // Constructors
    public Image() {}
    
    public Image(String supabaseUrl, String filename) {
        this.supabaseUrl = supabaseUrl;
        this.filename = filename;
    }
    
    public Image(Integer userId, String filename, String fileType, String supabaseUrl) {
        this.userId = userId;
        this.filename = filename;
        this.fileType = fileType;
        this.supabaseUrl = supabaseUrl;
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    
    public String getSupabaseUrl() { return supabaseUrl; }
    public void setSupabaseUrl(String supabaseUrl) { this.supabaseUrl = supabaseUrl; }
    
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}
