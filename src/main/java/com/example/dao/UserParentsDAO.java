package com.example.dao;

import com.example.model.UserParents;
import com.example.util.dbUtil;

import java.sql.*;

public class UserParentsDAO {
    
    public boolean saveParents(UserParents parents) {
        String sql = "INSERT INTO user_parents (application_id, father_full_name, father_citizenship, mother_maiden_name, mother_citizenship) VALUES (?, ?, ?, ?, ?) ";

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, parents.getApplicationId());
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

    public UserParents findByApplicationId(Integer applicationId) {
        String sql = "SELECT * FROM user_parents WHERE application_id = ?";

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, applicationId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                UserParents parents = new UserParents();
                parents.setApplicationId(rs.getInt("application_id"));
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

    public boolean deleteByApplicationId(Integer applicationId) {
        String sql = "DELETE FROM user_parents WHERE application_id = ?";

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
