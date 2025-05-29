package com.example.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.Main;
import com.example.util.dbUtil;
import com.example.util.supabaseUtil;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class UploadImageController {
    
    @FXML
    private Button uploadButton;
    
    @FXML
    private Button loadImagesButton;
    
    @FXML
    private ProgressIndicator progressIndicator;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private VBox imageContainer;
    
    @FXML
    public void initialize() {
        uploadButton.setOnAction(event -> uploadImage());
        loadImagesButton.setOnAction(event -> backToPrimary());
    }
    
    /**
     * Handle image upload to Supabase and store URL in local database
     */
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        
        File selectedFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
        
        if (selectedFile != null) {
            // Show progress indicator
            progressIndicator.setVisible(true);
            statusLabel.setText("Uploading...");
            
            // Create a background task for uploading
            Task<String> uploadTask = new Task<>() {
                @Override
                protected String call() throws Exception {
                    return supabaseUtil.uploadFile(selectedFile);
                }
            };
            
            uploadTask.setOnSucceeded(e -> {
                String fileUrl = uploadTask.getValue();
                if (fileUrl != null) {
                    // Save URL to database
                    saveImageUrlToDatabase(fileUrl);
                } else {
                    Platform.runLater(() -> {
                        progressIndicator.setVisible(false);
                        statusLabel.setText("Upload failed");
                        showAlert("Error", "Failed to upload image to Supabase");
                    });
                }
            });
            
            uploadTask.setOnFailed(e -> {
                Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    statusLabel.setText("Upload failed");
                    showAlert("Error", "Upload failed: " + uploadTask.getException().getMessage());
                    uploadTask.getException().printStackTrace();
                });
            });
            
            // Start the upload task
            new Thread(uploadTask).start();
        }
    }
    
    /**
     * Save the image URL to the local database
     */
    private void saveImageUrlToDatabase(String imageUrl) {
        try {
            Connection conn = dbUtil.getConnection();
            String query = "INSERT INTO images (supabase_url) VALUES (?)";
            
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, imageUrl);
                int rowsAffected = pstmt.executeUpdate();
                
                Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    
                    if (rowsAffected > 0) {
                        statusLabel.setText("Upload successful");
                        showAlert("Success", "Image was uploaded successfully!\nURL: " + imageUrl);
                    } else {
                        statusLabel.setText("Database update failed");
                        showAlert("Error", "Failed to save image URL to database");
                    }
                });
            }
        } catch (SQLException e) {
            Platform.runLater(() -> {
                progressIndicator.setVisible(false);
                statusLabel.setText("Database error");
                showAlert("Database Error", "Error: " + e.getMessage());
                e.printStackTrace();
            });
        }
    }
    
    /**
     * Display an alert dialog
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Return to the primary view
     */
    @FXML
    private void backToPrimary() {
        try {
            Main.setRoot("primary");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Could not return to primary view: " + e.getMessage());
        }
    }
}
