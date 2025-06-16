package com.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;

public class UserApplicationFormController {

    @FXML
    private SidebarController sidebarController;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane scrollContent;
    
    // Text fields
    @FXML private TextField lblLastName;
    @FXML private TextField lblFirstName;
    @FXML private TextField lblMiddleName;
    // Other form fields...
    
    // ComboBoxes
    @FXML private ComboBox<Integer> lblBirthDay;
    @FXML private ComboBox<String> lblBirthMonth;
    @FXML private ComboBox<Integer> lblBirthYear;
    @FXML private ComboBox<String> lblGender;
    @FXML private ComboBox<Integer> lblIssueDay;
    @FXML private ComboBox<String> lblIssueMonth;
    @FXML private ComboBox<Integer> lblIssueYear;
    
    // File upload components
    @FXML private Button btnUploadValidID;
    @FXML private Button btnUploadPSA;
    @FXML private Label lblValidIdFilename;
    @FXML private Label lblPSAFilename;
    
    // Radio button groups
    @FXML private ToggleGroup civilStatusGroup;
    @FXML private ToggleGroup citizenshipGroup;
    @FXML private ToggleGroup foreignPassportGroup;
    @FXML private ToggleGroup philippinePassportGroup;
    @FXML private ToggleGroup minorGroup;

    // Store the selected files
    private File validIdFile;
    private File psaFile;

    private final ObservableList<String> months = FXCollections.observableArrayList(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    );

    private final ObservableList<String> genders = FXCollections.observableArrayList(
            "Male", "Female"
    );

    @FXML
    public void initialize() {
        // Set the active tab in the sidebar
        if (sidebarController != null) {
            sidebarController.setActiveTab("application");
        }

        // Scroll Pane setup
        if (scrollPane != null) {
            scrollPane.setFitToWidth(true);
            scrollPane.setPannable(true);
        }

        // Setup birth date ComboBoxes
        setupDateComboBoxes(lblBirthMonth, lblBirthYear, lblBirthDay);
        
        // Setup issue date ComboBoxes
        setupDateComboBoxes(lblIssueMonth, lblIssueYear, lblIssueDay);

        // Setup gender ComboBox
        if (lblGender != null) {
            lblGender.setItems(genders);
            lblGender.setValue("Male");
            lblGender.setEditable(false); // Make non-editable
        }
    }

    private void setupDateComboBoxes(ComboBox<String> monthCombo, ComboBox<Integer> yearCombo, ComboBox<Integer> dayCombo) {
        // Make ComboBoxes non-editable to prevent type conversion issues
        if (monthCombo != null) {
            monthCombo.setOnAction(null);
            monthCombo.setItems(months);
            monthCombo.setValue("January");
            monthCombo.setEditable(false); // Make non-editable
        }

        if (yearCombo != null) {
            yearCombo.setOnAction(null);
            yearCombo.setItems(generateYears(1925, 2025));
            yearCombo.setValue(2024);
            yearCombo.setEditable(false); // Make non-editable
        }

        if (dayCombo != null) {
            dayCombo.setOnAction(null);
            dayCombo.setEditable(false); // Make non-editable
        }

        // First update days without listeners
        updateDays(monthCombo, yearCombo, dayCombo);

        // Now add the listeners with error handling
        if (monthCombo != null) {
            monthCombo.setOnAction(e -> {
                try {
                    updateDays(monthCombo, yearCombo, dayCombo);
                } catch (Exception ex) {
                    System.err.println("Error updating days based on month change: " + ex.getMessage());
                }
            });
        }
        
        if (yearCombo != null) {
            yearCombo.setOnAction(e -> {
                try {
                    updateDays(monthCombo, yearCombo, dayCombo);
                } catch (Exception ex) {
                    System.err.println("Error updating days based on year change: " + ex.getMessage());
                }
            });
        }
    }

    @FXML
    void uploadValidIDBtn(ActionEvent event) {
        File selectedFile = showFileChooser("Select Valid ID Image");
        
        if (selectedFile != null) {
            validIdFile = selectedFile;
            lblValidIdFilename.setText(selectedFile.getName());
            System.out.println("Valid ID selected: " + selectedFile.getAbsolutePath());
        }
    }

    @FXML
    void uploadPSABtn(ActionEvent event) {
        File selectedFile = showFileChooser("Select PSA Birth Certificate");
        
        if (selectedFile != null) {
            psaFile = selectedFile;
            lblPSAFilename.setText(selectedFile.getName());
            System.out.println("PSA Birth Certificate selected: " + selectedFile.getAbsolutePath());
        }
    }
    
    private File showFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        
        // Set allowed file types
        fileChooser.getExtensionFilters().addAll(
            new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"),
            new ExtensionFilter("All Files", "*.*")
        );
        
        // Set initial directory to user home folder
        fileChooser.setInitialDirectory(
            new File(System.getProperty("user.home"))
        );
        
        // Show the file chooser dialog
        return fileChooser.showOpenDialog(scrollPane.getScene().getWindow());
    }

    @FXML
    void applyBtn(ActionEvent event) {
        System.out.println("Apply button clicked - attachment validation only");
        
        // Simple validation for file attachments
        boolean hasValidId = (validIdFile != null);
        boolean hasPsaBirthCertificate = (psaFile != null);
        
        StringBuilder message = new StringBuilder("Document Validation:\n");
        message.append("Valid ID: ").append(hasValidId ? "✓ Attached" : "✗ Missing").append("\n");
        message.append("PSA Birth Certificate: ").append(hasPsaBirthCertificate ? "✓ Attached" : "✗ Missing");
        
        // Show validation status
        Alert alert;
        if (hasValidId && hasPsaBirthCertificate) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Documents Complete");
            alert.setHeaderText("All Required Documents Attached");
        } else {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Documents");
            alert.setHeaderText("Required Documents Missing");
        }
        
        alert.setContentText(message.toString());
        alert.showAndWait();
    }

    private void updateDays(ComboBox<String> monthCombo, ComboBox<Integer> yearCombo, ComboBox<Integer> dayCombo) {
        if (dayCombo == null || monthCombo == null || yearCombo == null) {
            return;
        }
        
        String selectedMonth = monthCombo.getValue();
        Integer selectedYear = yearCombo.getValue();
        Integer currentDay = null;
        
        try {
            currentDay = dayCombo.getValue();
        } catch (ClassCastException e) {
            // Handle case where value isn't an Integer
            System.err.println("Warning: Day value is not an integer: " + e.getMessage());
        }

        if (selectedMonth == null || selectedYear == null) {
            return;
        }
        
        int daysInMonth = getDaysInMonth(selectedMonth, selectedYear);
        ObservableList<Integer> days = FXCollections.observableArrayList();
        for (int i = 1; i <= daysInMonth; i++) {
            days.add(i);
        }
        
        // Store previous selection temporarily
        Integer previousSelection = currentDay;
        
        // Update items without triggering any events
        dayCombo.setItems(days);
        
        // Try to keep the same day selected if possible
        if (previousSelection == null || previousSelection > daysInMonth) {
            dayCombo.setValue(1);
        } else {
            dayCombo.setValue(previousSelection);
        }
    }

    private int getDaysInMonth(String month, int year) {
        switch (month) {
            case "February":
                return isLeapYear(year) ? 29 : 28;
            case "April":
            case "June":
            case "September":
            case "November":
                return 30;
            default:
                return 31;
        }
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private ObservableList<Integer> generateYears(int start, int end) {
        ObservableList<Integer> years = FXCollections.observableArrayList();
        for (int y = start; y <= end; y++) {
            years.add(y);
        }
        return years;
    }
}

