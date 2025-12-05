package com.jminiapp.examples.passwordvault;

/**
 * Domain model representing a secured credential entry in the vault.
 * 
 * A Credential is identified by its service and username combination.
 * This class encapsulates the core domain logic for password management.
 */
public class Credential {
    private String service;
    private String username;
    private String password;
    private Category category;
    private boolean revealed;

    /**
     * Default constructor for serialization.
     */
    public Credential() {
        this.category = Category.GENERAL;
        this.revealed = false;
    }

    /**
     * Create a new secured credential entry.
     *
     * @param service the service name (e.g., "Gmail", "Facebook")
     * @param username the username or email
     * @param password the password value
     * @param category the credential category
     */
    public Credential(String service, String username, String password, Category category) {
        this.service = service;
        this.username = username;
        this.password = password;
        this.category = category != null ? category : Category.GENERAL;
        this.revealed = false;
    }

    // Getters and Setters
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    /**
     * Domain operation: Mask the password for secure display.
     * 
     * @return masked password string
     */
    public String mask() {
        this.revealed = false;
        return "********";
    }

    /**
     * Domain operation: Reveal the actual password.
     * 
     * @return the actual password
     */
    public String reveal() {
        this.revealed = true;
        return this.password;
    }

    /**
     * Get the display password based on revealed state.
     * 
     * @return masked or actual password
     */
    public String getDisplayPassword() {
        return revealed ? password : mask();
    }

    /**
     * Domain validation: Check if this credential matches a service.
     * 
     * @param searchTerm the term to search for
     * @return true if service matches
     */
    public boolean matchesService(String searchTerm) {
        return service.toLowerCase().contains(searchTerm.toLowerCase());
    }

    /**
     * Domain validation: Check if credential belongs to a category.
     * 
     * @param cat the category to check
     * @return true if matches
     */
    public boolean belongsToCategory(Category cat) {
        return this.category == cat;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s", category, service, username);
    }
}

