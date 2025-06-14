package com.example.controller;

import java.io.IOException;

import com.example.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class UserNotPassportHolderController {

    @FXML
    private AnchorPane bg1;

    @FXML
    private ImageView pic1;

    @FXML
    private Label userLbl;

    @FXML
    private AnchorPane whiteBg;

    @FXML
    private Label yetLbl;

    @FXML
    void applicationBtn(ActionEvent event) {

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

    @FXML
    void logoutBtn(ActionEvent event) {
        try {
            Main.setRoot("LandingPage");
        } catch (IOException e) {
            System.err.println("Error loading LandingPage.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void profileBtn(ActionEvent event) {
        try {
            Main.setRoot("UserProfile");
        } catch (IOException e) {
            System.err.println("Error loading UserProfile.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
