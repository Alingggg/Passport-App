package com.example.dao;

import com.example.model.UserContact;
import com.example.util.dbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserContactDAO {
    
    public boolean saveUserContacts(Integer userId, List<UserContact> contacts) {
        String deleteSql = "DELETE FROM user_contact WHERE user_id = ?";
        String insertSql = "INSERT INTO user_contact (user_id, mobile_number, telephone_number, email_address) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbUtil.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            // Delete existing contacts for the user
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, userId);
                deleteStmt.executeUpdate();
            }

            // Insert new contacts
            if (contacts != null && !contacts.isEmpty()) {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    for (UserContact contact : contacts) {
                        insertStmt.setInt(1, userId);
                        insertStmt.setString(2, contact.getMobileNumber());
                        insertStmt.setString(3, contact.getTelephoneNumber());
                        insertStmt.setString(4, contact.getEmailAddress());
                        insertStmt.addBatch();
                    }
                    insertStmt.executeBatch();
                }
            }

            conn.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            // Consider rolling back if connection is not auto-closed with error
            return false;
        }
    }
    
    public List<UserContact> findByUserId(Integer userId) {
        List<UserContact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM user_contact WHERE user_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                UserContact contact = new UserContact();
                contact.setContactId(rs.getInt("contact_id"));
                contact.setUserId(rs.getInt("user_id"));
                contact.setMobileNumber(rs.getString("mobile_number"));
                contact.setTelephoneNumber(rs.getString("telephone_number"));
                contact.setEmailAddress(rs.getString("email_address"));
                contacts.add(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
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
