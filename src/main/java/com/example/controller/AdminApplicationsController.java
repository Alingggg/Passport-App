package com.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class AdminApplicationsController {

        @FXML
    private SidebarController sidebarController;

        @FXML
        private Label lblAppAdminID;
        @FXML
        private Label lblApplications;

        @FXML
        private VBox applicationsVBox;

        @FXML private AnchorPane applicant1;
        @FXML private Label lblLastName;
        @FXML private Label lblFirstName;
        @FXML private Label lblMiddleName;
        @FXML private Label btnApplication;

        @FXML private AnchorPane applicant2;
        @FXML private Label lblLastName2;
        @FXML private Label lblFirstName1;
        @FXML private Label lblMiddleName1;
        @FXML private Label btnApplication2;

        @FXML private AnchorPane applicant3;
        @FXML private Label lblLastName3;
        @FXML private Label lblFirstName2;
        @FXML private Label lblMiddleName2;
        @FXML private Label btnApplication3;

        @FXML private AnchorPane applicant4;
        @FXML private Label lblLastNam4;
        @FXML private Label lblFirstName3;
        @FXML private Label lblMiddleName3;
        @FXML private Label btnApplication4;

        @FXML private AnchorPane applicant5;
        @FXML private Label lblLastName5;
        @FXML private Label lblFirstName4;
        @FXML private Label lblMiddleName4;
        @FXML private Label btnApplication5;

        @FXML private AnchorPane applicant6;
        @FXML private Label lblLastName6;
        @FXML private Label lblFirstName5;
        @FXML private Label lblMiddleName5;
        @FXML private Label btnApplication6;

        @FXML private AnchorPane applicant61;
        @FXML private Label lblLastName61;
        @FXML private Label lblFirstName51;
        @FXML private Label lblMiddleName51;
        @FXML private Label btnApplication61;

        @FXML
    public void initialize() {
        if (sidebarController != null) {
            sidebarController.setActiveTab("applications");
        }
    }

}
