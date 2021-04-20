package org.library;

/**
 * Keeps track of the application's authentication state.
 */
public class AuthenticationModel {

    private AccountManager accountManager;
    private Account account = null;

    /**
     * Creates a new instance of AuthenticationModel.
     * @param accountManager An instance of AccountManager for fetching and retrieving accounts.
     */
    public AuthenticationModel(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    /**
     * @return Whether or not an account is currently logged in.
     */
    public boolean isAuthenticated() {
        return this.account != null;
    }

    /**
     * Attempt login with a given username and password.
     * @param username Username of the Account that is being logged into.
     * @param password Password of the Account that is being logged into.
     */
    public void login(String username, String password) throws Exception {
        this.account = this.accountManager.authenticate(username, password);
    }

    /**
     * Logout the current Account.
     */
    public boolean logout() {
        this.account = null;
        return true;
    }

    /**
     * @return The authenticated Account if authenticated, otherwise null.
     */
    public Account getAccount() {
        return this.account;
    }

    /**
     * @return An instance of AccountManager.
     */
    public AccountManager getAccountManager() {
        return this.accountManager;
    }
}