package com.example.controller;

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
        System.out.println("Profile button clicked");
    }

    @FXML
    void applicationBtn(ActionEvent event) {
        System.out.println("Application button clicked");
    }

    @FXML
    void logOutBtn(ActionEvent event) {
        System.out.println("Logout button clicked");
    }

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane scrollContent;

    @FXML
    private TextField lnTxtF;

    @FXML
    private TextField fnTxtF;

    @FXML
    private TextField mnTxtF;

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
        // Scroll Pane setup
        if (scrollPane != null) {
            scrollPane.setFitToWidth(true);
            scrollPane.setPannable(true);
        }

        // Birth Date ComboBox setup
        if (monthCombo != null) {
            monthCombo.setItems(months);
            monthCombo.setValue("January");
        }

        if (yearCombo != null) {
            yearCombo.setItems(generateYears(1925, 2025));
            yearCombo.setValue(2024);
        }

        updateDays();

        if (monthCombo != null) monthCombo.setOnAction(e -> updateDays());
        if (yearCombo != null) yearCombo.setOnAction(e -> updateDays());
    }

    @FXML
    void uploadValidIDBtn(ActionEvent event) {
        System.out.println("Upload Valid ID clicked");
    }

    @FXML
    void uploadPSABtn(ActionEvent event) {
        System.out.println("Upload PSA Birth Certificate clicked");
    }

    @FXML
    void applyBtn(ActionEvent event) {
        System.out.println("Apply button clicked");
    }

    private void updateDays() {
        if (dayCombo != null && monthCombo != null && yearCombo != null) {
            String selectedMonth = monthCombo.getValue();
            Integer selectedYear = yearCombo.getValue();

            if (selectedMonth != null && selectedYear != null) {
                int daysInMonth = getDaysInMonth(selectedMonth, selectedYear);
                ObservableList<Integer> days = FXCollections.observableArrayList();
                for (int i = 1; i <= daysInMonth; i++) {
                    days.add(i);
                }
                dayCombo.setItems(days);
                if (dayCombo.getValue() == null || dayCombo.getValue() > daysInMonth) {
                    dayCombo.setValue(1);
                }
            }
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

