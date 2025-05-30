package com.inventory.controller;

import com.inventory.model.Supplier;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Controller for Supplier add/edit dialog.
 */
public class SupplierDialogController {

    @FXML private TextField companyNameField;
    @FXML private TextField contactNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private TextField cityField;
    @FXML private TextField stateField;
    @FXML private TextField postalCodeField;
    @FXML private TextField countryField;
    @FXML private TextField websiteField;
    @FXML private TextField paymentTermsField;
    @FXML private TextField leadTimeDaysField;
    @FXML private TextField minimumOrderField;
    @FXML private TextField ratingField;
    @FXML private TextArea notesArea;
    @FXML private CheckBox activeCheckBox;

    private Stage dialogStage;
    private Supplier supplier;
    private boolean okClicked = false;

    public void initialize() {
        setupValidation();
    }

    private void setupValidation() {
        // Numeric field validation
        leadTimeDaysField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                leadTimeDaysField.setText(oldVal);
            }
        });

        minimumOrderField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                minimumOrderField.setText(oldVal);
            }
        });

        ratingField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                ratingField.setText(oldVal);
            }
        });
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;

        if (supplier != null) {
            companyNameField.setText(supplier.getCompanyName());
            contactNameField.setText(supplier.getContactName());
            emailField.setText(supplier.getEmail());
            phoneField.setText(supplier.getPhone());
            addressField.setText(supplier.getAddress());
            cityField.setText(supplier.getCity());
            stateField.setText(supplier.getState());
            postalCodeField.setText(supplier.getPostalCode());
            countryField.setText(supplier.getCountry());
            websiteField.setText(supplier.getWebsite());
            paymentTermsField.setText(supplier.getPreferredPaymentTerms());
            leadTimeDaysField.setText(String.valueOf(supplier.getLeadTimeDays()));
            minimumOrderField.setText(String.valueOf(supplier.getMinimumOrderAmount()));
            ratingField.setText(String.valueOf(supplier.getRating()));
            notesArea.setText(supplier.getNotes());
            activeCheckBox.setSelected(supplier.isActive());
        } else {
            // Set default values for new supplier
            activeCheckBox.setSelected(true);
            leadTimeDaysField.setText("7");
            minimumOrderField.setText("0.00");
            ratingField.setText("5.0");
            countryField.setText("USA");
            paymentTermsField.setText("Net 30");
        }
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            if (supplier == null) {
                supplier = new Supplier();
                String supplierId = "SUP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                supplier.setSupplierId(supplierId);
                supplier.setDateAdded(LocalDateTime.now());
            }

            // Update supplier with form values
            supplier.setCompanyName(companyNameField.getText());
            supplier.setContactName(contactNameField.getText());
            supplier.setEmail(emailField.getText());
            supplier.setPhone(phoneField.getText());
            supplier.setAddress(addressField.getText());
            supplier.setCity(cityField.getText());
            supplier.setState(stateField.getText());
            supplier.setPostalCode(postalCodeField.getText());
            supplier.setCountry(countryField.getText());
            supplier.setWebsite(websiteField.getText());
            supplier.setPreferredPaymentTerms(paymentTermsField.getText());
            supplier.setNotes(notesArea.getText());
            supplier.setActive(activeCheckBox.isSelected());

            // Set numeric fields
            try {
                supplier.setLeadTimeDays(Integer.parseInt(leadTimeDaysField.getText()));
            } catch (NumberFormatException e) {
                supplier.setLeadTimeDays(7);
            }

            try {
                supplier.setMinimumOrderAmount(Double.parseDouble(minimumOrderField.getText()));
            } catch (NumberFormatException e) {
                supplier.setMinimumOrderAmount(0.0);
            }

            try {
                supplier.setRating(Double.parseDouble(ratingField.getText()));
            } catch (NumberFormatException e) {
                supplier.setRating(5.0);
            }

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (companyNameField.getText() == null || companyNameField.getText().trim().isEmpty()) {
            errorMessage += "Company name is required!\n";
        }

        if (contactNameField.getText() == null || contactNameField.getText().trim().isEmpty()) {
            errorMessage += "Contact name is required!\n";
        }

        String email = emailField.getText();
        if (email != null && !email.trim().isEmpty() && !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errorMessage += "Invalid email format!\n";
        }

        try {
            double rating = Double.parseDouble(ratingField.getText());
            if (rating < 1.0 || rating > 5.0) {
                errorMessage += "Rating must be between 1.0 and 5.0!\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Invalid rating format!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Please correct the following errors:");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
} 