package com.example.controller;

import java.io.IOException;

import com.example.Main;
import com.example.service.ApplicationService;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class UserProfileController {

    @FXML
    private SidebarController sidebarController;

    @FXML
    private Label lblStatusMessage;

    @FXML
    private Button btnAction;

    private final ApplicationService applicationService = new ApplicationService();

    @FXML
    public void initialize() {
        if (sidebarController != null) {
            sidebarController.setActiveTab("profile");
        }
        // Defer the database check to avoid blocking the UI thread during initialization
        Platform.runLater(this::updateUserStatusView);
    }

    private void updateUserStatusView() {
        // The order of these checks is important to show the most relevant status.
        if (applicationService.hasPendingApplication()) {
            lblStatusMessage.setText("You have a pending application.");
            btnAction.setText("View Status");
        } else if (applicationService.hasExpiredPassport()) {
            lblStatusMessage.setText("Your passport has expired.");
            btnAction.setText("Re-apply Now");
        } else if (applicationService.hasAcceptedApplication()) {
            lblStatusMessage.setText("You are a valid passport holder.");
            btnAction.setText("View Details");
        } else {
            lblStatusMessage.setText("You are not yet a passport holder.");
            btnAction.setText("Apply Now");
        }
    }

    @FXML
    void handleAction(ActionEvent event) {
        try {
            String buttonText = btnAction.getText();
            if ("Re-apply Now".equals(buttonText) || "Apply Now".equals(buttonText)) {
                Main.setRoot("UserNotPassportHolder");
            } else if ("View Status".equals(buttonText) || "View Details".equals(buttonText)) {
                Main.setRoot("UserApplicationStatus");
            }
        } catch (IOException e) {
            System.err.println("Error loading FXML from UserProfileController: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
