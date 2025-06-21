package com.example.controller;

import java.io.IOException;

import com.example.Main;
import com.example.dao.AccountDAO;
import com.example.model.Account;
import com.example.service.ApplicationService;
import com.example.util.PasswordUtil;
import com.example.util.UserSession;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class UserLoginController {

    @FXML
    private TextField emailTxtF;

    @FXML
    private TextField passTxtF;

    private AccountDAO accountDAO;
    private ApplicationService applicationService;

    @FXML
    public void initialize() {
        accountDAO = new AccountDAO();
        applicationService = new ApplicationService();
    }

    @FXML
    void loginBtn(ActionEvent event) {
        String email = emailTxtF.getText().trim();
        String password = passTxtF.getText();

        // Validate input
        if (!validateInput(email, password)) {
            return;
        }

        try {
            // Authenticate user
            Account account = accountDAO.findByUsername(email);
            
            if (account == null) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", 
                         "No account found with this email address.");
                return;
            }

            // Verify password
            if (!PasswordUtil.verifyPassword(password, account.getPassword())) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", 
                         "Invalid password. Please try again.");
                return;
            }

            // Check if account is a user (not admin)
            if (!"user".equals(account.getRole())) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", 
                         "This account is not authorized for user login.");
                return;
            }

            // Login successful - create user session
            UserSession.getInstance().login(account.getUserId(), account.getUsername(), account.getRole());

            // Clear form
            clearForm();

            // Check user's application status and route accordingly
            routeUserBasedOnApplicationStatus();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Login Error", 
                     "An error occurred during login: " + e.getMessage());
        }
    }

    @FXML
    void registerBtn(ActionEvent event) {
        try {
            Main.setRoot("UserRegister");
        } catch (IOException e) {
            System.err.println("Error loading UserRegister.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateInput(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", 
                     "Please fill in all fields.");
            return false;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", 
                     "Please enter a valid email address.");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    // Renamed and updated logic
    private void routeUserBasedOnApplicationStatus() {
        try {
            // Use the lightweight check to determine if an accepted profile exists.
            if (applicationService.hasAcceptedApplication()) {
                // User has an accepted application - go to UserPassportInfo
                System.out.println("User has an accepted application. Routing to UserPassportInfo...");
                Main.setRoot("UserPassportInfo");
            } else {
                // User doesn't have an accepted application (or no application at all) - go to UserProfile
                // UserProfileController will then decide whether to show UserApplicationStatus or UserNotPassportHolder
                System.out.println("User does not have an accepted application. Routing to UserProfile...");
                Main.setRoot("UserProfile");
            }
            
        } catch (IOException e) {
            System.err.println("Error during post-login navigation: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                     "Login successful, but could not navigate to the appropriate page.");
        }
    }

    @FXML
    void switchBtn(ActionEvent event) {
        try {
            Main.setRoot("AdminLogin");
        } catch (IOException e) {
            System.err.println("Error loading AdminLogin.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        emailTxtF.clear();
        passTxtF.clear();
    }
}
