package com.example.util;

import com.example.dao.AdminInfoDAO;
import com.example.model.AdminInfo;

public class UserSession {
    private static UserSession instance;
    private Integer userId;
    private String username;
    private String role;
    private boolean authenticated;
    
    // Cache admin info to avoid repeated DB calls
    private AdminInfo adminInfo;
    private AdminInfoDAO adminInfoDAO;
    
    private UserSession() {
        this.authenticated = false;
        this.adminInfoDAO = new AdminInfoDAO();
    }
    
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    
    public void login(Integer userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.authenticated = true;
        
        // Clear cached admin info on new login
        this.adminInfo = null;
    }
    
    public void login(Integer userId, String username) {
        // For backward compatibility - defaults to 'user' role
        this.login(userId, username, "user");
    }
    
    public void logout() {
        this.userId = null;
        this.username = null;
        this.role = null;
        this.authenticated = false;
        this.adminInfo = null;
    }
    
    // Getters
    public Integer getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public boolean isAuthenticated() { return authenticated; }
    
    // Role checking methods
    public boolean isUser() { return "user".equals(role); }
    public boolean isAdmin() { return "admin".equals(role); }
    
    /**
     * Get admin full name (only for admin users)
     * Returns cached value or fetches from database
     */
    public String getAdminFullName() {
        if (!isAdmin() || userId == null) {
            return null;
        }
        
        // Return cached value if available
        if (adminInfo != null) {
            return adminInfo.getFullName();
        }
        
        // Fetch from database and cache
        adminInfo = adminInfoDAO.findByAccountId(userId);
        return adminInfo != null ? adminInfo.getFullName() : "Admin User";
    }
    
    /**
     * Get display name based on role
     * - For admins: returns full name from admin_info
     * - For users: returns username (they have UserInfo for detailed profile)
     */
    public String getDisplayName() {
        if (isAdmin()) {
            String fullName = getAdminFullName();
            return fullName != null ? fullName : username;
        } else {
            return username;
        }
    }
    
    /**
     * Clear cached admin info (call when admin info is updated)
     */
    public void clearAdminCache() {
        this.adminInfo = null;
    }
    
    // For debugging/testing
    @Override
    public String toString() {
        return String.format("UserSession{userId=%d, username='%s', role='%s', authenticated=%b}", 
                           userId, username, role, authenticated);
    }
}
