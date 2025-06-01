package com.example.dao;

import com.example.model.PassportApplication;
import com.example.util.dbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PassportApplicationDAO {
    
    public boolean createApplication(PassportApplication application) {
        String sql = "INSERT INTO passport_application (user_id, reference_id) VALUES (?, ?)";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Generate a unique reference ID
            String referenceId = "PA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            
            pstmt.setInt(1, application.getUserId());
            pstmt.setString(2, referenceId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public PassportApplication findByUserId(Integer userId) {
        String sql = "SELECT user_id, status, feedback, reference_id, submitted_at, reviewed_at " +
                    "FROM passport_application WHERE user_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                PassportApplication app = new PassportApplication();
                app.setUserId(rs.getInt("user_id"));
                app.setStatus(rs.getString("status"));
                app.setFeedback(rs.getString("feedback"));
                app.setReferenceId(rs.getString("reference_id"));
                app.setSubmittedAt(rs.getTimestamp("submitted_at").toLocalDateTime());
                
                Timestamp reviewedAt = rs.getTimestamp("reviewed_at");
                if (reviewedAt != null) {
                    app.setReviewedAt(reviewedAt.toLocalDateTime());
                }
                
                return app;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updateStatus(Integer userId, String status, String feedback) {
        String sql = "UPDATE passport_application SET status = ?, feedback = ?, reviewed_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setString(2, feedback);
            pstmt.setInt(3, userId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<PassportApplication> getAllApplications() {
        String sql = "SELECT user_id, status, feedback, reference_id, submitted_at, reviewed_at " +
                    "FROM passport_application ORDER BY submitted_at DESC";
        List<PassportApplication> applications = new ArrayList<>();
        
        try (Connection conn = dbUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                PassportApplication app = new PassportApplication();
                app.setUserId(rs.getInt("user_id"));
                app.setStatus(rs.getString("status"));
                app.setFeedback(rs.getString("feedback"));
                app.setReferenceId(rs.getString("reference_id"));
                app.setSubmittedAt(rs.getTimestamp("submitted_at").toLocalDateTime());
                
                Timestamp reviewedAt = rs.getTimestamp("reviewed_at");
                if (reviewedAt != null) {
                    app.setReviewedAt(reviewedAt.toLocalDateTime());
                }
                
                applications.add(app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applications;
    }
    
    public boolean applicationExists(Integer userId) {
        return findByUserId(userId) != null;
    }
}