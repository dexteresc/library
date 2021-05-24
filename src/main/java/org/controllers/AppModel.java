package org.controllers;

import org.library.account.AccountManager;
import org.library.account.AccountModel;
import org.library.admin.AdminModel;
import org.library.loan.LoanManager;
import org.library.loan.LoanModel;
import org.library.loan.ReturnsModel;
import org.library.media.BookManager;
import org.library.media.MediaItemManager;
import org.library.media.MovieManager;
import org.library.search.SearchModel;
import org.library.security.AuthenticationModel;
import org.library.util.Database;

public final class AppModel {

    private final Database database;

    // Managers
    private final AccountManager accountManager;
    private final LoanManager loanManager;
    private final BookManager bookManager;
    private final MovieManager movieManager;
    private final MediaItemManager mediaItemManager;

    // Models
    private final AuthenticationModel authenticationModel;
    private final SearchModel searchModel;
    private final LoanModel loanModel;
    private final AdminModel adminModel;
    private final AccountModel accountModel;
    private final ReturnsModel returnsModel;

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
        this.adminModel =
                new AdminModel(this.bookManager, this.movieManager, this.mediaItemManager);
        this.accountModel = new AccountModel(this.accountManager);
        this.returnsModel = new ReturnsModel(this.loanManager);
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

    public ReturnsModel getReturnsModel() {
        return returnsModel;
    }

    public LoanManager getLoanManager() {
        return loanManager;
    }

    public BookManager getBookManager() {
        return bookManager;
    }
}
