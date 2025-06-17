package com.example.controller;

import com.example.Main;
import com.example.controller.components.UserPassportCardController;
import com.example.dao.PassportApplicationDAO;
import com.example.dao.UserInfoDAO;
import com.example.dao.UserPhilippinePassportDAO; // Added
import com.example.model.PassportApplication;
import com.example.model.UserInfo;
import com.example.model.UserPhilippinePassport; // Added
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.format.DateTimeFormatter; // Added
import java.util.List;

public class AdminUsersController {

    @FXML
    private SidebarController sidebarController;

    @FXML
    private Label lblAppAdminID; // This seems to be from an older version or a different context, ensure it's used if needed.
    @FXML
    private Label lblApplications; // Similarly, ensure this label is used if intended.

    @FXML
    private VBox usersVBox; // This VBox will hold the user cards.

    private PassportApplicationDAO applicationDAO = new PassportApplicationDAO();
    private UserInfoDAO userInfoDAO = new UserInfoDAO();
    private UserPhilippinePassportDAO philippinePassportDAO = new UserPhilippinePassportDAO(); // Added

    @FXML
    public void initialize() {
        if (sidebarController != null) {
            sidebarController.setActiveTab("users"); // Assuming "users" is the correct tab name
        }
        // If lblAppAdminID or lblApplications need to be populated, do it here.
        // For example, if lblApplications is a title:
        if (lblApplications != null) {
            lblApplications.setText("Accepted Users");
        }
        loadAcceptedUsers(); // Renamed for clarity based on previous requests
    }

    // Renamed from loadUsers to reflect that it loads users with accepted applications
    private void loadAcceptedUsers() {
        if (usersVBox == null) {
            System.err.println("applicationsVBox is null. Check FXML fx:id in AdminUsers.fxml.");
            return;
        }
        usersVBox.getChildren().clear();
        // Fetch applications with "Accepted" status
        List<PassportApplication> acceptedApplications = applicationDAO.getApplicationsByStatus("Accepted");
        System.out.println("Accepted applications found: " + acceptedApplications.size());

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

        for (PassportApplication app : acceptedApplications) {
            try {
                UserInfo userInfo = userInfoDAO.findByUserId(app.getUserId());
                UserPhilippinePassport philippinePassport = philippinePassportDAO.findByUserId(app.getUserId());

                if (userInfo == null) {
                    System.out.println("No user info found for userId: " + app.getUserId() + " with accepted application.");
                    continue;
                }
                // Philippine passport details are crucial for the UserPassportCard
                if (philippinePassport == null || !philippinePassport.getHasPhilippinePassport()) {
                    System.out.println("No valid Philippine passport details found for userId: " + app.getUserId());
                    // Depending on requirements, you might still want to show a card with N/A for passport details
                    // or skip this user. For now, we'll proceed and show N/A if details are missing.
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxml/components/UserPassportCard.fxml"));
                Node cardNode = loader.load();
                UserPassportCardController cardController = loader.getController();

                String fullName = (userInfo.getFirstName() != null ? userInfo.getFirstName() : "") + " " +
                                  (userInfo.getMiddleName() != null && !userInfo.getMiddleName().isEmpty() ? userInfo.getMiddleName().charAt(0) + ". " : "") +
                                  (userInfo.getLastName() != null ? userInfo.getLastName() : "");
                
                String passportId = "N/A";
                String issueDateStr = "N/A";
                String expiryDateStr = "N/A";

                if (philippinePassport != null && philippinePassport.getHasPhilippinePassport()) {
                    passportId = philippinePassport.getPhilippinePassportNumber();
                    if (philippinePassport.getIssueDate() != null) {
                        issueDateStr = philippinePassport.getIssueDate().format(dateFormatter);
                    }
                    if (philippinePassport.getExpiryDate() != null) {
                        expiryDateStr = philippinePassport.getExpiryDate().format(dateFormatter);
                    }
                }
                
                // Set data for the card according to UserPassportCardController's setData method
                cardController.setData(
                    fullName.trim(),
                    passportId,
                    issueDateStr,
                    expiryDateStr,
                    app // Pass the application object as context, or userInfo/userId if more appropriate for details view
                );
                
                // Set the action for the "View" button on the card
                // This will navigate to AdminUserDetails, passing the userId
                cardController.setOnViewDetailsAction(() -> {
                    try {
                        AdminUserDetailsController.setCurrentApplication(app);
                        com.example.Main.setRoot("AdminUserDetails");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                usersVBox.getChildren().add(cardNode);
                System.out.println("Added user card for (Accepted Status): " + fullName.trim());
            } catch (Exception e) {
                System.err.println("Error loading user card for accepted application: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
