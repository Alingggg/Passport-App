package com.example.model;

public class UserWork {
    private Integer workId;
    private Integer userId;
    private String occupation;
    private String workAddress;
    private String workTelephoneNumber;
    
    // Constructors
    public UserWork() {}
    
    public UserWork(Integer userId) {
        this.userId = userId;
    }
    
    // Getters and Setters
    public Integer getWorkId() { return workId; }
    public void setWorkId(Integer workId) { this.workId = workId; }
    
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    
    public String getWorkAddress() { return workAddress; }
    public void setWorkAddress(String workAddress) { this.workAddress = workAddress; }
    
    public String getWorkTelephoneNumber() { return workTelephoneNumber; }
    public void setWorkTelephoneNumber(String workTelephoneNumber) { this.workTelephoneNumber = workTelephoneNumber; }
}
