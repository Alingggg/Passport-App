package com.example.model;

public class UserContact {
    private Integer contactId;
    private Integer userId;
    private String mobileNumber;
    private String telephoneNumber;
    private String emailAddress;
    
    // Constructors
    public UserContact() {}
    
    public UserContact(Integer userId) {
        this.userId = userId;
    }
    
    // Getters and Setters
    public Integer getContactId() { return contactId; }
    public void setContactId(Integer contactId) { this.contactId = contactId; }
    
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    
    public String getTelephoneNumber() { return telephoneNumber; }
    public void setTelephoneNumber(String telephoneNumber) { this.telephoneNumber = telephoneNumber; }
    
    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }
}
