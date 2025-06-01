package com.example.dao;

import com.example.model.UserParents;
import com.example.util.dbUtil;

import java.sql.*;

public class UserParentsDAO {
    
    public boolean saveParents(UserParents parents) {
        String sql = "INSERT INTO user_parents (user_id, father_full_name, father_citizenship, mother_maiden_name, mother_citizenship) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON CONFLICT (user_id) DO UPDATE SET " +
                    "father_full_name = EXCLUDED.father_full_name, " +
                    "father_citizenship = EXCLUDED.father_citizenship, " +
                    "mother_maiden_name = EXCLUDED.mother_maiden_name, " +
                    "mother_citizenship = EXCLUDED.mother_citizenship";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, parents.getUserId());
            pstmt.setString(2, parents.getFatherFullName());
            pstmt.setString(3, parents.getFatherCitizenship());
            pstmt.setString(4, parents.getMotherMaidenName());
            pstmt.setString(5, parents.getMotherCitizenship());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public UserParents findByUserId(Integer userId) {
        String sql = "SELECT * FROM user_parents WHERE user_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                UserParents parents = new UserParents();
                parents.setUserId(rs.getInt("user_id"));
                parents.setFatherFullName(rs.getString("father_full_name"));
                parents.setFatherCitizenship(rs.getString("father_citizenship"));
                parents.setMotherMaidenName(rs.getString("mother_maiden_name"));
                parents.setMotherCitizenship(rs.getString("mother_citizenship"));
                return parents;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean deleteByUserId(Integer userId) {
        String sql = "DELETE FROM user_parents WHERE user_id = ?";
        
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
