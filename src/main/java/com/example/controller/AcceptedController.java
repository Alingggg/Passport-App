package com.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AcceptedController {

    @FXML
    private Button btnApplication;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnProfile;

    @FXML
    private Button btnViewDetails;

    @FXML
    void handleApplication(ActionEvent event) {
        System.out.println("Application button clicked.");
    }

    @FXML
    void handleLogout(ActionEvent event) {
        System.out.println("Logout button clicked.");
    }

    @FXML
    void handleProfile(ActionEvent event) {
        System.out.println("Profile button clicked.");
    }

    @FXML
    void handleViewDetails(ActionEvent event) {
        System.out.println("View Details button clicked.");
    }

}

