package com.example.dao;

import com.example.model.UserPhilippinePassport;
import com.example.util.dbUtil;

import java.sql.*;

public class UserPhilippinePassportDAO {
    
    public boolean savePhilippinePassport(UserPhilippinePassport philippinePassport) {
        String sql = "INSERT INTO user_philippine_passport (user_id, has_philippine_passport, philippine_passport_number, issue_date, issue_place) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON CONFLICT (user_id) DO UPDATE SET " +
                    "has_philippine_passport = EXCLUDED.has_philippine_passport, " +
                    "philippine_passport_number = EXCLUDED.philippine_passport_number, " +
                    "issue_date = EXCLUDED.issue_date, " +
                    "issue_place = EXCLUDED.issue_place";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, philippinePassport.getUserId());
            pstmt.setBoolean(2, philippinePassport.getHasPhilippinePassport());
            pstmt.setString(3, philippinePassport.getPhilippinePassportNumber());
            
            if (philippinePassport.getIssueDate() != null) {
                pstmt.setDate(4, Date.valueOf(philippinePassport.getIssueDate()));
            } else {
                pstmt.setNull(4, Types.DATE);
            }
            
            pstmt.setString(5, philippinePassport.getIssuePlace());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public UserPhilippinePassport findByUserId(Integer userId) {
        String sql = "SELECT * FROM user_philippine_passport WHERE user_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                UserPhilippinePassport philippinePassport = new UserPhilippinePassport();
                philippinePassport.setUserId(rs.getInt("user_id"));
                philippinePassport.setHasPhilippinePassport(rs.getBoolean("has_philippine_passport"));
                philippinePassport.setPhilippinePassportNumber(rs.getString("philippine_passport_number"));
                
                Date issueDate = rs.getDate("issue_date");
                if (issueDate != null) {
                    philippinePassport.setIssueDate(issueDate.toLocalDate());
                }
                
                philippinePassport.setIssuePlace(rs.getString("issue_place"));
                return philippinePassport;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean deleteByUserId(Integer userId) {
        String sql = "DELETE FROM user_philippine_passport WHERE user_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
