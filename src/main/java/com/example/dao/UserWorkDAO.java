package com.example.dao;

import com.example.model.UserWork;
import com.example.util.dbUtil;

import java.sql.*;

public class UserWorkDAO {
    
    public boolean saveUserWork(UserWork userWork) {
        String sql = "INSERT INTO user_work (user_id, occupation, work_address, work_telephone_number) " +
                    "VALUES (?, ?, ?, ?) " +
                    "ON CONFLICT (user_id) DO UPDATE SET " +
                    "occupation = EXCLUDED.occupation, " +
                    "work_address = EXCLUDED.work_address, " +
                    "work_telephone_number = EXCLUDED.work_telephone_number";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userWork.getUserId());
            pstmt.setString(2, userWork.getOccupation());
            pstmt.setString(3, userWork.getWorkAddress());
            pstmt.setString(4, userWork.getWorkTelephoneNumber());
            
            return pstmt.executeUpdate() > 0;
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
