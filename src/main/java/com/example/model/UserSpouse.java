package com.example.model;

public class UserSpouse {
    private Integer applicationId;
    private String spouseFullName;
    private String spouseCitizenship;
    
    // Constructors
    public UserSpouse() {}
    
    public UserSpouse(Integer applicationId) {
        this.applicationId = applicationId;
    }
    
    // Getters and Setters
    public Integer getApplicationId() { return applicationId; }
    public void setApplicationId(Integer applicationId) { this.applicationId = applicationId; }

    public String getSpouseFullName() { return spouseFullName; }
    public void setSpouseFullName(String spouseFullName) { this.spouseFullName = spouseFullName; }
    
    public String getSpouseCitizenship() { return spouseCitizenship; }
    public void setSpouseCitizenship(String spouseCitizenship) { this.spouseCitizenship = spouseCitizenship; }
}
