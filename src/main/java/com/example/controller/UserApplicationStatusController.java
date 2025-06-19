package com.example.controller;

import com.example.Main;
import com.example.model.PassportApplication;
import com.example.service.ApplicationService;
import com.example.util.UserSession;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.IOException;

public class UserApplicationStatusController {

    @FXML
    private SidebarController sidebarController;

    @FXML
    private Button btnView;

    @FXML
    private Label lblStatus;

    @FXML
    private Label lblFeedback;

    private ApplicationService applicationService;
    private UserSession userSession;
    private PassportApplication currentUserApplication;

    public UserApplicationStatusController() {
        this.applicationService = new ApplicationService();
        this.userSession = UserSession.getInstance();
    }

    @FXML
    public void initialize() {
        if (sidebarController != null) {
            sidebarController.setActiveTab("application");
        }
        loadApplicationStatus();
    }

    private void loadApplicationStatus() {
        Integer userId = userSession.getUserId();
        if (userId == null) {
            lblStatus.setText("User not logged in.");
            lblStatus.setStyle("-fx-text-fill: #F20707;");
            lblFeedback.setVisible(false);
            if (btnView != null) btnView.setVisible(false);
            return;
        }

        currentUserApplication = applicationService.getUserApplication();

        if (currentUserApplication != null) {
            String status = currentUserApplication.getStatus();
            lblStatus.setText(status != null ? status : "N/A");
            if (btnView != null) btnView.setVisible(true);

            // Ensure status strings in case statements match what the model provides (e.g., uppercase)
            switch (status != null ? status.toUpperCase() : "") { // Convert to uppercase for reliable matching
                case "PENDING":
                    lblStatus.setStyle("-fx-text-fill: #cc9d05;");
                    lblFeedback.setVisible(false); // Hide feedback for PENDING
                    break;
                case "ACCEPTED":
                    lblStatus.setStyle("-fx-text-fill: #06C012;");
                    lblFeedback.setVisible(false); // Hide feedback for ACCEPTED
                    break;
                case "DENIED":
                    lblStatus.setStyle("-fx-text-fill: #F20707;");
                    String feedbackText = currentUserApplication.getFeedback();
                    lblFeedback.setText("Feedback: " + (feedbackText != null && !feedbackText.trim().isEmpty() ? feedbackText : "No specific feedback provided."));
                    lblFeedback.setVisible(true); // Show feedback for DENIED
                    break;
                default:
                    lblStatus.setStyle("-fx-text-fill: #696868;"); // Default color
                    lblFeedback.setText("Unknown application status.");
                    lblFeedback.setVisible(false); // Hide feedback for unknown status
                    break;
            }
        } else {
            lblStatus.setText("No Application Found");
            lblStatus.setStyle("-fx-text-fill: #696868;");
            lblFeedback.setVisible(false);
            if (btnView != null) btnView.setVisible(false);
        }
    }

    @FXML
    private void handleView() {
        if (currentUserApplication == null) {
            System.err.println("No application data to view details for.");
            return;
        }

        String status = currentUserApplication.getStatus();
        try {
            // Ensure status strings for navigation also match (e.g., uppercase)
            String upperStatus = status != null ? status.toUpperCase() : "";
            if ("ACCEPTED".equals(upperStatus)) {
                System.out.println("Navigating to UserAcceptedStatusDetails for status: " + status);
                Main.setRoot("UserAcceptedStatusDetails");
            } else if ("PENDING".equals(upperStatus) || "DENIED".equals(upperStatus)) {
                System.out.println("Navigating to UserViewDetails for status: " + status);
                Main.setRoot("UserViewDetails");
            } else {
                System.err.println("Unknown status for navigation: " + status);
            }
        } catch (IOException e) {
            System.err.println("Error navigating from UserApplicationStatusController: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
