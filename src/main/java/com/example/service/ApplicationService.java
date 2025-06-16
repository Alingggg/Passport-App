package com.example.service;

import com.example.dao.*;
import com.example.model.*;
import com.example.util.UserSession;
import com.example.util.dbUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
            if (!applicationDAO.createApplication(application)) {
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
}
