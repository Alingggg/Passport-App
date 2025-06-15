package com.example.controller;

import java.io.IOException;

import com.example.Main;
import com.example.service.ApplicationService;
import com.example.model.UserProfile;
import com.example.util.UserSession;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;


public class UserPassportInfoController {

    @FXML
    private SidebarController sidebarController;

    @FXML
    private Label passportInfoLbl;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane scrollContent;

    // Name fields
    @FXML private Label lnTxtF;
    @FXML private Label fnTxtF;
    @FXML private Label mnTxtF;

    // Date fields
    @FXML private Label dayCombo;
    @FXML private Label monthCombo;
    @FXML private Label yearCombo;

    // Personal info fields
    @FXML private Label pBirthTxtF;
    @FXML private Label cmpAddressTxtF;
    @FXML private Label civilRadio;
    @FXML private Label genderCombo;

    // Contact fields
    @FXML private Label mobileNum1TxtF;
    @FXML private Label mobileNum2TxtF;
    @FXML private Label telNum1TxtF;
    @FXML private Label telNum2TxtF;
    @FXML private Label emailAddTxtF;

    // Work fields
    @FXML private Label occupationTxtF;
    @FXML private Label workTelTxtF;
    @FXML private Label workAddTxtF;

    // Family fields
    @FXML private Label whNameTxtF;
    @FXML private Label fatherTxtF;
    @FXML private Label motherTxtF;

    // Citizenship fields
    @FXML private Label citizen1TxtF;
    @FXML private Label citizen2TxtF;
    @FXML private Label citizen3TxtF;
    @FXML private Label acquiredByRadio;

    // Passport fields
    @FXML private Label foreignRadio;
    @FXML private Label countryTxtF;
    @FXML private Label passNoTxtF;
    @FXML private Label philPassRadio;
    @FXML private Label passNo2TxtF;
    @FXML private Label txtIssueDate;
    @FXML private Label placeIssueTxtF;

    // Minor info fields
    @FXML private Label minor18Radio;
    @FXML private Label travCompTxtF;
    @FXML private Label compRSTxtF;
    @FXML private Label contactNoTxtF;

    private ApplicationService applicationService;

    @FXML
    public void initialize() {
        if (sidebarController != null) {
            sidebarController.setActiveTab("profile");
        }

        applicationService = new ApplicationService();
        loadUserData();
    }

    private void loadUserData() {
        try {
            UserSession session = UserSession.getInstance();
            if (!session.isAuthenticated()) {
                // User not logged in, redirect to login
                Main.setRoot("UserLogin");
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
            setText(lnTxtF, userInfo.getLastName());
            setText(fnTxtF, userInfo.getFirstName());
            setText(mnTxtF, userInfo.getMiddleName());
            setText(pBirthTxtF, userInfo.getBirthPlace());
            setText(cmpAddressTxtF, userInfo.getCurrentAddress());
            setText(civilRadio, userInfo.getCivilStatus());
            setText(genderCombo, userInfo.getGender());
            setText(acquiredByRadio, userInfo.getAcquiredCitizenship());

            // Birth date
            if (userInfo.getBirthDate() != null) {
                setText(dayCombo, String.valueOf(userInfo.getBirthDate().getDayOfMonth()));
                setText(monthCombo, userInfo.getBirthDate().getMonth().toString());
                setText(yearCombo, String.valueOf(userInfo.getBirthDate().getYear()));
            }
        }

        // Contact info
        if (profile.getUserContact() != null) {
            var contact = profile.getUserContact();
            setText(mobileNum1TxtF, contact.getMobileNumber());
            setText(telNum1TxtF, contact.getTelephoneNumber());
            setText(emailAddTxtF, contact.getEmailAddress());
        }

        // Work info
        if (profile.getUserWork() != null) {
            var work = profile.getUserWork();
            setText(occupationTxtF, work.getOccupation());
            setText(workTelTxtF, work.getWorkTelephoneNumber());
            setText(workAddTxtF, work.getWorkAddress());
        }

        // Spouse info
        if (profile.getSpouse() != null) {
            var spouse = profile.getSpouse();
            setText(whNameTxtF, spouse.getSpouseFullName());
            setText(citizen1TxtF, spouse.getSpouseCitizenship());
        }

        // Parents info
        if (profile.getParents() != null) {
            var parents = profile.getParents();
            setText(fatherTxtF, parents.getFatherFullName());
            setText(motherTxtF, parents.getMotherMaidenName());
            setText(citizen2TxtF, parents.getFatherCitizenship());
            setText(citizen3TxtF, parents.getMotherCitizenship());
        }

        // Foreign passport info
        if (profile.getForeignPassport() != null) {
            var foreign = profile.getForeignPassport();
            setText(foreignRadio, foreign.getHasForeignPassport() ? "YES" : "NO");
            setText(countryTxtF, foreign.getIssuingCountry());
            setText(passNoTxtF, foreign.getForeignPassportNumber());
        }

        // Philippine passport info
        if (profile.getPhilippinePassport() != null) {
            var phil = profile.getPhilippinePassport();
            setText(philPassRadio, phil.getHasPhilippinePassport() ? "YES" : "NO");
            setText(passNo2TxtF, phil.getPhilippinePassportNumber());
            setText(placeIssueTxtF, phil.getIssuePlace());
            
            if (phil.getIssueDate() != null) {
                setText(txtIssueDate, phil.getIssueDate().toString());
            }
        }

        // Minor info
        if (profile.getMinorInfo() != null) {
            var minor = profile.getMinorInfo();
            setText(minor18Radio, minor.getIsMinor() ? "YES" : "NO");
            setText(travCompTxtF, minor.getCompanionFullName());
            setText(compRSTxtF, minor.getCompanionRelationship());
            setText(contactNoTxtF, minor.getCompanionContactNumber());
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
