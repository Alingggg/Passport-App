package com.example.dao;

import com.example.model.UserContact;
import com.example.util.dbUtil;

import java.sql.*;

public class UserContactDAO {
    
    public boolean saveUserContact(UserContact contact) {
        String sql = "INSERT INTO user_contact (user_id, mobile_number, telephone_number, email_address) " +
                    "VALUES (?, ?, ?, ?) " +
                    "ON CONFLICT (user_id) DO UPDATE SET " +
                    "mobile_number = EXCLUDED.mobile_number, " +
                    "telephone_number = EXCLUDED.telephone_number, " +
                    "email_address = EXCLUDED.email_address";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, contact.getUserId());
            pstmt.setString(2, contact.getMobileNumber());
            pstmt.setString(3, contact.getTelephoneNumber());
            pstmt.setString(4, contact.getEmailAddress());
            
            return pstmt.executeUpdate() > 0;
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
}
