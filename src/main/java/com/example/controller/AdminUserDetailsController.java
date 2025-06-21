package com.example.controller;

import com.example.model.*;
import com.example.service.ApplicationService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminUserDetailsController {
    private static PassportApplication currentApplication;

    public static void setCurrentApplication(PassportApplication app) {
        currentApplication = app;
    }

    @FXML private SidebarController sidebarController;

    @FXML private Label lblLastName;
    @FXML private Label lblFirstName;
    @FXML private Label lblMiddleName;
    @FXML private Label lblBirthDate;
    @FXML private Label lblPlaceOfBirth;
    @FXML private Label lblCompleteAddress;
    @FXML private Label lblCivilStatus;
    @FXML private Label lblGender;
    @FXML private Label lblMobileNumber1;
    @FXML private Label lblMobileNumber2;
    @FXML private Label lblTelephoneNumber1;
    @FXML private Label lblTelephoneNumber2;
    @FXML private Label lblEmailAddress1;
    @FXML private Label lblEmailAddress2;
    @FXML private Label lblOccupation1;
    @FXML private Label lblOccupation2;
    @FXML private Label lblWorkAddress1;
    @FXML private Label lblWorkAddress2;
    @FXML private Label lblWorkTelephone1;
    @FXML private Label lblWorkTelephone2;
    @FXML private Label lblSpouseName;
    @FXML private Label lblSpouseCitizenship;
    @FXML private Label lblFatherName;
    @FXML private Label lblMotherName;
    @FXML private Label lblFatherCitizenship;
    @FXML private Label lblMotherCitizenship;
    @FXML private Label lblCitizenshipAcquiredBy;
    @FXML private Label lblForeignPassportHolder;
    @FXML private Label lblCountry;
    @FXML private Label lblForeignPassportNo;
    @FXML private Label lblPhilippinePassportHolder;
    @FXML private Label lblPhilippinePassportNo;
    @FXML private Label lblIssueDate;
    @FXML private Label lblPlaceOfIssue;
    @FXML private Label lblIsMinor;
    @FXML private Label lblTravelingCompanion;
    @FXML private Label lblCompanionRelationship;
    @FXML private Label lblMinorContactNumber;
    @FXML private Button btnViewValidID;
    @FXML private Button btnViewPSA;
    @FXML private Button btnAcceptApplication;
    @FXML private Button btnDenyApplication;

    private ApplicationService applicationService = new ApplicationService();
    private UserProfile currentUserProfile;

    @FXML
    public void initialize() {
        if (sidebarController != null) {
            sidebarController.setActiveTab("users");
        }
        Platform.runLater(this::loadApplicationDetails);
    }

    private void loadApplicationDetails() {
        if (currentApplication == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No application selected.");
            return;
        }
        int applicationId = currentApplication.getApplicationId();
        currentUserProfile = applicationService.getCompleteUserProfile(applicationId);

        if (currentUserProfile != null) {
            populateFields(currentUserProfile);
            configureImageViewButtons(currentUserProfile.getImages());
        } else {
            showAlert(Alert.AlertType.WARNING, "No Data", "No application details found for the selected user.");
        }
    }

    private void populateFields(UserProfile profile) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

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

        List<UserContact> contacts = profile.getUserContacts();
        if (contacts != null) {
            if (contacts.size() > 0 && contacts.get(0) != null) {
                UserContact contact1 = contacts.get(0);
                setText(lblMobileNumber1, contact1.getMobileNumber());
                setText(lblTelephoneNumber1, contact1.getTelephoneNumber());
                setText(lblEmailAddress1, contact1.getEmailAddress());
            }
            if (contacts.size() > 1 && contacts.get(1) != null) {
                UserContact contact2 = contacts.get(1);
                setText(lblMobileNumber2, contact2.getMobileNumber());
                setText(lblTelephoneNumber2, contact2.getTelephoneNumber());
                setText(lblEmailAddress2, contact2.getEmailAddress());
            }
        }

        List<UserWork> works = profile.getUserWorks();
        if (works != null) {
            if (works.size() > 0 && works.get(0) != null) {
                UserWork work1 = works.get(0);
                setText(lblOccupation1, work1.getOccupation());
                setText(lblWorkTelephone1, work1.getWorkTelephoneNumber());
                setText(lblWorkAddress1, work1.getWorkAddress());
            }
            if (works.size() > 1 && works.get(1) != null) {
                UserWork work2 = works.get(1);
                setText(lblOccupation2, work2.getOccupation());
                setText(lblWorkTelephone2, work2.getWorkTelephoneNumber());
                setText(lblWorkAddress2, work2.getWorkAddress());
            }
        }

        UserSpouse spouse = profile.getSpouse();
        if (spouse != null) {
            setText(lblSpouseName, spouse.getSpouseFullName());
            setText(lblSpouseCitizenship, spouse.getSpouseCitizenship());
        } else {
            setText(lblSpouseName, "N/A");
            setText(lblSpouseCitizenship, "N/A");
        }

        UserParents parents = profile.getParents();
        if (parents != null) {
            setText(lblFatherName, parents.getFatherFullName());
            setText(lblMotherName, parents.getMotherMaidenName());
            setText(lblFatherCitizenship, parents.getFatherCitizenship());
            setText(lblMotherCitizenship, parents.getMotherCitizenship());
        }

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

        UserMinorInfo minorInfo = profile.getMinorInfo();
        if (minorInfo != null) {
            setText(lblIsMinor, minorInfo.isMinor() ? "YES" : "NO");
            if (minorInfo.isMinor()) {
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
    private void viewValidIDImage(ActionEvent event) {
        viewImageByType("Valid ID");
    }

    @FXML
    private void viewPSAImage(ActionEvent event) {
        viewImageByType("Birth Certificate");
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
                javafx.stage.Stage imageStage = new javafx.stage.Stage();
                javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(new javafx.scene.image.Image(imageUrl, true));
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(800); // Adjust as needed
                imageView.setFitHeight(600); // Adjust as needed

                javafx.scene.layout.StackPane pane = new javafx.scene.layout.StackPane(imageView);
                javafx.scene.Scene scene = new javafx.scene.Scene(pane);

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

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
