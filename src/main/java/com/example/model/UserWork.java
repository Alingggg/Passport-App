package com.example.model;

public class UserWork {
    private Integer workId;
    private Integer applicationId;
    private String occupation;
    private String workAddress;
    private String workTelephoneNumber;
    private String occupation2;
    private String workAddress2;
    private String workTelephoneNumber2;

    // Constructors
    public UserWork() {}

    public UserWork(Integer applicationId) {
        this.applicationId = applicationId;
    }

    // Getters and Setters
    public Integer getWorkId() { return workId; }
    public void setWorkId(Integer workId) { this.workId = workId; }

    public Integer getApplicationId() { return applicationId; }
    public void setApplicationId(Integer applicationId) { this.applicationId = applicationId; }

    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public String getWorkAddress() { return workAddress; }
    public void setWorkAddress(String workAddress) { this.workAddress = workAddress; }

    public String getWorkTelephoneNumber() { return workTelephoneNumber; }
    public void setWorkTelephoneNumber(String workTelephoneNumber) { this.workTelephoneNumber = workTelephoneNumber; }

    public String getOccupation2() { return occupation2; }
    public void setOccupation2(String occupation2) { this.occupation2 = occupation2; }

    public String getWorkAddress2() { return workAddress2; }
    public void setWorkAddress2(String workAddress2) { this.workAddress2 = workAddress2; }

    public String getWorkTelephoneNumber2() { return workTelephoneNumber2; }
    public void setWorkTelephoneNumber2(String workTelephoneNumber2) { this.workTelephoneNumber2 = workTelephoneNumber2; }
}
