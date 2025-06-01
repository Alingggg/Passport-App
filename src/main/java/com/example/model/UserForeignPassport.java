package com.example.model;

public class UserForeignPassport {
    private Integer userId;
    private Boolean hasForeignPassport;
    private String issuingCountry;
    private String foreignPassportNumber;
    
    // Constructors
    public UserForeignPassport() {}
    
    public UserForeignPassport(Integer userId) {
        this.userId = userId;
        this.hasForeignPassport = false;
    }
    
    // Getters and Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public Boolean getHasForeignPassport() { return hasForeignPassport; }
    public void setHasForeignPassport(Boolean hasForeignPassport) { this.hasForeignPassport = hasForeignPassport; }
    
    public String getIssuingCountry() { return issuingCountry; }
    public void setIssuingCountry(String issuingCountry) { this.issuingCountry = issuingCountry; }
    
    public String getForeignPassportNumber() { return foreignPassportNumber; }
    public void setForeignPassportNumber(String foreignPassportNumber) { this.foreignPassportNumber = foreignPassportNumber; }
}
