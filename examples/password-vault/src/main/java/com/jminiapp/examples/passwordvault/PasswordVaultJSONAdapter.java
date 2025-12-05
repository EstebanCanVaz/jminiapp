package com.jminiapp.examples.passwordvault;

import com.jminiapp.core.adapters.JSONAdapter;

/**
 * Format adapter for Credential serialization and deserialization.
 *
 * This adapter enables the Password Vault to persist credentials
 * using JSON format through the JMiniApp framework.
 *
 * <p>Example JSON format:</p>
 * <pre>
 * [
 *   {
 *     "service": "Gmail",
 *     "username": "user@gmail.com",
 *     "password": "SecurePass123",
 *     "category": "EMAIL",
 *     "revealed": false
 *   }
 * ]
 * </pre>
 */
public class PasswordVaultJSONAdapter implements JSONAdapter<Credential> {

    @Override
    public Class<Credential> getstateClass() {
        return Credential.class;
    }
}

