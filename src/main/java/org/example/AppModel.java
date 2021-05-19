package org.example;

import org.library.account.AccountManager;
import org.library.account.AccountModel;
import org.library.admin.AdminModel;
import org.library.loan.LoanManager;
import org.library.loan.LoanModel;
import org.library.media.BookManager;
import org.library.media.MediaItemManager;
import org.library.media.MovieManager;
import org.library.search.SearchModel;
import org.library.security.AuthenticationModel;
import org.library.util.Database;

public final class AppModel {

    private Database database;

    // Managers
    private AccountManager accountManager;
    private LoanManager loanManager;
    private BookManager bookManager;
    private MovieManager movieManager;
    private MediaItemManager mediaItemManager;

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
        this.loanManager = new LoanManager(this.database);
        this.bookManager = new BookManager(this.database);
        this.movieManager = new MovieManager(this.database);
        this.mediaItemManager = new MediaItemManager(this.database);

        // Models
        this.authenticationModel = new AuthenticationModel(this.database, this.accountManager);
        this.searchModel = new SearchModel(this.database);
        this.loanModel = new LoanModel(this.loanManager, this.mediaItemManager);
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
