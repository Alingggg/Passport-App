package com.example.dao;

import com.example.model.Account;
import com.example.util.dbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    
    public boolean createAccount(Account account) {
        String sql = "INSERT INTO account (username, password, role) VALUES (?, ?, ?)";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, account.getUsername());
            pstmt.setString(2, account.getPassword());
            pstmt.setString(3, account.getRole());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Account findByUsername(String username) {
        String sql = "SELECT user_id, username, password, role, created_at FROM account WHERE username = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Account account = new Account();
                account.setUserId(rs.getInt("user_id"));
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));
                account.setRole(rs.getString("role"));
                account.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Account findById(Integer userId) {
        String sql = "SELECT user_id, username, password, role, created_at FROM account WHERE user_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Account account = new Account();
                account.setUserId(rs.getInt("user_id"));
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));
                account.setRole(rs.getString("role"));
                account.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean usernameExists(String username) {
        return findByUsername(username) != null;
    }
    
    public boolean updatePassword(Integer userId, String newPassword) {
        String sql = "UPDATE account SET password = ? WHERE user_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Account> getAllAccounts() {
        String sql = "SELECT user_id, username, password, role, created_at FROM account ORDER BY created_at DESC";
        List<Account> accounts = new ArrayList<>();
        
        try (Connection conn = dbUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Account account = new Account();
                account.setUserId(rs.getInt("user_id"));
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));
                account.setRole(rs.getString("role"));
                account.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }
    
    public List<Account> getAccountsByRole(String role) {
        String sql = "SELECT user_id, username, password, role, created_at FROM account WHERE role = ? ORDER BY created_at DESC";
        List<Account> accounts = new ArrayList<>();
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, role);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Account account = new Account();
                account.setUserId(rs.getInt("user_id"));
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));
                account.setRole(rs.getString("role"));
                account.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }
    
    public boolean deleteAccount(Integer userId) {
        String sql = "DELETE FROM account WHERE user_id = ?";
        
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