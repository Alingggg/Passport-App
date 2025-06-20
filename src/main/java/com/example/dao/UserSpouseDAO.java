package com.example.dao;

import com.example.model.UserSpouse;
import com.example.util.dbUtil;

import java.sql.*;

public class UserSpouseDAO {
    
    public boolean saveSpouse(UserSpouse spouse) {
        String sql = "INSERT INTO user_spouse (application_id, spouse_full_name, spouse_citizenship) VALUES (?, ?, ?)";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, spouse.getApplicationId());
            pstmt.setString(2, spouse.getSpouseFullName());
            pstmt.setString(3, spouse.getSpouseCitizenship());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public UserSpouse findByApplicationId(Integer applicationId) {
        String sql = "SELECT * FROM user_spouse WHERE application_id = ?";

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, applicationId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                UserSpouse spouse = new UserSpouse();
                spouse.setApplicationId(rs.getInt("application_id"));
                spouse.setSpouseFullName(rs.getString("spouse_full_name"));
                spouse.setSpouseCitizenship(rs.getString("spouse_citizenship"));
                return spouse;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteByApplicationId(Integer applicationId) {
        String sql = "DELETE FROM user_spouse WHERE application_id = ?";

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
