package com.example.dao;

import com.example.model.UserContact;
import com.example.util.dbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserContactDAO {
    
    public boolean saveUserContacts(Integer applicationId, List<UserContact> contacts) {
        String insertSql = "INSERT INTO user_contact (application_id, mobile_number, telephone_number, email_address) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbUtil.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            // Insert new contacts
            if (contacts != null && !contacts.isEmpty()) {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    for (UserContact contact : contacts) {
                        insertStmt.setInt(1, applicationId);
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
    
    public List<UserContact> findByApplicationId(Integer applicationId) {
        List<UserContact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM user_contact WHERE application_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, applicationId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                UserContact contact = new UserContact();
                contact.setContactId(rs.getInt("contact_id"));
                contact.setApplicationId(rs.getInt("application_id"));
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

    public boolean deleteByApplicationId(Integer applicationId) {
        String sql = "DELETE FROM user_contact WHERE application_id = ?";
        try (Connection conn = dbUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, applicationId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
