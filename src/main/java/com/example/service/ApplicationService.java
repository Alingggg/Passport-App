package com.example.service;

import com.example.dao.*;
import com.example.model.*;
import com.example.util.UserSession;
import com.example.util.dbUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Random;

public class ApplicationService {
    
    private UserInfoDAO userInfoDAO = new UserInfoDAO();
    private UserContactDAO userContactDAO = new UserContactDAO();
    private UserWorkDAO userWorkDAO = new UserWorkDAO();
    private UserForeignPassportDAO foreignPassportDAO = new UserForeignPassportDAO();
    private UserSpouseDAO spouseDAO = new UserSpouseDAO();
    private UserParentsDAO parentsDAO = new UserParentsDAO();
    private UserPhilippinePassportDAO philippinePassportDAO = new UserPhilippinePassportDAO();
    private UserMinorInfoDAO minorInfoDAO = new UserMinorInfoDAO();
    private PassportApplicationDAO applicationDAO = new PassportApplicationDAO();
    private ImageDAO imageDAO = new ImageDAO();
    
    // Submit complete passport application with all user data
    public boolean submitCompleteApplication(
            UserInfo userInfo,
            List<UserContact> userContacts,
            List<UserWork> userWorks,
            UserForeignPassport foreignPassport,
            UserSpouse spouse,
            UserParents parents,
            UserPhilippinePassport philippinePassport,
            UserMinorInfo minorInfo,
            List<Image> images) {
        
        Integer userId = UserSession.getInstance().getUserId();
        if (userId == null) return false;
        
        Connection conn = null;
        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Create a new application record first to get an application_id
            PassportApplication application = new PassportApplication(userId);
            int applicationId = applicationDAO.saveApplication(application);
            if (applicationId == -1) {
                conn.rollback();
                return false;
            }

            // 2. Set the new application_id on all child objects
            userInfo.setApplicationId(applicationId); // Assuming you add this setter
            foreignPassport.setApplicationId(applicationId);
            spouse.setApplicationId(applicationId);
            parents.setApplicationId(applicationId);
            philippinePassport.setApplicationId(applicationId);
            minorInfo.setApplicationId(applicationId);
            
            // 3. Save all child records, which are now simple INSERTS
            if (!userInfoDAO.saveUserInfo(userInfo) ||
                !userContactDAO.saveUserContacts(applicationId, userContacts) ||
                !userWorkDAO.saveUserWorks(applicationId, userWorks) ||
                !foreignPassportDAO.saveForeignPassport(foreignPassport) ||
                !spouseDAO.saveSpouse(spouse) ||
                !parentsDAO.saveParents(parents) ||
                !philippinePassportDAO.savePhilippinePassport(philippinePassport) ||
                !minorInfoDAO.saveMinorInfo(minorInfo)) {
                conn.rollback();
                return false;
            }
            
            // Save images
            for (Image image : images) {
                image.setApplicationId(applicationId); // Set application ID for images
                if (!imageDAO.saveImage(applicationId, image)) {
                    conn.rollback();
                    return false;
                }
            }
            
            conn.commit(); // Complete transaction
            return true;
            
        } catch (Exception e) { // Changed from SQLException to Exception to catch all errors
            if (conn != null) {
                try {
                    System.err.println("Transaction is being rolled back due to an error.");
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error during transaction rollback: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    // Checks if the current user has an application with 'Pending' status.
    public boolean hasPendingApplication() {
        Integer userId = UserSession.getInstance().getUserId();
        return userId != null && applicationDAO.hasPendingApplication(userId);
    }
    
    /**
     * Checks if the current user has at least one application with 'Accepted' status.
     * This is a lightweight check for routing purposes.
     */
    public boolean hasAcceptedApplication() {
        Integer userId = UserSession.getInstance().getUserId();
        return userId != null && applicationDAO.hasAcceptedApplication(userId);
    }

    /**
     * Gets the most recent application for the current user, regardless of status.
     * This is useful for checking the status of the latest submission.
     */
    public PassportApplication getLatestApplication() {
        Integer userId = UserSession.getInstance().getUserId();
        if (userId == null) return null;
        return applicationDAO.findLatestApplicationByUserId(userId);
    }
    
    // Gets the complete profile for the user's latest ONGOING application (e.g., Pending, Denied).
    public UserProfile getLatestOngoingApplicationProfile() {
        Integer userId = UserSession.getInstance().getUserId();
        if (userId == null) return null;
        
        Integer latestAppId = applicationDAO.findLatestApplicationIdByUserId(userId);
        if (latestAppId == null) return null;

        return getCompleteUserProfile(latestAppId);
    }

    /**
     * Gets the complete profile for the user's latest ACCEPTED application.
     * This represents their current, official passport information.
     */
    public UserProfile getLatestAcceptedUserProfile() {
        Integer userId = UserSession.getInstance().getUserId();
        if (userId == null) return null;

        Integer acceptedAppId = applicationDAO.findLatestAcceptedApplicationIdByUserId(userId);
        if (acceptedAppId == null) return null;

        return getCompleteUserProfile(acceptedAppId);
    }

    public boolean updateCompleteUserProfile(UserProfile profile) {
        Integer userId = UserSession.getInstance().getUserId();
        if (userId == null) {
            return false;
        }

        // 1. Update UserInfo
        UserInfo userInfo = profile.getUserInfo();
        userInfo.setApplicationId(userId);
        userInfoDAO.saveUserInfo(userInfo);

        // Save contacts
        userContactDAO.saveUserContacts(userId, profile.getUserContacts());

        // Save work info
        userWorkDAO.saveUserWorks(userId, profile.getUserWorks());

        // Save foreign passport info
        UserForeignPassport foreignPassport = profile.getForeignPassport();
        if (foreignPassport != null) {
            foreignPassport.setApplicationId(userId);
            foreignPassportDAO.saveForeignPassport(foreignPassport);
        }

        // Save spouse info
        UserSpouse spouse = profile.getSpouse();
        if (spouse != null) {
            spouse.setApplicationId(userId);
            spouseDAO.saveSpouse(spouse);
        }

        // Save parents info
        UserParents parents = profile.getParents();
        if (parents != null) {
            parents.setApplicationId(userId);
            parentsDAO.saveParents(parents);
        }

        // Save Philippine passport info
        UserPhilippinePassport philippinePassport = profile.getPhilippinePassport();
        if (philippinePassport != null) {
            philippinePassport.setApplicationId(userId);
            philippinePassportDAO.savePhilippinePassport(philippinePassport);
        }

        // Save minor info
        UserMinorInfo minorInfo = profile.getMinorInfo();
        if (minorInfo != null) {
            minorInfo.setApplicationId(userId);
            minorInfoDAO.saveMinorInfo(minorInfo);
        }

        // 2. Handle images (assuming images are part of the profile)
        List<Image> images = profile.getImages();
        if (images != null) {
            // Delete old images if any
            imageDAO.deleteByApplicationId(userId);
            // Save new images
            for (Image image : images) {
                image.setApplicationId(userId);
                imageDAO.saveImage(userId, image);
            }
        }

        return true;
    }

    public UserProfile getCompleteUserProfile(int applicationId) {
        UserProfile profile = new UserProfile();
        profile.setUserInfo(userInfoDAO.findByApplicationId(applicationId));
        profile.setUserContacts(userContactDAO.findByApplicationId(applicationId));
        profile.setUserWorks(userWorkDAO.findByApplicationId(applicationId));
        profile.setForeignPassport(foreignPassportDAO.findByApplicationId(applicationId));
        profile.setSpouse(spouseDAO.findByApplicationId(applicationId));
        profile.setParents(parentsDAO.findByApplicationId(applicationId));
        profile.setPhilippinePassport(philippinePassportDAO.findByApplicationId(applicationId));
        profile.setMinorInfo(minorInfoDAO.findByApplicationId(applicationId));
        profile.setImages(imageDAO.findByApplicationId(applicationId));
        profile.setApplication(applicationDAO.findByApplicationId(applicationId));
        return profile;
    }

    public boolean processApplicationAcceptance(int applicationId) {
        // 1. Update application status to "Accepted" and reviewed_at
        boolean statusUpdated = applicationDAO.updateStatus(applicationId, "Accepted", null);
        if (!statusUpdated) {
            System.err.println("Failed to update application status to Accepted for application_id: " + applicationId);
            return false;
        }

        // 2. Fetch UserInfo to determine age for expiry calculation
        UserInfo userInfo = userInfoDAO.findByApplicationId(applicationId);
        // It's crucial that userInfo and birthDate are available.
        // If not, the age calculation and thus expiry date might be incorrect.
        // For this example, we'll proceed, but in a real app, this might warrant stricter error handling
        // or ensuring data integrity upstream.

        // 3. Prepare UserPhilippinePassport details
        UserPhilippinePassport passportDetails = new UserPhilippinePassport();
        passportDetails.setApplicationId(applicationId);
        passportDetails.setHasPhilippinePassport(true); // Mark as having a Philippine passport

        // 4. Generate passport number: "P" + 7 random digits
        Random random = new Random();
        int randomNumber = 1000000 + random.nextInt(9000000); // Generates a 7-digit number (1,000,000 to 9,999,999)
        String passportNumber = "P" + randomNumber;
        passportDetails.setPhilippinePassportNumber(passportNumber);

        // 5. Set issue_date to current date (date of acceptance)
        LocalDate issueDate = LocalDate.now();
        passportDetails.setIssueDate(issueDate);

        // 6. Calculate expiry_date based on issue_date and user's age at time of issue
        LocalDate expiryDate;
        if (userInfo != null && userInfo.getBirthDate() != null) {
            int ageAtIssue = Period.between(userInfo.getBirthDate(), issueDate).getYears();
            if (ageAtIssue < 18) {
                expiryDate = issueDate.plusYears(5); // 5-year validity for minors
            } else {
                expiryDate = issueDate.plusYears(10); // 10-year validity for adults
            }
        } else {
            // Fallback if age cannot be determined (e.g., default to 10 years)
            // This scenario should ideally be prevented by ensuring complete user data.
            System.err.println("User birth date not found for user_id: " + applicationId + ". Defaulting passport expiry to 10 years from issue.");
            expiryDate = issueDate.plusYears(10);
        }
        passportDetails.setExpiryDate(expiryDate);

        // 7. Set issue_place to blank (null)
        passportDetails.setIssuePlace(null);

        // 8. Save/Update UserPhilippinePassport record
        // The savePhilippinePassport DAO method should handle INSERT or UPDATE (ON CONFLICT)
        boolean passportSaved = philippinePassportDAO.savePhilippinePassport(passportDetails);
        if (!passportSaved) {
            System.err.println("Failed to save/update Philippine passport details for user_id: " + applicationId + " after application acceptance.");
            // Consider if the overall operation should fail if passport details can't be saved.
        }
        
        return true;
    }

    public boolean processApplicationDenial(int applicationId, String feedback) {
        // Update application status to "Denied", set feedback, and reviewed_at
        boolean statusUpdated = applicationDAO.updateStatus(applicationId, "Denied", feedback);
        if (!statusUpdated) {
            System.err.println("Failed to update application status to Denied for application_id: " + applicationId);
            return false;
        }
        
        return true; 
    }

    public boolean cancelApplication(int applicationId) {
        return applicationDAO.updateStatusToCancelled(applicationId);
    }

    public boolean setCardReceived(int applicationId) {
        return applicationDAO.updateCardReceivedStatus(applicationId, true);
    }

    /**
     * Delete all user application data for a given userId.
     * This removes all related records from all user-related tables.
     */
    public boolean deleteCompleteApplication(int applicationId) {
        Connection conn = null;
        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false);

            // Delete in order: child tables first, then parent (application)
            boolean imagesDeleted = imageDAO.deleteByApplicationId(applicationId);
            boolean minorInfoDeleted = minorInfoDAO.deleteByApplicationId(applicationId);
            boolean philippinePassportDeleted = philippinePassportDAO.deleteByApplicationId(applicationId);
            boolean parentsDeleted = parentsDAO.deleteByApplicationId(applicationId);
            boolean spouseDeleted = spouseDAO.deleteByApplicationId(applicationId);
            boolean foreignPassportDeleted = foreignPassportDAO.deleteByApplicationId(applicationId);
            boolean userWorkDeleted = userWorkDAO.deleteByApplicationId(applicationId);
            boolean userContactDeleted = userContactDAO.deleteByApplicationId(applicationId);
            boolean userInfoDeleted = userInfoDAO.deleteByApplicationId(applicationId);
            boolean applicationDeleted = applicationDAO.deleteByApplicationId(applicationId);

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
}
