package com.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class Admin_ApplicationDets_Controller {

    @FXML
    private Button btnLogout;
    @FXML
    private ImageView logo;
    @FXML
    private Button btnApplication;
    @FXML
    private Button btnProfile;
    @FXML
    private AnchorPane scrollableContent;
    @FXML
    private Label lblAppDetails;
    @FXML
    private Label lblUser;
    @FXML
    private Label lblLastName;
    @FXML
    private Label lblFirstName;
    @FXML
    private Label lblMiddleName;
    @FXML
    private Label txtLastName;
    @FXML
    private Label txtFirstName;
    @FXML
    private Label txtMiddleName;
    @FXML
    private Label lblBirthdate;
    @FXML
    private Label lblPlaceofBirth;
    @FXML
    private Label txtPlaceOfBirth;
    @FXML
    private Label lblAddress;
    @FXML
    private Label txtAddress;
    @FXML
    private Label lblCivilStatus;
    @FXML
    private Label lblGender;
    @FXML
    private Label lblMobile;
    @FXML
    private Label txtMobile1;
    @FXML
    private Label txtMobile2;
    @FXML
    private Label lblTelNum;
    @FXML
    private Label txtTel1;
    @FXML
    private Label txtTel2;
    @FXML
    private Label lblEmailAdd;
    @FXML
    private Label txtEmail;
    @FXML
    private Label lblOccupation;
    @FXML
    private Label txtOccupation;
    @FXML
    private Label lblWorkTel;
    @FXML
    private Label txtWorkTel;
    @FXML
    private Label lblWorkAddress;
    @FXML
    private Label txtWorkAddress;
    @FXML
    private Label lblNameWH;
    @FXML
    private Label txtNameWH;
    @FXML
    private Label lblWHCitizenship;
    @FXML
    private Label txtWHCitizenship;
    @FXML
    private Label lblNameFather;
    @FXML
    private Label txtNameFather;
    @FXML
    private Label lblMaidenName;
    @FXML
    private Label txtMaidenName;
    @FXML
    private Label lblFatherCitizenship;
    @FXML
    private Label txtFatherCitizenship;
    @FXML
    private Label lblMotherCitizenship;
    @FXML
    private Label txtMotherCitizenship;
    @FXML
    private Label lblCitizenship;
    @FXML
    private Label txtCitizenshipAcquired;
    @FXML
    private Label lblForeignPassport;
    @FXML
    private Label lblCountry;
    @FXML
    private Label txtCountry;
    @FXML
    private Label lblForeignPassNum;
    @FXML
    private Label txtForeignPassNum;
    @FXML
    private Label lblPassportIssued;
    @FXML
    private Label lblPhPassportNum;
    @FXML
    private Label txtPhPassportNum;
    @FXML
    private Label lblIssueDate;
    @FXML
    private Label txtIssueDate;
    @FXML
    private Label lblPlaceofIssue;
    @FXML
    private Label txtPlaceOfIssue;
    @FXML
    private Label lbl18yo;
    @FXML
    private Label lbltravelingcom;
    @FXML
    private Label txtTravelingCompanion;
    @FXML
    private Label lblComRelationship;
    @FXML
    private Label txtComRelationship;
    @FXML
    private Label lblCompanionContact;
    @FXML
    private Label txtCompanionContact;
    @FXML
    private Label lblForVerification;
    @FXML
    private Button btnValidImage;
    @FXML
    private Button btnPSAImage;
    @FXML
    private ImageView btnNext;
    @FXML
    private Button btnAcceptApplication;
    @FXML
    private Button btnDenyApplication;

    @FXML
    private void logoutBtn(ActionEvent event) {
        System.out.println("Logout button clicked!");
    }

    @FXML
    private void applicationBtn(ActionEvent event) {
        System.out.println("Application button clicked!");
    }

    @FXML
    private void profileBtn(ActionEvent event) {
        System.out.println("Profile button clicked!");
    }

    @FXML
    private void viewValidId(ActionEvent event) {
        System.out.println("View Valid ID button clicked!");
    }

    @FXML
    private void viewPSABirthCert(ActionEvent event) {
        System.out.println("View PSA Birth Certificate button clicked!");
    }

    @FXML
    private void acceptApplication(ActionEvent event) {
        System.out.println("Accept Application button clicked!");
    }

    @FXML
    private void denyApplication(ActionEvent event) {
        System.out.println("Deny Application button clicked!");
    }

    @FXML
    public void initialize() {
        // Initialize any required components
    }
}
