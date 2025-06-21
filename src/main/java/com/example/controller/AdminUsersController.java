package com.example.controller;

import com.example.controller.components.UserPassportCardController;
import com.example.dao.PassportApplicationDAO;
import com.example.dao.UserInfoDAO;
import com.example.dao.UserPhilippinePassportDAO;
import com.example.model.PassportApplication;
import com.example.model.UserInfo;
import com.example.model.UserPhilippinePassport;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminUsersController {

    @FXML private SidebarController sidebarController;

    @FXML private VBox usersVBox;

    private PassportApplicationDAO applicationDAO = new PassportApplicationDAO();
    private UserInfoDAO userInfoDAO = new UserInfoDAO();
    private UserPhilippinePassportDAO philippinePassportDAO = new UserPhilippinePassportDAO();

    @FXML
    public void initialize() {
        if (sidebarController != null) {
            sidebarController.setActiveTab("users");
        }

        loadAcceptedUsers();
    }

    private void loadAcceptedUsers() {
        if (usersVBox == null) {
            System.err.println("applicationsVBox is null. Check FXML fx:id in AdminUsers.fxml.");
            return;
        }
        usersVBox.getChildren().clear();
        // Fetch only the latest accepted application for each user
        List<PassportApplication> acceptedApplications = applicationDAO.getLatestAcceptedApplicationForEachUser();
        System.out.println("Latest accepted applications found: " + acceptedApplications.size());

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

        for (PassportApplication app : acceptedApplications) {
            try {
                UserInfo userInfo = userInfoDAO.findByApplicationId(app.getApplicationId());
                UserPhilippinePassport philippinePassport = philippinePassportDAO.findByApplicationId(app.getApplicationId());

                if (userInfo == null) {
                    System.out.println("No user info found for applicationId: " + app.getApplicationId() + " with accepted application.");
                    continue;
                }

                if (philippinePassport == null || !philippinePassport.getHasPhilippinePassport()) {
                    System.out.println("No valid Philippine passport details found for applicationId: " + app.getApplicationId());
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
