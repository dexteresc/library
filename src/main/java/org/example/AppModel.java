package org.example;

import org.library.AccountManager;
import org.library.AccountRepository;
import org.library.AuthenticationModel;
import org.library.Database;

public final class AppModel {

    private Database database;

    // Models
    private AuthenticationModel authenticationModel;

    // Repositories
    private AccountRepository accountRepository;

    public AppModel(Database database) {
        this.database = database;

        // Repositories
        this.accountRepository = new AccountRepository(this.database);

        // Models
        this.authenticationModel = new AuthenticationModel(new AccountManager(this.accountRepository));
    }

    // Models
    public AuthenticationModel getAuthenticationModel() {
        return authenticationModel;
    }

    // Repositories
    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

}
