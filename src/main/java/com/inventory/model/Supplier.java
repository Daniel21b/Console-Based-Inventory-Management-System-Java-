package com.inventory.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Supplier entity representing business suppliers with comprehensive contact and performance information.
 */
public class Supplier {
    
    @JsonProperty("supplierId")
    private String supplierId;
    
    @JsonProperty("companyName")
    private String companyName;
    
    @JsonProperty("contactName")
    private String contactName;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("phone")
    private String phone;
    
    @JsonProperty("address")
    private String address;
    
    @JsonProperty("city")
    private String city;
    
    @JsonProperty("state")
    private String state;
    
    @JsonProperty("postalCode")
    private String postalCode;
    
    @JsonProperty("country")
    private String country;
    
    @JsonProperty("website")
    private String website;
    
    @JsonProperty("notes")
    private String notes;
    
    @JsonProperty("active")
    private boolean active;
    
    @JsonProperty("preferredPaymentTerms")
    private String preferredPaymentTerms;
    
    @JsonProperty("leadTimeDays")
    private int leadTimeDays;
    
    @JsonProperty("minimumOrderAmount")
    private double minimumOrderAmount;
    
    @JsonProperty("rating")
    private double rating; // 1-5 stars
    
    @JsonProperty("totalOrders")
    private int totalOrders;
    
    @JsonProperty("totalOrderValue")
    private double totalOrderValue;
    
    @JsonProperty("lastOrderDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastOrderDate;
    
    @JsonProperty("dateAdded")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateAdded;
    
    // Default constructor
    public Supplier() {
        this.active = true;
        this.rating = 0.0;
        this.leadTimeDays = 7; // Default 7 days
        this.dateAdded = LocalDateTime.now();
    }
    
    // Constructor with essential fields
    public Supplier(String supplierId, String companyName, String contactName, String email, String phone) {
        this();
        this.supplierId = supplierId;
        this.companyName = companyName;
        this.contactName = contactName;
        this.email = email;
        this.phone = phone;
    }
    
    // Getters and Setters
    public String getSupplierId() {
        return supplierId;
    }
    
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getContactName() {
        return contactName;
    }
    
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public String getPreferredPaymentTerms() {
        return preferredPaymentTerms;
    }
    
    public void setPreferredPaymentTerms(String preferredPaymentTerms) {
        this.preferredPaymentTerms = preferredPaymentTerms;
    }
    
    public int getLeadTimeDays() {
        return leadTimeDays;
    }
    
    public void setLeadTimeDays(int leadTimeDays) {
        this.leadTimeDays = leadTimeDays;
    }
    
    public double getMinimumOrderAmount() {
        return minimumOrderAmount;
    }
    
    public void setMinimumOrderAmount(double minimumOrderAmount) {
        this.minimumOrderAmount = minimumOrderAmount;
    }
    
    public double getRating() {
        return rating;
    }
    
    public void setRating(double rating) {
        this.rating = Math.max(0, Math.min(5, rating)); // Clamp between 0-5
    }
    
    public int getTotalOrders() {
        return totalOrders;
    }
    
    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }
    
    public double getTotalOrderValue() {
        return totalOrderValue;
    }
    
    public void setTotalOrderValue(double totalOrderValue) {
        this.totalOrderValue = totalOrderValue;
    }
    
    public LocalDateTime getLastOrderDate() {
        return lastOrderDate;
    }
    
    public void setLastOrderDate(LocalDateTime lastOrderDate) {
        this.lastOrderDate = lastOrderDate;
    }
    
    public LocalDateTime getDateAdded() {
        return dateAdded;
    }
    
    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }
    
    // Business logic methods
    public double getAverageOrderValue() {
        if (totalOrders > 0) {
            return totalOrderValue / totalOrders;
        }
        return 0.0;
    }
    
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (address != null && !address.trim().isEmpty()) {
            sb.append(address);
        }
        if (city != null && !city.trim().isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(city);
        }
        if (state != null && !state.trim().isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(state);
        }
        if (postalCode != null && !postalCode.trim().isEmpty()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(postalCode);
        }
        if (country != null && !country.trim().isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(country);
        }
        return sb.toString();
    }
    
    public void recordOrder(double orderValue) {
        this.totalOrders++;
        this.totalOrderValue += orderValue;
        this.lastOrderDate = LocalDateTime.now();
    }
    
    public boolean isReliable() {
        return rating >= 4.0 && totalOrders >= 5;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Supplier supplier = (Supplier) o;
        return Objects.equals(supplierId, supplier.supplierId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(supplierId);
    }
    
    @Override
    public String toString() {
        return String.format("Supplier{id='%s', company='%s', contact='%s', rating=%.1f}", 
                supplierId, companyName, contactName, rating);
    }
} 