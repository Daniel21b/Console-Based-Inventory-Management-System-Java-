package com.inventory;

import com.inventory.controller.MainController;
import com.inventory.repository.InventoryManager;
import com.inventory.service.*;
import com.inventory.util.DataGenerator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Main JavaFX application class for the Inventory Management System.
 * Implements comprehensive retail business simulation with optimized performance.
 */
public class InventoryApp extends Application {
    
    public static final String APP_TITLE = "Inventory Management System";
    public static final String VERSION = "1.0.0";
    
    private static InventoryManager inventoryManager;
    private static ProductService productService;
    private static OrderService orderService;
    private static SupplierService supplierService;
    private static ReportService reportService;
    private static Stage primaryStage;
    
    @Override
    public void start(Stage stage) {
        try {
            primaryStage = stage;
            
            // Initialize core services
            initializeServices();
            
            // Load sample data for demonstration
            loadSampleData();
            
            // Load and setup main scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 800);
            
            // Apply CSS styling
            scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
            
            // Configure stage
            stage.setTitle(APP_TITLE + " v" + VERSION);
            stage.setScene(scene);
            stage.setMinWidth(1000);
            stage.setMinHeight(700);
            stage.centerOnScreen();
            
            // Setup shutdown handler
            stage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });
            
            // Get controller and initialize
            MainController controller = loader.getController();
            controller.initialize();
            
            stage.show();
            
            System.out.println("Inventory Management System started successfully");
            System.out.println("Products loaded: " + inventoryManager.getProductCount());
            System.out.println("Orders loaded: " + inventoryManager.getOrderCount());
            System.out.println("Suppliers loaded: " + inventoryManager.getSupplierCount());
            
        } catch (Exception e) {
            showErrorAlert("Application Startup Error", 
                    "Failed to start the application: " + e.getMessage());
            e.printStackTrace();
            Platform.exit();
        }
    }
    
    /**
     * Initialize all core services with dependency injection
     */
    private void initializeServices() {
        // Initialize core data manager
        inventoryManager = new InventoryManager();
        
        // Initialize business services
        productService = new ProductService(inventoryManager);
        orderService = new OrderService(inventoryManager);
        supplierService = new SupplierService(inventoryManager);
        reportService = new ReportService(inventoryManager);
        
        System.out.println("All services initialized successfully");
    }
    
    /**
     * Load sample data for demonstration and testing
     */
    private void loadSampleData() {
        try {
            DataGenerator generator = new DataGenerator();
            
            // Generate sample suppliers (20)
            var suppliers = generator.generateSampleSuppliers(20);
            suppliers.forEach(inventoryManager::addSupplier);
            
            // Generate sample products (250+)
            var products = generator.generateSampleProducts(250, suppliers);
            products.forEach(inventoryManager::addProduct);
            
            // Generate sample orders (50)
            var orders = generator.generateSampleOrders(50, products);
            orders.forEach(inventoryManager::addOrder);
            
            System.out.println("Sample data loaded successfully");
            
        } catch (Exception e) {
            System.err.println("Warning: Failed to load sample data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Show error alert dialog
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Static getters for services (Dependency Injection alternative)
    public static InventoryManager getInventoryManager() {
        return inventoryManager;
    }
    
    public static ProductService getProductService() {
        return productService;
    }
    
    public static OrderService getOrderService() {
        return orderService;
    }
    
    public static SupplierService getSupplierService() {
        return supplierService;
    }
    
    public static ReportService getReportService() {
        return reportService;
    }
    
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    
    /**
     * Main entry point
     */
    public static void main(String[] args) {
        // Performance monitoring
        long startTime = System.currentTimeMillis();
        
        // JavaFX system properties for better performance
        System.setProperty("javafx.animation.pulse", "60");
        System.setProperty("javafx.animation.fullspeed", "true");
        System.setProperty("prism.lcdtext", "false");
        
        // Launch JavaFX application
        launch(args);
        
        long endTime = System.currentTimeMillis();
        System.out.println("Application startup time: " + (endTime - startTime) + "ms");
    }
} 