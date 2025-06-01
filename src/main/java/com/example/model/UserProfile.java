package com.example.model;

import java.util.List;

/**
 * Wrapper class to hold all user-related data for easy handling
 */
public class UserProfile {
    private UserInfo userInfo;
    private UserContact userContact;
    private UserWork userWork;
    private UserForeignPassport foreignPassport;
    private UserSpouse spouse;
    private UserParents parents;
    private UserPhilippinePassport philippinePassport;
    private UserMinorInfo minorInfo;
    private List<Image> images;
    private PassportApplication application;
    
    // Constructors
    public UserProfile() {}
    
    // Getters and Setters
    public UserInfo getUserInfo() { return userInfo; }
    public void setUserInfo(UserInfo userInfo) { this.userInfo = userInfo; }
    
    public UserContact getUserContact() { return userContact; }
    public void setUserContact(UserContact userContact) { this.userContact = userContact; }
    
    public UserWork getUserWork() { return userWork; }
    public void setUserWork(UserWork userWork) { this.userWork = userWork; }
    
    public UserForeignPassport getForeignPassport() { return foreignPassport; }
    public void setForeignPassport(UserForeignPassport foreignPassport) { this.foreignPassport = foreignPassport; }
    
    public UserSpouse getSpouse() { return spouse; }
    public void setSpouse(UserSpouse spouse) { this.spouse = spouse; }
    
    public UserParents getParents() { return parents; }
    public void setParents(UserParents parents) { this.parents = parents; }
    
    public UserPhilippinePassport getPhilippinePassport() { return philippinePassport; }
    public void setPhilippinePassport(UserPhilippinePassport philippinePassport) { this.philippinePassport = philippinePassport; }
    
    public UserMinorInfo getMinorInfo() { return minorInfo; }
    public void setMinorInfo(UserMinorInfo minorInfo) { this.minorInfo = minorInfo; }
    
    public List<Image> getImages() { return images; }
    public void setImages(List<Image> images) { this.images = images; }
    
    public PassportApplication getApplication() { return application; }
    public void setApplication(PassportApplication application) { this.application = application; }
    
    /**
     * Check if profile is complete for submission
     */
    public boolean isComplete() {
        return userInfo != null && 
               userContact != null && 
               userWork != null && 
               foreignPassport != null && 
               spouse != null && 
               parents != null && 
               philippinePassport != null && 
               minorInfo != null &&
               images != null && !images.isEmpty();
    }
    
    /**
     * Get application status or "Not Submitted" if no application exists
     */
    public String getApplicationStatus() {
        return application != null ? application.getStatus() : "Not Submitted";
    }
}
