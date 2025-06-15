package com.example.controller;

import java.io.IOException;

import com.example.Main;
import com.example.dao.AccountDAO;
import com.example.model.Account;
import com.example.util.PasswordUtil;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class UserRegisterController {

    @FXML
    private TextField emailTxtF;

    @FXML
    private PasswordField passTxtF;

    @FXML
    private PasswordField confirmTxtF;

    private AccountDAO accountDAO;

    @FXML
    public void initialize() {
        accountDAO = new AccountDAO();
    }

    @FXML
    void registerBtn(ActionEvent event) {
        String email = emailTxtF.getText().trim();
        String password = passTxtF.getText();
        String confirmPassword = confirmTxtF.getText();

        // Validate input
        if (!validateInput(email, password, confirmPassword)) {
            return;
        }

        try {
            // Check if user already exists
            if (accountDAO.usernameExists(email)) {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", 
                         "An account with this email already exists.");
                return;
            }

            // Hash the password
            String hashedPassword = PasswordUtil.hashPassword(password);

            // Create new account
            Account newAccount = new Account(email, hashedPassword, "user");
            
            // Save to database
            boolean success = accountDAO.createAccount(newAccount);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Registration Successful", 
                         "Your account has been created successfully!\nYou can now log in with your credentials.");
                
                // Clear form fields
                clearForm();
                
                // Navigate to login page
                navigateToLogin();
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", 
                         "Failed to create account. Please try again.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Registration Error", 
                     "An error occurred during registration: " + e.getMessage());
        }
    }

    @FXML
    void loginBtn(ActionEvent event) {
        navigateToLogin();
    }

    private boolean validateInput(String email, String password, String confirmPassword) {
        // Check if fields are empty
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", 
                     "Please fill in all fields.");
            return false;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", 
                     "Please enter a valid email address.");
            return false;
        }

        // Check password length
        if (password.length() < 6) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", 
                     "Password must be at least 6 characters long.");
            return false;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", 
                     "Passwords do not match.");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        // Simple email validation regex
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
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
        confirmTxtF.clear();
    }

    private void navigateToLogin() {
        try {
            Main.setRoot("UserLogin");
        } catch (IOException e) {
            System.err.println("Error loading UserLogin.fxml: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                     "Could not navigate to login page.");
        }
    }
}
