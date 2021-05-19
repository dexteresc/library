package org.example;

import org.library.*;
import org.library.account.AccountManager;
import org.library.account.AccountModel;
import org.library.loan.LoanManager;
import org.library.loan.LoanModel;
import org.library.media.MediaManager;
import org.library.search.SearchModel;
import org.library.security.AuthenticationModel;
import org.library.util.Database;

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
    private AccountModel accountModel;

    public AppModel(Database database) {
        this.database = database;

        // Managers
        this.accountManager = new AccountManager(this.database);
        this.mediaManager = new MediaManager(this.database);
        this.loanManager = new LoanManager(this.database);

        // Models
        this.authenticationModel = new AuthenticationModel(this.database, this.accountManager);
        this.searchModel = new SearchModel(this.database);
        this.loanModel = new LoanModel(this.loanManager, this.mediaManager);
        this.adminModel = new AdminModel();
        this.accountModel = new AccountModel(this.accountManager);
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

    public AccountModel getAccountModel() {
        return accountModel;
    }
}
