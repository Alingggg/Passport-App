package com.example.controller;

import com.example.dao.PassportApplicationDAO;
import com.example.model.PassportApplication;
import com.example.util.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserAcceptedStatusDetailsController {

    @FXML
    private SidebarController sidebarController;

    @FXML
    private Label lblReferenceNumber;

    private PassportApplicationDAO applicationDAO;
    private UserSession userSession;

    @FXML
    public void initialize() {
        if (sidebarController != null) {
            sidebarController.setActiveTab("application");
        }

        applicationDAO = new PassportApplicationDAO();
        userSession = UserSession.getInstance();

        loadApplicationDetails();
    }

    private void loadApplicationDetails() {
        Integer userId = userSession.getUserId();

        PassportApplication application = applicationDAO.findByUserId(userId);

        if (application != null) {
            if (application.getReferenceId() != null && !application.getReferenceId().isEmpty()) {
                lblReferenceNumber.setText(application.getReferenceId());
            } else {
                lblReferenceNumber.setText("N/A - Reference ID not found");
            }
        } else {
            lblReferenceNumber.setText("N/A - Application not found");
        }
    }
}
