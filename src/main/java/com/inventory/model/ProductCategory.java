package com.inventory.model;

/**
 * Enumeration of product categories for inventory classification.
 * Supports hierarchical product organization and filtering.
 */
public enum ProductCategory {
    ELECTRONICS("Electronics", "Electronic devices and accessories"),
    CLOTHING("Clothing", "Apparel and fashion items"),
    BOOKS("Books", "Books and literature"),
    HOME_GARDEN("Home & Garden", "Home improvement and garden supplies"),
    SPORTS_OUTDOORS("Sports & Outdoors", "Sports equipment and outdoor gear"),
    HEALTH_BEAUTY("Health & Beauty", "Health care and beauty products"),
    AUTOMOTIVE("Automotive", "Vehicle parts and accessories"),
    TOYS_GAMES("Toys & Games", "Children's toys and games"),
    FOOD_BEVERAGES("Food & Beverages", "Consumable food and drink items"),
    OFFICE_SUPPLIES("Office Supplies", "Business and office equipment"),
    PET_SUPPLIES("Pet Supplies", "Pet care and accessories"),
    JEWELRY("Jewelry", "Jewelry and watches"),
    TOOLS_HARDWARE("Tools & Hardware", "Tools and hardware supplies"),
    CRAFTS("Arts & Crafts", "Craft supplies and materials"),
    MUSIC_INSTRUMENTS("Musical Instruments", "Musical instruments and accessories"),
    OTHER("Other", "Miscellaneous items");
    
    private final String displayName;
    private final String description;
    
    ProductCategory(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
    
    /**
     * Parse category from display name
     */
    public static ProductCategory fromDisplayName(String displayName) {
        for (ProductCategory category : values()) {
            if (category.displayName.equalsIgnoreCase(displayName)) {
                return category;
            }
        }
        return OTHER;
    }
} 