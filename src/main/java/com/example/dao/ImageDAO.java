package com.example.dao;

import com.example.model.Image;
import com.example.util.dbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImageDAO {
    
    /**
     * Saves an image record to the database.
     * If an image for the user with the same file_type already exists, it overrides the existing entry.
     * @param image The image object to save, containing filename, fileType, and supabaseUrl.
     * @return true if successful, false otherwise.
     */
    public boolean saveImage(Integer applicationId, Image image) {
        String sql = "INSERT INTO images (application_id, filename, file_type, supabase_url, uploaded_at) " +
                     "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP) ";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, applicationId);
            pstmt.setString(2, image.getFilename());
            pstmt.setString(3, image.getFileType());
            pstmt.setString(4, image.getSupabaseUrl());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get all images from the database
     * @return List of all images
     */
    public List<Image> getAllImages() {
        List<Image> images = new ArrayList<>();
        String query = "SELECT id, application_id, filename, file_type, supabase_url, uploaded_at FROM images ORDER BY uploaded_at DESC";
        
        try (Connection conn = dbUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Image image = new Image();
                image.setId(rs.getInt("id"));
                image.setApplicationId(rs.getInt("application_id"));
                image.setFilename(rs.getString("filename"));
                image.setFileType(rs.getString("file_type"));
                image.setSupabaseUrl(rs.getString("supabase_url"));
                image.setUploadedAt(rs.getTimestamp("uploaded_at").toLocalDateTime());
                
                images.add(image);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return images;
    }
    
    /**
     * Get an image by ID
     * @param id The image ID
     * @return Image object or null if not found
     */
    public Image getImageById(int id) {
        String query = "SELECT id, application_id, filename, file_type, supabase_url, uploaded_at FROM images WHERE id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Image image = new Image();
                image.setId(rs.getInt("id"));
                image.setApplicationId(rs.getInt("application_id"));
                image.setFilename(rs.getString("filename"));
                image.setFileType(rs.getString("file_type"));
                image.setSupabaseUrl(rs.getString("supabase_url"));
                image.setUploadedAt(rs.getTimestamp("uploaded_at").toLocalDateTime());
                return image;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Delete an image from the database
     * @param id The image ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteImage(Integer imageId, Integer applicationId) {
        String sql = "DELETE FROM images WHERE id = ? AND application_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, imageId);
            pstmt.setInt(2, applicationId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Find images by application ID
     * @param applicationId The application ID
     * @return List of images for the application
     */
    public List<Image> findByApplicationId(Integer applicationId) {
        String sql = "SELECT id, application_id, filename, file_type, supabase_url, uploaded_at FROM images WHERE application_id = ?";
        List<Image> images = new ArrayList<>();
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, applicationId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Image image = new Image();
                image.setId(rs.getInt("id"));
                image.setApplicationId(rs.getInt("application_id"));
                image.setFilename(rs.getString("filename"));
                image.setFileType(rs.getString("file_type"));
                image.setSupabaseUrl(rs.getString("supabase_url"));
                image.setUploadedAt(rs.getTimestamp("uploaded_at").toLocalDateTime());
                images.add(image);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return images;
    }
    
    /**
     * Find images by file type for an application
     * @param applicationId The application ID
     * @param fileType The file type
     * @return List of images matching the file type
     */
    public List<Image> findByFileType(Integer applicationId, String fileType) {
        String sql = "SELECT id, application_id, filename, file_type, supabase_url, uploaded_at " +
                    "FROM images WHERE application_id = ? AND file_type = ?";
        List<Image> images = new ArrayList<>();
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, applicationId);
            pstmt.setString(2, fileType);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Image image = new Image();
                image.setId(rs.getInt("id"));
                image.setApplicationId(rs.getInt("application_id"));
                image.setFilename(rs.getString("filename"));
                image.setFileType(rs.getString("file_type"));
                image.setSupabaseUrl(rs.getString("supabase_url"));
                image.setUploadedAt(rs.getTimestamp("uploaded_at").toLocalDateTime());
                images.add(image);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return images;
    }

    public boolean deleteByApplicationId(Integer applicationId) {
        String sql = "DELETE FROM images WHERE application_id = ?";
        try (Connection conn = dbUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, applicationId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
