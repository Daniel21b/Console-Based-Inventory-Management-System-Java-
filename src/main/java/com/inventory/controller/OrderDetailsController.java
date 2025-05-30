package com.inventory.controller;

import com.inventory.model.Order;
import com.inventory.model.OrderItem;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;

/**
 * Controller for Order details display.
 */
public class OrderDetailsController {

    @FXML private Label orderIdLabel;
    @FXML private Label customerLabel;
    @FXML private Label dateLabel;
    @FXML private Label statusLabel;
    @FXML private Label priorityLabel;
    @FXML private Label totalLabel;
    @FXML private TextArea notesArea;
    @FXML private TableView<OrderItem> itemsTable;
    @FXML private TableColumn<OrderItem, String> productColumn;
    @FXML private TableColumn<OrderItem, Integer> quantityColumn;
    @FXML private TableColumn<OrderItem, String> priceColumn;
    @FXML private TableColumn<OrderItem, String> subtotalColumn;

    private NumberFormat currencyFormat;
    private DateTimeFormatter dateFormatter;

    public void initialize() {
        currencyFormat = NumberFormat.getCurrencyInstance();
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        // Make notes area read-only
        notesArea.setEditable(false);
    }

    public void setOrder(Order order) {
        if (order != null) {
            orderIdLabel.setText(order.getOrderId());
            customerLabel.setText(order.getCustomerName());
            dateLabel.setText(order.getOrderDate().format(dateFormatter));
            statusLabel.setText(order.getStatus().getDisplayName());
            priorityLabel.setText(order.getPriority().getDisplayName());
            totalLabel.setText(currencyFormat.format(order.getTotalAmount()));
            notesArea.setText(order.getNotes());

            // Load order items
            itemsTable.setItems(FXCollections.observableArrayList(order.getItems()));
        }
    }
} 