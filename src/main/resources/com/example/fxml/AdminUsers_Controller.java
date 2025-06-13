package com.example.javafxfrontend;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.control.ScrollPane; // Added import
import javafx.scene.layout.AnchorPane; // Added import

public class AdminUsers_Controller {

    @FXML
    private Label btnUsers;
    @FXML
    private Label btnApplications;
    @FXML
    private Label btnLogout;

    @FXML
    private Label lblAdminID;

    @FXML
    private Label lblUsers;
    @FXML
    private Label lblSearchUser;
    @FXML
    private TextField tfSearch;
    @FXML
    private ImageView btnSearch;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox usersVBox;

    @FXML
    private AnchorPane scrollSearch1;
    @FXML
    private Label lblName;
    @FXML
    private Label lblPassportID;
    @FXML
    private Label lblIssueDate;
    @FXML
    private Label lblExpiryDate;
    @FXML
    private Label lblStatus;
    @FXML
    private Label btnViewDetails4111111;

    @FXML
    private AnchorPane scrollSearch2;
    @FXML
    private Label lblName1;
    @FXML
    private Label lblPassportID1;
    @FXML
    private Label lblIssueDate1;
    @FXML
    private Label lblExpiryDate1;
    @FXML
    private Label lblStatus1;
    @FXML
    private Label btnViewDetails41111111;

    @FXML
    private AnchorPane scrollSearch3;
    @FXML
    private Label lblName11;
    @FXML
    private Label lblPassportID11;
    @FXML
    private Label lblIssueDate11;
    @FXML
    private Label lblExpiryDate11;
    @FXML
    private Label lblStatus11;
    @FXML
    private Label btnViewDetails411111111;

    @FXML
    private AnchorPane scrollSearch4;
    @FXML
    private Label lblName12;
    @FXML
    private Label lblPassportID12;
    @FXML
    private Label lblIssueDate12;
    @FXML
    private Label lblExpiryDate12;
    @FXML
    private Label lblStatus12;
    @FXML
    private Label btnViewDetails411111112;

    @FXML
    private AnchorPane scrollSearch5;
    @FXML
    private Label lblName13;
    @FXML
    private Label lblPassportID13;
    @FXML
    private Label lblIssueDate13;
    @FXML
    private Label lblExpiryDate13;
    @FXML
    private Label lblStatus13;
    @FXML
    private Label btnViewDetails411111113;

    @FXML
    private AnchorPane scrollSearch6;
    @FXML
    private Label lblName14;
    @FXML
    private Label lblPassportID14;
    @FXML
    private Label lblIssueDate14;
    @FXML
    private Label lblExpiryDate14;
    @FXML
    private Label lblStatus14;
    @FXML
    private Label btnViewDetails411111114;

    private void handleUsersClick() {
        System.out.println("Users button clicked!");
        btnUsers.setStyle("-fx-background-color: white; -fx-text-fill: #40659C; -fx-background-radius: 15;");
        btnApplications.setStyle("-fx-background-color: #40659C; -fx-text-fill: white; -fx-background-radius: 15;");
    }

    private void handleApplicationsClick() {
        System.out.println("Applications button clicked!");
        btnApplications.setStyle("-fx-background-color: white; -fx-text-fill: #40659C; -fx-background-radius: 15;");
        btnUsers.setStyle("-fx-background-color: #40659C; -fx-text-fill: white; -fx-background-radius: 15;");
    }

    private void handleLogoutClick() {
        System.out.println("Logout button clicked!");
    }

    private void handleSearchClick() {
        String searchText = tfSearch.getText();
        System.out.println("Searching for user: " + searchText);
    }

    private void handleViewDetailsClick(String userName) {
        System.out.println("View Details clicked for: " + userName);
    }

    private void populateUsers() {

    }
}