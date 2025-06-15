package com.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserApplicationPendingController {

    @FXML
    private SidebarController sidebarController;

    @FXML
    private Label pendingLbl;

    @FXML
    public void initialize() {
        if (sidebarController != null) {
            sidebarController.setActiveTab("application");
        }
    }

    @FXML
    void viewBtn(ActionEvent event) {
        System.out.println("thank you");
    }

}
