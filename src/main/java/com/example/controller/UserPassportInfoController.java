package com.example.controller;

import java.io.IOException;

import com.example.Main;
import com.example.service.ApplicationService;
import com.example.model.UserProfile;
import com.example.util.UserSession;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;


public class UserPassportInfoController {

    @FXML
    private SidebarController sidebarController;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane scrollContent;

    // Name fields
    @FXML private Label lblLastName;
    @FXML private Label lblFirstName;
    @FXML private Label lblMiddleName;

    // Date fields
    @FXML private Label lblBirthDay;
    @FXML private Label lblBirthMonth;
    @FXML private Label lblBirthYear;

    // Personal info fields
    @FXML private Label lblPlaceOfBirth;
    @FXML private Label lblCompleteAddress;
    @FXML private Label lblCivilStatus;
    @FXML private Label lblGender;

    // Contact fields
    @FXML private Label lblMobileNumber1;
    @FXML private Label lblMobileNumber2;
    @FXML private Label lblTelephoneNumber1;
    @FXML private Label lblTelephoneNumber2;
    @FXML private Label lblEmailAddress;

    // Work fields
    @FXML private Label lblOccupation;
    @FXML private Label lblWorkTelephone;
    @FXML private Label lblWorkAddress;

    // Family fields
    @FXML private Label lblSpouseName;
    @FXML private Label lblFatherName;
    @FXML private Label lblMotherName;

    // Citizenship fields
    @FXML private Label lblSpouseCitizenship;
    @FXML private Label lblFatherCitizenship;
    @FXML private Label lblMotherCitizenship;
    @FXML private Label lblCitizenshipAcquiredBy;

    // Passport fields
    @FXML private Label lblForeignPassportHolder;
    @FXML private Label lblForeignCountry;
    @FXML private Label lblForeignPassportNumber;
    @FXML private Label lblPhilippinePassport;
    @FXML private Label lblPhilippinePassportNumber;
    @FXML private Label lblIssueDate;
    @FXML private Label lblPlaceOfIssue;
    @FXML private Label lblIssueMonth;
    @FXML private Label lblIssueDay;
    @FXML private Label lblIssueYear;

    // Minor info fields
    @FXML private Label lblMinor18;
    @FXML private Label lblTravelingCompanion;
    @FXML private Label lblCompanionRelationship;
    @FXML private Label lblContactNumber;
    
    // Image view labels
    @FXML private Label lblViewImage1;
    @FXML private Label lblViewImage2;

    private ApplicationService applicationService;

    @FXML
    public void initialize() {
        if (sidebarController != null) {
            sidebarController.setActiveTab("profile");
        }

        applicationService = new ApplicationService();
        
        // Use Platform.runLater to delay the authentication check until after the scene is fully initialized
        Platform.runLater(this::loadUserData);
    }

    private void loadUserData() {
        try {
            UserSession session = UserSession.getInstance();
            if (!session.isAuthenticated()) {
                // User not logged in, redirect to login
                try {
                    Main.setRoot("UserLogin");
                } catch (IOException e) {
                    System.err.println("Error loading UserLogin.fxml: " + e.getMessage());
                    e.printStackTrace();
                }
                return;
            }

            // Load complete user profile
            UserProfile profile = applicationService.getCompleteUserProfile();
            if (profile != null) {
                populateFields(profile);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading user data: " + e.getMessage());
        }
    }

    private void populateFields(UserProfile profile) {
        // Basic info
        if (profile.getUserInfo() != null) {
            var userInfo = profile.getUserInfo();
            setText(lblLastName, userInfo.getLastName());
            setText(lblFirstName, userInfo.getFirstName());
            setText(lblMiddleName, userInfo.getMiddleName());
            setText(lblPlaceOfBirth, userInfo.getBirthPlace());
            setText(lblCompleteAddress, userInfo.getCurrentAddress());
            setText(lblCivilStatus, userInfo.getCivilStatus());
            setText(lblGender, userInfo.getGender());
            setText(lblCitizenshipAcquiredBy, userInfo.getAcquiredCitizenship());

            // Birth date
            if (userInfo.getBirthDate() != null) {
                setText(lblBirthDay, String.valueOf(userInfo.getBirthDate().getDayOfMonth()));
                setText(lblBirthMonth, userInfo.getBirthDate().getMonth().toString());
                setText(lblBirthYear, String.valueOf(userInfo.getBirthDate().getYear()));
            }
        }

        // Contact info
        if (profile.getUserContact() != null) {
            var contact = profile.getUserContact();
            setText(lblMobileNumber1, contact.getMobileNumber());
            setText(lblTelephoneNumber1, contact.getTelephoneNumber());
            setText(lblEmailAddress, contact.getEmailAddress());
        }

        // Work info
        if (profile.getUserWork() != null) {
            var work = profile.getUserWork();
            setText(lblOccupation, work.getOccupation());
            setText(lblWorkTelephone, work.getWorkTelephoneNumber());
            setText(lblWorkAddress, work.getWorkAddress());
        }

        // Spouse info
        if (profile.getSpouse() != null) {
            var spouse = profile.getSpouse();
            setText(lblSpouseName, spouse.getSpouseFullName());
            setText(lblSpouseCitizenship, spouse.getSpouseCitizenship());
        }

        // Parents info
        if (profile.getParents() != null) {
            var parents = profile.getParents();
            setText(lblFatherName, parents.getFatherFullName());
            setText(lblMotherName, parents.getMotherMaidenName());
            setText(lblFatherCitizenship, parents.getFatherCitizenship());
            setText(lblMotherCitizenship, parents.getMotherCitizenship());
        }

        // Foreign passport info
        if (profile.getForeignPassport() != null) {
            var foreign = profile.getForeignPassport();
            setText(lblForeignPassportHolder, foreign.getHasForeignPassport() ? "YES" : "NO");
            setText(lblForeignCountry, foreign.getIssuingCountry());
            setText(lblForeignPassportNumber, foreign.getForeignPassportNumber());
        }

        // Philippine passport info
        if (profile.getPhilippinePassport() != null) {
            var phil = profile.getPhilippinePassport();
            setText(lblPhilippinePassport, phil.getHasPhilippinePassport() ? "YES" : "NO");
            setText(lblPhilippinePassportNumber, phil.getPhilippinePassportNumber());
            setText(lblPlaceOfIssue, phil.getIssuePlace());
            
            if (phil.getIssueDate() != null) {
                setText(lblIssueDate, phil.getIssueDate().toString());
                // If you need to set the individual components of the issue date
                setText(lblIssueDay, String.valueOf(phil.getIssueDate().getDayOfMonth()));
                setText(lblIssueMonth, phil.getIssueDate().getMonth().toString());
                setText(lblIssueYear, String.valueOf(phil.getIssueDate().getYear()));
            }
        }

        // Minor info
        if (profile.getMinorInfo() != null) {
            var minor = profile.getMinorInfo();
            setText(lblMinor18, minor.getIsMinor() ? "YES" : "NO");
            setText(lblTravelingCompanion, minor.getCompanionFullName());
            setText(lblCompanionRelationship, minor.getCompanionRelationship());
            setText(lblContactNumber, minor.getCompanionContactNumber());
        }
    }

    private void setText(Label label, String text) {
        if (label != null) {
            label.setText(text != null ? text : "");
        }
    }

    @FXML
    void uploadValidIDBtn(ActionEvent event) {
        System.out.println("Upload Valid ID clicked");
    }

    @FXML
    void uploadPSABtn(ActionEvent event) {
        System.out.println("Upload PSA Birth Certificate clicked");
    }

    @FXML
    void reapplyBtn(ActionEvent event) {
        try {
            Main.setRoot("UserApplication");
        } catch (IOException e) {
            System.err.println("Error loading UserApplication.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
