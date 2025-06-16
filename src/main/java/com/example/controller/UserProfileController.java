package com.example.controller;

import java.io.IOException;

import com.example.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class UserProfileController {

    @FXML
    private SidebarController sidebarController;

    @FXML
    public void initialize() {
        if (sidebarController != null) {
            sidebarController.setActiveTab("profile");
        }
    }

    @FXML
    void applyBtn(ActionEvent event) {
        try {
            Main.setRoot("UserNotPassportHolder");
        } catch (IOException e) {
            System.err.println("Error loading UserNotPassportHolder.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
