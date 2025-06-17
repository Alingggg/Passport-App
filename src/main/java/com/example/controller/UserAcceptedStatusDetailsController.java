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
        if (userId == null) {
            // Handle case where user is not logged in, though typically this page wouldn't be accessible
            lblReferenceNumber.setText("N/A - User not logged in");
            return;
        }

        PassportApplication application = applicationDAO.findByUserId(userId);

        if (application != null) {
            if (application.getReferenceId() != null && !application.getReferenceId().isEmpty()) {
                lblReferenceNumber.setText(application.getReferenceId());
            } else {
                lblReferenceNumber.setText("N/A - Reference ID not found");
            }
            // You can also display other details from the 'application' object if needed
            // For example, if you add more labels for status, submitted_at, etc.
        } else {
            lblReferenceNumber.setText("N/A - Application not found");
        }
    }
}
