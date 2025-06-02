package com.example.model;

import java.time.LocalDateTime;

public class Account {
    private Integer userId;
    private String username;
    private String password;
    private String role;
    private LocalDateTime createdAt;
    
    // Constructors
    public Account() {}
    
    public Account(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    public Account(Integer userId, String username, String password, String role, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    // Role checking methods
    public boolean isUser() { return "user".equals(role); }
    public boolean isAdmin() { return "admin".equals(role); }
}