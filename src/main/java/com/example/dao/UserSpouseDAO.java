package com.example.dao;

import com.example.model.UserSpouse;
import com.example.util.dbUtil;

import java.sql.*;

public class UserSpouseDAO {
    
    public boolean saveSpouse(UserSpouse spouse) {
        String sql = "INSERT INTO user_spouse (user_id, spouse_full_name, spouse_citizenship) " +
                    "VALUES (?, ?, ?) " +
                    "ON CONFLICT (user_id) DO UPDATE SET " +
                    "spouse_full_name = EXCLUDED.spouse_full_name, " +
                    "spouse_citizenship = EXCLUDED.spouse_citizenship";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, spouse.getUserId());
            pstmt.setString(2, spouse.getSpouseFullName());
            pstmt.setString(3, spouse.getSpouseCitizenship());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public UserSpouse findByUserId(Integer userId) {
        String sql = "SELECT * FROM user_spouse WHERE user_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                UserSpouse spouse = new UserSpouse();
                spouse.setUserId(rs.getInt("user_id"));
                spouse.setSpouseFullName(rs.getString("spouse_full_name"));
                spouse.setSpouseCitizenship(rs.getString("spouse_citizenship"));
                return spouse;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean deleteByUserId(Integer userId) {
        String sql = "DELETE FROM user_spouse WHERE user_id = ?";
        
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
