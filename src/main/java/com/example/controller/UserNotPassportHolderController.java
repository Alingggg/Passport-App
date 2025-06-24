package com.example.controller;

import java.io.IOException;

import com.example.Main;
import com.example.service.ApplicationService;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class UserNotPassportHolderController {

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
            sidebarController.setActiveTab("application");
        }
        Platform.runLater(this::updateStatus);
    }

    private void updateStatus() {
        if (applicationService.hasExpiredPassport()) {
            lblStatusMessage.setText("Your passport has expired.");
            btnAction.setText("Re-apply Now");
        }
    }

    @FXML
    void applyBtn(ActionEvent event) {
        try {
            Main.setRoot("UserApplicationForm");
        } catch (IOException e) {
            System.err.println("Error loading ApplicationForm.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
