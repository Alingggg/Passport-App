package com.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class User_RegisterController {

    @FXML
    private AnchorPane bg1;

    @FXML
    private ImageView pic1;

    @FXML
    private ImageView pic2;

    @FXML
    private AnchorPane whiteBg;

    @FXML
    private Label registerLbl;

    @FXML
    private Label emailLbl;

    @FXML
    private Label passLbl;

    @FXML
    private Label confirmLbl;

    @FXML
    private TextField emailTxtF;

    @FXML
    private TextField passTxtF;

    @FXML
    private TextField confirmTxtF;


    @FXML
    void registerBtn(ActionEvent event) {
        System.out.println("thank you");
    }

    @FXML
    void userBtn(ActionEvent event) {
        System.out.println("thank you");
    }

}

