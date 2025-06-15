package com.example.controller;

import java.io.IOException;

import com.example.Main;
import com.example.dao.AccountDAO;
import com.example.dao.UserPhilippinePassportDAO;
import com.example.model.Account;
import com.example.model.UserPhilippinePassport;
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
    private UserPhilippinePassportDAO philippinePassportDAO;

    @FXML
    public void initialize() {
        accountDAO = new AccountDAO();
        philippinePassportDAO = new UserPhilippinePassportDAO();
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

            // Check if user has Philippine passport and route accordingly
            routeUserBasedOnPassportStatus(account.getUserId());

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

    private void routeUserBasedOnPassportStatus(Integer userId) {
        try {
            // Check if user has Philippine passport information
            UserPhilippinePassport philippinePassport = philippinePassportDAO.findByUserId(userId);
            
            if (philippinePassport != null && philippinePassport.getHasPhilippinePassport()) {
                // User has Philippine passport - go to UserPassportInfo to view their data
                System.out.println("User has Philippine passport. Routing to UserPassportInfo...");
                Main.setRoot("UserPassportInfo");
            } else {
                // User doesn't have Philippine passport - go to UserProfile to apply
                System.out.println("User doesn't have Philippine passport. Routing to UserProfile...");
                Main.setRoot("UserProfile");
            }
            
        } catch (IOException e) {
            System.err.println("Error during post-login navigation: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                     "Login successful, but could not navigate to the appropriate page.");
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
