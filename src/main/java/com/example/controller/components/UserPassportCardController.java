package com.example.controller.components;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class UserPassportCardController {

    @FXML
    private Label lblName;

    @FXML
    private Label lblPassportID;

    @FXML
    private Label lblIssueDate;

    @FXML
    private Label lblExpiryDate;

    private Runnable onViewDetailsAction;
    private Object dataContext;

    public void setData(String name, String passportId, String issueDate, String expiryDate, Object context) {
        lblName.setText(name != null ? name : "N/A");
        lblPassportID.setText(passportId != null ? passportId : "N/A");
        lblIssueDate.setText(issueDate != null ? issueDate : "N/A");
        lblExpiryDate.setText(expiryDate != null ? expiryDate : "N/A");
        this.dataContext = context;
    }

    public void setOnViewDetailsAction(Runnable action) {
        this.onViewDetailsAction = action;
    }

    public Object getDataContext() {
        return dataContext;
    }

    @FXML
    private void handleViewDetailsClick(MouseEvent event) {
        if (onViewDetailsAction != null) {
            onViewDetailsAction.run();
        }
    }
}