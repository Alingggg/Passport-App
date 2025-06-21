package com.example.controller;

import com.example.Main;
import com.example.model.PassportApplication;
import com.example.service.ApplicationService;
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
    private final ApplicationService applicationService = new ApplicationService();
    
    @FXML
    public void initialize() {
        // Set up the sidebar based on user role
        configureForUserRole();
    }
    
    // Configure the sidebar based on the user's role from UserSession
    private void configureForUserRole() {
        boolean isAdmin = userSession.isAdmin();
        
        // Set visibility based on role
        btnUserProfile.setVisible(!isAdmin);
        btnUserApplication.setVisible(!isAdmin);
        btnAdminUsers.setVisible(isAdmin);
        btnAdminApplications.setVisible(isAdmin);
        
        System.out.println("Sidebar initialized for role: " + userSession.getRole());
    }
    
    // Set the active tab for the current user type
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
            // Use the lightweight check to determine if an accepted profile exists.
            if (applicationService.hasAcceptedApplication()) {
                // An accepted profile exists, so the user has a valid passport.
                System.out.println("User has an accepted application. Navigating to UserPassportInfo...");
                Main.setRoot("UserPassportInfo");
            } else {
                // No accepted profile found. Navigate to the generic user profile page.
                System.out.println("User does not have an accepted application. Navigating to UserProfile...");
                Main.setRoot("UserProfile");
            }
        } catch (IOException e) {
            System.err.println("Navigation error in profileBtn: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void applicationBtn(ActionEvent event) {
        try {
            // Get the user's most recent application
            PassportApplication latestApplication = applicationService.getLatestApplication();

            if (latestApplication != null) {
                String status = latestApplication.getStatus().toUpperCase();
                boolean isCardReceived = latestApplication.isCardReceived();

                // These statuses mean the application is "active" and the user should see its status.
                if ("PENDING".equals(status) || "DENIED".equals(status) || ("ACCEPTED".equals(status) && !isCardReceived)) {
                    System.out.println("User has an active application. Navigating to UserApplicationStatus...");
                    Main.setRoot("UserApplicationStatus");
                } else {
                    // This covers "Cancelled" status, or "Accepted" where the card has been received.
                    // In these cases, the user should be able to start a new application.
                    // We check if they have any other accepted passport to decide which starting page to show.
                    if (applicationService.hasAcceptedApplication()) {
                        System.out.println("User has a completed/cancelled application but also an accepted one. Navigating to UserAlreadyPassportHolder...");
                        Main.setRoot("UserAlreadyPassportHolder");
                    } else {
                        System.out.println("User's latest application is completed/cancelled, and they have no other accepted one. Navigating to UserNotPassportHolder...");
                        Main.setRoot("UserNotPassportHolder");
                    }
                }
            } else {
                // No application record exists at all. This is a first-time applicant.
                System.out.println("User has no application. Navigating to UserNotPassportHolder...");
                Main.setRoot("UserNotPassportHolder");
            }
        } catch (IOException e) {
            System.err.println("Error loading FXML from SidebarController applicationBtn: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void usersBtn(ActionEvent event) {
        try {
            Main.setRoot("AdminUsers");
        } catch (IOException e) {
            System.err.println("Navigation error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void applicationsBtn(ActionEvent event) {
        try {
            Main.setRoot("AdminApplications");
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
