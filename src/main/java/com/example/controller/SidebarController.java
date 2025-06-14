package com.example.controller;

import com.example.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class SidebarController {
    
    // Define an enum for the different types of users
    public enum UserType {
        REGULAR_USER,
        ADMIN
    }
    
    private UserType userType = UserType.REGULAR_USER; // Default
    
    @FXML
    private Button btnProfile;
    
    @FXML
    private Button btnApplication;
    
    @FXML
    private Button btnLogout;
    
    // Method to set the active button/tab
    public void setActiveTab(String tabId) {
        // Reset all buttons to default style
        btnProfile.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-background-radius: 15;");
        btnApplication.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-background-radius: 15;");
        
        // Set the active button style
        if ("profile".equals(tabId)) {
            btnProfile.setStyle("-fx-background-color: white; -fx-text-fill: #40659C; -fx-background-radius: 15;");
        } else if ("application".equals(tabId)) {
            btnApplication.setStyle("-fx-background-color: white; -fx-text-fill: #40659C; -fx-background-radius: 15;");
        }
    }
    
    // Set the user type to customize sidebar behavior
    public void setUserType(UserType type) {
        this.userType = type;
    }
    
    @FXML
    void profileBtn(ActionEvent event) {
        try {
            if (userType == UserType.ADMIN) {
                Main.setRoot("AdminUsers");
            } else {
                Main.setRoot("UserProfile");
            }
        } catch (IOException e) {
            System.err.println("Navigation error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void applicationBtn(ActionEvent event) {
        try {
            if (userType == UserType.ADMIN) {
                Main.setRoot("AdminApplications");
            } else {
                Main.setRoot("UserApplication");
            }
        } catch (IOException e) {
            System.err.println("Navigation error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void logOutBtn(ActionEvent event) {
        try {
            Main.setRoot("LandingPage");
        } catch (IOException e) {
            System.err.println("Error loading LandingPage.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
