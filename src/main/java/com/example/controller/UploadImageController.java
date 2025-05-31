package com.example.controller;

import java.io.File;
import java.io.IOException;

import com.example.Main;
import com.example.dao.ImageDAO;
import com.example.model.Image;
import com.example.util.supabaseUtil;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.FileChooser;

public class UploadImageController {
    
    @FXML
    private Button uploadButton;
    
    @FXML
    private Button backButton;
    
    @FXML
    private ProgressIndicator progressIndicator;
    
    @FXML
    private Label statusLabel;
    
    private ImageDAO imageDAO;
    
    @FXML
    public void initialize() {
        imageDAO = new ImageDAO();
        uploadButton.setOnAction(event -> uploadImage());
        backButton.setOnAction(event -> backToPrimary());
    }
    
    /**
     * Handle image upload to Supabase and store in database
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
                    // Create Image object and save to database
                    Image image = new Image(fileUrl, selectedFile.getName());
                    saveImageToDatabase(image);
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
     * Save the image to the database using DAO
     */
    private void saveImageToDatabase(Image image) {
        Task<Boolean> saveTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return imageDAO.saveImage(image);
            }
        };
        
        saveTask.setOnSucceeded(e -> {
            Platform.runLater(() -> {
                progressIndicator.setVisible(false);
                
                if (saveTask.getValue()) {
                    statusLabel.setText("Upload successful");
                    showAlert("Success", "Image uploaded successfully!\nFile: " + image.getFileName());
                } else {
                    statusLabel.setText("Database save failed");
                    showAlert("Error", "Failed to save image to database");
                }
            });
        });
        
        saveTask.setOnFailed(e -> {
            Platform.runLater(() -> {
                progressIndicator.setVisible(false);
                statusLabel.setText("Database error");
                showAlert("Database Error", "Error saving to database: " + saveTask.getException().getMessage());
                saveTask.getException().printStackTrace();
            });
        });
        
        new Thread(saveTask).start();
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
