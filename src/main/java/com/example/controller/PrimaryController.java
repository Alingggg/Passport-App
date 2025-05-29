package com.example.controller;

import java.io.IOException;

import com.example.Main;

import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        Main.setRoot("secondary");
    }

    @FXML
    private void switchToTesting() throws IOException {
        Main.setRoot("testing");
    }

    @FXML
    private void switchToUpload() throws IOException {
        Main.setRoot("uploadImage");
    }
}
