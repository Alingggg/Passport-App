package com.example.controller;

import java.io.IOException;

import com.example.Main;
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
            if (applicationService.hasPendingApplication()) {
                System.out.println("User has a pending application. Navigating to UserApplicationStatus from UserProfile...");
                Main.setRoot("UserApplicationStatus");
            } else {
                System.out.println("User does not have a pending application. Navigating to UserNotPassportHolder from UserProfile...");
                Main.setRoot("UserNotPassportHolder");
            }
        } catch (IOException e) {
            System.err.println("Error loading FXML from UserProfileController applyBtn: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
