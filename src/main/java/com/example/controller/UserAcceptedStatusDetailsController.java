package com.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserAcceptedStatusDetailsController {

    @FXML
    private SidebarController sidebarController;

    @FXML
    private Label lblReferenceNumber;

    @FXML
    public void initialize() {
        if (sidebarController != null) {
            sidebarController.setActiveTab("application");
        }
    }
}
