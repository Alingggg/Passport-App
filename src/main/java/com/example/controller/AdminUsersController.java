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
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminUsersController {

    @FXML private SidebarController sidebarController;
    @FXML private VBox usersVBox;
    @FXML private Label btnAll;
    @FXML private Label btnExpired;
    @FXML private Label btnCardPending;

    private PassportApplicationDAO applicationDAO = new PassportApplicationDAO();
    private UserInfoDAO userInfoDAO = new UserInfoDAO();
    private UserPhilippinePassportDAO philippinePassportDAO = new UserPhilippinePassportDAO();

    private String currentFilter = "ALL"; // Default filter

    @FXML
    public void initialize() {
        if (sidebarController != null) {
            sidebarController.setActiveTab("users");
        }
        loadUsers(currentFilter);
        updateFilterStyles();
    }

    @FXML
    private void handleAllFilter(MouseEvent event) {
        currentFilter = "ALL";
        loadUsers(currentFilter);
        updateFilterStyles();
    }

    @FXML
    private void handleExpiredFilter(MouseEvent event) {
        currentFilter = "EXPIRED";
        loadUsers(currentFilter);
        updateFilterStyles();
    }

    @FXML
    private void handleCardPendingFilter(MouseEvent event) {
        currentFilter = "CARD_PENDING";
        loadUsers(currentFilter);
        updateFilterStyles();
    }

    private void updateFilterStyles() {
        // Reset all styles
        String inactiveStyle = "-fx-text-fill: #696868; -fx-underline: false;";
        btnAll.setStyle(inactiveStyle);
        btnExpired.setStyle(inactiveStyle);
        btnCardPending.setStyle(inactiveStyle);

        // Apply active style
        String activeStyle = "-fx-text-fill: #000000; -fx-underline: true;";
        switch (currentFilter) {
            case "ALL":
                btnAll.setStyle(activeStyle);
                break;
            case "EXPIRED":
                btnExpired.setStyle(activeStyle);
                break;
            case "CARD_PENDING":
                btnCardPending.setStyle(activeStyle);
                break;
        }
    }

    private void loadUsers(String filter) {
        if (usersVBox == null) {
            System.err.println("usersVBox is null. Check FXML fx:id in AdminUsers.fxml.");
            return;
        }
        usersVBox.getChildren().clear();

        List<PassportApplication> applications;
        switch (filter) {
            case "EXPIRED":
                applications = applicationDAO.getLatestExpiredApplicationForEachUser();
                break;
            case "CARD_PENDING":
                applications = applicationDAO.getLatestCardPendingApplicationForEachUser();
                break;
            case "ALL":
            default:
                applications = applicationDAO.getLatestAcceptedApplicationForEachUser();
                break;
        }
        System.out.println("Filter '" + filter + "' found " + applications.size() + " applications.");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

        for (PassportApplication app : applications) {
            try {
                UserInfo userInfo = userInfoDAO.findByApplicationId(app.getApplicationId());
                UserPhilippinePassport philippinePassport = philippinePassportDAO.findByApplicationId(app.getApplicationId());

                if (userInfo == null) {
                    System.out.println("No user info found for applicationId: " + app.getApplicationId());
                    continue;
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
                    passportId = philippinePassport.getCurrentPhilippinePassportNumber();
                    if (philippinePassport.getIssueDate() != null) {
                        issueDateStr = philippinePassport.getIssueDate().format(dateFormatter);
                    }
                    if (philippinePassport.getExpiryDate() != null) {
                        expiryDateStr = philippinePassport.getExpiryDate().format(dateFormatter);
                    }
                }
                
                cardController.setData(
                    fullName.trim(),
                    passportId,
                    issueDateStr,
                    expiryDateStr,
                    app
                );
                
                cardController.setOnViewDetailsAction(() -> {
                    try {
                        AdminUserDetailsController.setCurrentApplication(app);
                        com.example.Main.setRoot("AdminUserDetails");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                usersVBox.getChildren().add(cardNode);
            } catch (Exception e) {
                System.err.println("Error loading user card for accepted application: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
