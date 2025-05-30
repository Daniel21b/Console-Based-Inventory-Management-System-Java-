package com.inventory.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Order entity representing customer orders with full lifecycle management.
 * Supports order processing, status tracking, and business analytics.
 */
public class Order {
    
    @JsonProperty("orderId")
    private String orderId;
    
    @JsonProperty("customerId")
    private String customerId;
    
    @JsonProperty("customerName")
    private String customerName;
    
    @JsonProperty("customerEmail")
    private String customerEmail;
    
    @JsonProperty("customerPhone")
    private String customerPhone;
    
    @JsonProperty("items")
    private List<OrderItem> items;
    
    @JsonProperty("status")
    private OrderStatus status;
    
    @JsonProperty("orderDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;
    
    @JsonProperty("shippedDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime shippedDate;
    
    @JsonProperty("deliveredDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deliveredDate;
    
    @JsonProperty("subtotal")
    private double subtotal;
    
    @JsonProperty("taxAmount")
    private double taxAmount;
    
    @JsonProperty("shippingCost")
    private double shippingCost;
    
    @JsonProperty("totalAmount")
    private double totalAmount;
    
    @JsonProperty("discountAmount")
    private double discountAmount;
    
    @JsonProperty("shippingAddress")
    private String shippingAddress;
    
    @JsonProperty("billingAddress")
    private String billingAddress;
    
    @JsonProperty("paymentMethod")
    private String paymentMethod;
    
    @JsonProperty("notes")
    private String notes;
    
    @JsonProperty("priority")
    private OrderPriority priority;
    
    // Default constructor
    public Order() {
        this.items = new ArrayList<>();
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
        this.priority = OrderPriority.NORMAL;
    }
    
    // Constructor with essential fields
    public Order(String orderId, String customerId, String customerName) {
        this();
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
    }
    
    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }
    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerEmail() {
        return customerEmail;
    }
    
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    
    public String getCustomerPhone() {
        return customerPhone;
    }
    
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    
    public List<OrderItem> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItem> items) {
        this.items = items;
        calculateTotals();
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
        if (status == OrderStatus.SHIPPED && shippedDate == null) {
            this.shippedDate = LocalDateTime.now();
        } else if (status == OrderStatus.DELIVERED && deliveredDate == null) {
            this.deliveredDate = LocalDateTime.now();
        }
    }
    
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
    
    public LocalDateTime getShippedDate() {
        return shippedDate;
    }
    
    public void setShippedDate(LocalDateTime shippedDate) {
        this.shippedDate = shippedDate;
    }
    
    public LocalDateTime getDeliveredDate() {
        return deliveredDate;
    }
    
    public void setDeliveredDate(LocalDateTime deliveredDate) {
        this.deliveredDate = deliveredDate;
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    public double getTaxAmount() {
        return taxAmount;
    }
    
    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }
    
    public double getShippingCost() {
        return shippingCost;
    }
    
    public void setShippingCost(double shippingCost) {
        this.shippingCost = shippingCost;
        calculateTotals();
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public double getDiscountAmount() {
        return discountAmount;
    }
    
    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
        calculateTotals();
    }
    
    public String getShippingAddress() {
        return shippingAddress;
    }
    
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    
    public String getBillingAddress() {
        return billingAddress;
    }
    
    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public OrderPriority getPriority() {
        return priority;
    }
    
    public void setPriority(OrderPriority priority) {
        this.priority = priority;
    }
    
    // Business logic methods
    public void addItem(OrderItem item) {
        this.items.add(item);
        calculateTotals();
    }
    
    public void removeItem(OrderItem item) {
        this.items.remove(item);
        calculateTotals();
    }
    
    public void calculateTotals() {
        this.subtotal = items.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
        
        this.taxAmount = items.stream()
                .filter(item -> item.isTaxable())
                .mapToDouble(OrderItem::getTotalPrice)
                .sum() * 0.08; // 8% tax rate
        
        this.totalAmount = subtotal + taxAmount + shippingCost - discountAmount;
    }
    
    public int getTotalItemCount() {
        return items.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }
    
    public boolean canBeShipped() {
        return status == OrderStatus.PENDING || status == OrderStatus.PROCESSING;
    }
    
    public boolean canBeCancelled() {
        return status == OrderStatus.PENDING || status == OrderStatus.PROCESSING;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderId, order.orderId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }
    
    @Override
    public String toString() {
        return String.format("Order{id='%s', customer='%s', status=%s, total=%.2f, items=%d}", 
                orderId, customerName, status, totalAmount, items.size());
    }
} 