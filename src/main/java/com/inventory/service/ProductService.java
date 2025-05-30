package com.inventory.service;

import com.inventory.model.Product;
import com.inventory.model.ProductCategory;
import com.inventory.repository.InventoryManager;
import com.inventory.util.SearchFilter;

import java.util.List;
import java.util.UUID;

/**
 * Service class for product management business logic.
 * Provides high-level operations for product handling.
 */
public class ProductService {
    
    private final InventoryManager inventoryManager;
    
    public ProductService(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }
    
    /**
     * Create a new product with auto-generated ID
     */
    public Product createProduct(String name, ProductCategory category, double price, int quantity) {
        String productId = generateProductId();
        Product product = new Product(productId, name, category, price, quantity);
        
        if (inventoryManager.addProduct(product)) {
            return product;
        }
        return null;
    }
    
    /**
     * Add an existing product
     */
    public boolean addProduct(Product product) {
        return inventoryManager.addProduct(product);
    }
    
    /**
     * Update product information
     */
    public boolean updateProduct(Product product) {
        return inventoryManager.updateProduct(product);
    }
    
    /**
     * Remove product by ID
     */
    public boolean removeProduct(String productId) {
        return inventoryManager.removeProduct(productId);
    }
    
    /**
     * Get product by ID
     */
    public Product getProductById(String productId) {
        return inventoryManager.getProductById(productId);
    }
    
    /**
     * Get product by barcode
     */
    public Product getProductByBarcode(String barcode) {
        return inventoryManager.getProductByBarcode(barcode);
    }
    
    /**
     * Search products with advanced criteria
     */
    public List<Product> searchProducts(SearchFilter filter) {
        return inventoryManager.searchProducts(filter);
    }
    
    /**
     * Get all products
     */
    public List<Product> getAllProducts() {
        return inventoryManager.getAllProducts();
    }
    
    /**
     * Get products sorted by name
     */
    public List<Product> getProductsSortedByName() {
        return inventoryManager.getProductsSortedByName();
    }
    
    /**
     * Get products by category
     */
    public List<Product> getProductsByCategory(ProductCategory category) {
        return inventoryManager.getProductsByCategory(category);
    }
    
    /**
     * Get low stock products
     */
    public List<Product> getLowStockProducts() {
        return inventoryManager.getLowStockProducts();
    }
    
    /**
     * Update product quantity
     */
    public boolean updateProductQuantity(String productId, int newQuantity) {
        Product product = inventoryManager.getProductById(productId);
        if (product != null) {
            product.setQuantity(newQuantity);
            return inventoryManager.updateProduct(product);
        }
        return false;
    }
    
    /**
     * Adjust product quantity (add/subtract)
     */
    public boolean adjustProductQuantity(String productId, int adjustment) {
        Product product = inventoryManager.getProductById(productId);
        if (product != null) {
            product.adjustQuantity(adjustment);
            return inventoryManager.updateProduct(product);
        }
        return false;
    }
    
    /**
     * Check if product exists
     */
    public boolean productExists(String productId) {
        return inventoryManager.getProductById(productId) != null;
    }
    
    /**
     * Generate unique product ID
     */
    private String generateProductId() {
        return "PRD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * Generate barcode for product
     */
    public String generateBarcode() {
        return String.valueOf(System.currentTimeMillis());
    }
    
    /**
     * Validate product data
     */
    public boolean validateProduct(Product product) {
        if (product == null) return false;
        if (product.getName() == null || product.getName().trim().isEmpty()) return false;
        if (product.getCategory() == null) return false;
        if (product.getPrice() < 0) return false;
        if (product.getQuantity() < 0) return false;
        return true;
    }
    
    /**
     * Get total product count
     */
    public int getProductCount() {
        return inventoryManager.getProductCount();
    }
    
    /**
     * Get total inventory value
     */
    public double getTotalInventoryValue() {
        return inventoryManager.getTotalInventoryValue();
    }
} 