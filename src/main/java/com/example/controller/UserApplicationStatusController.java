package com.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class UserApplicationStatusController {

    @FXML
    private SidebarController sidebarController;

    @FXML
    private Button btnReapply;

    @FXML
    private Label lblFeedback;

    @FXML
    public void initialize() {
        if (sidebarController != null) {
            sidebarController.setActiveTab("application");
        }
    }

    @FXML
    private void handleReapply() {
        System.out.println("Reapply button clicked");
    }
}
