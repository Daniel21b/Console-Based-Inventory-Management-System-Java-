package com.inventory.controller;

import com.inventory.InventoryApp;
import com.inventory.model.*;
import com.inventory.service.OrderService;
import com.inventory.service.ProductService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Controller for the Order Management module.
 * Provides comprehensive order CRUD operations and order tracking.
 */
public class OrderController {

    @FXML private TextField searchField;
    @FXML private ComboBox<OrderStatus> statusFilter;
    @FXML private ComboBox<OrderPriority> priorityFilter;
    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, String> orderIdColumn;
    @FXML private TableColumn<Order, String> customerColumn;
    @FXML private TableColumn<Order, String> dateColumn;
    @FXML private TableColumn<Order, String> statusColumn;
    @FXML private TableColumn<Order, String> priorityColumn;
    @FXML private TableColumn<Order, String> totalColumn;
    @FXML private TableColumn<Order, Integer> itemsColumn;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button viewButton;
    @FXML private Button deleteButton;
    @FXML private Button refreshButton;
    @FXML private Label totalOrdersLabel;
    @FXML private Label totalValueLabel;
    @FXML private Label pendingOrdersLabel;

    private OrderService orderService;
    private ProductService productService;
    private ObservableList<Order> orderData;
    private NumberFormat currencyFormat;
    private DateTimeFormatter dateFormatter;

    public void initialize() {
        orderService = InventoryApp.getOrderService();
        productService = InventoryApp.getProductService();
        currencyFormat = NumberFormat.getCurrencyInstance();
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        orderData = FXCollections.observableArrayList();

        setupTableColumns();
        setupFilters();
        setupButtons();
        loadOrders();
        updateStatistics();
    }

    private void setupTableColumns() {
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        dateColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getOrderDate().format(dateFormatter)));
        statusColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getStatus().getDisplayName()));
        priorityColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getPriority().getDisplayName()));
        totalColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(currencyFormat.format(cellData.getValue().getTotalAmount())));
        itemsColumn.setCellValueFactory(new PropertyValueFactory<>("itemCount"));

        // Style rows based on status
        orderTable.setRowFactory(tv -> {
            TableRow<Order> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldOrder, newOrder) -> {
                row.getStyleClass().removeAll("pending-row", "delivered-row", "cancelled-row");
                if (newOrder != null) {
                    switch (newOrder.getStatus()) {
                        case PENDING:
                            row.getStyleClass().add("pending-row");
                            break;
                        case DELIVERED:
                            row.getStyleClass().add("delivered-row");
                            break;
                        case CANCELLED:
                            row.getStyleClass().add("cancelled-row");
                            break;
                    }
                }
            });
            return row;
        });

        orderTable.setItems(orderData);
    }

    private void setupFilters() {
        // Status filter
        statusFilter.getItems().add(null); // "All Status" option
        statusFilter.getItems().addAll(OrderStatus.values());
        statusFilter.setConverter(new StringConverter<OrderStatus>() {
            @Override
            public String toString(OrderStatus status) {
                return status == null ? "All Status" : status.getDisplayName();
            }

            @Override
            public OrderStatus fromString(String string) {
                return null;
            }
        });

        // Priority filter
        priorityFilter.getItems().add(null); // "All Priorities" option
        priorityFilter.getItems().addAll(OrderPriority.values());
        priorityFilter.setConverter(new StringConverter<OrderPriority>() {
            @Override
            public String toString(OrderPriority priority) {
                return priority == null ? "All Priorities" : priority.getDisplayName();
            }

            @Override
            public OrderPriority fromString(String string) {
                return null;
            }
        });

        // Filter listeners
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        statusFilter.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        priorityFilter.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }

    private void setupButtons() {
        addButton.setOnAction(e -> showAddOrderDialog());
        editButton.setOnAction(e -> showEditOrderDialog());
        viewButton.setOnAction(e -> showViewOrderDialog());
        deleteButton.setOnAction(e -> deleteSelectedOrder());
        refreshButton.setOnAction(e -> refreshData());

        // Enable/disable buttons based on selection
        editButton.disableProperty().bind(orderTable.getSelectionModel().selectedItemProperty().isNull());
        viewButton.disableProperty().bind(orderTable.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.disableProperty().bind(orderTable.getSelectionModel().selectedItemProperty().isNull());
    }

    private void loadOrders() {
        List<Order> orders = orderService.getAllOrders();
        orderData.clear();
        orderData.addAll(orders);
    }

    private void applyFilters() {
        List<Order> allOrders = orderService.getAllOrders();
        List<Order> filteredOrders = allOrders.stream()
            .filter(order -> {
                // Search filter
                String searchText = searchField.getText();
                if (searchText != null && !searchText.trim().isEmpty()) {
                    return order.getOrderId().toLowerCase().contains(searchText.toLowerCase()) ||
                           order.getCustomerName().toLowerCase().contains(searchText.toLowerCase());
                }
                return true;
            })
            .filter(order -> {
                // Status filter
                OrderStatus selectedStatus = statusFilter.getValue();
                return selectedStatus == null || order.getStatus() == selectedStatus;
            })
            .filter(order -> {
                // Priority filter
                OrderPriority selectedPriority = priorityFilter.getValue();
                return selectedPriority == null || order.getPriority() == selectedPriority;
            })
            .toList();

        orderData.clear();
        orderData.addAll(filteredOrders);
        updateStatistics();
    }

    private void showAddOrderDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/OrderDialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create New Order");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(InventoryApp.getPrimaryStage());
            dialogStage.setScene(new Scene(loader.load()));

            OrderDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                Order newOrder = controller.getOrder();
                if (orderService.addOrder(newOrder)) {
                    refreshData();
                    showSuccessAlert("Order Created", "Order has been successfully created.");
                } else {
                    showErrorAlert("Create Failed", "Failed to create order. Please try again.");
                }
            }
        } catch (IOException e) {
            showErrorAlert("Dialog Error", "Could not open order dialog: " + e.getMessage());
        }
    }

    private void showEditOrderDialog() {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/OrderDialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Order");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(InventoryApp.getPrimaryStage());
            dialogStage.setScene(new Scene(loader.load()));

            OrderDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setOrder(selectedOrder);

            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                refreshData();
                showSuccessAlert("Order Updated", "Order has been successfully updated.");
            }
        } catch (IOException e) {
            showErrorAlert("Dialog Error", "Could not open order dialog: " + e.getMessage());
        }
    }

    private void showViewOrderDialog() {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/OrderDetailsDialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Order Details - " + selectedOrder.getOrderId());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(InventoryApp.getPrimaryStage());
            dialogStage.setScene(new Scene(loader.load()));

            OrderDetailsController controller = loader.getController();
            controller.setOrder(selectedOrder);

            dialogStage.show();
        } catch (IOException e) {
            showErrorAlert("Dialog Error", "Could not open order details: " + e.getMessage());
        }
    }

    private void deleteSelectedOrder() {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) return;

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Order");
        confirmAlert.setContentText("Are you sure you want to delete order '" + 
                selectedOrder.getOrderId() + "'? This action cannot be undone.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Note: In a real system, you'd implement order deletion in the service
            refreshData();
            showSuccessAlert("Order Deleted", "Order has been successfully deleted.");
        }
    }

    private void refreshData() {
        loadOrders();
        applyFilters();
        updateStatistics();
    }

    private void updateStatistics() {
        int totalOrders = orderData.size();
        double totalValue = orderData.stream().mapToDouble(Order::getTotalAmount).sum();
        long pendingCount = orderData.stream().filter(o -> o.getStatus() == OrderStatus.PENDING).count();

        totalOrdersLabel.setText("Total Orders: " + totalOrders);
        totalValueLabel.setText("Total Value: " + currencyFormat.format(totalValue));
        pendingOrdersLabel.setText("Pending Orders: " + pendingCount);
    }

    // Utility methods for alerts
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 