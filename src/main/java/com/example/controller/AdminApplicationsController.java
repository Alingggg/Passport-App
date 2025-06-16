package com.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import com.example.model.PassportApplication;
import com.example.model.UserInfo;
import com.example.dao.PassportApplicationDAO;
import com.example.dao.UserInfoDAO;
import com.example.controller.components.ApplicantCardController;

import java.util.List;

public class AdminApplicationsController {

    @FXML
    private SidebarController sidebarController;

    @FXML
    private Label lblAppAdminID;
    @FXML
    private Label lblApplications;

    @FXML
    private VBox applicationsVBox;

    private PassportApplicationDAO applicationDAO = new PassportApplicationDAO();
    private UserInfoDAO userInfoDAO = new UserInfoDAO();

    @FXML
    public void initialize() {
        if (sidebarController != null) {
            sidebarController.setActiveTab("applications");
        }
        loadPendingApplications();
    }

    private void loadPendingApplications() {
        applicationsVBox.getChildren().clear();
        List<PassportApplication> pendingApps = applicationDAO.getPendingApplications();
        System.out.println("Pending applications found: " + pendingApps.size());

        for (PassportApplication app : pendingApps) {
            try {
                UserInfo userInfo = userInfoDAO.findByUserId(app.getUserId());
                if (userInfo == null) {
                    System.out.println("No user info found for userId: " + app.getUserId());
                    continue;
                }
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxml/components/ApplicantCard.fxml"));
                Node cardNode = loader.load();
                ApplicantCardController cardController = loader.getController();

                cardController.setData(
                    userInfo.getLastName(),
                    userInfo.getFirstName(),
                    userInfo.getMiddleName(),
                    app
                );

                cardController.setOnViewApplicationAction(() -> {
                    try {
                        AdminApplicationDetailsController.setCurrentApplication(app);
                        com.example.Main.setRoot("AdminApplicationDetails");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                applicationsVBox.getChildren().add(cardNode);
                System.out.println("Added card for user: " + userInfo.getFirstName() + " " + userInfo.getLastName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
