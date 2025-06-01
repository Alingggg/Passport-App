package com.example.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.Main;
import com.example.dao.ImageDAO;
import com.example.model.Image;
import com.example.util.supabaseUtil;
import com.example.util.UserSession;

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
    private Button uploadValidIdButton;
    
    @FXML
    private Button uploadBirthCertButton;
    
    @FXML
    private Button submitButton;
    
    @FXML
    private Button backButton;
    
    @FXML
    private ProgressIndicator progressIndicator;
    
    @FXML
    private Label validIdStatusLabel;
    
    @FXML
    private Label birthCertStatusLabel;
    
    @FXML
    private Label generalStatusLabel;
    
    private ImageDAO imageDAO;
    private File validIdFile;
    private File birthCertFile;
    
    @FXML
    public void initialize() {
        imageDAO = new ImageDAO();
        
        // Set up temporary user session for testing
        if (!UserSession.getInstance().isAuthenticated()) {
            UserSession.getInstance().login(1, "testuser");
        }
        
        uploadValidIdButton.setOnAction(event -> selectDocument("Valid ID"));
        uploadBirthCertButton.setOnAction(event -> selectDocument("Birth Certificate"));
        submitButton.setOnAction(event -> submitDocuments());
        backButton.setOnAction(event -> backToPrimary());
        
        // Initially disable submit button
        submitButton.setDisable(true);
    }
    
    /**
     * Handle document selection (not upload yet)
     */
    private void selectDocument(String documentType) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select " + documentType + " File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.pdf")
        );
        
        File selectedFile = fileChooser.showOpenDialog(uploadValidIdButton.getScene().getWindow());
        
        if (selectedFile != null) {
            // Store the selected file locally (don't upload yet)
            if (documentType.equals("Valid ID")) {
                validIdFile = selectedFile;
                validIdStatusLabel.setText("✓ Selected: " + selectedFile.getName());
                validIdStatusLabel.setStyle("-fx-text-fill: blue;");
            } else {
                birthCertFile = selectedFile;
                birthCertStatusLabel.setText("✓ Selected: " + selectedFile.getName());
                birthCertStatusLabel.setStyle("-fx-text-fill: blue;");
            }
            
            generalStatusLabel.setText(documentType + " file selected. Ready to submit when both documents are selected.");
            
            // Enable submit button if both documents are selected
            checkSubmitButtonState();
        }
    }
    
    /**
     * Submit both documents - upload to Supabase and save to database
     */
    private void submitDocuments() {
        if (validIdFile == null || birthCertFile == null) {
            showAlert("Error", "Please select both Valid ID and Birth Certificate files before submitting.");
            return;
        }
        
        progressIndicator.setVisible(true);
        generalStatusLabel.setText("Uploading documents to Supabase and saving to database...");
        setButtonsEnabled(false);
        
        Task<Boolean> submitTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                // Upload Valid ID to Supabase
                updateMessage("Uploading Valid ID to Supabase...");
                String validIdFileUrl = supabaseUtil.uploadFile(validIdFile);
                if (validIdFileUrl == null) {
                    throw new Exception("Failed to upload Valid ID to Supabase");
                }
                
                // Upload Birth Certificate to Supabase
                updateMessage("Uploading Birth Certificate to Supabase...");
                String birthCertFileUrl = supabaseUtil.uploadFile(birthCertFile);
                if (birthCertFileUrl == null) {
                    throw new Exception("Failed to upload Birth Certificate to Supabase");
                }
                
                // Create image records for database
                List<Image> images = new ArrayList<>();
                
                // Create Valid ID image record
                Image validIdImage = new Image();
                validIdImage.setFilename(validIdFile.getName());
                validIdImage.setFileType("Valid ID");
                validIdImage.setSupabaseUrl(validIdFileUrl);
                images.add(validIdImage);
                
                // Create Birth Certificate image record
                Image birthCertImage = new Image();
                birthCertImage.setFilename(birthCertFile.getName());
                birthCertImage.setFileType("Birth Certificate");
                birthCertImage.setSupabaseUrl(birthCertFileUrl);
                images.add(birthCertImage);
                
                // Save both images to database
                updateMessage("Saving to database...");
                boolean allSaved = true;
                for (Image image : images) {
                    if (!imageDAO.saveImage(image)) {
                        allSaved = false;
                        break;
                    }
                }
                
                if (!allSaved) {
                    throw new Exception("Failed to save some images to database");
                }
                
                return true;
            }
        };
        
        // Bind the task message to the general status label
        generalStatusLabel.textProperty().bind(submitTask.messageProperty());
        
        submitTask.setOnSucceeded(e -> {
            Platform.runLater(() -> {
                progressIndicator.setVisible(false);
                generalStatusLabel.textProperty().unbind();
                setButtonsEnabled(true);
                
                if (submitTask.getValue()) {
                    // Update status labels to show successful upload
                    validIdStatusLabel.setText("✓ Uploaded: " + validIdFile.getName());
                    validIdStatusLabel.setStyle("-fx-text-fill: green;");
                    
                    birthCertStatusLabel.setText("✓ Uploaded: " + birthCertFile.getName());
                    birthCertStatusLabel.setStyle("-fx-text-fill: green;");
                    
                    generalStatusLabel.setText("Documents uploaded and saved successfully!");
                    
                    showAlert("Success", 
                        "Both documents have been uploaded to Supabase and saved to the database!\n\n" +
                        "Valid ID: " + validIdFile.getName() + "\n" +
                        "Birth Certificate: " + birthCertFile.getName());
                    
                    // Reset the form
                    resetForm();
                } else {
                    generalStatusLabel.setText("Failed to process documents");
                    showAlert("Error", "Failed to process documents");
                }
            });
        });
        
        submitTask.setOnFailed(e -> {
            Platform.runLater(() -> {
                progressIndicator.setVisible(false);
                generalStatusLabel.textProperty().unbind();
                setButtonsEnabled(true);
                generalStatusLabel.setText("Error occurred during submission");
                showAlert("Error", "Error during submission: " + submitTask.getException().getMessage());
                submitTask.getException().printStackTrace();
            });
        });
        
        new Thread(submitTask).start();
    }
    
    /**
     * Reset the form after successful submission
     */
    private void resetForm() {
        validIdFile = null;
        birthCertFile = null;
        
        validIdStatusLabel.setText("Not selected");
        validIdStatusLabel.setStyle("-fx-text-fill: #666;");
        
        birthCertStatusLabel.setText("Not selected");
        birthCertStatusLabel.setStyle("-fx-text-fill: #666;");
        
        submitButton.setDisable(true);
        generalStatusLabel.setText("Ready to upload documents");
    }
    
    /**
     * Check if submit button should be enabled
     */
    private void checkSubmitButtonState() {
        submitButton.setDisable(validIdFile == null || birthCertFile == null);
    }
    
    /**
     * Enable/disable buttons
     */
    private void setButtonsEnabled(boolean enabled) {
        uploadValidIdButton.setDisable(!enabled);
        uploadBirthCertButton.setDisable(!enabled);
        submitButton.setDisable(!enabled || validIdFile == null || birthCertFile == null);
        backButton.setDisable(!enabled);
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
