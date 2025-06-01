package com.example.dao;

import com.example.model.UserMinorInfo;
import com.example.util.dbUtil;

import java.sql.*;

public class UserMinorInfoDAO {
    
    public boolean saveMinorInfo(UserMinorInfo minorInfo) {
        String sql = "INSERT INTO user_minor_info (user_id, is_minor, companion_full_name, companion_relationship) " +
                    "VALUES (?, ?, ?, ?) " +
                    "ON CONFLICT (user_id) DO UPDATE SET " +
                    "is_minor = EXCLUDED.is_minor, " +
                    "companion_full_name = EXCLUDED.companion_full_name, " +
                    "companion_relationship = EXCLUDED.companion_relationship";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, minorInfo.getUserId());
            pstmt.setBoolean(2, minorInfo.getIsMinor());
            pstmt.setString(3, minorInfo.getCompanionFullName());
            pstmt.setString(4, minorInfo.getCompanionRelationship());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public UserMinorInfo findByUserId(Integer userId) {
        String sql = "SELECT * FROM user_minor_info WHERE user_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                UserMinorInfo minorInfo = new UserMinorInfo();
                minorInfo.setUserId(rs.getInt("user_id"));
                minorInfo.setIsMinor(rs.getBoolean("is_minor"));
                minorInfo.setCompanionFullName(rs.getString("companion_full_name"));
                minorInfo.setCompanionRelationship(rs.getString("companion_relationship"));
                return minorInfo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean deleteByUserId(Integer userId) {
        String sql = "DELETE FROM user_minor_info WHERE user_id = ?";
        
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
