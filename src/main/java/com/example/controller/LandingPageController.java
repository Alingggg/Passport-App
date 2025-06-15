package com.example.controller;

import com.example.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;

public class LandingPageController {

    @FXML
    void adminBtn(ActionEvent event) {
        try {
            Main.setRoot("AdminLogin");
        } catch (IOException e) {
            System.err.println("Error loading AdminLogin.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void applicantBtn(ActionEvent event) {
        try {
            Main.setRoot("UserLogin");
        } catch (IOException e) {
            System.err.println("Error loading UserLogin.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
