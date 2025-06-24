package com.example.controller;

import com.example.util.supabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.Main;
import com.example.model.*;
import com.example.service.ApplicationService;
import com.example.util.ApplicationFormMapper;
import com.example.util.FormValidator;

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
    @FXML private TextField lblPlaceOfBirth;
    @FXML private TextField txtCitizenOthers;
    @FXML private TextField lblCompleteAddress;
    @FXML private TextField lblMobileNumber1;
    @FXML private TextField lblMobileNumber2;
    @FXML private TextField lblTelephoneNumber1;
    @FXML private TextField lblTelephoneNumber2;
    @FXML private TextField lblEmailAddress1;
    @FXML private TextField lblEmailAddress2;
    @FXML private TextField lblOccupation1;
    @FXML private TextField lblOccupation2;
    @FXML private TextField lblCountry;
    @FXML private TextField lblForeignPassportNo;
    @FXML private TextField lblWorkAddress1;
    @FXML private TextField lblWorkAddress2;
    @FXML private TextField lblWorkTelephone1;
    @FXML private TextField lblWorkTelephone2;
    @FXML private TextField lblSpouseName;
    @FXML private TextField lblSpouseCitizenship;
    @FXML private TextField lblFatherName;
    @FXML private TextField lblFatherCitizenship;
    @FXML private TextField lblMotherName;
    @FXML private TextField lblMotherCitizenship;
    @FXML private TextField lblPhilippinePassportNo;
    @FXML private TextField lblPlaceOfIssue;
    @FXML private TextField lblTravelingCompanion;
    @FXML private TextField lblCompanionRelationship;
    @FXML private TextField lblContactNumber;

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

        // Setup listeners for dependent fields
        setupDependentField(lblMobileNumber1, lblMobileNumber2);
        setupDependentField(lblTelephoneNumber1, lblTelephoneNumber2);
        setupDependentField(lblEmailAddress1, lblEmailAddress2);
        
        // Occupation 1 -> Occupation 2, Work Address 1, Work Telephone 1
        setupDependentField(lblOccupation1, lblOccupation2, lblWorkAddress1, lblWorkTelephone1);
        // Occupation 2 -> Work Address 2, Work Telephone 2
        setupDependentField(lblOccupation2, lblWorkAddress2, lblWorkTelephone2);

        // Listener for Acquired Citizenship
        txtCitizenOthers.setDisable(true);
        citizenshipGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                RadioButton selected = (RadioButton) newToggle;
                txtCitizenOthers.setDisable(!selected.getText().equals("Others:"));
            }
        });

        // Listener for Foreign Passport
        lblCountry.setDisable(true);
        lblForeignPassportNo.setDisable(true);
        foreignPassportGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            boolean isEnabled = newToggle != null && ((RadioButton) newToggle).getText().equals("YES");
            lblCountry.setDisable(!isEnabled);
            lblForeignPassportNo.setDisable(!isEnabled);
        });

        // Listener for Philippine Passport
        lblPhilippinePassportNo.setDisable(true);
        lblPlaceOfIssue.setDisable(true);
        lblIssueDay.setDisable(true);
        lblIssueMonth.setDisable(true);
        lblIssueYear.setDisable(true);
        philippinePassportGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            boolean isEnabled = newToggle != null && ((RadioButton) newToggle).getText().equals("YES");
            lblPhilippinePassportNo.setDisable(!isEnabled);
            lblPlaceOfIssue.setDisable(!isEnabled);
            lblIssueDay.setDisable(!isEnabled);
            lblIssueMonth.setDisable(!isEnabled);
            lblIssueYear.setDisable(!isEnabled);
        });

        // Listener for Minor/Companion fields
        lblTravelingCompanion.setDisable(true);
        lblCompanionRelationship.setDisable(true);
        lblContactNumber.setDisable(true);
        minorGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            boolean isEnabled = newToggle != null && ((RadioButton) newToggle).getText().equals("YES");
            lblTravelingCompanion.setDisable(!isEnabled);
            lblCompanionRelationship.setDisable(!isEnabled);
            lblContactNumber.setDisable(!isEnabled);
        });
    }

    private void setupDependentField(TextField primaryField, Control... dependentFields) {
        // Initially disable all dependent fields
        for (Control field : dependentFields) {
            field.setDisable(true);
        }

        // Add a listener to the primary field's text property
        primaryField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Enable the dependent fields only if the primary field is not empty
            boolean isDisabled = newValue == null || newValue.trim().isEmpty();
            for (Control field : dependentFields) {
                field.setDisable(isDisabled);
            }
        });
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
        System.out.println("Apply button clicked - starting application submission");
        
        ApplicationService applicationService = new ApplicationService();

        // Check if the user already has an application before proceeding
        PassportApplication existingApplication = applicationService.getLatestApplication();

        if (existingApplication != null) {
            String status = existingApplication.getStatus();
            
            // Prevent new application if one is still "Pending"
            if ("Pending".equalsIgnoreCase(status)) {
                showAlert(Alert.AlertType.WARNING, "Application In Progress", "You already have a pending application. You cannot submit a new one at this time.");
                return; // Stop further processing
            }

            // Prevent new application if one is "Accepted" but the card has not yet been received.
            if ("Accepted".equalsIgnoreCase(status) && !existingApplication.isCardReceived()) {
                showAlert(Alert.AlertType.WARNING, "Application In Progress", "Your application has been accepted, but the process is not yet complete. You cannot submit a new application until you receive your card.");
                return; // Stop further processing
            }
        }
        
        // --- START OF NEW VALIDATION LOGIC ---
        if (!isFormValid()) {
            return; // Stop if form is not valid
        }
        // --- END OF NEW VALIDATION LOGIC ---
        
        try {
            // Instantiate the mapper to handle data conversion
            ApplicationFormMapper mapper = new ApplicationFormMapper(months);

            // Create model objects using the mapper
            UserInfo userInfo = mapper.createUserInfo(lblLastName, lblFirstName, lblMiddleName, lblPlaceOfBirth, lblBirthYear, lblBirthMonth, lblBirthDay, lblGender, civilStatusGroup, lblCompleteAddress, citizenshipGroup, txtCitizenOthers);
            List<UserContact> userContacts = mapper.createUserContacts(lblMobileNumber1, lblTelephoneNumber1, lblEmailAddress1, lblMobileNumber2, lblTelephoneNumber2, lblEmailAddress2);
            List<UserWork> userWorks = mapper.createUserWorks(lblOccupation1, lblWorkAddress1, lblWorkTelephone1, lblOccupation2, lblWorkAddress2, lblWorkTelephone2);
            UserForeignPassport foreignPassport = mapper.createForeignPassport(foreignPassportGroup, lblCountry, lblForeignPassportNo);
            UserSpouse spouse = mapper.createSpouse(lblSpouseName, lblSpouseCitizenship);
            UserParents parents = mapper.createParents(lblFatherName, lblFatherCitizenship, lblMotherName, lblMotherCitizenship);
            UserPhilippinePassport philippinePassport = mapper.createPhilippinePassport(philippinePassportGroup, lblIssueDay, lblIssueMonth, lblIssueYear, lblPlaceOfIssue, lblPhilippinePassportNo);
            UserMinorInfo minorInfo = mapper.createMinorInfo(minorGroup, lblTravelingCompanion, lblCompanionRelationship, lblContactNumber);
            
            // Upload images to Supabase and create image records
            List<Image> images = createImages();
            
            // Check if image upload was successful
            if (images.isEmpty() && (validIdFile != null || psaFile != null)) { 
                System.err.println("Image upload failed or was canceled, but files were selected.");
                return;
            }
            
            // Submit application using the service
            boolean success = applicationService.submitCompleteApplication(
                userInfo, userContacts, userWorks, foreignPassport, 
                spouse, parents, philippinePassport, minorInfo, images
            );
            
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Application Submitted", "Your application was submitted successfully! Your passport application is now being processed.");
                try {
                    Main.setRoot("UserApplicationStatus");
                } catch (IOException e) {
                    System.err.println("Error loading UserApplicationStatus.fxml: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Submission Failed", "There was an error processing your application. Please try again later.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please check that all required fields are filled in correctly.\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isFormValid() {
        FormValidator validator = new FormValidator();

        // Personal Info
        validator.validateRequiredTextField(lblLastName, "Last Name");
        validator.validateRequiredTextField(lblFirstName, "First Name");
        validator.validateRequiredTextField(lblMiddleName, "Middle Name");
        validator.validateRequiredTextField(lblPlaceOfBirth, "Place of Birth");
        validator.validateRequiredTextField(lblCompleteAddress, "Complete Address");
        validator.validateRequiredComboBox(lblBirthDay, "Birth Day");
        validator.validateRequiredComboBox(lblBirthMonth, "Birth Month");
        validator.validateRequiredComboBox(lblBirthYear, "Birth Year");
        validator.validateRequiredComboBox(lblGender, "Gender");
        validator.validateRequiredToggleGroup(civilStatusGroup, "Civil Status");
        validator.validateRequiredToggleGroup(citizenshipGroup, "Acquired Citizenship");

        if (citizenshipGroup.getSelectedToggle() != null && ((RadioButton)citizenshipGroup.getSelectedToggle()).getText().equals("Others:")) {
            validator.validateRequiredTextField(txtCitizenOthers, "Other Citizenship");
        }

        // Contact Info
        validator.validateNumericTextField(lblMobileNumber1, "Mobile Number 1");
        validator.validateEmailField(lblEmailAddress1, "Email Address 1");
        validator.validateNumericTextField(lblTelephoneNumber1, "Telephone Number 1");

        // Work Info
        validator.validateRequiredTextField(lblOccupation1, "Occupation 1");
        validator.validateRequiredTextField(lblWorkAddress1, "Work Address 1");
        validator.validateNumericTextField(lblWorkTelephone1, "Work Telephone 1");

        // Spouse Info
        if (civilStatusGroup.getSelectedToggle() != null && ((RadioButton)civilStatusGroup.getSelectedToggle()).getText().equals("Married")) {
            validator.validateRequiredTextField(lblSpouseName, "Spouse's Full Name");
            validator.validateRequiredTextField(lblSpouseCitizenship, "Spouse's Citizenship");
        }

        // Parents Info
        validator.validateRequiredTextField(lblFatherName, "Father's Full Name");
        validator.validateRequiredTextField(lblFatherCitizenship, "Father's Citizenship");
        validator.validateRequiredTextField(lblMotherName, "Mother's Maiden Name");
        validator.validateRequiredTextField(lblMotherCitizenship, "Mother's Citizenship");

        // Foreign Passport
        validator.validateRequiredToggleGroup(foreignPassportGroup, "Foreign Passport Holder");
        if (foreignPassportGroup.getSelectedToggle() != null && ((RadioButton)foreignPassportGroup.getSelectedToggle()).getText().equals("YES")) {
            validator.validateRequiredTextField(lblCountry, "Issuing Country");
            validator.validateRequiredTextField(lblForeignPassportNo, "Foreign Passport Number");
        }

        // Philippine Passport
        validator.validateRequiredToggleGroup(philippinePassportGroup, "Philippine Passport Holder");
        if (philippinePassportGroup.getSelectedToggle() != null && ((RadioButton)philippinePassportGroup.getSelectedToggle()).getText().equals("YES")) {
            validator.validateRequiredTextField(lblPhilippinePassportNo, "Philippine Passport Number");
            validator.validateRequiredTextField(lblPlaceOfIssue, "Place of Issue");
            validator.validateRequiredComboBox(lblIssueDay, "Issue Day");
            validator.validateRequiredComboBox(lblIssueMonth, "Issue Month");
            validator.validateRequiredComboBox(lblIssueYear, "Issue Year");
        }

        // Minor Info
        validator.validateRequiredToggleGroup(minorGroup, "Accompanied by a minor");
        if (minorGroup.getSelectedToggle() != null && ((RadioButton)minorGroup.getSelectedToggle()).getText().equals("YES")) {
            validator.validateRequiredTextField(lblTravelingCompanion, "Traveling Companion's Name");
            validator.validateRequiredTextField(lblCompanionRelationship, "Companion's Relationship");
            validator.validateNumericTextField(lblContactNumber, "Companion's Contact Number");
        }

        // File Uploads
        if (validIdFile == null) validator.getErrors().add("A Valid ID image is required.");
        if (psaFile == null) validator.getErrors().add("A PSA Birth Certificate image is required.");

        List<String> errors = validator.getErrors();
        if (!errors.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Please correct the following errors:\n\n");
            for (String error : errors) {
                errorMessage.append("- ").append(error).append("\n");
            }
            showAlert(Alert.AlertType.ERROR, "Validation Error", errorMessage.toString());
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setPrefWidth(400);
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

    private List<Image> createImages() {
        List<Image> images = new ArrayList<>();
        
        // --- START: Improved Progress Dialog ---
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setPrefSize(60, 60);

        Label titleLabel = new Label("Uploading Documents");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #40659C;");

        Label statusLabel = new Label("Please wait, this may take a moment...");
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #696868;");
        statusLabel.setWrapText(true);
        statusLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        VBox progressBox = new VBox(20, titleLabel, progressIndicator, statusLabel);
        progressBox.setAlignment(Pos.CENTER);
        progressBox.setPadding(new Insets(30));
        progressBox.setStyle("-fx-background-color: #F8F4F1;");
        progressBox.setPrefSize(400, 250);

        Stage progressStage = new Stage();
        progressStage.initModality(Modality.APPLICATION_MODAL);
        progressStage.initOwner(scrollPane.getScene().getWindow());
        progressStage.setResizable(false);
        progressStage.setTitle("Uploading...");

        Scene scene = new Scene(progressBox);
        progressStage.setScene(scene);
        // --- END: Improved Progress Dialog ---
        
        // Create and start the upload task
        Task<List<Image>> uploadTask = new Task<>() {
            @Override
            protected List<Image> call() throws Exception {
                List<Image> uploadedImages = new ArrayList<>();
                try {
                    if (validIdFile != null) {
                        updateMessage("Uploading Valid ID to Supabase...");
                        String validIdSupabaseUrl = supabaseUtil.uploadFile(validIdFile); // This returns the public URL structure
                        if (validIdSupabaseUrl == null) {
                            throw new IOException("Failed to upload Valid ID to Supabase");
                        }
                        // Extract the unique filename from the URL that supabaseUtil constructed
                        String uniqueValidIdName = validIdSupabaseUrl.substring(validIdSupabaseUrl.lastIndexOf('/') + 1);
                        
                        Image validIdImage = new Image();
                        validIdImage.setFilename(uniqueValidIdName); // STORE THE UNIQUE FILENAME
                        validIdImage.setFileType("Valid ID");
                        validIdImage.setSupabaseUrl(validIdSupabaseUrl); // Store the original public-like URL for reference if needed
                        uploadedImages.add(validIdImage);
                    }

                    if (psaFile != null) {
                        updateMessage("Uploading Birth Certificate to Supabase...");
                        String psaSupabaseUrl = supabaseUtil.uploadFile(psaFile);
                        if (psaSupabaseUrl == null) {
                            throw new IOException("Failed to upload Birth Certificate to Supabase");
                        }
                        String uniquePsaName = psaSupabaseUrl.substring(psaSupabaseUrl.lastIndexOf('/') + 1);

                        Image psaImage = new Image();
                        psaImage.setFilename(uniquePsaName); // STORE THE UNIQUE FILENAME
                        psaImage.setFileType("Birth Certificate");
                        psaImage.setSupabaseUrl(psaSupabaseUrl);
                        uploadedImages.add(psaImage);
                    }
                    
                    updateMessage("Upload completed successfully");
                    return uploadedImages;
                } catch (Exception e) {
                    updateMessage("Upload failed: " + e.getMessage());
                    e.printStackTrace();
                    throw e; // Re-throw to be caught by onFailed
                }
            }
        };
        
        // Bind the task message to the status label
        statusLabel.textProperty().bind(uploadTask.messageProperty());
        
        // Handle task completion
        uploadTask.setOnSucceeded(e -> {
            progressStage.close();
            images.addAll(uploadTask.getValue());
        });
        
        uploadTask.setOnFailed(e -> {
            progressStage.close();
            Throwable exception = uploadTask.getException();
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Upload Error");
            alert.setHeaderText("Failed to upload documents");
            alert.setContentText("An error occurred while uploading documents to Supabase:\n" + 
                                exception.getMessage());
            alert.showAndWait();
            
            exception.printStackTrace();
        });
        
        // Start the task in a new thread
        new Thread(uploadTask).start();
        
        // Show the progress stage and wait until it's closed
        progressStage.showAndWait();
        
        return images; // This will be populated after showAndWait() if task is successful
    }
}

