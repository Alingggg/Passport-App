package com.example.model;

import java.time.LocalDate;

public class UserInfo {
    private Integer applicationId;
    private String lastName;
    private String firstName;
    private String middleName;
    private String birthPlace;
    private LocalDate birthDate;
    private String gender;
    private String civilStatus;
    private String currentAddress;
    private String acquiredCitizenship;
    
    // Constructors
    public UserInfo() {}
    
    public UserInfo(Integer applicationId) {
        this.applicationId = applicationId;
    }
    
    // Getters and Setters
    public Integer getApplicationId() { return applicationId; }
    public void setApplicationId(Integer applicationId) { this.applicationId = applicationId; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    
    public String getBirthPlace() { return birthPlace; }
    public void setBirthPlace(String birthPlace) { this.birthPlace = birthPlace; }
    
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getCivilStatus() { return civilStatus; }
    public void setCivilStatus(String civilStatus) { this.civilStatus = civilStatus; }
    
    public String getCurrentAddress() { return currentAddress; }
    public void setCurrentAddress(String currentAddress) { this.currentAddress = currentAddress; }
    
    public String getAcquiredCitizenship() { return acquiredCitizenship; }
    public void setAcquiredCitizenship(String acquiredCitizenship) { this.acquiredCitizenship = acquiredCitizenship; }
    
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (firstName != null) fullName.append(firstName);
        if (middleName != null && !middleName.trim().isEmpty()) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(middleName);
        }
        if (lastName != null) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(lastName);
        }
        return fullName.toString();
    }
}
