package com.jminiapp.examples.passwordvault;

import com.jminiapp.core.engine.JMiniAppRunner;

/**
 * Bootstrap configuration for Password Vault application.
 * 
 * This class demonstrates the framework's bootstrap process,
 * registering the format adapter and launching the application.
 */
public class PasswordVaultAppRunner {
    public static void main(String[] args) {
        JMiniAppRunner
            .forApp(PasswordVaultApp.class)
            .withState(Credential.class)
            .withAdapters(new PasswordVaultJSONAdapter())
            .named("PasswordVault")
            .run(args);
    }
}

