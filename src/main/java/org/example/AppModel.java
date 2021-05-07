package org.example;

import org.library.AccountManager;
import org.library.AuthenticationModel;
import org.library.Database;

public final class AppModel {

    private Database database;

    // Models
    private AuthenticationModel authenticationModel;

    public AppModel(Database database) {
        this.database = database;

        // Models
        this.authenticationModel = new AuthenticationModel(new AccountManager(this.database));
    }

    // Models
    public AuthenticationModel getAuthenticationModel() {
        return authenticationModel;
    }

}
