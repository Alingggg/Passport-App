package com.example.controller;

import java.io.IOException;

import com.example.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class AlreadyPassportHolderController {


    @FXML
    private AnchorPane bg1;

    @FXML
    private ImageView pic1;

    @FXML
    private AnchorPane whiteBg;

    @FXML
    private Label yetLbl;

    @FXML
    private Label userLbl;

    @FXML
    void profileBtn(ActionEvent event) {
        try {
            Main.setRoot("UserPassportInfo");
        } catch (IOException e) {
            System.err.println("Error loading UserPassportInfo.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void applicationBtn(ActionEvent event) {
        try {
            Main.setRoot("AlreadyPassportHolder");
        } catch (IOException e) {
            System.err.println("Error loading AlreadyPassportHolder.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void reapplyBtn(ActionEvent event) {
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

}


