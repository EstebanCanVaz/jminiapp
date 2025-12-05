package com.jminiapp.examples.passwordvault;

import com.jminiapp.core.api.JMiniApp;
import com.jminiapp.core.api.JMiniAppConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Password Vault application using domain-driven design principles.
 *
 * This application manages a secure vault of credentials, demonstrating:
 * - Domain modeling (Credential, Category)
 * - Domain operations (store, retrieve, search, remove)
 * - Business rules (validation, categorization)
 * - Ubiquitous language (vault, credential, store, retrieve)
 */
public class PasswordVaultApp extends JMiniApp {
    private Scanner scanner;
    private List<Credential> vault;  // Using domain term "vault"
    private boolean running;

    public PasswordVaultApp(JMiniAppConfig config) {
        super(config);
    }

    @Override
    protected void initialize() {
        System.out.println("\n=== Secure Password Vault ===");
        System.out.println("Domain-Driven Password Management");

        scanner = new Scanner(System.in);
        running = true;

        // Bootstrap: Load existing vault
        List<Credential> data = context.getData();
        if (data != null && !data.isEmpty()) {
            vault = new ArrayList<>(data);
            System.out.println("Vault loaded: " + vault.size() + " credential(s)");
        } else {
            vault = new ArrayList<>();
            System.out.println("New vault initialized");
        }
    }

    @Override
    protected void run() {
        while (running) {
            displayMenu();
            handleUserInput();
        }
    }

    @Override
    protected void shutdown() {
        // Persist vault state
        context.setData(vault);

        scanner.close();
        System.out.println("\nVault secured with " + vault.size() + " credential(s)");
        System.out.println("Goodbye!");
    }

    private void displayMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("SECURE VAULT | Entries: " + vault.size());
        System.out.println("=".repeat(50));
        System.out.println("1. Store new credential");
        System.out.println("2. Retrieve all credentials");
        System.out.println("3. Search by service");
        System.out.println("4. Filter by category");
        System.out.println("5. Remove credential");
        System.out.println("6. Generate secure password");
        System.out.println("7. Export vault");
        System.out.println("8. Import vault");
        System.out.println("9. Exit");
        System.out.print("\nChoose operation: ");
    }

    private void handleUserInput() {
        try {
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    storeCredential();
                    break;
                case "2":
                    retrieveAllCredentials();
                    break;
                case "3":
                    searchCredentials();
                    break;
                case "4":
                    filterByCategory();
                    break;
                case "5":
                    removeCredential();
                    break;
                case "6":
                    generateSecurePassword();
                    break;
                case "7":
                    exportVault();
                    break;
                case "8":
                    importVault();
                    break;
                case "9":
                    running = false;
                    System.out.println("\nSecuring vault...");
                    break;
                default:
                    System.out.println("Invalid operation. Choose 1-9.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Domain operation: Store a new credential in the vault.
     */
    private void storeCredential() {
        System.out.print("\nService name: ");
        String service = scanner.nextLine().trim();

        // Business rule: Service is mandatory
        if (service.isEmpty()) {
            System.out.println("Service name is required!");
            return;
        }

        System.out.print("Username/Email: ");
        String username = scanner.nextLine().trim();

        // Business rule: Username is mandatory
        if (username.isEmpty()) {
            System.out.println("Username is required!");
            return;
        }

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        // Business rule: Password is mandatory
        if (password.isEmpty()) {
            System.out.println("Password is required!");
            return;
        }

        System.out.print("Category (EMAIL/SOCIAL/BANKING/WORK/GENERAL): ");
        String categoryInput = scanner.nextLine().trim();
        Category category = Category.fromString(categoryInput);

        // Create and store credential
        Credential credential = new Credential(service, username, password, category);
        vault.add(credential);

        System.out.println("Credential stored successfully!");
        System.out.println("   " + credential);
    }

    /**
     * Domain operation: Retrieve and display all credentials.
     */
    private void retrieveAllCredentials() {
        if (vault.isEmpty()) {
            System.out.println("\nVault is empty");
            return;
        }

        System.out.println("\n=== VAULT CONTENTS ===");
        for (int i = 0; i < vault.size(); i++) {
            Credential c = vault.get(i);
            System.out.printf("%d. %s\n", i + 1, c);
            System.out.println("   Password: " + c.getDisplayPassword());
        }
    }

    /**
     * Domain operation: Search credentials by service name.
     */
    private void searchCredentials() {
        System.out.print("\nSearch service: ");
        String searchTerm = scanner.nextLine().trim();

        // Domain operation: filter using domain method
        List<Credential> results = vault.stream()
                .filter(c -> c.matchesService(searchTerm))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            System.out.println("No credentials found for: " + searchTerm);
            return;
        }

        System.out.println("\n=== SEARCH RESULTS ===");
        for (Credential c : results) {
            System.out.println(c);
            System.out.println("   Password: " + c.reveal());  // Domain operation
        }
    }

    /**
     * Domain operation: Filter credentials by category.
     */
    private void filterByCategory() {
        System.out.print("\nCategory (EMAIL/SOCIAL/BANKING/WORK/GENERAL): ");
        String input = scanner.nextLine().trim();
        Category category = Category.fromString(input);

        // Domain operation: filter by category
        List<Credential> filtered = vault.stream()
                .filter(c -> c.belongsToCategory(category))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            System.out.println("No credentials in category: " + category);
            return;
        }

        System.out.println("\n=== " + category.getDisplayName().toUpperCase() + " ===");
        for (Credential c : filtered) {
            System.out.println(c);
        }
    }

    /**
     * Domain operation: Remove a credential from the vault.
     */
    private void removeCredential() {
        if (vault.isEmpty()) {
            System.out.println("Vault is empty");
            return;
        }

        retrieveAllCredentials();
        System.out.print("\nEntry number to remove: ");

        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;

            // Business rule: valid index
            if (index < 0 || index >= vault.size()) {
                System.out.println("Invalid entry number!");
                return;
            }

            Credential removed = vault.remove(index);
            System.out.println("Removed: " + removed.getService());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        }
    }

    /**
     * Domain operation: Generate a secure password.
     */
    private void generateSecurePassword() {
        // Domain logic for password generation
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%^&*";
        String allChars = uppercase + lowercase + digits + special;

        Random random = new Random();
        StringBuilder password = new StringBuilder();

        // Business rule: Strong password must have at least one of each type
        password.append(uppercase.charAt(random.nextInt(uppercase.length())));
        password.append(lowercase.charAt(random.nextInt(lowercase.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(special.charAt(random.nextInt(special.length())));

        // Fill remaining with random characters
        for (int i = 4; i < 12; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        System.out.println("\nSecure password generated:");
        System.out.println("   " + password);
        System.out.println("   (Copy and use when storing a credential)");
    }

    /**
     * Domain operation: Export vault to persistent storage.
     */
    private void exportVault() {
        try {
            context.setData(vault);
            context.exportData("json");
            System.out.println("Vault exported: PasswordVault.json");
        } catch (IOException e) {
            System.out.println("Export failed: " + e.getMessage());
        }
    }

    /**
     * Domain operation: Import vault from persistent storage.
     */
    private void importVault() {
        try {
            context.importData("json");

            List<Credential> data = context.getData();
            if (data != null && !data.isEmpty()) {
                vault = new ArrayList<>(data);
                System.out.println("Vault imported successfully!");
                System.out.println("   Loaded " + vault.size() + " credential(s)");
            } else {
                System.out.println("No data found in file");
            }
        } catch (IOException e) {
            System.out.println("Import failed: " + e.getMessage());
        }
    }
}

