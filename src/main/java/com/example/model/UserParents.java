package com.example.model;

public class UserParents {
    private Integer userId;
    private String fatherFullName;
    private String fatherCitizenship;
    private String motherMaidenName;
    private String motherCitizenship;
    
    // Constructors
    public UserParents() {}
    
    public UserParents(Integer userId) {
        this.userId = userId;
    }
    
    // Getters and Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public String getFatherFullName() { return fatherFullName; }
    public void setFatherFullName(String fatherFullName) { this.fatherFullName = fatherFullName; }
    
    public String getFatherCitizenship() { return fatherCitizenship; }
    public void setFatherCitizenship(String fatherCitizenship) { this.fatherCitizenship = fatherCitizenship; }
    
    public String getMotherMaidenName() { return motherMaidenName; }
    public void setMotherMaidenName(String motherMaidenName) { this.motherMaidenName = motherMaidenName; }
    
    public String getMotherCitizenship() { return motherCitizenship; }
    public void setMotherCitizenship(String motherCitizenship) { this.motherCitizenship = motherCitizenship; }
}
