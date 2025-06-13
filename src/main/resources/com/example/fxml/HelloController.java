package com.example.ayokona;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class HelloController {


    @FXML
    private AnchorPane bg1;

    @FXML
    private ImageView pic1;

    @FXML
    private ImageView pic2;

    @FXML
    private AnchorPane whiteBg;


    @FXML
    void adminBtn(ActionEvent event) {
        System.out.println("thank you");
    }

    @FXML
    void applicantBtn(ActionEvent event) {
        System.out.println("Yehey");
    }
}

