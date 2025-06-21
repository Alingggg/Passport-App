package com.example.controller;

import java.io.IOException;

import com.example.Main;
import com.example.model.PassportApplication;
import com.example.service.ApplicationService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class UserProfileController {

    @FXML
    private SidebarController sidebarController;

    private final ApplicationService applicationService = new ApplicationService();

    @FXML
    public void initialize() {
        if (sidebarController != null) {
            sidebarController.setActiveTab("profile");
        }
    }

    @FXML
    void applyBtn(ActionEvent event) {
        try {
            // Get the user's latest application regardless of status
            PassportApplication latestApplication = applicationService.getLatestApplication();
            
            // If there's an existing application and the card is not yet received,
            // redirect to the application status page
            if (latestApplication != null && !latestApplication.isCardReceived()) {
                System.out.println("User has an active application (card not received). Navigating to UserApplicationStatus...");
                Main.setRoot("UserApplicationStatus");
            } else {
                // Otherwise, user can start a new application process
                System.out.println("User has no active applications or has received their card. Navigating to UserNotPassportHolder...");
                Main.setRoot("UserNotPassportHolder");
            }
        } catch (IOException e) {
            System.err.println("Error loading FXML from UserProfileController applyBtn: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
