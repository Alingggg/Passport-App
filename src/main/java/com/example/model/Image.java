package com.example.model;

import java.time.LocalDateTime;

public class Image {
    private Integer id;
    private Integer applicationId;
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
    
    public Image(Integer applicationId, String filename, String fileType, String supabaseUrl) {
        this.applicationId = applicationId;
        this.filename = filename;
        this.fileType = fileType;
        this.supabaseUrl = supabaseUrl;
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Integer getApplicationId() { return applicationId; }
    public void setApplicationId(Integer applicationId) { this.applicationId = applicationId; }
    
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    
    public String getSupabaseUrl() { return supabaseUrl; }
    public void setSupabaseUrl(String supabaseUrl) { this.supabaseUrl = supabaseUrl; }
    
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}
