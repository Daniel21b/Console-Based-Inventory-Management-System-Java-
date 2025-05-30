package com.inventory.controller;

import com.inventory.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Controller for Order add/edit dialog.
 */
public class OrderDialogController {

    @FXML private TextField customerIdField;
    @FXML private TextField customerNameField;
    @FXML private ComboBox<OrderStatus> statusComboBox;
    @FXML private ComboBox<OrderPriority> priorityComboBox;
    @FXML private TextArea notesArea;

    private Stage dialogStage;
    private Order order;
    private boolean okClicked = false;

    public void initialize() {
        statusComboBox.getItems().addAll(OrderStatus.values());
        priorityComboBox.getItems().addAll(OrderPriority.values());
        
        // Set default values
        statusComboBox.setValue(OrderStatus.PENDING);
        priorityComboBox.setValue(OrderPriority.NORMAL);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setOrder(Order order) {
        this.order = order;

        if (order != null) {
            customerIdField.setText(order.getCustomerId());
            customerNameField.setText(order.getCustomerName());
            statusComboBox.setValue(order.getStatus());
            priorityComboBox.setValue(order.getPriority());
            notesArea.setText(order.getNotes());
        }
    }

    public Order getOrder() {
        return order;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            if (order == null) {
                order = new Order();
                String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                order.setOrderId(orderId);
                order.setOrderDate(LocalDateTime.now());
            }

            order.setCustomerId(customerIdField.getText());
            order.setCustomerName(customerNameField.getText());
            order.setStatus(statusComboBox.getValue());
            order.setPriority(priorityComboBox.getValue());
            order.setNotes(notesArea.getText());

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

        if (customerNameField.getText() == null || customerNameField.getText().trim().isEmpty()) {
            errorMessage += "Customer name is required!\n";
        }

        if (statusComboBox.getValue() == null) {
            errorMessage += "Please select a status!\n";
        }

        if (priorityComboBox.getValue() == null) {
            errorMessage += "Please select a priority!\n";
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