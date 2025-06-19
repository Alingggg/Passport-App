package com.example.dao;

import com.example.model.UserWork;
import com.example.util.dbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserWorkDAO {
    
    public boolean saveUserWorks(Integer userId, List<UserWork> works) {
        String deleteSql = "DELETE FROM user_work WHERE user_id = ?";
        String insertSql = "INSERT INTO user_work (user_id, occupation, work_address, work_telephone_number) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbUtil.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, userId);
                deleteStmt.executeUpdate();
            }

            if (works != null && !works.isEmpty()) {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    for (UserWork work : works) {
                        insertStmt.setInt(1, userId);
                        insertStmt.setString(2, work.getOccupation());
                        insertStmt.setString(3, work.getWorkAddress());
                        insertStmt.setString(4, work.getWorkTelephoneNumber());
                        insertStmt.addBatch();
                    }
                    insertStmt.executeBatch();
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<UserWork> findByUserId(Integer userId) {
        List<UserWork> works = new ArrayList<>();
        String sql = "SELECT * FROM user_work WHERE user_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                UserWork userWork = new UserWork();
                userWork.setWorkId(rs.getInt("work_id"));
                userWork.setUserId(rs.getInt("user_id"));
                userWork.setOccupation(rs.getString("occupation"));
                userWork.setWorkAddress(rs.getString("work_address"));
                userWork.setWorkTelephoneNumber(rs.getString("work_telephone_number"));
                works.add(userWork);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return works;
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