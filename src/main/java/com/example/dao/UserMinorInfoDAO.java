package com.example.dao;

import com.example.model.UserMinorInfo;
import com.example.util.dbUtil;

import java.sql.*;

public class UserMinorInfoDAO {
    
    public boolean saveMinorInfo(UserMinorInfo minorInfo) {
        String sql = "INSERT INTO user_minor_info (application_id, is_minor, companion_full_name, companion_relationship, companion_contact_number) VALUES (?, ?, ?, ?, ?) ";

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, minorInfo.getApplicationId());
            pstmt.setBoolean(2, minorInfo.isMinor());
            pstmt.setString(3, minorInfo.getCompanionFullName());
            pstmt.setString(4, minorInfo.getCompanionRelationship());
            pstmt.setString(5, minorInfo.getCompanionContactNumber());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public UserMinorInfo findByApplicationId(Integer applicationId) {
        String sql = "SELECT * FROM user_minor_info WHERE application_id = ?";

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, applicationId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                UserMinorInfo minorInfo = new UserMinorInfo();
                minorInfo.setApplicationId(rs.getInt("application_id"));
                minorInfo.setIsMinor(rs.getBoolean("is_minor"));
                minorInfo.setCompanionFullName(rs.getString("companion_full_name"));
                minorInfo.setCompanionRelationship(rs.getString("companion_relationship"));
                minorInfo.setCompanionContactNumber(rs.getString("companion_contact_number"));
                return minorInfo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteByApplicationId(Integer applicationId) {
        String sql = "DELETE FROM user_minor_info WHERE application_id = ?";

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
