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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.Main;
import com.example.model.*;
import com.example.service.ApplicationService;

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
    // @FXML private TextField lblMobileNumber2;
    @FXML private TextField lblTelephoneNumber1;
    // @FXML private TextField lblTelephoneNumber2;
    @FXML private TextField lblEmailAddress;
    @FXML private TextField lblOccupation;
    @FXML private TextField lblCountry;
    @FXML private TextField lblForeignPassportNo;
    @FXML private TextField lblWorkAddress;
    @FXML private TextField lblWorkTelephone;
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
        
        ApplicationService applicationService = new ApplicationService(); // Instantiate ApplicationService for the check

        // Check if the user already has an application before proceeding
        if (applicationService.hasExistingApplication()) {
            Alert alert = new Alert(Alert.AlertType.WARNING); // Changed to WARNING for better user feedback
            alert.setTitle("Application Already Submitted");
            alert.setHeaderText("Duplicate Application Attempt");
            alert.setContentText("You have already submitted an application. You cannot submit another one at this time.");
            alert.showAndWait();
            return; // Stop further processing
        }
        
        // Validate required files
        boolean hasValidId = (validIdFile != null);
        boolean hasPsaBirthCertificate = (psaFile != null);
        
        if (!hasValidId || !hasPsaBirthCertificate) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Documents");
            alert.setHeaderText("Required Documents Missing");
            
            StringBuilder message = new StringBuilder("Please attach the following required documents:\n");
            if (!hasValidId) message.append("- Valid ID\n");
            if (!hasPsaBirthCertificate) message.append("- PSA Birth Certificate");
            
            alert.setContentText(message.toString());
            alert.showAndWait();
            return;
        }
        
        try {
            // Create model objects from form data
            UserInfo userInfo = createUserInfo();
            UserContact userContact = createUserContact();
            UserWork userWork = createUserWork();
            UserForeignPassport foreignPassport = createForeignPassport();
            UserSpouse spouse = createSpouse();
            UserParents parents = createParents();
            UserPhilippinePassport philippinePassport = createPhilippinePassport();
            UserMinorInfo minorInfo = createMinorInfo();
            
            // Upload images to Supabase and create image records
            List<Image> images = createImages();
            
            // Check if image upload was successful
            if (images.isEmpty() && (validIdFile != null || psaFile != null)) { 
                System.err.println("Image upload failed or was canceled, but files were selected.");
                // Alert for image upload failure is typically handled within createImages()
                return;
            }
            
            // Submit application using the service
            // ApplicationService applicationService = new ApplicationService(); // Already instantiated above
            boolean success = applicationService.submitCompleteApplication(
                userInfo, userContact, userWork, foreignPassport, 
                spouse, parents, philippinePassport, minorInfo, images
            );
            
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Application Submitted");
                alert.setHeaderText("Your application was submitted successfully!");
                alert.setContentText("Your passport application is now being processed.");
                alert.showAndWait();
                
                // Navigate to the status page
                try {
                    Main.setRoot("UserApplicationStatus");
                } catch (IOException e) {
                    System.err.println("Error loading UserApplicationStatus.fxml: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Submission Failed");
                alert.setHeaderText("Application could not be submitted");
                alert.setContentText("There was an error processing your application. Please try again later.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Form Error");
            alert.setHeaderText("Error in application form");
            alert.setContentText("Please check that all required fields are filled in correctly.\n" + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
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

    // Helper methods to create model objects from form fields
    private UserInfo createUserInfo() {
        UserInfo info = new UserInfo();
        info.setLastName(lblLastName.getText());
        info.setFirstName(lblFirstName.getText());
        info.setMiddleName(lblMiddleName.getText());
        
        // Set place of birth
        info.setBirthPlace(lblPlaceOfBirth.getText());

        // Set birth date
        LocalDate birthDate = LocalDate.of(
            lblBirthYear.getValue(),
            getMonthNumber(lblBirthMonth.getValue()),
            lblBirthDay.getValue()
        );
        info.setBirthDate(birthDate);
        
        // Set gender
        info.setGender(lblGender.getValue());
        
        // Set civil status from radio group
        RadioButton selectedCivilStatus = (RadioButton) civilStatusGroup.getSelectedToggle();
        if (selectedCivilStatus != null) {
            info.setCivilStatus(selectedCivilStatus.getText());
        }

        // Set address
        info.setCurrentAddress(lblCompleteAddress.getText());
        
        // Set citizenship acquisition type
        RadioButton selectedCitizenship = (RadioButton) citizenshipGroup.getSelectedToggle();
        if (selectedCitizenship != null) {
            String citizenshipType = selectedCitizenship.getText();
            
            // If "Others" is selected, get the value from the text field
            if (citizenshipType.equals("Others") && txtCitizenOthers.getText() != null && !txtCitizenOthers.getText().isEmpty()) {
                citizenshipType = txtCitizenOthers.getText();
            }
            
            info.setAcquiredCitizenship(citizenshipType);
        }
        
        return info;
    }

    private UserContact createUserContact() {
        UserContact contact = new UserContact();
        
        // Set mobile numbers
        if (lblMobileNumber1.getText() != null && !lblMobileNumber1.getText().isEmpty()) {
            contact.setMobileNumber(lblMobileNumber1.getText());
        }
        
        // if (lblMobileNumber2.getText() != null && !lblMobileNumber2.getText().isEmpty()) {
        //     contact.setMobileNumber2(lblMobileNumber2.getText());
        // }
        
        // Set telephone numbers
        if (lblTelephoneNumber1.getText() != null && !lblTelephoneNumber1.getText().isEmpty()) {
            contact.setTelephoneNumber(lblTelephoneNumber1.getText());
        }
        
        // if (lblTelephoneNumber2.getText() != null && !lblTelephoneNumber2.getText().isEmpty()) {
        //     contact.setTelephoneNumber2(lblTelephoneNumber2.getText());
        // }
        
        // Set email
        contact.setEmailAddress(lblEmailAddress.getText());
        
        return contact;
    }

    private UserWork createUserWork() {
        UserWork work = new UserWork();
        
        // Set occupation
        work.setOccupation(lblOccupation.getText());
        
        // Set work address
        work.setWorkAddress(lblWorkAddress.getText());
        
        // Set work telephone
        if (lblWorkTelephone.getText() != null && !lblWorkTelephone.getText().isEmpty()) {
            work.setWorkTelephoneNumber(lblWorkTelephone.getText());
        }
        
        return work;
    }

    private UserForeignPassport createForeignPassport() {
        UserForeignPassport foreignPassport = new UserForeignPassport();
        
        // Set has foreign passport flag based on radio selection
        RadioButton selectedOption = (RadioButton) foreignPassportGroup.getSelectedToggle();
        if (selectedOption != null) {
            foreignPassport.setHasForeignPassport(selectedOption.getText().equals("YES"));
        }

        if (lblCountry.getText() != null && !lblCountry.getText().isEmpty()) {
            foreignPassport.setIssuingCountry(lblCountry.getText());
        }

        if (lblForeignPassportNo.getText() != null && !lblForeignPassportNo.getText().isEmpty()) {
            foreignPassport.setForeignPassportNumber(lblForeignPassportNo.getText());
        }
        
        return foreignPassport;
    }

    private UserSpouse createSpouse() {
        UserSpouse spouse = new UserSpouse();
        
        // Set spouse name
        if (lblSpouseName.getText() != null && !lblSpouseName.getText().isEmpty()) {
            spouse.setSpouseFullName(lblSpouseName.getText());
        }
        
        // Set spouse citizenship
        if (lblSpouseCitizenship.getText() != null && !lblSpouseCitizenship.getText().isEmpty()) {
            spouse.setSpouseCitizenship(lblSpouseCitizenship.getText());
        }
        
        return spouse;
    }

    private UserParents createParents() {
        UserParents parents = new UserParents();
        
        // Set father's name
        if (lblFatherName.getText() != null && !lblFatherName.getText().isEmpty()) {
            parents.setFatherFullName(lblFatherName.getText());
        }
        
        // Set father's citizenship
        if (lblFatherCitizenship.getText() != null && !lblFatherCitizenship.getText().isEmpty()) {
            parents.setFatherCitizenship(lblFatherCitizenship.getText());
        }
        
        // Set mother's maiden name
        if (lblMotherName.getText() != null && !lblMotherName.getText().isEmpty()) {
            parents.setMotherMaidenName(lblMotherName.getText());
        }
        
        // Set mother's citizenship
        if (lblMotherCitizenship.getText() != null && !lblMotherCitizenship.getText().isEmpty()) {
            parents.setMotherCitizenship(lblMotherCitizenship.getText());
        }
        
        return parents;
    }

    private UserPhilippinePassport createPhilippinePassport() {
        UserPhilippinePassport philippinePassport = new UserPhilippinePassport();
        
        // Set has Philippine passport flag based on radio selection
        RadioButton selectedOption = (RadioButton) philippinePassportGroup.getSelectedToggle();
        if (selectedOption != null) {
            boolean hasPassport = selectedOption.getText().equals("YES");
            philippinePassport.setHasPhilippinePassport(hasPassport);
            
            // If user has a previous passport, collect the details
            if (hasPassport) {
                // Set issue date if all date components are selected
                if (lblIssueDay.getValue() != null && 
                    lblIssueMonth.getValue() != null && 
                    lblIssueYear.getValue() != null) {
                
                    LocalDate issueDate = LocalDate.of(
                        lblIssueYear.getValue(),
                        getMonthNumber(lblIssueMonth.getValue()),
                        lblIssueDay.getValue()
                    );
                    philippinePassport.setIssueDate(issueDate);
                }
                
                // Set place of issue
                if (lblPlaceOfIssue.getText() != null && !lblPlaceOfIssue.getText().isEmpty()) {
                    philippinePassport.setIssuePlace(lblPlaceOfIssue.getText());
                }

                if (lblPhilippinePassportNo.getText() != null && !lblPhilippinePassportNo.getText().isEmpty()) {
                    philippinePassport.setPhilippinePassportNumber(lblPhilippinePassportNo.getText());
                }
            }
        }

        return philippinePassport;
    }

    private UserMinorInfo createMinorInfo() {
        UserMinorInfo minorInfo = new UserMinorInfo();
        
        // Set is minor flag based on radio selection
        RadioButton selectedOption = (RadioButton) minorGroup.getSelectedToggle();
        if (selectedOption != null) {
            boolean isMinor = selectedOption.getText().equals("YES");
            minorInfo.setIsMinor(isMinor);
            
            // If applicant is a minor, collect companion information
            if (isMinor) {
                // Set traveling companion name
                if (lblTravelingCompanion.getText() != null && !lblTravelingCompanion.getText().isEmpty()) {
                    minorInfo.setCompanionFullName(lblTravelingCompanion.getText());
                }
                
                // Set companion relationship
                if (lblCompanionRelationship.getText() != null && !lblCompanionRelationship.getText().isEmpty()) {
                    minorInfo.setCompanionRelationship(lblCompanionRelationship.getText());
                }
                
                // Set companion contact number
                if (lblContactNumber.getText() != null && !lblContactNumber.getText().isEmpty()) {
                    minorInfo.setCompanionContactNumber(lblContactNumber.getText());
                }
            }
        }
        
        return minorInfo;
    }

    private List<Image> createImages() {
        List<Image> images = new ArrayList<>();
        
        // Show a progress dialog
        ProgressIndicator progress = new ProgressIndicator();
        progress.setMaxSize(50, 50);
        
        Label statusLabel = new Label("Uploading documents...");
        
        VBox progressBox = new VBox(10, statusLabel, progress);
        progressBox.setAlignment(Pos.CENTER);
        progressBox.setPadding(new Insets(20));
        
        Stage progressStage = new Stage();
        progressStage.setScene(new Scene(progressBox));
        progressStage.setTitle("Uploading Documents");
        progressStage.initModality(Modality.APPLICATION_MODAL);
        progressStage.setResizable(false);
        
        // Create and start the upload task
        Task<List<Image>> uploadTask = new Task<>() {
            @Override
            protected List<Image> call() throws Exception {
                List<Image> uploadedImages = new ArrayList<>();
                
                try {
                    // Upload Valid ID to Supabase
                    updateMessage("Uploading Valid ID to Supabase...");
                    String validIdUrl = supabaseUtil.uploadFile(validIdFile);
                    if (validIdUrl == null) {
                        throw new IOException("Failed to upload Valid ID to Supabase");
                    }
                    
                    // Create Valid ID image record
                    Image validIdImage = new Image();
                    validIdImage.setFilename(validIdFile.getName());
                    validIdImage.setFileType("Valid ID");
                    validIdImage.setSupabaseUrl(validIdUrl);
                    uploadedImages.add(validIdImage);
                    
                    // Upload PSA Birth Certificate to Supabase
                    updateMessage("Uploading Birth Certificate to Supabase...");
                    String psaUrl = supabaseUtil.uploadFile(psaFile);
                    if (psaUrl == null) {
                        throw new IOException("Failed to upload Birth Certificate to Supabase");
                    }
                    
                    // Create PSA Birth Certificate image record
                    Image psaImage = new Image();
                    psaImage.setFilename(psaFile.getName());
                    psaImage.setFileType("Birth Certificate");
                    psaImage.setSupabaseUrl(psaUrl);
                    uploadedImages.add(psaImage);
                    
                    updateMessage("Upload completed successfully");
                    return uploadedImages;
                } catch (Exception e) {
                    updateMessage("Upload failed: " + e.getMessage());
                    e.printStackTrace();
                    throw e;
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
        
        return images;
    }

    // Helper method to convert month name to number
    private int getMonthNumber(String month) {
        return months.indexOf(month) + 1; // +1 because months are 1-based
    }
}

