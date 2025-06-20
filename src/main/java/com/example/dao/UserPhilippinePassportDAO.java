package com.example.dao;

import com.example.model.UserPhilippinePassport;
import com.example.util.dbUtil;

import java.sql.*;

public class UserPhilippinePassportDAO {
    
    public boolean savePhilippinePassport(UserPhilippinePassport philippinePassport) {
        String sql = "INSERT INTO user_philippine_passport (application_id, has_philippine_passport, philippine_passport_number, issue_date, issue_place, expiry_date) VALUES (?, ?, ?, ?, ?, ?) ";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, philippinePassport.getApplicationId());
            pstmt.setBoolean(2, philippinePassport.getHasPhilippinePassport());
            pstmt.setString(3, philippinePassport.getPhilippinePassportNumber());
            
            if (philippinePassport.getIssueDate() != null) {
                pstmt.setDate(4, java.sql.Date.valueOf(philippinePassport.getIssueDate()));
            } else {
                pstmt.setNull(4, java.sql.Types.DATE);
            }
            
            pstmt.setString(5, philippinePassport.getIssuePlace());
            
            if (philippinePassport.getExpiryDate() != null) {
                pstmt.setDate(6, java.sql.Date.valueOf(philippinePassport.getExpiryDate()));
            } else {
                pstmt.setNull(6, java.sql.Types.DATE);
            }
            
            return pstmt.executeUpdate() > 0;
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
                philippinePassport.setPhilippinePassportNumber(rs.getString("philippine_passport_number"));
                
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
