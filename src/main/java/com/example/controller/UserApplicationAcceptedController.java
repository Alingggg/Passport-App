package com.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class UserApplicationAcceptedController {

    @FXML
    private SidebarController sidebarController;

    @FXML
    private Button btnViewDetails;

    @FXML
    public void initialize() {
        if (sidebarController != null) {
            sidebarController.setActiveTab("application");
        }
    }

    @FXML
    void handleViewDetails(ActionEvent event) {
        System.out.println("View Details button clicked.");
    }

}