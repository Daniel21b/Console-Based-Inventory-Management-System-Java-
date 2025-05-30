package com.inventory.controller;

import com.inventory.InventoryApp;
import com.inventory.model.Product;
import com.inventory.model.ProductCategory;
import com.inventory.model.Supplier;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.List;
import java.util.UUID;

/**
 * Controller for the Product Add/Edit dialog.
 */
public class ProductDialogController {

    @FXML private TextField nameField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<ProductCategory> categoryComboBox;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private TextField reorderPointField;
    @FXML private ComboBox<Supplier> supplierComboBox;
    @FXML private TextField barcodeField;
    @FXML private TextField skuField;
    @FXML private TextField weightField;
    @FXML private TextField dimensionsField;
    @FXML private TextField costField;
    @FXML private CheckBox activeCheckBox;
    @FXML private CheckBox taxableCheckBox;

    private Stage dialogStage;
    private Product product;
    private boolean okClicked = false;

    public void initialize() {
        setupComboBoxes();
        setupValidation();
    }

    private void setupComboBoxes() {
        // Category ComboBox
        categoryComboBox.getItems().addAll(ProductCategory.values());
        categoryComboBox.setConverter(new StringConverter<ProductCategory>() {
            @Override
            public String toString(ProductCategory category) {
                return category != null ? category.getDisplayName() : "";
            }

            @Override
            public ProductCategory fromString(String string) {
                return null;
            }
        });

        // Supplier ComboBox
        List<Supplier> suppliers = InventoryApp.getSupplierService().getAllSuppliers();
        supplierComboBox.getItems().addAll(suppliers);
        supplierComboBox.setConverter(new StringConverter<Supplier>() {
            @Override
            public String toString(Supplier supplier) {
                return supplier != null ? supplier.getCompanyName() : "";
            }

            @Override
            public Supplier fromString(String string) {
                return null;
            }
        });
    }

    private void setupValidation() {
        // Add input validation listeners
        priceField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                priceField.setText(oldVal);
            }
        });

        quantityField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                quantityField.setText(oldVal);
            }
        });

        reorderPointField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                reorderPointField.setText(oldVal);
            }
        });

        weightField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                weightField.setText(oldVal);
            }
        });

        costField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                costField.setText(oldVal);
            }
        });
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setProduct(Product product) {
        this.product = product;

        if (product != null) {
            nameField.setText(product.getName());
            descriptionArea.setText(product.getDescription());
            categoryComboBox.setValue(product.getCategory());
            priceField.setText(String.valueOf(product.getPrice()));
            quantityField.setText(String.valueOf(product.getQuantity()));
            reorderPointField.setText(String.valueOf(product.getReorderPoint()));
            barcodeField.setText(product.getBarcode());
            skuField.setText(product.getSku());
            weightField.setText(String.valueOf(product.getWeight()));
            dimensionsField.setText(product.getDimensions());
            costField.setText(String.valueOf(product.getCost()));
            activeCheckBox.setSelected(product.isActive());
            taxableCheckBox.setSelected(product.isTaxable());

            // Set supplier
            if (product.getSupplierId() != null) {
                Supplier supplier = InventoryApp.getSupplierService().getSupplierById(product.getSupplierId());
                supplierComboBox.setValue(supplier);
            }
        } else {
            // Set default values for new product
            activeCheckBox.setSelected(true);
            taxableCheckBox.setSelected(true);
            reorderPointField.setText("10");
            quantityField.setText("0");
            priceField.setText("0.00");
            costField.setText("0.00");
            weightField.setText("0.0");
        }
    }

    public Product getProduct() {
        return product;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            if (product == null) {
                // Create new product
                product = new Product();
                String productId = "PRD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                product.setProductId(productId);
            }

            // Update product with form values
            product.setName(nameField.getText());
            product.setDescription(descriptionArea.getText());
            product.setCategory(categoryComboBox.getValue());
            product.setPrice(Double.parseDouble(priceField.getText()));
            product.setQuantity(Integer.parseInt(quantityField.getText()));
            product.setReorderPoint(Integer.parseInt(reorderPointField.getText()));
            product.setBarcode(barcodeField.getText());
            product.setSku(skuField.getText());
            product.setWeight(Double.parseDouble(weightField.getText()));
            product.setDimensions(dimensionsField.getText());
            product.setCost(Double.parseDouble(costField.getText()));
            product.setActive(activeCheckBox.isSelected());
            product.setTaxable(taxableCheckBox.isSelected());

            if (supplierComboBox.getValue() != null) {
                product.setSupplierId(supplierComboBox.getValue().getSupplierId());
            }

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    @FXML
    private void handleGenerateBarcode() {
        barcodeField.setText(InventoryApp.getProductService().generateBarcode());
    }

    @FXML
    private void handleGenerateSku() {
        if (nameField.getText() != null && !nameField.getText().trim().isEmpty()) {
            String sku = "SKU-" + nameField.getText().replaceAll("\\s+", "").toUpperCase();
            if (sku.length() > 20) {
                sku = sku.substring(0, 20);
            }
            skuField.setText(sku);
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().trim().isEmpty()) {
            errorMessage += "Product name is required!\n";
        }

        if (categoryComboBox.getValue() == null) {
            errorMessage += "Please select a category!\n";
        }

        try {
            double price = Double.parseDouble(priceField.getText());
            if (price < 0) {
                errorMessage += "Price must be non-negative!\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Invalid price format!\n";
        }

        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity < 0) {
                errorMessage += "Quantity must be non-negative!\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Invalid quantity format!\n";
        }

        try {
            int reorderPoint = Integer.parseInt(reorderPointField.getText());
            if (reorderPoint < 0) {
                errorMessage += "Reorder point must be non-negative!\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Invalid reorder point format!\n";
        }

        try {
            double weight = Double.parseDouble(weightField.getText());
            if (weight < 0) {
                errorMessage += "Weight must be non-negative!\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Invalid weight format!\n";
        }

        try {
            double cost = Double.parseDouble(costField.getText());
            if (cost < 0) {
                errorMessage += "Cost must be non-negative!\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Invalid cost format!\n";
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