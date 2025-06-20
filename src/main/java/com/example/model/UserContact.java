package com.example.model;

public class UserContact {
    private Integer contactId;
    private Integer applicationId;
    private String mobileNumber;
    private String telephoneNumber;
    private String emailAddress;
    private String mobileNumber2;
    private String telephoneNumber2;
    private String emailAddress2;
    
    // Constructors
    public UserContact() {}
    
    public UserContact(Integer applicationId) {
        this.applicationId = applicationId;
    }
    
    // Getters and Setters
    public Integer getContactId() { return contactId; }
    public void setContactId(Integer contactId) { this.contactId = contactId; }
    
    public Integer getApplicationId() { return applicationId; }
    public void setApplicationId(Integer applicationId) { this.applicationId = applicationId; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    
    public String getTelephoneNumber() { return telephoneNumber; }
    public void setTelephoneNumber(String telephoneNumber) { this.telephoneNumber = telephoneNumber; }
    
    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    public String getMobileNumber2() { return mobileNumber2; }
    public void setMobileNumber2(String mobileNumber2) { this.mobileNumber2 = mobileNumber2; }

    public String getTelephoneNumber2() { return telephoneNumber2; }
    public void setTelephoneNumber2(String telephoneNumber2) { this.telephoneNumber2 = telephoneNumber2; }

    public String getEmailAddress2() { return emailAddress2; }
    public void setEmailAddress2(String emailAddress2) { this.emailAddress2 = emailAddress2; }
}
