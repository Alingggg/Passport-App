package com.example.ayokona;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


public class User_ApplicationController {

    @FXML
    private AnchorPane bg1;

    @FXML
    private ImageView pic1;

    @FXML
    private AnchorPane whiteBg;

    @FXML
    private Label applicationLbl;

    @FXML
    private Label userLbl;

    @FXML
    void profileBtn(ActionEvent event) {
        System.out.println("thank you");
    }

    @FXML
    void applicationBtn(ActionEvent event) {
        System.out.println("thank you");
    }

    @FXML
    void logOutBtn(ActionEvent event) {
        System.out.println("thank you");
    }

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane scrollContent;

    @FXML
    private Label lnLbl;

    @FXML
    private Label fnLbl;

    @FXML
    private Label mnLbl;

    @FXML
    private TextField lnTxtF;

    @FXML
    private TextField fnTxtF;

    @FXML
    private TextField mnTxtF;

    @FXML
    private Label bDateLbl;

    @FXML
    private ComboBox<Integer> dayCombo;

    @FXML
    private ComboBox<String> monthCombo;

    @FXML
    private ComboBox<Integer> yearCombo;

    private final ObservableList<String> months = FXCollections.observableArrayList(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    );

    @FXML
    public void initialize() {

        // Scroll Pane
        if (scrollPane != null) {
            scrollPane.setFitToWidth(true);
            scrollPane.setPannable(true);
        }

        if (scrollContent != null) {
            for (int i = 0; i < 20; i++) {
                Label label = new Label("Label " + (i + 1));
                label.setLayoutX(10);
                label.setLayoutY(30 * i); // space between labels
                scrollContent.getChildren().add(label);
            }

            scrollContent.setPrefHeight(30 * 20 + 10); // enough height for 20 items
        } else {
            System.err.println("scrollContent is null. Did you assign fx:id correctly?");
        }

        //Birth Date ComboBox
        monthCombo.setItems(months);
        yearCombo.setItems(FXCollections.observableArrayList(generateYears(1925, 2025)));

        monthCombo.setValue("January");
        yearCombo.setValue(2024);

        updateDays();

        monthCombo.setOnAction(e -> updateDays());
        yearCombo.setOnAction(e -> updateDays());

        // Civil Status RadioButtons
        singleRadio.setSelected(true);


        // Gender ComboBox
        if (genderCombo != null) {
            genderCombo.setItems(FXCollections.observableArrayList("Male", "Female"));
            genderCombo.setValue("Male"); // Set default value if desired
        } else {
            System.err.println("genderCombo is null. Check fx:id in your FXML.");
        }

        // Issue Date ComboBox
        initIssueDateComboBoxes();
        
    }

    private void updateDays() {
        String selectedMonth = monthCombo.getValue();
        Integer selectedYear = yearCombo.getValue();

        if (selectedMonth == null || selectedYear == null) return;

        int daysInMonth = switch (selectedMonth) {
            case "April", "June", "September", "November" -> 30;
            case "February" -> isLeapYear(selectedYear) ? 29 : 28;
            default -> 31;
        };

        ObservableList<Integer> days = FXCollections.observableArrayList();
        for (int i = 1; i <= daysInMonth; i++) {
            days.add(i);
        }

        Integer previousDay = dayCombo.getValue();
        dayCombo.setItems(days);

        if (previousDay != null && previousDay <= daysInMonth) {
            dayCombo.setValue(previousDay);
        } else {
            dayCombo.setValue(daysInMonth);
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

    private void initIssueDateComboBoxes() {
        if (issueMonthsCombo != null && issueYearCombo != null && issueDaysCombo != null) {
            // Set months
            issueMonthsCombo.setItems(FXCollections.observableArrayList(
                    "January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December"
            ));
            issueMonthsCombo.setValue("January");

            ObservableList<Integer> years = FXCollections.observableArrayList();
            for (int y = 1925; y <= 2025; y++) {
                years.add(y);
            }
            issueYearCombo.setItems(years);
            issueYearCombo.setValue(2024);

            updateIssueDays();

            issueMonthsCombo.setOnAction(e -> updateIssueDays());
            issueYearCombo.setOnAction(e -> updateIssueDays());
        }
    }

    private void updateIssueDays() {
        String selectedMonth = issueMonthsCombo.getValue();
        Integer selectedYear = issueYearCombo.getValue();

        if (selectedMonth == null || selectedYear == null) return;

        int daysInMonth = switch (selectedMonth) {
            case "April", "June", "September", "November" -> 30;
            case "February" -> isLeapYear(selectedYear) ? 29 : 28;
            default -> 31;
        };

        ObservableList<Integer> days = FXCollections.observableArrayList();
        for (int i = 1; i <= daysInMonth; i++) {
            days.add(i);
        }

        Integer previousDay = issueDaysCombo.getValue();
        issueDaysCombo.setItems(days);

        if (previousDay != null && previousDay <= daysInMonth) {
            issueDaysCombo.setValue(previousDay);
        } else {
            issueDaysCombo.setValue(daysInMonth);
        }
    }


    @FXML
    private Label pBirthLbl;

    @FXML
    private TextField pBirthTxtF;

    @FXML
    private Label cmpAddressLbl;

    @FXML
    private TextField cmpAddressTxtF;

    @FXML
    private Label civilLbl;

    @FXML
    private RadioButton singleRadio;

    @FXML
    private RadioButton marriedRadio;

    @FXML
    private RadioButton widowedRadio;

    @FXML
    private RadioButton separatedRadio;

    @FXML
    private RadioButton annulledRadio;

    @FXML
    private Label genderLbl;

    @FXML
    private ComboBox<String> genderCombo;

    @FXML
    private Label mobileNumLbl;

    @FXML
    private TextField mobileNum1TxtF;

    @FXML
    private TextField mobileNum2TxtF;

    @FXML
    private Label telNumLbl;

    @FXML
    private TextField telNum1TxtF;

    @FXML
    private TextField telNum2TxtF;

    @FXML
    private Label emailAddLbl;

    @FXML
    private TextField emailAddTxtF;

    @FXML
    private Label occupationLbl;

    @FXML
    private TextField occupationTxtF;

    @FXML
    private Label workTelLbl;

    @FXML
    private Label workAddLbl;

    @FXML
    private TextField workTelTxtF;

    @FXML
    private TextField workAddTxtF;

    @FXML
    private Label whNameLbl;

    @FXML
    private Label fatherLbl;

    @FXML
    private Label motherLbl;

    @FXML
    private TextField whNameTxtF;

    @FXML
    private TextField fatherTxtF;

    @FXML
    private TextField motherTxtF;

    @FXML
    private Label citizen1Lbl;

    @FXML
    private Label citizen2Lbl;

    @FXML
    private Label citizen3Lbl;

    @FXML
    private TextField citizen1TxtF;

    @FXML
    private TextField citizen2TxtF;

    @FXML
    private TextField citizen3TxtF;

    @FXML
    private Label acquiredByLbl;

    @FXML
    private RadioButton birthRadio;

    @FXML
    private RadioButton electionRadio;

    @FXML
    private RadioButton marriageRadio;

    @FXML
    private RadioButton naturalRadio;

    @FXML
    private RadioButton ra9225Radio;

    @FXML
    private RadioButton othersRadio;

    @FXML
    private TextField othersTxtF;

    @FXML
    private Label foreignPassLbl;

    @FXML
    private RadioButton yesRadio;

    @FXML
    private RadioButton noRadio;

    @FXML
    private Label countryLbl;

    @FXML
    private TextField countryTxtF;

    @FXML
    private Label passNoLbl;

    @FXML
    private TextField passNoTxtF;

    @FXML
    private Label philPassLbl;

    @FXML
    private RadioButton yes1Radio;

    @FXML
    private RadioButton no2Radio;

    @FXML
    private Label passNo1Lbl;

    @FXML
    private TextField passNo2TxtF;

    @FXML
    private Label issueDateLbl;

    @FXML
    private ComboBox<String> issueMonthsCombo;

    @FXML
    private ComboBox<Integer> issueDaysCombo;

    @FXML
    private ComboBox<Integer> issueYearCombo;

    @FXML
    private Label placeIssueLbl;

    @FXML
    private TextField placeIssueTxtF;

    @FXML
    private Label minor18Lbl;

    @FXML
    private RadioButton yesMinorRadio;

    @FXML
    private RadioButton noMinorRadio;

    @FXML
    private Label travCompLbl;

    @FXML
    private TextField travCompTxtF;

    @FXML
    private Label compRSLbl;

    @FXML
    private TextField compRSTxtF;

    @FXML
    private Label contactNoLbl;

    @FXML
    private TextField contactNoTxtF;

    @FXML
    private Label forVerificationLbl;

    @FXML
    private ImageView pic2;

    @FXML
    private ImageView pic3;

    @FXML
    private Label uploadVAlidIDLbl;

    @FXML
    void uploadValidIDBtn(ActionEvent event) {
        System.out.println("thank you");
    }

    @FXML
    private Label uploadPSALbl;

    @FXML
    void uploadPSABtn(ActionEvent event) {
        System.out.println("thank you");
    }

    @FXML
    void applyBtn(ActionEvent event) {
        System.out.println("thank you");
    }

}
