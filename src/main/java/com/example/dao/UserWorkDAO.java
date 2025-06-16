package com.example.dao;

import com.example.model.UserWork;
import com.example.util.dbUtil;

import java.sql.*;

public class UserWorkDAO {
    
    public boolean saveUserWork(UserWork userWork) {
        // First check if a record exists for this user
        String checkSql = "SELECT COUNT(*) FROM user_work WHERE user_id = ?";
        String insertSql = "INSERT INTO user_work (user_id, occupation, work_address, work_telephone_number) " +
                      "VALUES (?, ?, ?, ?)";
        String updateSql = "UPDATE user_work SET " +
                      "occupation = ?, work_address = ?, work_telephone_number = ? " +
                      "WHERE user_id = ?";
        
        try (Connection conn = dbUtil.getConnection()) {
            // Check if record exists
            boolean exists;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, userWork.getUserId());
                ResultSet rs = checkStmt.executeQuery();
                rs.next();
                exists = rs.getInt(1) > 0;
            }
            
            // Either insert or update based on existence
            if (exists) {
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, userWork.getOccupation());
                    updateStmt.setString(2, userWork.getWorkAddress());
                    updateStmt.setString(3, userWork.getWorkTelephoneNumber());
                    updateStmt.setInt(4, userWork.getUserId());
                    return updateStmt.executeUpdate() > 0;
                }
            } else {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, userWork.getUserId());
                    insertStmt.setString(2, userWork.getOccupation());
                    insertStmt.setString(3, userWork.getWorkAddress());
                    insertStmt.setString(4, userWork.getWorkTelephoneNumber());
                    return insertStmt.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public UserWork findByUserId(Integer userId) {
        String sql = "SELECT * FROM user_work WHERE user_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                UserWork userWork = new UserWork();
                userWork.setWorkId(rs.getInt("work_id"));
                userWork.setUserId(rs.getInt("user_id"));
                userWork.setOccupation(rs.getString("occupation"));
                userWork.setWorkAddress(rs.getString("work_address"));
                userWork.setWorkTelephoneNumber(rs.getString("work_telephone_number"));
                return userWork;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean deleteByUserId(Integer userId) {
        String sql = "DELETE FROM user_work WHERE user_id = ?";
        
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