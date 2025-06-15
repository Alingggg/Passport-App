package com.example.controller;

import java.io.IOException;

import com.example.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class UserNotPassportHolderController {

    @FXML
    private SidebarController sidebarController;

    @FXML
    public void initialize() {
        if (sidebarController != null) {
            sidebarController.setActiveTab("application");
        }
    }

    @FXML
    void applyBtn(ActionEvent event) {
        try {
            Main.setRoot("ApplicationForm");
        } catch (IOException e) {
            System.err.println("Error loading ApplicationForm.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
