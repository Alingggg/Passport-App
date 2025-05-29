package com.example.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.Main;
import com.example.util.dbUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class TestingController {

    @FXML
    private TextField nameTextField;

    @FXML
    private void submitBtn() throws IOException {
        Main.setRoot("primary");
    }
    
    @FXML
    private void handleSubmit() {
        String name = nameTextField.getText().trim();
        
        if (name.isEmpty()) {
            showAlert("Error", "Name cannot be empty");
            return;
        }
        
        try {
            Connection conn = dbUtil.getConnection();
            String query = "INSERT INTO users (name) VALUES (?)";
            
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, name);
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    showAlert("Success", "Name '" + name + "' was added successfully!");
                    nameTextField.clear();
                } else {
                    showAlert("Error", "Failed to add name to database");
                }
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
