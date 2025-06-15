package com.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class UserApplicationDeniedController {

    @FXML
    private SidebarController sidebarController;

    @FXML
    private Button btnReapply;

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
