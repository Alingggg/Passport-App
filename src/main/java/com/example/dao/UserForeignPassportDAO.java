package com.example.dao;

import com.example.model.UserForeignPassport;
import com.example.util.dbUtil;

import java.sql.*;

public class UserForeignPassportDAO {
    
    public boolean saveForeignPassport(UserForeignPassport foreignPassport) {
        String sql = "INSERT INTO user_foreign_passport (application_id, has_foreign_passport, issuing_country, foreign_passport_number) ";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, foreignPassport.getApplicationId());
            pstmt.setBoolean(2, foreignPassport.getHasForeignPassport());
            pstmt.setString(3, foreignPassport.getIssuingCountry());
            pstmt.setString(4, foreignPassport.getForeignPassportNumber());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public UserForeignPassport findByApplicationId(Integer applicationId) {
        String sql = "SELECT * FROM user_foreign_passport WHERE application_id = ?";

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, applicationId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                UserForeignPassport foreignPassport = new UserForeignPassport();
                foreignPassport.setApplicationId(rs.getInt("application_id"));
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

    public boolean deleteByApplicationId(Integer applicationId) {
        String sql = "DELETE FROM user_foreign_passport WHERE application_id = ?";

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
