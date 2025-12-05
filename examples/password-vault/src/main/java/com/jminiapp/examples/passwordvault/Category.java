package com.jminiapp.examples.passwordvault;

/**
 * Value object representing credential categories.
 * 
 * This enum defines the valid categories for organizing credentials
 * in the vault, following domain-driven design principles.
 */
public enum Category {
    EMAIL("Email"),
    SOCIAL("Social Media"),
    BANKING("Banking"),
    WORK("Work"),
    GENERAL("General");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Parse a category from string input.
     * 
     * @param input the category name
     * @return the matching Category or GENERAL as default
     */
    public static Category fromString(String input) {
        if (input == null || input.trim().isEmpty()) {
            return GENERAL;
        }

        String normalized = input.trim().toUpperCase();
        try {
            return Category.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            return GENERAL;
        }
    }

    @Override
    public String toString() {
        return displayName;
    }
}

