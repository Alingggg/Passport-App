package com.example.model;

import java.time.LocalDate;

public class UserPhilippinePassport {
    private Integer applicationId;
    private boolean hasPhilippinePassport;
    private String oldPhilippinePassportNumber;
    private String currentPhilippinePassportNumber;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String issuePlace;

    // Constructors
    public UserPhilippinePassport() {}

    public UserPhilippinePassport(Integer applicationId) {
        this.applicationId = applicationId;
        this.hasPhilippinePassport = false;
    }

    // Getters and Setters
    public Integer getApplicationId() { return applicationId; }
    public void setApplicationId(Integer applicationId) { this.applicationId = applicationId; }

    public boolean getHasPhilippinePassport() { return hasPhilippinePassport; }
    public void setHasPhilippinePassport(boolean hasPhilippinePassport) { this.hasPhilippinePassport = hasPhilippinePassport; }

    public String getOldPhilippinePassportNumber() { return oldPhilippinePassportNumber; }
    public void setOldPhilippinePassportNumber(String oldPhilippinePassportNumber) { this.oldPhilippinePassportNumber = oldPhilippinePassportNumber; }

    public String getCurrentPhilippinePassportNumber() { return currentPhilippinePassportNumber; }
    public void setCurrentPhilippinePassportNumber(String currentPhilippinePassportNumber) { this.currentPhilippinePassportNumber = currentPhilippinePassportNumber; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getIssuePlace() { return issuePlace; }
    public void setIssuePlace(String issuePlace) { this.issuePlace = issuePlace; }

}
