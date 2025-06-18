package com.example.dao;

import com.example.model.UserContact;
import com.example.util.dbUtil;

import java.sql.*;

public class UserContactDAO {
    
    public boolean saveUserContact(UserContact contact) {
        // First check if a record exists for this user
        String checkSql = "SELECT COUNT(*) FROM user_contact WHERE user_id = ?";
        String insertSql = "INSERT INTO user_contact (user_id, mobile_number, telephone_number, email_address) " +
                      "VALUES (?, ?, ?, ?)";
        String updateSql = "UPDATE user_contact SET " +
                      "mobile_number = ?, telephone_number = ?, email_address = ? " +
                      "WHERE user_id = ?";
        
        try (Connection conn = dbUtil.getConnection()) {
            // Check if record exists
            boolean exists;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, contact.getUserId());
                ResultSet rs = checkStmt.executeQuery();
                rs.next();
                exists = rs.getInt(1) > 0;
            }
            
            // Either insert or update based on existence
            if (exists) {
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, contact.getMobileNumber());
                    updateStmt.setString(2, contact.getTelephoneNumber());
                    updateStmt.setString(3, contact.getEmailAddress());
                    updateStmt.setInt(4, contact.getUserId());
                    return updateStmt.executeUpdate() > 0;
                }
            } else {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, contact.getUserId());
                    insertStmt.setString(2, contact.getMobileNumber());
                    insertStmt.setString(3, contact.getTelephoneNumber());
                    insertStmt.setString(4, contact.getEmailAddress());
                    return insertStmt.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public UserContact findByUserId(Integer userId) {
        String sql = "SELECT * FROM user_contact WHERE user_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                UserContact contact = new UserContact();
                contact.setContactId(rs.getInt("contact_id"));
                contact.setUserId(rs.getInt("user_id"));
                contact.setMobileNumber(rs.getString("mobile_number"));
                contact.setTelephoneNumber(rs.getString("telephone_number"));
                contact.setEmailAddress(rs.getString("email_address"));
                return contact;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteByUserId(Integer userId) {
        String sql = "DELETE FROM user_contact WHERE user_id = ?";
        try (Connection conn = dbUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
