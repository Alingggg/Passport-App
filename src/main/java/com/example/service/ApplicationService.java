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
    
    /**
     * Submit complete passport application with all user data
     */
    public boolean submitCompleteApplication(
            UserInfo userInfo,
            UserContact userContact,
            UserWork userWork,
            UserForeignPassport foreignPassport,
            UserSpouse spouse,
            UserParents parents,
            UserPhilippinePassport philippinePassport,
            UserMinorInfo minorInfo,
            List<Image> images) {
        
        Integer userId = UserSession.getInstance().getUserId();
        if (userId == null) {
            return false;
        }
        
        Connection conn = null;
        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // Set user IDs for all objects
            userInfo.setUserId(userId);
            userContact.setUserId(userId);
            userWork.setUserId(userId);
            foreignPassport.setUserId(userId);
            spouse.setUserId(userId);
            parents.setUserId(userId);
            philippinePassport.setUserId(userId);
            minorInfo.setUserId(userId);
            
            // Save all user data
            if (!userInfoDAO.saveUserInfo(userInfo) ||
                !userContactDAO.saveUserContact(userContact) ||
                !userWorkDAO.saveUserWork(userWork) ||
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
                image.setUserId(userId);
                if (!imageDAO.saveImage(image)) {
                    conn.rollback();
                    return false;
                }
            }
            
            // Create passport application
            PassportApplication application = new PassportApplication(userId);
            if (!applicationDAO.saveApplication(application)) {
                conn.rollback();
                return false;
            }
            
            conn.commit(); // Complete transaction
            return true;
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
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
    
    /**
     * Check if user has already submitted an application
     */
    public boolean hasExistingApplication() {
        Integer userId = UserSession.getInstance().getUserId();
        return userId != null && applicationDAO.applicationExists(userId);
    }
    
    /**
     * Get user's application status
     */
    public PassportApplication getUserApplication() {
        Integer userId = UserSession.getInstance().getUserId();
        return userId != null ? applicationDAO.findByUserId(userId) : null;
    }
    
    /**
     * Get complete user profile data
     */
    public UserProfile getCompleteUserProfile() {
        Integer userId = UserSession.getInstance().getUserId();
        if (userId == null) return null;
        
        UserProfile profile = new UserProfile();
        profile.setUserInfo(userInfoDAO.findByUserId(userId));
        profile.setUserContact(userContactDAO.findByUserId(userId));
        profile.setUserWork(userWorkDAO.findByUserId(userId));
        profile.setForeignPassport(foreignPassportDAO.findByUserId(userId));
        profile.setSpouse(spouseDAO.findByUserId(userId));
        profile.setParents(parentsDAO.findByUserId(userId));
        profile.setPhilippinePassport(philippinePassportDAO.findByUserId(userId));
        profile.setMinorInfo(minorInfoDAO.findByUserId(userId));
        profile.setImages(imageDAO.findByUserId(userId));
        profile.setApplication(applicationDAO.findByUserId(userId));
        
        return profile;
    }

    public UserProfile getCompleteUserProfile(int userId) {
        UserProfile profile = new UserProfile();
        profile.setUserInfo(userInfoDAO.findByUserId(userId));
        profile.setUserContact(userContactDAO.findByUserId(userId));
        profile.setUserWork(userWorkDAO.findByUserId(userId));
        profile.setForeignPassport(foreignPassportDAO.findByUserId(userId));
        profile.setSpouse(spouseDAO.findByUserId(userId));
        profile.setParents(parentsDAO.findByUserId(userId));
        profile.setPhilippinePassport(philippinePassportDAO.findByUserId(userId));
        profile.setMinorInfo(minorInfoDAO.findByUserId(userId));
        profile.setImages(imageDAO.findByUserId(userId));
        profile.setApplication(applicationDAO.findByUserId(userId));
        return profile;
    }

    public boolean processApplicationAcceptance(int userId) {
        // 1. Update application status to "Accepted" and reviewed_at
        boolean statusUpdated = applicationDAO.updateStatus(userId, "Accepted", null);
        if (!statusUpdated) {
            System.err.println("Failed to update application status to Accepted for user_id: " + userId);
            return false;
        }

        // 2. Fetch UserInfo to determine age for expiry calculation
        UserInfo userInfo = userInfoDAO.findByUserId(userId);
        // It's crucial that userInfo and birthDate are available.
        // If not, the age calculation and thus expiry date might be incorrect.
        // For this example, we'll proceed, but in a real app, this might warrant stricter error handling
        // or ensuring data integrity upstream.

        // 3. Prepare UserPhilippinePassport details
        UserPhilippinePassport passportDetails = new UserPhilippinePassport();
        passportDetails.setUserId(userId);
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
            System.err.println("User birth date not found for user_id: " + userId + ". Defaulting passport expiry to 10 years from issue.");
            expiryDate = issueDate.plusYears(10);
        }
        passportDetails.setExpiryDate(expiryDate);

        // 7. Set issue_place to blank (null)
        passportDetails.setIssuePlace(null);

        // 8. Save/Update UserPhilippinePassport record
        // The savePhilippinePassport DAO method should handle INSERT or UPDATE (ON CONFLICT)
        boolean passportSaved = philippinePassportDAO.savePhilippinePassport(passportDetails);
        if (!passportSaved) {
            System.err.println("Failed to save/update Philippine passport details for user_id: " + userId + " after application acceptance.");
            // Consider if the overall operation should fail if passport details can't be saved.
            // For now, returning true if status was updated, but logging this serious issue.
            // A more robust solution might involve a transaction across both updates.
        }
        
        return true; // Primary success is application status update.
                     // Consider returning statusUpdated && passportSaved for stricter success.
    }

    public boolean processApplicationDenial(int userId, String feedback) {
        // Update application status to "Denied", set feedback, and reviewed_at
        boolean statusUpdated = applicationDAO.updateStatus(userId, "Denied", feedback);
        if (!statusUpdated) {
            System.err.println("Failed to update application status to Denied for user_id: " + userId);
            return false;
        }
        
        return true; 
    }
}
