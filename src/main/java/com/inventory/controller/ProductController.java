package com.inventory.controller;

import com.inventory.InventoryApp;
import com.inventory.model.Product;
import com.inventory.model.ProductCategory;
import com.inventory.service.ProductService;
import com.inventory.util.SearchFilter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Optional;

/**
 * Controller for the Product Management module.
 * Provides comprehensive product CRUD operations with advanced search and filtering.
 */
public class ProductController {

    @FXML private TextField searchField;
    @FXML private ComboBox<ProductCategory> categoryFilter;
    @FXML private CheckBox lowStockFilter;
    @FXML private CheckBox activeOnlyFilter;
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> idColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> categoryColumn;
    @FXML private TableColumn<Product, String> priceColumn;
    @FXML private TableColumn<Product, Integer> quantityColumn;
    @FXML private TableColumn<Product, String> supplierColumn;
    @FXML private TableColumn<Product, String> statusColumn;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button refreshButton;
    @FXML private Label totalProductsLabel;
    @FXML private Label totalValueLabel;
    @FXML private Label lowStockLabel;

    private ProductService productService;
    private ObservableList<Product> productData;
    private NumberFormat currencyFormat;

    public void initialize() {
        productService = InventoryApp.getProductService();
        currencyFormat = NumberFormat.getCurrencyInstance();
        productData = FXCollections.observableArrayList();

        setupTableColumns();
        setupFilters();
        setupButtons();
        loadProducts();
        updateStatistics();

        // Auto-refresh every 30 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(30), e -> refreshData()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getCategory().getDisplayName()));
        priceColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(currencyFormat.format(cellData.getValue().getPrice())));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        supplierColumn.setCellValueFactory(cellData -> {
            String supplierId = cellData.getValue().getSupplierId();
            if (supplierId != null) {
                var supplier = InventoryApp.getSupplierService().getSupplierById(supplierId);
                return new SimpleStringProperty(supplier != null ? supplier.getCompanyName() : "Unknown");
            }
            return new SimpleStringProperty("No Supplier");
        });
        statusColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            String status = product.isActive() ? "Active" : "Inactive";
            if (product.isLowStock()) {
                status += " (Low Stock)";
            }
            return new SimpleStringProperty(status);
        });

        // Style low stock items
        productTable.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldProduct, newProduct) -> {
                if (newProduct != null && newProduct.isLowStock()) {
                    row.getStyleClass().add("low-stock-row");
                } else {
                    row.getStyleClass().remove("low-stock-row");
                }
            });
            return row;
        });

        productTable.setItems(productData);
    }

    private void setupFilters() {
        // Category filter
        categoryFilter.getItems().add(null); // "All Categories" option
        categoryFilter.getItems().addAll(ProductCategory.values());
        categoryFilter.setConverter(new StringConverter<ProductCategory>() {
            @Override
            public String toString(ProductCategory category) {
                return category == null ? "All Categories" : category.getDisplayName();
            }

            @Override
            public ProductCategory fromString(String string) {
                return null; // Not needed for this use case
            }
        });

        // Filter listeners
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        categoryFilter.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        lowStockFilter.selectedProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        activeOnlyFilter.selectedProperty().addListener((obs, oldVal, newVal) -> applyFilters());

        // Set default values
        activeOnlyFilter.setSelected(true);
    }

    private void setupButtons() {
        addButton.setOnAction(e -> showAddProductDialog());
        editButton.setOnAction(e -> showEditProductDialog());
        deleteButton.setOnAction(e -> deleteSelectedProduct());
        refreshButton.setOnAction(e -> refreshData());

        // Enable/disable edit and delete buttons based on selection
        editButton.disableProperty().bind(productTable.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.disableProperty().bind(productTable.getSelectionModel().selectedItemProperty().isNull());
    }

    private void loadProducts() {
        List<Product> products = productService.getAllProducts();
        productData.clear();
        productData.addAll(products);
    }

    private void applyFilters() {
        SearchFilter filter = new SearchFilter();
        
        String searchText = searchField.getText();
        if (searchText != null && !searchText.trim().isEmpty()) {
            filter.setProductName(searchText.trim());
        }

        ProductCategory selectedCategory = categoryFilter.getValue();
        if (selectedCategory != null) {
            filter.setCategory(selectedCategory);
        }

        filter.setLowStockOnly(lowStockFilter.isSelected());
        filter.setActiveOnly(activeOnlyFilter.isSelected());

        List<Product> filteredProducts = productService.searchProducts(filter);
        productData.clear();
        productData.addAll(filteredProducts);
        updateStatistics();
    }

    private void showAddProductDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProductDialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Product");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(InventoryApp.getPrimaryStage());
            dialogStage.setScene(new Scene(loader.load()));

            ProductDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                Product newProduct = controller.getProduct();
                if (productService.addProduct(newProduct)) {
                    refreshData();
                    showSuccessAlert("Product Added", "Product has been successfully added to inventory.");
                } else {
                    showErrorAlert("Add Failed", "Failed to add product. Please check the product details.");
                }
            }
        } catch (IOException e) {
            showErrorAlert("Dialog Error", "Could not open product dialog: " + e.getMessage());
        }
    }

    private void showEditProductDialog() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showWarningAlert("No Selection", "Please select a product to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProductDialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Product");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(InventoryApp.getPrimaryStage());
            dialogStage.setScene(new Scene(loader.load()));

            ProductDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setProduct(selectedProduct);

            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                Product updatedProduct = controller.getProduct();
                if (productService.updateProduct(updatedProduct)) {
                    refreshData();
                    showSuccessAlert("Product Updated", "Product has been successfully updated.");
                } else {
                    showErrorAlert("Update Failed", "Failed to update product. Please try again.");
                }
            }
        } catch (IOException e) {
            showErrorAlert("Dialog Error", "Could not open product dialog: " + e.getMessage());
        }
    }

    private void deleteSelectedProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showWarningAlert("No Selection", "Please select a product to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Product");
        confirmAlert.setContentText("Are you sure you want to delete the product '" + 
                selectedProduct.getName() + "'? This action cannot be undone.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (productService.removeProduct(selectedProduct.getProductId())) {
                refreshData();
                showSuccessAlert("Product Deleted", "Product has been successfully deleted.");
            } else {
                showErrorAlert("Delete Failed", "Failed to delete product. Please try again.");
            }
        }
    }

    private void refreshData() {
        loadProducts();
        applyFilters();
        updateStatistics();
    }

    private void updateStatistics() {
        int totalProducts = productData.size();
        double totalValue = productData.stream().mapToDouble(Product::getTotalValue).sum();
        long lowStockCount = productData.stream().filter(Product::isLowStock).count();

        totalProductsLabel.setText("Total Products: " + totalProducts);
        totalValueLabel.setText("Total Value: " + currencyFormat.format(totalValue));
        lowStockLabel.setText("Low Stock Items: " + lowStockCount);
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