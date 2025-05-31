package com.example.dao;

import com.example.model.Image;
import com.example.util.dbUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ImageDAO {
    
    /**
     * Save an image record to the database
     * @param image The image object to save
     * @return true if successful, false otherwise
     */
    public boolean saveImage(Image image) {
        String query = "INSERT INTO images (supabase_url, file_name, uploaded_at) VALUES (?, ?, ?)";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, image.getSupabaseUrl());
            pstmt.setString(2, image.getFileName());
            pstmt.setTimestamp(3, Timestamp.valueOf(image.getUploadedAt()));
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Get the generated ID
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        image.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get all images from the database
     * @return List of all images
     */
    public List<Image> getAllImages() {
        List<Image> images = new ArrayList<>();
        String query = "SELECT * FROM images ORDER BY uploaded_at DESC";
        
        try (Connection conn = dbUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Image image = new Image();
                image.setId(rs.getInt("id"));
                image.setSupabaseUrl(rs.getString("supabase_url"));
                image.setFileName(rs.getString("file_name"));
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
        String query = "SELECT * FROM images WHERE id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Image image = new Image();
                image.setId(rs.getInt("id"));
                image.setSupabaseUrl(rs.getString("supabase_url"));
                image.setFileName(rs.getString("file_name"));
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
    public boolean deleteImage(int id) {
        String query = "DELETE FROM images WHERE id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
