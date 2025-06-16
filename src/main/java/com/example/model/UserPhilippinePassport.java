package com.example.model;

import java.time.LocalDate;

public class UserPhilippinePassport {
    private Integer userId;
    private Boolean hasPhilippinePassport;
    private String philippinePassportNumber;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String issuePlace;

    // Constructors
    public UserPhilippinePassport() {}

    public UserPhilippinePassport(Integer userId) {
        this.userId = userId;
        this.hasPhilippinePassport = false;
    }

    // Getters and Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Boolean getHasPhilippinePassport() { return hasPhilippinePassport; }
    public void setHasPhilippinePassport(Boolean hasPhilippinePassport) { this.hasPhilippinePassport = hasPhilippinePassport; }

    public String getPhilippinePassportNumber() { return philippinePassportNumber; }
    public void setPhilippinePassportNumber(String philippinePassportNumber) { this.philippinePassportNumber = philippinePassportNumber; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getIssuePlace() { return issuePlace; }
    public void setIssuePlace(String issuePlace) { this.issuePlace = issuePlace; }

}
