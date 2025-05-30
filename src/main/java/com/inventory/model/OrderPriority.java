package com.inventory.model;

/**
 * Enumeration of order priorities for processing prioritization.
 */
public enum OrderPriority {
    LOW("Low", 1),
    NORMAL("Normal", 2),
    HIGH("High", 3),
    URGENT("Urgent", 4);
    
    private final String displayName;
    private final int level;
    
    OrderPriority(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getLevel() {
        return level;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
} 