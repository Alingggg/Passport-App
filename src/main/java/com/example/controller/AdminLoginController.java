package com.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class AdminLoginController {
    
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
    private Label lblUsername;
    @FXML
    private Label passLbl;
    @FXML
    private TextField emailTxtF;
    @FXML
    private TextField passTxtF;
    
    @FXML
    void loginBtn(ActionEvent event) {
        System.out.println("Admin login clicked");
    }
    
    @FXML
    void userBtn(ActionEvent event) {
        System.out.println("Switch to user login clicked");
    }
}

