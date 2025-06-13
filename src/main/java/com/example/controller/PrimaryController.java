package com.example.controller;

import java.io.IOException;

import com.example.Main;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class PrimaryController {

    @FXML
    private Button uploadButton;

    @FXML
    private void switchToSecondary() throws IOException {
        Main.setRoot("secondary");
    }

    @FXML
    private void switchToTesting() throws IOException {
        Main.setRoot("testing");
    }

    @FXML
    private void switchToUpload() {
        try {
            Main.setRoot("uploadImage");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Could not navigate to upload view: " + e.getMessage());
        }
    }

    /**
     * Display an alert dialog
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

