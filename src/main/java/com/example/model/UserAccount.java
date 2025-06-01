package com.example.model;

import java.time.LocalDateTime;

public class UserAccount {
    private Integer userId;
    private String username;
    private String password;
    private LocalDateTime createdAt;
    
    // Constructors
    public UserAccount() {}
    
    public UserAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public UserAccount(Integer userId, String username, String password, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
