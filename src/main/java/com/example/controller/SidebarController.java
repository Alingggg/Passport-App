package com.example.controller;

import com.example.Main;
import com.example.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class SidebarController {
    
    @FXML
    private Button btnUserProfile;
    
    @FXML
    private Button btnUserApplication;
    
    @FXML
    private Button btnAdminUsers;
    
    @FXML
    private Button btnAdminApplications;
    
    @FXML
    private Button btnLogout;
    
    private final UserSession userSession = UserSession.getInstance();
    
    @FXML
    public void initialize() {
        // Set up the sidebar based on user role
        configureForUserRole();
    }
    
    /**
     * Configure the sidebar based on the user's role from UserSession
     */
    private void configureForUserRole() {
        boolean isAdmin = userSession.isAdmin();
        
        // Set visibility based on role
        btnUserProfile.setVisible(!isAdmin);
        btnUserApplication.setVisible(!isAdmin);
        btnAdminUsers.setVisible(isAdmin);
        btnAdminApplications.setVisible(isAdmin);
        
        System.out.println("Sidebar initialized for role: " + userSession.getRole());
    }
    
    /**
     * Set the active tab for the current user type
     */
    public void setActiveTab(String tabId) {
        // Reset all buttons to default style
        String defaultStyle = "-fx-background-color: transparent; -fx-text-fill: white; -fx-background-radius: 15;";
        String activeStyle = "-fx-background-color: white; -fx-text-fill: #40659C; -fx-background-radius: 15;";
        
        btnUserProfile.setStyle(defaultStyle);
        btnUserApplication.setStyle(defaultStyle);
        btnAdminUsers.setStyle(defaultStyle);
        btnAdminApplications.setStyle(defaultStyle);
        
        // Set the active button style based on role and tab ID
        if (userSession.isAdmin()) {
            if ("users".equals(tabId)) {
                btnAdminUsers.setStyle(activeStyle);
            } else if ("applications".equals(tabId)) {
                btnAdminApplications.setStyle(activeStyle);
            }
        } else {
            if ("profile".equals(tabId)) {
                btnUserProfile.setStyle(activeStyle);
            } else if ("application".equals(tabId)) {
                btnUserApplication.setStyle(activeStyle);
            }
        }
    }
    
    @FXML
    void profileBtn(ActionEvent event) {
        try {
            Main.setRoot("User/UserProfile");
        } catch (IOException e) {
            System.err.println("Navigation error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void applicationBtn(ActionEvent event) {
        try {
            // Here you can check application status to determine where to route
            Main.setRoot("User/UserApplicationPending");  // Or another appropriate page
        } catch (IOException e) {
            System.err.println("Navigation error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void usersBtn(ActionEvent event) {
        try {
            Main.setRoot("Admin/AdminUsers");
        } catch (IOException e) {
            System.err.println("Navigation error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void applicationsBtn(ActionEvent event) {
        try {
            Main.setRoot("Admin/AdminApplications");
        } catch (IOException e) {
            System.err.println("Navigation error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void logOutBtn(ActionEvent event) {
        try {
            // Clear the user session
            userSession.logout();
            
            // Navigate back to landing page
            Main.setRoot("LandingPage");
        } catch (IOException e) {
            System.err.println("Error loading LandingPage.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
