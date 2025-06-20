package com.example.dao;

import com.example.model.PassportApplication;
import com.example.util.dbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PassportApplicationDAO {
    
    /**
     * Saves a new application for a user and returns the new application_id.
     * @param application The application object, containing the user_id.
     * @return The generated application_id, or -1 on failure.
     */
    public int saveApplication(PassportApplication application) {
        String sql = "INSERT INTO passport_application (application_id, reference_id, status, feedback, submitted_at, reviewed_at) VALUES (?, ?, 'Pending', NULL, CURRENT_TIMESTAMP, NULL)";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            String referenceId = "PA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            
            pstmt.setInt(1, application.getApplicationId());
            pstmt.setString(2, referenceId);
            
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Return the new application_id
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Indicate failure
    }

    public PassportApplication findByApplicationId(Integer applicationId) {
        String sql = "SELECT application_id, status, feedback, reference_id, submitted_at, reviewed_at " +
                    "FROM passport_application WHERE application_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, applicationId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                PassportApplication app = new PassportApplication();
                app.setApplicationId(rs.getInt("application_id"));
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

    public Integer findLatestApplicationIdByUserId(int userId) {
        String sql = "SELECT application_id FROM passport_application WHERE user_id = ? ORDER BY submitted_at DESC LIMIT 1";
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("application_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer findLatestAcceptedApplicationIdByUserId(int userId) {
        String sql = "SELECT application_id FROM passport_application WHERE user_id = ? AND status = 'Accepted' ORDER BY submitted_at DESC LIMIT 1";
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("application_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PassportApplication findLatestApplicationByUserId(int userId) {
        Integer latestAppId = findLatestApplicationIdByUserId(userId);
        if (latestAppId != null) {
            return findByApplicationId(latestAppId);
        }
        return null;
    }

    public boolean hasPendingApplication(int userId) {
        String sql = "SELECT 1 FROM passport_application WHERE user_id = ? AND status = 'Pending' LIMIT 1";
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStatus(Integer applicationId, String status, String feedback) {
        String sql = "UPDATE passport_application SET status = ?, feedback = ?, reviewed_at = CURRENT_TIMESTAMP WHERE application_id = ?";

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setString(2, feedback);
            pstmt.setInt(3, applicationId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<PassportApplication> getAllApplications() {
        String sql = "SELECT application_id, status, feedback, reference_id, submitted_at, reviewed_at " +
                    "FROM passport_application ORDER BY submitted_at DESC";
        List<PassportApplication> applications = new ArrayList<>();
        
        try (Connection conn = dbUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                PassportApplication app = new PassportApplication();
                app.setApplicationId(rs.getInt("application_id"));
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

    public boolean applicationExists(Integer applicationId) {
        return findByApplicationId(applicationId) != null;
    }
    
    public List<PassportApplication> getApplicationsByStatus(String status) {
        String sql = "SELECT * FROM passport_application WHERE status = ? ORDER BY submitted_at DESC";
        List<PassportApplication> applications = new ArrayList<>();
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                PassportApplication app = new PassportApplication();
                app.setApplicationId(rs.getInt("application_id"));
                applications.add(app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applications;
    }
    
    /**
     * Delete a passport application for a user.
     * @param userId The user ID whose application should be deleted.
     * @return true if a row was deleted, false otherwise.
     */
    public boolean deleteByApplicationId(Integer applicationId) {
        String sql = "DELETE FROM passport_application WHERE application_id = ?";
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