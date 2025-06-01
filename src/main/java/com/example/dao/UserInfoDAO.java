package com.example.dao;

import com.example.model.UserInfo;
import com.example.util.dbUtil;

import java.sql.*;

public class UserInfoDAO {
    
    public boolean saveUserInfo(UserInfo userInfo) {
        String sql = "INSERT INTO user_info (user_id, last_name, first_name, middle_name, birth_place, " +
                    "birth_date, gender, civil_status, current_address, acquired_citizenship) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON CONFLICT (user_id) DO UPDATE SET " +
                    "last_name = EXCLUDED.last_name, first_name = EXCLUDED.first_name, " +
                    "middle_name = EXCLUDED.middle_name, birth_place = EXCLUDED.birth_place, " +
                    "birth_date = EXCLUDED.birth_date, gender = EXCLUDED.gender, " +
                    "civil_status = EXCLUDED.civil_status, current_address = EXCLUDED.current_address, " +
                    "acquired_citizenship = EXCLUDED.acquired_citizenship";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userInfo.getUserId());
            pstmt.setString(2, userInfo.getLastName());
            pstmt.setString(3, userInfo.getFirstName());
            pstmt.setString(4, userInfo.getMiddleName());
            pstmt.setString(5, userInfo.getBirthPlace());
            pstmt.setDate(6, Date.valueOf(userInfo.getBirthDate()));
            pstmt.setString(7, userInfo.getGender());
            pstmt.setString(8, userInfo.getCivilStatus());
            pstmt.setString(9, userInfo.getCurrentAddress());
            pstmt.setString(10, userInfo.getAcquiredCitizenship());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public UserInfo findByUserId(Integer userId) {
        String sql = "SELECT * FROM user_info WHERE user_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(rs.getInt("user_id"));
                userInfo.setLastName(rs.getString("last_name"));
                userInfo.setFirstName(rs.getString("first_name"));
                userInfo.setMiddleName(rs.getString("middle_name"));
                userInfo.setBirthPlace(rs.getString("birth_place"));
                userInfo.setBirthDate(rs.getDate("birth_date").toLocalDate());
                userInfo.setGender(rs.getString("gender"));
                userInfo.setCivilStatus(rs.getString("civil_status"));
                userInfo.setCurrentAddress(rs.getString("current_address"));
                userInfo.setAcquiredCitizenship(rs.getString("acquired_citizenship"));
                return userInfo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean deleteByUserId(Integer userId) {
        String sql = "DELETE FROM user_info WHERE user_id = ?";
        
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
