package com.example.model;

public class UserMinorInfo {
    private Integer userId;
    private Boolean isMinor;
    private String companionFullName;
    private String companionRelationship;
    private String companionContactNumber;
    
    // Constructors
    public UserMinorInfo() {}
    
    public UserMinorInfo(Integer userId) {
        this.userId = userId;
        this.isMinor = false;
    }
    
    // Getters and Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public Boolean getIsMinor() { return isMinor; }
    public void setIsMinor(Boolean isMinor) { this.isMinor = isMinor; }
    
    public String getCompanionFullName() { return companionFullName; }
    public void setCompanionFullName(String companionFullName) { this.companionFullName = companionFullName; }
    
    public String getCompanionRelationship() { return companionRelationship; }
    public void setCompanionRelationship(String companionRelationship) { this.companionRelationship = companionRelationship; }
    
    public String getCompanionContactNumber() { return companionContactNumber; }
    public void setCompanionContactNumber(String companionContactNumber) { this.companionContactNumber = companionContactNumber; }
}
