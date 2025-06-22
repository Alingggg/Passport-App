package com.example.dao;

import com.example.model.UserPhilippinePassport;
import com.example.util.dbUtil;

import java.sql.*;

public class UserPhilippinePassportDAO {
    
    public boolean savePhilippinePassport(UserPhilippinePassport philippinePassport) {
        String sql = "INSERT INTO user_philippine_passport (application_id, has_philippine_passport, old_philippine_passport_number, current_philippine_passport_number, issue_date, issue_place, expiry_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                     "ON CONFLICT (application_id) DO UPDATE SET " +
                     "has_philippine_passport = EXCLUDED.has_philippine_passport, " +
                     "old_philippine_passport_number = EXCLUDED.old_philippine_passport_number, " +
                     "current_philippine_passport_number = EXCLUDED.current_philippine_passport_number, " +
                     "issue_date = EXCLUDED.issue_date, " +
                     "issue_place = EXCLUDED.issue_place, " +
                     "expiry_date = EXCLUDED.expiry_date";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, philippinePassport.getApplicationId());
            pstmt.setBoolean(2, philippinePassport.getHasPhilippinePassport());
            pstmt.setString(3, philippinePassport.getOldPhilippinePassportNumber());
            pstmt.setString(4, philippinePassport.getCurrentPhilippinePassportNumber());
            
            if (philippinePassport.getIssueDate() != null) {
                pstmt.setDate(5, java.sql.Date.valueOf(philippinePassport.getIssueDate()));
            } else {
                pstmt.setNull(5, java.sql.Types.DATE);
            }
            
            pstmt.setString(6, philippinePassport.getIssuePlace());
            
            if (philippinePassport.getExpiryDate() != null) {
                pstmt.setDate(7, java.sql.Date.valueOf(philippinePassport.getExpiryDate()));
            } else {
                pstmt.setNull(7, java.sql.Types.DATE);
            }
            
            pstmt.executeUpdate();
            return true; // Assume success if no exception is thrown
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public UserPhilippinePassport findByApplicationId(Integer applicationId) {
        String sql = "SELECT * FROM user_philippine_passport WHERE application_id = ?";

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, applicationId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                UserPhilippinePassport philippinePassport = new UserPhilippinePassport();
                philippinePassport.setApplicationId(rs.getInt("application_id"));
                philippinePassport.setHasPhilippinePassport(rs.getBoolean("has_philippine_passport"));
                philippinePassport.setOldPhilippinePassportNumber(rs.getString("old_philippine_passport_number"));
                philippinePassport.setCurrentPhilippinePassportNumber(rs.getString("current_philippine_passport_number"));
                
                Date issueDate = rs.getDate("issue_date");
                if (issueDate != null) {
                    philippinePassport.setIssueDate(issueDate.toLocalDate());
                }
                
                philippinePassport.setIssuePlace(rs.getString("issue_place"));

                Date expiryDate = rs.getDate("expiry_date");
                if (expiryDate != null) {
                    philippinePassport.setExpiryDate(expiryDate.toLocalDate());
                }

                return philippinePassport;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateIssuePlace(int applicationId, String issuePlace) {
        String sql = "UPDATE user_philippine_passport SET issue_place = ? WHERE application_id = ?";
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, issuePlace);
            pstmt.setInt(2, applicationId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteByApplicationId(Integer applicationId) {
        String sql = "DELETE FROM user_philippine_passport WHERE application_id = ?";

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, applicationId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
