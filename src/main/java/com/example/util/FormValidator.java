package com.example.util;

import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FormValidator {
    private final List<String> errors = new ArrayList<>();

    public List<String> getErrors() {
        return errors;
    }

    public void validateRequiredTextField(TextField field, String fieldName) {
        if (field.getText() == null || field.getText().trim().isEmpty()) {
            errors.add(fieldName + " is required.");
        }
    }

    public void validateNumericTextField(TextField field, String fieldName) {
        if (!field.isDisabled()) {
            validateRequiredTextField(field, fieldName);
            String trimmedText = field.getText().trim();
            if (!trimmedText.isEmpty() && !isNumeric(trimmedText) && !"N/A".equalsIgnoreCase(trimmedText)) {
                errors.add(fieldName + " must contain only numbers or 'N/A'.");
            }
        }
    }

    public void validateEmailField(TextField field, String fieldName) {
        if (!field.isDisabled()) {
            validateRequiredTextField(field, fieldName);
            String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
            if (!field.getText().trim().isEmpty() && !Pattern.matches(emailRegex, field.getText())) {
                errors.add(fieldName + " is not a valid email address.");
            }
        }
    }

    public void validateRequiredComboBox(ComboBox<?> field, String fieldName) {
        if (!field.isDisabled() && field.getValue() == null) {
            errors.add("A selection for " + fieldName + " is required.");
        }
    }

    public void validateRequiredToggleGroup(ToggleGroup group, String fieldName) {
        if (group.getSelectedToggle() == null) {
            errors.add("A selection for " + fieldName + " is required.");
        }
    }

    private boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        return str.matches("\\d+");
    }
}
