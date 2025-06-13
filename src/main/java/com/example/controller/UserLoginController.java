package com.example.controller;

import java.io.IOException;

import com.example.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;


public class UserLoginController {

    @FXML
    private AnchorPane bg1;

    @FXML
    private ImageView pic1;

    @FXML
    private ImageView pic2;

    @FXML
    private AnchorPane whiteBg;

    @FXML
    private Label loginLbl;

    @FXML
    private Label emailLbl;

    @FXML
    private Label passLbl;

    @FXML
    private Label doesntLbl;

    @FXML
    private TextField emailTxtF;

    @FXML
    private TextField passTxtF;

    @FXML
    void loginBtn(ActionEvent event) {
        try {
            Main.setRoot("UserProfile");
        } catch (IOException e) {
            System.err.println("Error loading UserProfile.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void registerBtn(ActionEvent event) {
        try {
            Main.setRoot("UserRegister");
        } catch (IOException e) {
            System.err.println("Error loading UserRegister.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void userBtn(ActionEvent event) {
        System.out.println("thank you");
    }

}



