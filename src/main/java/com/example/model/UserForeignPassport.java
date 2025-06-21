package com.example.model;

public class UserForeignPassport {
    private Integer applicationId;
    private boolean hasForeignPassport;
    private String issuingCountry;
    private String foreignPassportNumber;
    
    // Constructors
    public UserForeignPassport() {}
    
    public UserForeignPassport(Integer applicationId) {
        this.applicationId = applicationId;
        this.hasForeignPassport = false;
    }
    
    // Getters and Setters
    public Integer getApplicationId() { return applicationId; }
    public void setApplicationId(Integer applicationId) { this.applicationId = applicationId; }

    public boolean getHasForeignPassport() { return hasForeignPassport; }
    public void setHasForeignPassport(boolean hasForeignPassport) { this.hasForeignPassport = hasForeignPassport; }
    
    public String getIssuingCountry() { return issuingCountry; }
    public void setIssuingCountry(String issuingCountry) { this.issuingCountry = issuingCountry; }
    
    public String getForeignPassportNumber() { return foreignPassportNumber; }
    public void setForeignPassportNumber(String foreignPassportNumber) { this.foreignPassportNumber = foreignPassportNumber; }
}
