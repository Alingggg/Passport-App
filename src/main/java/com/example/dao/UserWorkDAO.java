package com.example.dao;

import com.example.model.UserWork;
import com.example.util.dbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserWorkDAO {
    
    public boolean saveUserWorks(Integer applicationId, List<UserWork> works) {
        String insertSql = "INSERT INTO user_work (application_id, occupation, work_address, work_telephone_number) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbUtil.getConnection()) {
            conn.setAutoCommit(false);

            if (works != null && !works.isEmpty()) {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    for (UserWork work : works) {
                        insertStmt.setInt(1, applicationId);
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

    public List<UserWork> findByApplicationId(Integer applicationId) {
        List<UserWork> works = new ArrayList<>();
        String sql = "SELECT * FROM user_work WHERE application_id = ?";

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, applicationId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                UserWork userWork = new UserWork();
                userWork.setWorkId(rs.getInt("work_id"));
                userWork.setApplicationId(rs.getInt("application_id"));
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

    public boolean deleteByApplicationId(Integer applicationId) {
        String sql = "DELETE FROM user_work WHERE application_id = ?";

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