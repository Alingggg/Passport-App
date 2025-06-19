package com.example.controller;

import com.example.util.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminHeaderController {
    
    @FXML
    private Label lblAdminID;
    
    @FXML
    private Label lblAdminName;
    
    private final UserSession userSession = UserSession.getInstance();
    
    @FXML
    public void initialize() {

        updateAdminInfo();
    }
    
    // Update the admin ID and name with information from UserSession
    public void updateAdminInfo() {
        if (userSession.isAuthenticated() && userSession.isAdmin()) {
            // Update admin ID
            String adminId = userSession.getAdminId();
            if (lblAdminID != null) {
                lblAdminID.setText(adminId != null ? adminId : "N/A");
            }
            
            // Update admin name
            String adminName = userSession.getAdminFullName();
            if (lblAdminName != null) {
                lblAdminName.setText(adminName != null ? adminName : userSession.getUsername());
            }
        } else {
            // If not an admin or not authenticated, display placeholder or nothing
            if (lblAdminID != null) {
                lblAdminID.setText("Not logged in");
            }
            if (lblAdminName != null) {
                lblAdminName.setText("");
            }
        }
    }
    
    
    // Set custom text for the admin ID label
    public void setAdminId(String id) {
        if (lblAdminID != null) {
            lblAdminID.setText(id);
        }
    }
    
    
    // Set custom text for the admin name label
    public void setAdminName(String name) {
        if (lblAdminName != null) {
            lblAdminName.setText(name);
        }
    }
}
