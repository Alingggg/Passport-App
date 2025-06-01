package com.example.dao;

import com.example.model.UserForeignPassport;
import com.example.util.dbUtil;

import java.sql.*;

public class UserForeignPassportDAO {
    
    public boolean saveForeignPassport(UserForeignPassport foreignPassport) {
        String sql = "INSERT INTO user_foreign_passport (user_id, has_foreign_passport, issuing_country, foreign_passport_number) " +
                    "VALUES (?, ?, ?, ?) " +
                    "ON CONFLICT (user_id) DO UPDATE SET " +
                    "has_foreign_passport = EXCLUDED.has_foreign_passport, " +
                    "issuing_country = EXCLUDED.issuing_country, " +
                    "foreign_passport_number = EXCLUDED.foreign_passport_number";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, foreignPassport.getUserId());
            pstmt.setBoolean(2, foreignPassport.getHasForeignPassport());
            pstmt.setString(3, foreignPassport.getIssuingCountry());
            pstmt.setString(4, foreignPassport.getForeignPassportNumber());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public UserForeignPassport findByUserId(Integer userId) {
        String sql = "SELECT * FROM user_foreign_passport WHERE user_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                UserForeignPassport foreignPassport = new UserForeignPassport();
                foreignPassport.setUserId(rs.getInt("user_id"));
                foreignPassport.setHasForeignPassport(rs.getBoolean("has_foreign_passport"));
                foreignPassport.setIssuingCountry(rs.getString("issuing_country"));
                foreignPassport.setForeignPassportNumber(rs.getString("foreign_passport_number"));
                return foreignPassport;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean deleteByUserId(Integer userId) {
        String sql = "DELETE FROM user_foreign_passport WHERE user_id = ?";
        
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
