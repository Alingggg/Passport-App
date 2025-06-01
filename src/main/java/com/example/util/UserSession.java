package com.example.util;

/**
 * Singleton class to manage user session data
 */
public class UserSession {
    private static UserSession instance;
    private Integer userId;
    private String username;
    private boolean isAuthenticated;
    
    private UserSession() {
        this.isAuthenticated = false;
    }
    
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    
    public void login(Integer userId, String username) {
        this.userId = userId;
        this.username = username;
        this.isAuthenticated = true;
    }
    
    public void logout() {
        this.userId = null;
        this.username = null;
        this.isAuthenticated = false;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public boolean isAuthenticated() {
        return isAuthenticated;
    }
    
    public boolean hasUserId() {
        return userId != null;
    }
}
