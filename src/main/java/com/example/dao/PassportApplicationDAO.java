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
        // Corrected SQL to insert user_id and set is_card_received to false by default.
        // The application_id is generated automatically by the database.
        String sql = "INSERT INTO passport_application (user_id, reference_id, status, feedback, submitted_at, reviewed_at, is_card_received) VALUES (?, ?, 'Pending', NULL, CURRENT_TIMESTAMP, NULL, FALSE)";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            String referenceId = "PA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            
            // Use getUserId() instead of getApplicationId()
            pstmt.setInt(1, application.getUserId());
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
        String sql = "SELECT application_id, user_id, status, feedback, reference_id, submitted_at, reviewed_at, is_card_received " +
                    "FROM passport_application WHERE application_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, applicationId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                PassportApplication app = new PassportApplication();
                app.setApplicationId(rs.getInt("application_id"));
                app.setUserId(rs.getInt("user_id"));
                app.setStatus(rs.getString("status"));
                app.setFeedback(rs.getString("feedback"));
                app.setReferenceId(rs.getString("reference_id"));
                app.setSubmittedAt(rs.getTimestamp("submitted_at").toLocalDateTime());
                
                Timestamp reviewedAt = rs.getTimestamp("reviewed_at");
                if (reviewedAt != null) {
                    app.setReviewedAt(reviewedAt.toLocalDateTime());
                }
                app.setCardReceived(rs.getBoolean("is_card_received"));
                
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

    public boolean hasAcceptedApplication(int userId) {
        String sql = "SELECT 1 FROM passport_application WHERE user_id = ? AND status = 'Accepted' LIMIT 1";
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Returns true if a record is found
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

    public boolean updateStatusToCancelled(Integer applicationId) {
        String sql = "UPDATE passport_application SET status = 'Cancelled' WHERE application_id = ?";

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, applicationId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<PassportApplication> getApplicationsByStatus(String status) {
        List<PassportApplication> applications = new ArrayList<>();
        // Order by oldest first to process them in the order they were received
        String sql = "SELECT * FROM passport_application WHERE status = ? ORDER BY submitted_at ASC";

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                PassportApplication app = new PassportApplication();
                app.setApplicationId(rs.getInt("application_id"));
                app.setUserId(rs.getInt("user_id"));
                app.setStatus(rs.getString("status"));
                app.setFeedback(rs.getString("feedback"));
                app.setReferenceId(rs.getString("reference_id"));
                app.setSubmittedAt(rs.getTimestamp("submitted_at").toLocalDateTime());
                
                Timestamp reviewedAt = rs.getTimestamp("reviewed_at");
                if (reviewedAt != null) {
                    app.setReviewedAt(reviewedAt.toLocalDateTime());
                }
                app.setCardReceived(rs.getBoolean("is_card_received"));
                
                applications.add(app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applications;
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
    
    public List<PassportApplication> getLatestAcceptedApplicationForEachUser() {
        List<PassportApplication> applications = new ArrayList<>();
        String sql = "SELECT application_id, user_id, status, feedback, reference_id, submitted_at, reviewed_at, is_card_received " +
                     "FROM ( " +
                     "    SELECT *, " +
                     "           ROW_NUMBER() OVER(PARTITION BY user_id ORDER BY submitted_at DESC) as rn " +
                     "    FROM passport_application " +
                     "    WHERE status = 'Accepted' " +
                     ") t " +
                     "WHERE rn = 1 ORDER BY submitted_at DESC";

        try (Connection conn = dbUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PassportApplication app = new PassportApplication();
                app.setApplicationId(rs.getInt("application_id"));
                app.setUserId(rs.getInt("user_id"));
                app.setStatus(rs.getString("status"));
                app.setFeedback(rs.getString("feedback"));
                app.setReferenceId(rs.getString("reference_id"));
                app.setSubmittedAt(rs.getTimestamp("submitted_at").toLocalDateTime());
                
                Timestamp reviewedAt = rs.getTimestamp("reviewed_at");
                if (reviewedAt != null) {
                    app.setReviewedAt(reviewedAt.toLocalDateTime());
                }
                app.setCardReceived(rs.getBoolean("is_card_received"));
                
                applications.add(app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applications;
    }
    
    public List<PassportApplication> getLatestExpiredApplicationForEachUser() {
        List<PassportApplication> applications = new ArrayList<>();
        String sql = "SELECT pa.* " +
                     "FROM ( " +
                     "    SELECT *, ROW_NUMBER() OVER(PARTITION BY user_id ORDER BY submitted_at DESC) as rn " +
                     "    FROM passport_application " +
                     "    WHERE status = 'Accepted' " +
                     ") pa " +
                     "JOIN user_philippine_passport upp ON pa.application_id = upp.application_id " +
                     "WHERE pa.rn = 1 AND upp.expiry_date < CURRENT_DATE " +
                     "ORDER BY pa.submitted_at DESC";

        try (Connection conn = dbUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PassportApplication app = new PassportApplication();
                app.setApplicationId(rs.getInt("application_id"));
                app.setUserId(rs.getInt("user_id"));
                app.setStatus(rs.getString("status"));
                app.setFeedback(rs.getString("feedback"));
                app.setReferenceId(rs.getString("reference_id"));
                app.setSubmittedAt(rs.getTimestamp("submitted_at").toLocalDateTime());
                
                Timestamp reviewedAt = rs.getTimestamp("reviewed_at");
                if (reviewedAt != null) {
                    app.setReviewedAt(reviewedAt.toLocalDateTime());
                }
                app.setCardReceived(rs.getBoolean("is_card_received"));
                
                applications.add(app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applications;
    }

    public List<PassportApplication> getLatestCardPendingApplicationForEachUser() {
        List<PassportApplication> applications = new ArrayList<>();
        String sql = "SELECT * " +
                     "FROM ( " +
                     "    SELECT *, ROW_NUMBER() OVER(PARTITION BY user_id ORDER BY submitted_at DESC) as rn " +
                     "    FROM passport_application " +
                     "    WHERE status = 'Accepted' AND is_card_received = false " +
                     ") t " +
                     "WHERE rn = 1 ORDER BY submitted_at DESC";

        try (Connection conn = dbUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PassportApplication app = new PassportApplication();
                app.setApplicationId(rs.getInt("application_id"));
                app.setUserId(rs.getInt("user_id"));
                app.setStatus(rs.getString("status"));
                app.setFeedback(rs.getString("feedback"));
                app.setReferenceId(rs.getString("reference_id"));
                app.setSubmittedAt(rs.getTimestamp("submitted_at").toLocalDateTime());
                
                Timestamp reviewedAt = rs.getTimestamp("reviewed_at");
                if (reviewedAt != null) {
                    app.setReviewedAt(reviewedAt.toLocalDateTime());
                }
                app.setCardReceived(rs.getBoolean("is_card_received"));
                
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

    public boolean updateCardReceivedStatus(int applicationId, boolean isReceived) {
        String sql = "UPDATE passport_application SET is_card_received = ? WHERE application_id = ?";
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, isReceived);
            pstmt.setInt(2, applicationId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}