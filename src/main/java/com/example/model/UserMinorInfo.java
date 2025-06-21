package com.example.model;

public class UserMinorInfo {
    private Integer applicationId;
    private boolean isMinor;
    private String companionFullName;
    private String companionRelationship;
    private String companionContactNumber;

    // Constructors
    public UserMinorInfo() {}

    // Getters and Setters
    public Integer getApplicationId() { return applicationId; }
    public void setApplicationId(Integer applicationId) { this.applicationId = applicationId; }

    public boolean isMinor() { return isMinor; }
    public void setIsMinor(boolean isMinor) { this.isMinor = isMinor; }

    public String getCompanionFullName() { return companionFullName; }
    public void setCompanionFullName(String companionFullName) { this.companionFullName = companionFullName; }

    public String getCompanionRelationship() { return companionRelationship; }
    public void setCompanionRelationship(String companionRelationship) { this.companionRelationship = companionRelationship; }

    public String getCompanionContactNumber() { return companionContactNumber; }
    public void setCompanionContactNumber(String companionContactNumber) { this.companionContactNumber = companionContactNumber; }
}
