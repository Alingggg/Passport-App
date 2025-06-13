package com.example.ayokona;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;


public class User_LoginController {

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
        System.out.println("thank you");
    }

    @FXML
    void registerBtn(ActionEvent event) {
        System.out.println("thank you");
    }

    @FXML
    void userBtn(ActionEvent event) {
        System.out.println("thank you");
    }

}


