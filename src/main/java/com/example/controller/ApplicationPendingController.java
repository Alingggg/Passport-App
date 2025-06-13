package com.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class ApplicationPendingController {


    @FXML
    private AnchorPane bg1;

    @FXML
    private ImageView pic1;

    @FXML
    private AnchorPane whiteBg;

    @FXML
    private Label statusLbl;

    @FXML
    private Label pendingLbl;

    @FXML
    private Label userLbl;

    @FXML
    void profileBtn(ActionEvent event) {
        System.out.println("thank you");
    }

    @FXML
    void applicationBtn(ActionEvent event) {
        System.out.println("thank you");
    }

    @FXML
    void viewBtn(ActionEvent event) {
        System.out.println("thank you");
    }

    @FXML
    void logoutBtn(ActionEvent event) {
        System.out.println("thank you");
    }

}


