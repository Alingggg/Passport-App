package com.example.model;

public class AdminInfo {
    private Integer accountId;
    private String fullName;
    
    // Constructors
    public AdminInfo() {}
    
    public AdminInfo(Integer accountId, String fullName) {
        this.accountId = accountId;
        this.fullName = fullName;
    }
    
    // Getters and Setters
    public Integer getAccountId() { return accountId; }
    public void setAccountId(Integer accountId) { this.accountId = accountId; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    @Override
    public String toString() {
        return String.format("AdminInfo{accountId=%d, fullName='%s'}", accountId, fullName);
    }
}
