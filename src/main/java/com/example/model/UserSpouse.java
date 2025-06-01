package com.example.model;

public class UserSpouse {
    private Integer userId;
    private String spouseFullName;
    private String spouseCitizenship;
    
    // Constructors
    public UserSpouse() {}
    
    public UserSpouse(Integer userId) {
        this.userId = userId;
    }
    
    // Getters and Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public String getSpouseFullName() { return spouseFullName; }
    public void setSpouseFullName(String spouseFullName) { this.spouseFullName = spouseFullName; }
    
    public String getSpouseCitizenship() { return spouseCitizenship; }
    public void setSpouseCitizenship(String spouseCitizenship) { this.spouseCitizenship = spouseCitizenship; }
}
