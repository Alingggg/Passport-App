package com.example.ayokona;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class Application1_Controller {


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
        System.out.println("thank you");
    }

    @FXML
    void applicationBtn(ActionEvent event) {
        System.out.println("thank you");
    }

    @FXML
    void applyBtn(ActionEvent event) {
        System.out.println("thank you");
    }

    @FXML
    void logoutBtn(ActionEvent event) {
        System.out.println("thank you");
    }

}

