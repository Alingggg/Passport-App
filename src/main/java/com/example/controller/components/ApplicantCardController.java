package com.example.controller.components;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class ApplicantCardController {

    @FXML
    private Label lblLastName;

    @FXML
    private Label lblFirstName;

    @FXML
    private Label lblMiddleName;

    @FXML
    private Label btnViewApplication; // Though it's a Label, it acts as a button

    private Runnable onViewApplicationAction;
    private Object dataContext; // To store associated data like application ID

    public void setData(String lastName, String firstName, String middleName, Object context) {
        lblLastName.setText(lastName != null ? lastName : "N/A");
        lblFirstName.setText(firstName != null ? firstName : "N/A");
        lblMiddleName.setText(middleName != null ? middleName : "N/A");
        this.dataContext = context;
    }

    public void setOnViewApplicationAction(Runnable action) {
        this.onViewApplicationAction = action;
    }

    public Object getDataContext() {
        return dataContext;
    }

    @FXML
    private void handleViewApplicationClick(MouseEvent event) {
        if (onViewApplicationAction != null) {
            onViewApplicationAction.run();
        }
    }
}