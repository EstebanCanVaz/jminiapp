# Password Vault Example

A domain-driven password manager application demonstrating the JMiniApp framework.

## Overview

This example implements a secure password vault using **Domain-Driven Design (DDD)** principles, showcasing how to model a business domain with:
- Clear domain objects (Credential, Category)
- Domain operations (store, retrieve, search, remove)
- Ubiquitous language (vault, credential, secure)
- Business rules and validation

## Domain Model

### **Domain Objects**
- `Credential`: Core entity representing a password entry
- `Category`: Value object for organizing credentials
- `Vault`: Collection of credentials (List<Credential>)

### **Domain Operations**
- `store()`: Add new credential to vault
- `retrieve()`: Get credentials from vault
- `search()`: Find credentials by service
- `remove()`: Delete credential from vault
- `generate()`: Create secure password
- `mask()/reveal()`: Hide/show passwords

### **Ubiquitous Language**
- **Vault** instead of "list" or "storage"
- **Credential** instead of "password entry"
- **Store** instead of "add"
- **Retrieve** instead of "list" or "get"
- **Secure** instead of "safe"

## Features

- **Store Credentials**: Add service credentials with username and password
- **Retrieve Vault**: View all stored credentials
- **Search**: Find credentials by service name
- **Filter by Category**: Organize by EMAIL, SOCIAL, BANKING, WORK, GENERAL
- **Remove**: Delete credential entries
- **Generate Passwords**: Create strong random passwords
- **Export/Import**: Save and load vault to/from JSON
- **Password Masking**: Hide passwords by default for security
- **Domain Validation**: Enforce business rules

## Project Structure

```
password-vault/
├── pom.xml
├── README.md
└── src/main/java/com/jminiapp/examples/passwordvault/
    ├── PasswordVaultApp.java          # Application Service
    ├── PasswordVaultAppRunner.java    # Bootstrap
    ├── Credential.java                # Domain Model
    ├── Category.java                  # Value Object
    └── PasswordVaultJSONAdapter.java  # Format Adapter
```

## Business Rules

1. **Service name is mandatory** - Cannot store credential without service
2. **Username is mandatory** - Every credential must have username
3. **Password is mandatory** - Cannot store empty password
4. **Default category** - If no category specified, defaults to GENERAL
5. **Password masking** - Passwords hidden by default (••••••••)
6. **Strong password generation** - Generated passwords include uppercase, lowercase, digits, and special chars

## Building and Running

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Build

From the project root:
```bash
mvn clean install
```

### Run

```bash
cd examples/password-vault
mvn exec:java
```

Or use the JAR:
```bash
java -jar target/password-vault-app.jar
```

## Usage Example

### Storing a Credential

```
SECURE VAULT | Entries: 0
1. Store new credential
2. Retrieve all credentials
...

Choose operation: 1

Service name: Gmail
Username/Email: jorgev@gmail.com
Password: MySecure123!
Category (EMAIL/SOCIAL/BANKING/WORK/GENERAL): EMAIL
Credential stored successfully!
   [Email] Gmail - jorgev@gmail.com
```

### Retrieving Credentials

```
Choose operation: 2

=== VAULT CONTENTS ===
1. [Email] Gmail - jorgev@gmail.com
   Password: ••••••••
2. [Social Media] Facebook - jorge.varguez
   Password: ••••••••
```

### Searching

```
Choose operation: 3

Search service: gmail

=== SEARCH RESULTS ===
[Email] Gmail - jorgev@gmail.com
   Password: MySecure123!
```

### Generating Password

```
Choose operation: 6

Secure password generated:
   K9#mX2@pL5$a
   (Copy and use when storing a credential)
```

## JSON Format

Exported vault (`PasswordVault.json`):
```json
[
  {
    "service": "Gmail",
    "username": "jorgev@gmail.com",
    "password": "MySecure123!",
    "category": "EMAIL",
    "revealed": false
  },
  {
    "service": "Facebook",
    "username": "jorge.varguez",
    "password": "fbPass456!",
    "category": "SOCIAL",
    "revealed": false
  }
]
```

## Domain-Driven Design Concepts

### **Entities**
- `Credential`: Has identity (service + username combination)

### **Value Objects**
- `Category`: Immutable enum representing credential types

### **Domain Services**
- Password generation logic
- Search and filter operations

### **Repository Pattern**
- Vault acts as in-memory repository
- Framework context provides persistence

### **Ubiquitous Language**
All code uses domain-specific terminology consistently

## Security Note

**EDUCATIONAL PURPOSE ONLY**

This is a teaching example demonstrating domain modeling and framework usage. Production password managers require:
- Encryption at rest (AES-256)
- Master password with key derivation (PBKDF2, bcrypt)
- Secure memory handling
- Protection against side-channel attacks
- Regular security audits

## Extending the Application

### Add Password Strength Validation
```java
public enum PasswordStrength {
    WEAK, MEDIUM, STRONG;
    
    public static PasswordStrength evaluate(String password) {
        // Evaluation logic
    }
}
```

### Add Credential Notes
```java
public class Credential {
    private String notes;
    
    public void addNote(String note) {
        this.notes = note;
    }
}
```

### Add Last Modified Timestamp
```java
import java.time.LocalDateTime;

public class Credential {
    private LocalDateTime lastModified;
    
    public void updatePassword(String newPassword) {
        this.password = newPassword;
        this.lastModified = LocalDateTime.now();
    }
}
```

## Comparison: DDD vs Simple Approach

| Aspect | Simple | DDD Approach |
|--------|---------|-------------|
| **Model Name** | PasswordState | Credential |
| **Operations** | add/list/delete | store/retrieve/remove |
| **Categories** | String | Enum (Category) |
| **Validation** | Scattered | Domain methods |
| **Language** | Generic | Domain-specific |
| **Business Rules** | In app | In model |

## Key Takeaways

1. **Domain Modeling**: Clear separation of domain logic from application logic
2. **Ubiquitous Language**: Consistent terminology throughout code
3. **Business Rules**: Encapsulated in domain objects
4. **Value Objects**: Category enum ensures type safety
5. **Domain Operations**: Methods like `mask()`, `reveal()` express domain concepts

---

**Author:** [Esteban Canto]  
**Email:** [ecantovaz@gmail.com]  
**GitHub:** [EstebanCanVaz]  
**Date:** December 2025  
**Course:** Software Architecture

