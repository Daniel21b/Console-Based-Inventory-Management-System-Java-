package com.inventory.service;

import com.inventory.model.Order;
import com.inventory.model.OrderStatus;
import com.inventory.repository.InventoryManager;

import java.util.List;
import java.util.UUID;

/**
 * Service class for order management business logic.
 */
public class OrderService {
    
    private final InventoryManager inventoryManager;
    
    public OrderService(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }
    
    public Order createOrder(String customerId, String customerName) {
        String orderId = generateOrderId();
        return new Order(orderId, customerId, customerName);
    }
    
    public boolean addOrder(Order order) {
        return inventoryManager.addOrder(order);
    }
    
    public Order getOrderById(String orderId) {
        return inventoryManager.getOrderById(orderId);
    }
    
    public List<Order> getAllOrders() {
        return inventoryManager.getAllOrders();
    }
    
    public boolean updateOrderStatus(String orderId, OrderStatus status) {
        return inventoryManager.updateOrderStatus(orderId, status);
    }
    
    private String generateOrderId() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
} 