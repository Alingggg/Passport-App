package com.example.controller;

import com.example.Main;
import com.example.dao.UserPhilippinePassportDAO;
import com.example.model.PassportApplication;
import com.example.model.UserPhilippinePassport;
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
    private final UserPhilippinePassportDAO philippinePassportDAO = new UserPhilippinePassportDAO();
    private final ApplicationService applicationService = new ApplicationService();
    
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
            Integer userId = userSession.getUserId();
            if (userId == null) {
                Main.setRoot("UserLogin");
                return;
            }

            PassportApplication application = applicationService.getUserApplication();

            if (application != null && "ACCEPTED".equalsIgnoreCase(application.getStatus())) {
                System.out.println("User has an accepted application. Navigating to UserPassportInfo...");
                Main.setRoot("UserPassportInfo");
            } else {
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
            Integer userId = userSession.getUserId();
            if (userId == null) { 
                Main.setRoot("UserLogin");
                return;
            }

            // Check if an application already exists using ApplicationService
            if (applicationService.hasExistingApplication()) {
                System.out.println("User has an existing application. Navigating to UserApplicationStatus...");
                Main.setRoot("UserApplicationStatus");
            } else {
                // No existing application, logic based on passport ownership
                UserPhilippinePassport philippinePassport = philippinePassportDAO.findByUserId(userId);
                if (philippinePassport != null && philippinePassport.getHasPhilippinePassport()) {
                    System.out.println("User has passport but no current application. Navigating to UserAlreadyPassportHolder...");
                    Main.setRoot("UserAlreadyPassportHolder");
                } else {
                    System.out.println("User doesn't have passport and no current application. Navigating to UserNotPassportHolder...");
                    Main.setRoot("UserNotPassportHolder");
                }
            }
        } catch (IOException e) {
            System.err.println("Navigation error in applicationBtn: " + e.getMessage());
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
