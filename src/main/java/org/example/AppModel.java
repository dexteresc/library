package org.example;

import org.library.*;

public final class AppModel {

    private Database database;

    // Managers
    private AccountManager accountManager;
    private MediaManager mediaManager;
    private LoanManager loanManager;

    // Models
    private AuthenticationModel authenticationModel;
    private SearchModel searchModel;
    private LoanModel loanModel;
    private AdminModel adminModel;

    public AppModel(Database database) {
        this.database = database;

        // Managers
        this.accountManager = new AccountManager(this.database);
        this.mediaManager = new MediaManager(this.database);
        this.loanManager = new LoanManager(this.database);

        // Models
        this.authenticationModel = new AuthenticationModel(this.accountManager);
        this.searchModel = new SearchModel(this.mediaManager);
        this.loanModel = new LoanModel(this.loanManager, this.mediaManager);
        this.adminModel = new AdminModel();
    }

    // Models
    public AuthenticationModel getAuthenticationModel() {
        return authenticationModel;
    }

    public SearchModel getSearchModel() {
        return searchModel;
    }

    public LoanModel getLoanModel() {
        return loanModel;
    }

    public AdminModel getAdminModel() {
        return adminModel;
    }
}
