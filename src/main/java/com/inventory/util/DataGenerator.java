package com.inventory.util;

import com.inventory.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Utility class for generating sample data for demonstration and testing.
 */
public class DataGenerator {
    
    private final Random random = new Random();
    private final String[] companyNames = {
        "TechSupply Co.", "Global Electronics", "Premier Parts", "Quality Components",
        "Reliable Suppliers", "Fast Track Logistics", "Pro Equipment", "Elite Materials",
        "Advanced Systems", "Premium Solutions", "Smart Supplies", "Efficient Parts",
        "Modern Components", "Dynamic Distributors", "Excellence Corp", "Innovation Inc",
        "Superior Supplies", "Professional Parts", "Quality First", "Trusted Partners"
    };
    
    private final String[] productNames = {
        "Wireless Mouse", "Bluetooth Keyboard", "USB Cable", "HDMI Adapter", "Power Bank",
        "Smartphone Case", "Screen Protector", "Charging Dock", "Laptop Stand", "Web Camera",
        "Microphone", "Speakers", "Headphones", "Gaming Chair", "Desk Organizer",
        "Monitor Stand", "Cable Management", "Ergonomic Pad", "LED Light Strip", "Smart Watch",
        "Tablet Case", "Car Charger", "Wall Mount", "Surge Protector", "Extension Cord",
        "Network Switch", "Router", "Modem", "Ethernet Cable", "WiFi Extender"
    };
    
    private final String[] customerNames = {
        "John Smith", "Sarah Johnson", "Michael Brown", "Emily Davis", "David Wilson",
        "Lisa Anderson", "Robert Taylor", "Jessica Martinez", "Christopher Lee", "Amanda White",
        "Matthew Harris", "Jennifer Clark", "Daniel Lewis", "Ashley Robinson", "James Walker"
    };
    
    public List<Supplier> generateSampleSuppliers(int count) {
        List<Supplier> suppliers = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            String supplierId = "SUP-" + String.format("%04d", i + 1);
            String companyName = companyNames[i % companyNames.length];
            String contactName = "Contact " + (i + 1);
            String email = "contact" + (i + 1) + "@" + companyName.toLowerCase().replace(" ", "").replace(".", "") + ".com";
            String phone = String.format("(%03d) %03d-%04d", 
                    random.nextInt(900) + 100, 
                    random.nextInt(900) + 100, 
                    random.nextInt(9000) + 1000);
            
            Supplier supplier = new Supplier(supplierId, companyName, contactName, email, phone);
            supplier.setAddress(String.format("%d Main St", random.nextInt(9999) + 1));
            supplier.setCity("City " + (i + 1));
            supplier.setState("State " + (char)('A' + (i % 26)));
            supplier.setPostalCode(String.format("%05d", random.nextInt(99999) + 1));
            supplier.setCountry("USA");
            supplier.setRating(3.0 + random.nextDouble() * 2.0); // 3.0-5.0
            supplier.setLeadTimeDays(5 + random.nextInt(15)); // 5-20 days
            supplier.setMinimumOrderAmount(100 + random.nextDouble() * 400); // $100-$500
            
            suppliers.add(supplier);
        }
        
        return suppliers;
    }
    
    public List<Product> generateSampleProducts(int count, List<Supplier> suppliers) {
        List<Product> products = new ArrayList<>();
        ProductCategory[] categories = ProductCategory.values();
        
        for (int i = 0; i < count; i++) {
            String productId = "PRD-" + String.format("%04d", i + 1);
            String name = productNames[i % productNames.length] + " " + (i / productNames.length + 1);
            ProductCategory category = categories[random.nextInt(categories.length)];
            double price = 10.0 + random.nextDouble() * 490.0; // $10-$500
            int quantity = random.nextInt(100) + 1; // 1-100
            
            Product product = new Product(productId, name, category, price, quantity);
            product.setDescription("High quality " + name.toLowerCase() + " for professional use");
            product.setBarcode(String.valueOf(1000000000L + i));
            product.setSku("SKU-" + String.format("%06d", i + 1));
            product.setWeight(0.1 + random.nextDouble() * 4.9); // 0.1-5.0 kg
            product.setDimensions(String.format("%.1fx%.1fx%.1f cm", 
                    random.nextDouble() * 20 + 5,
                    random.nextDouble() * 20 + 5,
                    random.nextDouble() * 10 + 2));
            product.setReorderPoint(5 + random.nextInt(15)); // 5-20
            product.setCost(price * 0.6 + random.nextDouble() * price * 0.2); // 60-80% of price
            
            if (!suppliers.isEmpty()) {
                Supplier supplier = suppliers.get(random.nextInt(suppliers.size()));
                product.setSupplierId(supplier.getSupplierId());
            }
            
            products.add(product);
        }
        
        return products;
    }
    
    public List<Order> generateSampleOrders(int count, List<Product> products) {
        List<Order> orders = new ArrayList<>();
        OrderStatus[] statuses = OrderStatus.values();
        
        for (int i = 0; i < count; i++) {
            String orderId = "ORD-" + String.format("%04d", i + 1);
            String customerId = "CUST-" + String.format("%04d", random.nextInt(100) + 1);
            String customerName = customerNames[random.nextInt(customerNames.length)];
            
            Order order = new Order(orderId, customerId, customerName);
            order.setCustomerEmail(customerName.toLowerCase().replace(" ", ".") + "@email.com");
            order.setCustomerPhone(String.format("(%03d) %03d-%04d", 
                    random.nextInt(900) + 100, 
                    random.nextInt(900) + 100, 
                    random.nextInt(9000) + 1000));
            order.setShippingAddress(String.format("%d Customer St, City %d, State %s %05d", 
                    random.nextInt(9999) + 1, 
                    random.nextInt(100) + 1,
                    String.valueOf((char)('A' + random.nextInt(26))),
                    random.nextInt(99999) + 1));
            order.setStatus(statuses[random.nextInt(statuses.length)]);
            order.setOrderDate(LocalDateTime.now().minusDays(random.nextInt(30)));
            
            // Add 1-5 random products to the order
            int itemCount = random.nextInt(5) + 1;
            for (int j = 0; j < itemCount; j++) {
                if (!products.isEmpty()) {
                    Product product = products.get(random.nextInt(products.size()));
                    int quantity = random.nextInt(3) + 1; // 1-3 quantity
                    OrderItem item = new OrderItem(product, quantity);
                    order.addItem(item);
                }
            }
            
            order.setShippingCost(5.0 + random.nextDouble() * 15.0); // $5-$20
            order.calculateTotals();
            
            orders.add(order);
        }
        
        return orders;
    }
} 