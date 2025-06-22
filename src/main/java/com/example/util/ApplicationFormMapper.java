package com.example.util;

import com.example.model.*;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ApplicationFormMapper {

    private final ObservableList<String> months;

    public ApplicationFormMapper(ObservableList<String> months) {
        this.months = months;
    }

    public UserInfo createUserInfo(TextField lblLastName, TextField lblFirstName, TextField lblMiddleName, TextField lblPlaceOfBirth, ComboBox<Integer> lblBirthYear, ComboBox<String> lblBirthMonth, ComboBox<Integer> lblBirthDay, ComboBox<String> lblGender, ToggleGroup civilStatusGroup, TextField lblCompleteAddress, ToggleGroup citizenshipGroup, TextField txtCitizenOthers) {
        UserInfo info = new UserInfo();
        info.setLastName(lblLastName.getText());
        info.setFirstName(lblFirstName.getText());
        info.setMiddleName(lblMiddleName.getText());
        info.setBirthPlace(lblPlaceOfBirth.getText());

        LocalDate birthDate = LocalDate.of(lblBirthYear.getValue(), getMonthNumber(lblBirthMonth.getValue()), lblBirthDay.getValue());
        info.setBirthDate(birthDate);
        info.setGender(lblGender.getValue());

        RadioButton selectedCivilStatus = (RadioButton) civilStatusGroup.getSelectedToggle();
        if (selectedCivilStatus != null) {
            info.setCivilStatus(selectedCivilStatus.getText());
        }

        info.setCurrentAddress(lblCompleteAddress.getText());

        RadioButton selectedCitizenship = (RadioButton) citizenshipGroup.getSelectedToggle();
        if (selectedCitizenship != null) {
            String citizenshipType = selectedCitizenship.getText();
            if (citizenshipType.equals("Others:") && txtCitizenOthers.getText() != null && !txtCitizenOthers.getText().isEmpty()) {
                citizenshipType = txtCitizenOthers.getText();
            }
            info.setAcquiredCitizenship(citizenshipType);
        }
        return info;
    }

    public List<UserContact> createUserContacts(TextField lblMobileNumber1, TextField lblTelephoneNumber1, TextField lblEmailAddress1, TextField lblMobileNumber2, TextField lblTelephoneNumber2, TextField lblEmailAddress2) {
        List<UserContact> contacts = new ArrayList<>();
        UserContact contact1 = new UserContact();
        boolean contact1HasData = false;
        if (lblMobileNumber1.getText() != null && !lblMobileNumber1.getText().trim().isEmpty()) {
            contact1.setMobileNumber(lblMobileNumber1.getText());
            contact1HasData = true;
        }
        if (lblTelephoneNumber1.getText() != null && !lblTelephoneNumber1.getText().trim().isEmpty()) {
            contact1.setTelephoneNumber(lblTelephoneNumber1.getText());
            contact1HasData = true;
        }
        if (lblEmailAddress1.getText() != null && !lblEmailAddress1.getText().trim().isEmpty()) {
            contact1.setEmailAddress(lblEmailAddress1.getText());
            contact1HasData = true;
        }
        if (contact1HasData) {
            contacts.add(contact1);
        }

        UserContact contact2 = new UserContact();
        boolean contact2HasData = false;
        if (lblMobileNumber2.getText() != null && !lblMobileNumber2.getText().trim().isEmpty()) {
            contact2.setMobileNumber(lblMobileNumber2.getText());
            contact2HasData = true;
        }
        if (lblTelephoneNumber2.getText() != null && !lblTelephoneNumber2.getText().trim().isEmpty()) {
            contact2.setTelephoneNumber(lblTelephoneNumber2.getText());
            contact2HasData = true;
        }
        if (lblEmailAddress2.getText() != null && !lblEmailAddress2.getText().trim().isEmpty()) {
            contact2.setEmailAddress(lblEmailAddress2.getText());
            contact2HasData = true;
        }
        if (contact2HasData) {
            contacts.add(contact2);
        }
        return contacts;
    }

    public List<UserWork> createUserWorks(TextField lblOccupation1, TextField lblWorkAddress1, TextField lblWorkTelephone1, TextField lblOccupation2, TextField lblWorkAddress2, TextField lblWorkTelephone2) {
        List<UserWork> works = new ArrayList<>();
        UserWork work1 = new UserWork();
        boolean work1HasData = false;
        if (lblOccupation1.getText() != null && !lblOccupation1.getText().trim().isEmpty()) {
            work1.setOccupation(lblOccupation1.getText());
            work1HasData = true;
        }
        if (lblWorkAddress1.getText() != null && !lblWorkAddress1.getText().trim().isEmpty()) {
            work1.setWorkAddress(lblWorkAddress1.getText());
        }
        if (lblWorkTelephone1.getText() != null && !lblWorkTelephone1.getText().trim().isEmpty()) {
            work1.setWorkTelephoneNumber(lblWorkTelephone1.getText());
        }
        if (work1HasData) {
            works.add(work1);
        }

        UserWork work2 = new UserWork();
        boolean work2HasData = false;
        if (lblOccupation2.getText() != null && !lblOccupation2.getText().trim().isEmpty()) {
            work2.setOccupation(lblOccupation2.getText());
            work2HasData = true;
        }
        if (lblWorkAddress2.getText() != null && !lblWorkAddress2.getText().trim().isEmpty()) {
            work2.setWorkAddress(lblWorkAddress2.getText());
        }
        if (lblWorkTelephone2.getText() != null && !lblWorkTelephone2.getText().trim().isEmpty()) {
            work2.setWorkTelephoneNumber(lblWorkTelephone2.getText());
        }
        if (work2HasData) {
            works.add(work2);
        }
        return works;
    }

    public UserForeignPassport createForeignPassport(ToggleGroup foreignPassportGroup, TextField lblCountry, TextField lblForeignPassportNo) {
        UserForeignPassport foreignPassport = new UserForeignPassport();
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

    public UserSpouse createSpouse(TextField lblSpouseName, TextField lblSpouseCitizenship) {
        UserSpouse spouse = new UserSpouse();
        if (lblSpouseName.getText() != null && !lblSpouseName.getText().isEmpty()) {
            spouse.setSpouseFullName(lblSpouseName.getText());
        }
        if (lblSpouseCitizenship.getText() != null && !lblSpouseCitizenship.getText().isEmpty()) {
            spouse.setSpouseCitizenship(lblSpouseCitizenship.getText());
        }
        return spouse;
    }

    public UserParents createParents(TextField lblFatherName, TextField lblFatherCitizenship, TextField lblMotherName, TextField lblMotherCitizenship) {
        UserParents parents = new UserParents();
        if (lblFatherName.getText() != null && !lblFatherName.getText().isEmpty()) {
            parents.setFatherFullName(lblFatherName.getText());
        }
        if (lblFatherCitizenship.getText() != null && !lblFatherCitizenship.getText().isEmpty()) {
            parents.setFatherCitizenship(lblFatherCitizenship.getText());
        }
        if (lblMotherName.getText() != null && !lblMotherName.getText().isEmpty()) {
            parents.setMotherMaidenName(lblMotherName.getText());
        }
        if (lblMotherCitizenship.getText() != null && !lblMotherCitizenship.getText().isEmpty()) {
            parents.setMotherCitizenship(lblMotherCitizenship.getText());
        }
        return parents;
    }

    public UserPhilippinePassport createPhilippinePassport(ToggleGroup philippinePassportGroup, ComboBox<Integer> lblIssueDay, ComboBox<String> lblIssueMonth, ComboBox<Integer> lblIssueYear, TextField lblPlaceOfIssue, TextField lblPhilippinePassportNo) {
        UserPhilippinePassport philippinePassport = new UserPhilippinePassport();
        RadioButton selectedOption = (RadioButton) philippinePassportGroup.getSelectedToggle();
        if (selectedOption != null) {
            boolean hasPassport = selectedOption.getText().equals("YES");
            philippinePassport.setHasPhilippinePassport(hasPassport);
            if (hasPassport) {
                if (lblIssueDay.getValue() != null && lblIssueMonth.getValue() != null && lblIssueYear.getValue() != null) {
                    LocalDate issueDate = LocalDate.of(lblIssueYear.getValue(), getMonthNumber(lblIssueMonth.getValue()), lblIssueDay.getValue());
                    philippinePassport.setIssueDate(issueDate);
                }
                if (lblPlaceOfIssue.getText() != null && !lblPlaceOfIssue.getText().isEmpty()) {
                    philippinePassport.setIssuePlace(lblPlaceOfIssue.getText());
                }
                if (lblPhilippinePassportNo.getText() != null && !lblPhilippinePassportNo.getText().isEmpty()) {
                    philippinePassport.setOldPhilippinePassportNumber(lblPhilippinePassportNo.getText());
                }
            }
        }
        return philippinePassport;
    }

    public UserMinorInfo createMinorInfo(ToggleGroup minorGroup, TextField lblTravelingCompanion, TextField lblCompanionRelationship, TextField lblContactNumber) {
        UserMinorInfo minorInfo = new UserMinorInfo();
        RadioButton selectedOption = (RadioButton) minorGroup.getSelectedToggle();
        if (selectedOption != null) {
            boolean isMinor = selectedOption.getText().equals("YES");
            minorInfo.setIsMinor(isMinor);
            if (isMinor) {
                if (lblTravelingCompanion.getText() != null && !lblTravelingCompanion.getText().isEmpty()) {
                    minorInfo.setCompanionFullName(lblTravelingCompanion.getText());
                }
                if (lblCompanionRelationship.getText() != null && !lblCompanionRelationship.getText().isEmpty()) {
                    minorInfo.setCompanionRelationship(lblCompanionRelationship.getText());
                }
                if (lblContactNumber.getText() != null && !lblContactNumber.getText().isEmpty()) {
                    minorInfo.setCompanionContactNumber(lblContactNumber.getText());
                }
            }
        }
        return minorInfo;
    }

    private int getMonthNumber(String month) {
        return months.indexOf(month) + 1;
    }
}
