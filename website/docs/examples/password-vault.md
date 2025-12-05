---
sidebar_position: 2
---

# Password Vault: Domain-Driven Example

A password manager application demonstrating **Domain-Driven Design** principles with the JMiniApp framework.

**Key Concepts:**
- Domain modeling with entities and value objects
- Ubiquitous language in code
- Business rules in domain layer
- Domain operations
- Type-safe categories

**Source Code:** [examples/password-vault](https://github.com/yourusername/jminiapp/tree/main/examples/password-vault)

## Domain Analysis

### What is a Password Vault Domain?

A password vault is a **secure storage system for credentials**. The domain includes:

**Core Concepts:**
- **Vault**: The secure container
- **Credential**: Individual password entry
- **Category**: Type of service
- **Security**: Masking, revealing

**Operations:**
- Store (add credential)
- Retrieve (get credentials)
- Search (find by service)
- Remove (delete credential)
- Generate (create password)

**Business Rules:**
- Mandatory fields (service, username, password)
- Default categories
- Password masking
- Unique identification (service + username)

## Quick Start

```bash
cd examples/password-vault
mvn clean package
mvn exec:java
```

## Domain Model Design

### Step 1: Define the Core Entity

The `Credential` class is our main **domain entity**:

```java
public class Credential {
    // Identity
    private String service;
    private String username;
    
    // Value
    private String password;
    
    // Classification
    private Category category;
    
    // State
    private boolean revealed;
    
    // Domain operations
    public String mask() {
        this.revealed = false;
        return "••••••••";
    }
    
    public String reveal() {
        this.revealed = true;
        return this.password;
    }
    
    // Domain queries
    public boolean matchesService(String term) {
        return service.toLowerCase()
                     .contains(term.toLowerCase());
    }
    
    public boolean belongsToCategory(Category cat) {
        return this.category == cat;
    }
}
```

**Key Points:**
- **Identity**: service + username combination
- **Domain methods**: `mask()`, `reveal()`, `matchesService()`
- **Encapsulation**: Business logic inside the entity
- **State management**: `revealed` flag

### Step 2: Create Value Objects

The `Category` enum is a **value object**:

```java
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

    public static Category fromString(String input) {
        if (input == null || input.trim().isEmpty()) {
            return GENERAL;
        }
        try {
            return Category.valueOf(input.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return GENERAL; // Business rule: default category
        }
    }
}
```

**Benefits:**
- **Type safety**: Compile-time validation
- **No invalid states**: Only valid categories
- **Business logic**: `fromString()` with default
- **Display names**: User-friendly labels

### Step 3: Implement Domain Operations

Application service coordinates domain operations:

```java
public class PasswordVaultApp extends JMiniApp {
    private List<Credential> vault;  // Domain term

    // Domain operation: STORE
    private void storeCredential() {
        // Validate business rules
        if (service.isEmpty()) {
            System.out.println("Service required!");
            return;
        }
        
        // Create domain object
        Credential cred = new Credential(
            service, username, password, category
        );
        
        // Add to vault
        vault.add(cred);
    }

    // Domain operation: RETRIEVE
    private void retrieveAllCredentials() {
        for (Credential c : vault) {
            System.out.println(c);
            System.out.println("Password: " + c.getDisplayPassword());
        }
    }

    // Domain operation: SEARCH
    private void searchCredentials() {
        List<Credential> results = vault.stream()
            .filter(c -> c.matchesService(searchTerm))  // Domain method
            .collect(Collectors.toList());
    }

    // Domain operation: REMOVE
    private void removeCredential() {
        Credential removed = vault.remove(index);
        System.out.println("Removed: " + removed.getService());
    }
}
```

## Ubiquitous Language

The code uses **domain-specific terminology** consistently:

| Generic Term | Domain Term (Used in Code) |
|--------------|---------------------------|
| List | Vault |
| Password Entry | Credential |
| Add | Store |
| Get/List | Retrieve |
| Type | Category |
| Hide | Mask |
| Show | Reveal |

**Example in code:**
```java
// Generic approach
passwords.add(entry);
List<PasswordEntry> list = getAll();

// Domain-driven approach
vault.add(credential);
List<Credential> retrieved = retrieveAllCredentials();
```

## Business Rules Implementation

### Rule 1: Mandatory Fields

```java
// In storeCredential()
if (service.isEmpty()) {
    System.out.println("Service name is required!");
    return;
}

if (username.isEmpty()) {
    System.out.println("Username is required!");
    return;
}

if (password.isEmpty()) {
    System.out.println("Password is required!");
    return;
}
```

### Rule 2: Default Category

```java
// In Category enum
public static Category fromString(String input) {
    if (input == null || input.trim().isEmpty()) {
        return GENERAL;  // Business rule enforced
    }
    // ...
}

// In Credential constructor
public Credential(String service, String username, 
                 String password, Category category) {
    this.category = category != null ? category : Category.GENERAL;
}
```

### Rule 3: Password Masking

```java
// In Credential class
public String getDisplayPassword() {
    return revealed ? password : mask();
}

public String mask() {
    this.revealed = false;
    return "••••••••";  // Always 8 dots
}
```

### Rule 4: Strong Password Generation

```java
private void generateSecurePassword() {
    String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String lowercase = "abcdefghijklmnopqrstuvwxyz";
    String digits = "0123456789";
    String special = "!@#$%^&*";
    
    StringBuilder password = new StringBuilder();
    
    // Business rule: Must have at least one of each
    password.append(uppercase.charAt(random.nextInt(uppercase.length())));
    password.append(lowercase.charAt(random.nextInt(lowercase.length())));
    password.append(digits.charAt(random.nextInt(digits.length())));
    password.append(special.charAt(random.nextInt(special.length())));
    
    // Fill to 12 characters
    for (int i = 4; i < 12; i++) {
        password.append(allChars.charAt(random.nextInt(allChars.length())));
    }
}
```

## Framework Integration

### Lifecycle Management

```java
@Override
protected void initialize() {
    // Bootstrap: Load domain objects
    List<Credential> data = context.getData();
    vault = data != null ? new ArrayList<>(data) : new ArrayList<>();
}

@Override
protected void run() {
    // Execute: Domain operations loop
    while (running) {
        displayMenu();
        handleUserInput();
    }
}

@Override
protected void shutdown() {
    // Persist: Save domain state
    context.setData(vault);
}
```

### Persistence

```java
// Export vault
context.exportData("json");

// Import vault
context.importData("json");
List<Credential> data = context.getData();
```

## Testing the Application

### Workflow Example

```bash
$ mvn exec:java

=== Secure Password Vault ===
Domain-Driven Password Management
New vault initialized

SECURE VAULT | Entries: 0
Choose operation: 1

Service name: GitHub
Username/Email: jorgev@example.com
Password: SecurePass123!
Category (EMAIL/SOCIAL/BANKING/WORK/GENERAL): WORK
Credential stored successfully!
   [Work] GitHub - jorgev@example.com

Choose operation: 2

=== VAULT CONTENTS ===
1. [Work] GitHub - jorgev@example.com
   Password: ••••••••

Choose operation: 3

Search service: github

=== SEARCH RESULTS ===
[Work] GitHub - jorgev@example.com
   Password: SecurePass123!
```

## DDD Patterns Applied

### 1. **Entity Pattern**
- `Credential` has identity (service + username)
- Mutable state (can change password)
- Lifecycle managed by aggregate

### 2. **Value Object Pattern**
- `Category` enum is immutable
- No identity, just value
- Type-safe

### 3. **Repository Pattern**
- `vault` (List) acts as in-memory repository
- CRUD operations: store, retrieve, remove
- Framework context provides persistence

### 4. **Domain Service**
- Password generation is a domain service
- Not part of any single entity
- Stateless operation

### 5. **Ubiquitous Language**
- Same terms in code, docs, and conversation
- "Vault", "Credential", "Store", "Retrieve"

## Comparison: Simple vs DDD

### Simple Approach (Counter-like)
```java
public class PasswordState {
    private String service;
    private String password;
}

public class PasswordVaultApp {
    private List<PasswordState> passwords;
    
    private void addPassword() {
        passwords.add(new PasswordState(...));
    }
}
```

### DDD Approach (This Example)
```java
public class Credential {
    private String service;
    private Category category;
    
    public String mask() { /* domain logic */ }
    public boolean matchesService(String term) { /* domain query */ }
}

public class PasswordVaultApp {
    private List<Credential> vault;
    
    private void storeCredential() {
        Credential cred = new Credential(...);
        vault.add(cred);
    }
}
```

**Benefits of DDD:**
- Domain logic in domain objects
- Type-safe with enums
- Clear business rules
- Domain-specific language
- Easier to extend
- Better testability

## Architecture Alignment

This example aligns with JMiniApp's architecture:

### **Framework Layer** (Hot Spots)
- `JMiniApp` lifecycle
- `JMiniAppContext` for data
- `JSONAdapter` for persistence

### **Domain Layer** (Your Code)
- `Credential` entity
- `Category` value object
- Business rules
- Domain operations

### **Application Layer**
- `PasswordVaultApp` coordinates
- User interaction
- Operation dispatch

## Extending with DDD

### Add Password History (Entity)

```java
public class PasswordHistory {
    private LocalDateTime changedAt;
    private String oldPassword;
    
    public PasswordHistory(String password) {
        this.oldPassword = password;
        this.changedAt = LocalDateTime.now();
    }
}

public class Credential {
    private List<PasswordHistory> history = new ArrayList<>();
    
    public void changePassword(String newPassword) {
        history.add(new PasswordHistory(this.password));
        this.password = newPassword;
    }
}
```

### Add Password Strength (Value Object)

```java
public enum PasswordStrength {
    WEAK, MEDIUM, STRONG;
    
    public static PasswordStrength evaluate(String password) {
        int score = 0;
        if (password.length() >= 8) score++;
        if (password.matches(".*[A-Z].*")) score++;
        if (password.matches(".*[a-z].*")) score++;
        if (password.matches(".*\\d.*")) score++;
        if (password.matches(".*[!@#$%].*")) score++;
        
        if (score >= 4) return STRONG;
        if (score >= 2) return MEDIUM;
        return WEAK;
    }
}

public class Credential {
    public PasswordStrength getStrength() {
        return PasswordStrength.evaluate(password);
    }
}
```

### Add Vault Statistics (Domain Service)

```java
public class VaultStatistics {
    public static Map<Category, Long> countByCategory(List<Credential> vault) {
        return vault.stream()
            .collect(Collectors.groupingBy(
                Credential::getCategory,
                Collectors.counting()
            ));
    }
    
    public static long countWeak(List<Credential> vault) {
        return vault.stream()
            .filter(c -> c.getStrength() == PasswordStrength.WEAK)
            .count();
    }
}
```

## Key Takeaways

1. **Domain Model First**: Design entities and value objects before implementation
2. **Ubiquitous Language**: Use domain terms consistently in code
3. **Business Rules**: Encode them in domain objects, not just validation
4. **Type Safety**: Use enums instead of strings for fixed sets
5. **Domain Operations**: Methods like `mask()`, `reveal()` express domain concepts
6. **Separation**: Domain logic separate from framework and UI

## Common Pitfalls

### **Anemic Domain Model**
```java
public class Credential {
    private String service;
    // Only getters and setters, no behavior
}
```

### **Rich Domain Model**
```java
public class Credential {
    private String service;
    
    // Domain behavior
    public String mask() { ... }
    public boolean matchesService(String term) { ... }
}
```

### **Generic Language**
```java
list.add(entry);
getData();
```

### **Ubiquitous Language**
```java
vault.add(credential);
retrieveAllCredentials();
```

## Resources

- [Domain-Driven Design Book](https://www.domainlanguage.com/ddd/)
- [JMiniApp Core API](../guides/jminiapp.md)
- [Lifecycle Guide](../guides/lifecycle.md)
- [Counter Example](./counter.md)

---

This example demonstrates how to apply Domain-Driven Design principles in a JMiniApp application, creating a rich domain model with clear business logic, type safety, and ubiquitous language.

**Author:** Esteban Canto  
**GitHub:** EstebanCanVaz  
**Institution:** Universidad Autónoma de Yucatán  
**Date:** December 2025

