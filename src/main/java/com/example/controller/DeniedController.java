package com.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class DeniedController {

    @FXML
    private Button btnApplication;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnProfile;

    @FXML
    private Button btnReapply;

    @FXML
    private void handleProfile() {
        System.out.println("Profile button clicked");
    }

    @FXML
    private void handleApplication() {
        System.out.println("Application button clicked");
    }

    @FXML
    private void handleLogout() {
        System.out.println("Logout button clicked");
    }

    @FXML
    private void handleReapply() {
        System.out.println("Reapply button clicked");
    }
}

