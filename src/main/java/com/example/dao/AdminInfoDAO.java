package com.example.dao;

import com.example.model.AdminInfo;
import com.example.util.dbUtil;

import java.sql.*;

public class AdminInfoDAO {
    
    public boolean saveAdminInfo(AdminInfo adminInfo) {
        String sql = "INSERT INTO admin_info (account_id, full_name) VALUES (?, ?) " +
                    "ON CONFLICT (account_id) DO UPDATE SET " +
                    "full_name = EXCLUDED.full_name";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, adminInfo.getAccountId());
            pstmt.setString(2, adminInfo.getFullName());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public AdminInfo findByAccountId(Integer accountId) {
        String sql = "SELECT account_id, full_name FROM admin_info WHERE account_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                AdminInfo adminInfo = new AdminInfo();
                adminInfo.setAccountId(rs.getInt("account_id"));
                adminInfo.setFullName(rs.getString("full_name"));
                return adminInfo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean deleteByAccountId(Integer accountId) {
        String sql = "DELETE FROM admin_info WHERE account_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, accountId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
