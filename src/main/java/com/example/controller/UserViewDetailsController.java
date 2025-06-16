package com.example.controller;

import com.example.Main;
import com.example.model.*;
import com.example.service.ApplicationService;
import com.example.util.UserSession;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image; // For potential image display later
import javafx.scene.image.ImageView; // For potential image display later
import javafx.stage.Stage; // For potential image display later
import javafx.scene.Scene; // For potential image display later
import javafx.scene.layout.StackPane; // For potential image display later


import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserViewDetailsController {

    @FXML
    private SidebarController sidebarController;

    @FXML
    private ScrollPane scrollPane;

    // Personal Information Labels
    @FXML private Label lblLastName;
    @FXML private Label lblFirstName;
    @FXML private Label lblMiddleName;
    @FXML private Label lblBirthDate;
    @FXML private Label lblPlaceOfBirth;
    @FXML private Label lblCompleteAddress;
    @FXML private Label lblCivilStatus;
    @FXML private Label lblGender;

    // Contact Information Labels
    @FXML private Label lblMobileNumber1;
    @FXML private Label lblTelephoneNumber1;
    @FXML private Label lblEmailAddress;

    // Work Information Labels
    @FXML private Label lblOccupation;
    @FXML private Label lblWorkTelephone;
    @FXML private Label lblWorkAddress;

    // Family Information Labels
    @FXML private Label lblSpouseName;
    @FXML private Label lblSpouseCitizenship;
    @FXML private Label lblFatherName;
    @FXML private Label lblMotherName;
    @FXML private Label lblFatherCitizenship;
    @FXML private Label lblMotherCitizenship;

    // Citizenship Information Labels
    @FXML private Label lblCitizenshipAcquiredBy;

    // Foreign Passport Information Labels
    @FXML private Label lblForeignPassportHolder;
    @FXML private Label lblCountry;
    @FXML private Label lblForeignPassportNo;

    // Philippine Passport Information Labels
    @FXML private Label lblPhilippinePassportHolder;
    @FXML private Label lblPhilippinePassportNo;
    @FXML private Label lblIssueDate;
    @FXML private Label lblPlaceOfIssue;

    // Minor Information Labels
    @FXML private Label lblIsMinor;
    @FXML private Label lblTravelingCompanion;
    @FXML private Label lblCompanionRelationship;
    @FXML private Label lblMinorContactNumber;

    // Verification Buttons
    @FXML private Button btnViewValidID;
    @FXML private Button btnViewPSA;

    // Navigation Buttons
    @FXML private Button btnBack;
    @FXML private Button arrowBtn; // Assuming this also acts as a back button

    private ApplicationService applicationService;
    private UserSession userSession;
    private UserProfile currentUserProfile; // To store fetched profile for image viewing

    public UserViewDetailsController() {
        this.applicationService = new ApplicationService();
        this.userSession = UserSession.getInstance();
    }

    @FXML
    public void initialize() {
        if (sidebarController != null) {
            sidebarController.setActiveTab("application");
        }
        // Ensure UI updates are done on the JavaFX Application Thread
        Platform.runLater(this::loadApplicationDetails);
    }

    private void loadApplicationDetails() {
        Integer userId = userSession.getUserId();
        if (userId == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "User not logged in. Cannot display details.");
            try {
                Main.setRoot("UserLogin");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        currentUserProfile = applicationService.getCompleteUserProfile();

        if (currentUserProfile != null) {
            populateFields(currentUserProfile);
            // Enable/disable view image buttons based on image availability
            configureImageViewButtons(currentUserProfile.getImages());
        } else {
            showAlert(Alert.AlertType.WARNING, "No Data", "No application details found for the current user.");
            // Optionally disable all fields or show a message
        }
    }

    private void populateFields(UserProfile profile) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

        // UserInfo
        UserInfo userInfo = profile.getUserInfo();
        if (userInfo != null) {
            setText(lblLastName, userInfo.getLastName());
            setText(lblFirstName, userInfo.getFirstName());
            setText(lblMiddleName, userInfo.getMiddleName());
            if (userInfo.getBirthDate() != null) {
                setText(lblBirthDate, userInfo.getBirthDate().format(dateFormatter));
            }
            setText(lblPlaceOfBirth, userInfo.getBirthPlace());
            setText(lblCompleteAddress, userInfo.getCurrentAddress());
            setText(lblCivilStatus, userInfo.getCivilStatus());
            setText(lblGender, userInfo.getGender());
            setText(lblCitizenshipAcquiredBy, userInfo.getAcquiredCitizenship());
        }

        // UserContact
        UserContact contact = profile.getUserContact();
        if (contact != null) {
            setText(lblMobileNumber1, contact.getMobileNumber());
            setText(lblTelephoneNumber1, contact.getTelephoneNumber());
            setText(lblEmailAddress, contact.getEmailAddress());
        }

        // UserWork
        UserWork work = profile.getUserWork();
        if (work != null) {
            setText(lblOccupation, work.getOccupation());
            setText(lblWorkTelephone, work.getWorkTelephoneNumber());
            setText(lblWorkAddress, work.getWorkAddress());
        }

        // UserSpouse
        UserSpouse spouse = profile.getSpouse();
        if (spouse != null) {
            setText(lblSpouseName, spouse.getSpouseFullName());
            setText(lblSpouseCitizenship, spouse.getSpouseCitizenship());
        } else {
            setText(lblSpouseName, "N/A");
            setText(lblSpouseCitizenship, "N/A");
        }


        // UserParents
        UserParents parents = profile.getParents();
        if (parents != null) {
            setText(lblFatherName, parents.getFatherFullName());
            setText(lblMotherName, parents.getMotherMaidenName());
            setText(lblFatherCitizenship, parents.getFatherCitizenship());
            setText(lblMotherCitizenship, parents.getMotherCitizenship());
        }

        // UserForeignPassport
        UserForeignPassport foreignPassport = profile.getForeignPassport();
        if (foreignPassport != null) {
            setText(lblForeignPassportHolder, foreignPassport.getHasForeignPassport() ? "YES" : "NO");
            if (foreignPassport.getHasForeignPassport()) {
                setText(lblCountry, foreignPassport.getIssuingCountry());
                setText(lblForeignPassportNo, foreignPassport.getForeignPassportNumber());
            } else {
                setText(lblCountry, "N/A");
                setText(lblForeignPassportNo, "N/A");
            }
        }

        // UserPhilippinePassport
        UserPhilippinePassport philippinePassport = profile.getPhilippinePassport();
        if (philippinePassport != null) {
            setText(lblPhilippinePassportHolder, philippinePassport.getHasPhilippinePassport() ? "YES" : "NO");
            if (philippinePassport.getHasPhilippinePassport()) {
                setText(lblPhilippinePassportNo, philippinePassport.getPhilippinePassportNumber());
                if (philippinePassport.getIssueDate() != null) {
                    setText(lblIssueDate, philippinePassport.getIssueDate().format(dateFormatter));
                }
                setText(lblPlaceOfIssue, philippinePassport.getIssuePlace());
            } else {
                setText(lblPhilippinePassportNo, "N/A");
                setText(lblIssueDate, "N/A");
                setText(lblPlaceOfIssue, "N/A");
            }
        }

        // UserMinorInfo
        UserMinorInfo minorInfo = profile.getMinorInfo();
        if (minorInfo != null) {
            setText(lblIsMinor, minorInfo.getIsMinor() ? "YES" : "NO");
            if (minorInfo.getIsMinor()) {
                setText(lblTravelingCompanion, minorInfo.getCompanionFullName());
                setText(lblCompanionRelationship, minorInfo.getCompanionRelationship());
                setText(lblMinorContactNumber, minorInfo.getCompanionContactNumber());
            } else {
                setText(lblTravelingCompanion, "N/A");
                setText(lblCompanionRelationship, "N/A");
                setText(lblMinorContactNumber, "N/A");
            }
        }
    }
    
    private void configureImageViewButtons(List<com.example.model.Image> images) {
        boolean hasValidId = false;
        boolean hasPsa = false;

        if (images != null) {
            for (com.example.model.Image img : images) {
                if ("Valid ID".equals(img.getFileType())) {
                    hasValidId = true;
                } else if ("Birth Certificate".equals(img.getFileType())) {
                    hasPsa = true;
                }
            }
        }
        btnViewValidID.setDisable(!hasValidId);
        btnViewPSA.setDisable(!hasPsa);
    }


    private void setText(Label label, String text) {
        if (label != null) {
            label.setText(text != null && !text.trim().isEmpty() ? text : "N/A");
        }
    }

    @FXML
    void viewValidIDImage(ActionEvent event) {
        viewImageByType("valid_id");
    }

    @FXML
    void viewPSAImage(ActionEvent event) {
        viewImageByType("psa_birth_certificate");
    }
    
    private void viewImageByType(String imageType) {
        if (currentUserProfile == null || currentUserProfile.getImages() == null) {
            showAlert(Alert.AlertType.INFORMATION, "No Image", "No image data available.");
            return;
        }

        String imageUrl = null;
        for (com.example.model.Image img : currentUserProfile.getImages()) {
            if (imageType.equals(img.getFileType())) {
                imageUrl = img.getSupabaseUrl();
                break;
            }
        }

        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                // For simplicity, opening in default browser.
                // new ProcessBuilder("cmd", "/c", "start", imageUrl).start(); 
                // Or, display in a new JavaFX window:
                Stage imageStage = new Stage();
                ImageView imageView = new ImageView(new javafx.scene.image.Image(imageUrl, true)); // true for background loading
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(800); // Adjust as needed
                imageView.setFitHeight(600); // Adjust as needed

                StackPane pane = new StackPane(imageView);
                Scene scene = new Scene(pane);
                
                imageStage.setTitle(imageType.replace("_", " ").toUpperCase() + " Image");
                imageStage.setScene(scene);
                imageStage.show();

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Could not open image: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.INFORMATION, "No Image", "No " + imageType.replace("_", " ") + " image found for this application.");
        }
    }


    @FXML
    void backBtn(ActionEvent event) {
        try {
            // Navigate back to the application status page or another appropriate page
            Main.setRoot("UserApplicationStatus");
        } catch (IOException e) {
            System.err.println("Error navigating back: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not go back.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
