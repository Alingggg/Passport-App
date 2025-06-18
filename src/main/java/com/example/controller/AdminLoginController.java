package com.example.controller;

import com.example.Main;
import com.example.dao.AccountDAO;
import com.example.dao.AdminInfoDAO;
import com.example.model.Account;
import com.example.model.AdminInfo;
import com.example.util.PasswordUtil;
import com.example.util.UserSession;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AdminLoginController {
    
    @FXML
    private TextField usernameTxtF;
    @FXML
    private PasswordField passTxtF;
    
    private AccountDAO accountDAO;
    private AdminInfoDAO adminInfoDAO;
    
    @FXML
    public void initialize() {
        accountDAO = new AccountDAO();
        adminInfoDAO = new AdminInfoDAO();
    }
    
    @FXML
    void loginBtn(ActionEvent event) {
        String username = usernameTxtF.getText().trim();
        String password = passTxtF.getText();

        // Validate input
        if (!validateInput(username, password)) {
            return;
        }

        try {
            // Authenticate admin with username
            Account account = accountDAO.findByUsername(username);
            
            if (account == null) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", 
                         "No account found with this username.");
                return;
            }

            // Verify password
            if (!PasswordUtil.verifyPassword(password, account.getPassword())) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", 
                         "Invalid password. Please try again.");
                return;
            }

            // Check if account is an admin (not user)
            if (!"admin".equals(account.getRole())) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", 
                         "This account is not authorized for admin login.");
                return;
            }
            
            // Get admin info to retrieve admin ID
            AdminInfo adminInfo = adminInfoDAO.findByAccountId(account.getUserId());
            if (adminInfo == null) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", 
                         "Admin details not found. Please contact system administrator.");
                return;
            }

            // Login successful - create user session with admin info
            UserSession userSession = UserSession.getInstance();
            userSession.login(account.getUserId(), account.getUsername(), account.getRole());
            // This is redundant since login() already sets it, but keeping for clarity
            userSession.setAdminId(adminInfo.getAdminId());

            // Clear form
            clearForm();

            // Navigate to admin dashboard
            try {
                Main.setRoot("Admin/AdminUsers");
            } catch (IOException e) {
                System.err.println("Error loading AdminUsers.fxml: " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                         "Login successful, but could not navigate to the admin dashboard.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Login Error", 
                     "An error occurred during login: " + e.getMessage());
        }
    }
    
    @FXML
    void userBtn(ActionEvent event) {
        try {
            Main.setRoot("User/UserLogin");
        } catch (IOException e) {
            System.err.println("Error loading UserLogin.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private boolean validateInput(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", 
                     "Please fill in both username and password fields.");
            return false;
        }

        // Basic validation for admin username (customize as needed)
        if (username.length() < 3) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", 
                     "Username must be at least 3 characters long.");
            return false;
        }

        return true;
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void clearForm() {
        usernameTxtF.clear();
        passTxtF.clear();
    }
}
