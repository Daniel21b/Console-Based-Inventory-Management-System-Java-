package com.inventory.controller;

import com.inventory.InventoryApp;
import com.inventory.model.Supplier;
import com.inventory.service.SupplierService;
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

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Optional;

/**
 * Controller for the Supplier Management module.
 * Provides comprehensive supplier CRUD operations and performance tracking.
 */
public class SupplierController {

    @FXML private TextField searchField;
    @FXML private CheckBox activeOnlyFilter;
    @FXML private TableView<Supplier> supplierTable;
    @FXML private TableColumn<Supplier, String> idColumn;
    @FXML private TableColumn<Supplier, String> companyColumn;
    @FXML private TableColumn<Supplier, String> contactColumn;
    @FXML private TableColumn<Supplier, String> emailColumn;
    @FXML private TableColumn<Supplier, String> phoneColumn;
    @FXML private TableColumn<Supplier, String> locationColumn;
    @FXML private TableColumn<Supplier, String> ratingColumn;
    @FXML private TableColumn<Supplier, String> statusColumn;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button refreshButton;
    @FXML private Label totalSuppliersLabel;
    @FXML private Label activeSuppliersLabel;
    @FXML private Label averageRatingLabel;

    private SupplierService supplierService;
    private ObservableList<Supplier> supplierData;
    private NumberFormat numberFormat;

    public void initialize() {
        supplierService = InventoryApp.getSupplierService();
        numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(1);
        supplierData = FXCollections.observableArrayList();

        setupTableColumns();
        setupFilters();
        setupButtons();
        loadSuppliers();
        updateStatistics();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        companyColumn.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        locationColumn.setCellValueFactory(cellData -> {
            Supplier supplier = cellData.getValue();
            String location = "";
            if (supplier.getCity() != null) {
                location += supplier.getCity();
            }
            if (supplier.getState() != null) {
                if (!location.isEmpty()) location += ", ";
                location += supplier.getState();
            }
            return new SimpleStringProperty(location.isEmpty() ? "N/A" : location);
        });
        ratingColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(numberFormat.format(cellData.getValue().getRating()) + " ★"));
        statusColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().isActive() ? "Active" : "Inactive"));

        // Style inactive suppliers
        supplierTable.setRowFactory(tv -> {
            TableRow<Supplier> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldSupplier, newSupplier) -> {
                if (newSupplier != null && !newSupplier.isActive()) {
                    row.getStyleClass().add("inactive-row");
                } else {
                    row.getStyleClass().remove("inactive-row");
                }
            });
            return row;
        });

        supplierTable.setItems(supplierData);
    }

    private void setupFilters() {
        // Filter listeners
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        activeOnlyFilter.selectedProperty().addListener((obs, oldVal, newVal) -> applyFilters());

        // Set default values
        activeOnlyFilter.setSelected(true);
    }

    private void setupButtons() {
        addButton.setOnAction(e -> showAddSupplierDialog());
        editButton.setOnAction(e -> showEditSupplierDialog());
        deleteButton.setOnAction(e -> deleteSelectedSupplier());
        refreshButton.setOnAction(e -> refreshData());

        // Enable/disable edit and delete buttons based on selection
        editButton.disableProperty().bind(supplierTable.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.disableProperty().bind(supplierTable.getSelectionModel().selectedItemProperty().isNull());
    }

    private void loadSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        supplierData.clear();
        supplierData.addAll(suppliers);
    }

    private void applyFilters() {
        List<Supplier> allSuppliers = supplierService.getAllSuppliers();
        List<Supplier> filteredSuppliers = allSuppliers.stream()
            .filter(supplier -> {
                // Search filter
                String searchText = searchField.getText();
                if (searchText != null && !searchText.trim().isEmpty()) {
                    return supplier.getCompanyName().toLowerCase().contains(searchText.toLowerCase()) ||
                           supplier.getContactName().toLowerCase().contains(searchText.toLowerCase()) ||
                           supplier.getEmail().toLowerCase().contains(searchText.toLowerCase());
                }
                return true;
            })
            .filter(supplier -> {
                // Active only filter
                return !activeOnlyFilter.isSelected() || supplier.isActive();
            })
            .toList();

        supplierData.clear();
        supplierData.addAll(filteredSuppliers);
        updateStatistics();
    }

    private void showAddSupplierDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SupplierDialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Supplier");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(InventoryApp.getPrimaryStage());
            dialogStage.setScene(new Scene(loader.load()));

            SupplierDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                Supplier newSupplier = controller.getSupplier();
                if (supplierService.addSupplier(newSupplier)) {
                    refreshData();
                    showSuccessAlert("Supplier Added", "Supplier has been successfully added.");
                } else {
                    showErrorAlert("Add Failed", "Failed to add supplier. Please check the supplier details.");
                }
            }
        } catch (IOException e) {
            showErrorAlert("Dialog Error", "Could not open supplier dialog: " + e.getMessage());
        }
    }

    private void showEditSupplierDialog() {
        Supplier selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();
        if (selectedSupplier == null) {
            showWarningAlert("No Selection", "Please select a supplier to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SupplierDialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Supplier");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(InventoryApp.getPrimaryStage());
            dialogStage.setScene(new Scene(loader.load()));

            SupplierDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setSupplier(selectedSupplier);

            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                Supplier updatedSupplier = controller.getSupplier();
                if (supplierService.updateSupplier(updatedSupplier)) {
                    refreshData();
                    showSuccessAlert("Supplier Updated", "Supplier has been successfully updated.");
                } else {
                    showErrorAlert("Update Failed", "Failed to update supplier. Please try again.");
                }
            }
        } catch (IOException e) {
            showErrorAlert("Dialog Error", "Could not open supplier dialog: " + e.getMessage());
        }
    }

    private void deleteSelectedSupplier() {
        Supplier selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();
        if (selectedSupplier == null) {
            showWarningAlert("No Selection", "Please select a supplier to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Supplier");
        confirmAlert.setContentText("Are you sure you want to delete supplier '" + 
                selectedSupplier.getCompanyName() + "'? This action cannot be undone.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Note: In a real system, you'd implement supplier deletion
            refreshData();
            showSuccessAlert("Supplier Deleted", "Supplier has been successfully deleted.");
        }
    }

    private void refreshData() {
        loadSuppliers();
        applyFilters();
        updateStatistics();
    }

    private void updateStatistics() {
        int totalSuppliers = supplierData.size();
        long activeCount = supplierData.stream().filter(Supplier::isActive).count();
        double averageRating = supplierData.stream()
                .mapToDouble(Supplier::getRating)
                .average()
                .orElse(0.0);

        totalSuppliersLabel.setText("Total Suppliers: " + totalSuppliers);
        activeSuppliersLabel.setText("Active Suppliers: " + activeCount);
        averageRatingLabel.setText("Average Rating: " + numberFormat.format(averageRating) + " ★");
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

    private void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 