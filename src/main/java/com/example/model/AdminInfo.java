package com.example.model;

public class AdminInfo {
    private Integer accountId;
    private String adminId;
    private String fullName;
    
    // Constructors
    public AdminInfo() {}
    
    public AdminInfo(Integer accountId, String adminId, String fullName) {
        this.accountId = accountId;
        this.adminId = adminId;
        this.fullName = fullName;
    }
    
    // Getters and Setters
    public Integer getAccountId() { return accountId; }
    public void setAccountId(Integer accountId) { this.accountId = accountId; }
    
    public String getAdminId() { return adminId; }
    public void setAdminId(String adminId) { this.adminId = adminId; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    @Override
    public String toString() {
        return String.format("AdminInfo{accountId=%d, adminId='%s', fullName='%s'}", 
                             accountId, adminId, fullName);
    }
}
