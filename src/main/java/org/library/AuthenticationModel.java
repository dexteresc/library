package org.library;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Keeps track of the application's authentication state.
 */
public class AuthenticationModel {
    private static final Logger logger = LogManager.getLogger();

    private AccountManager accountManager;
    private Account account = null;

    /**
     * Creates a new instance of AuthenticationModel.
     * @param accountManager An instance of AccountManager for managing accounts.
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

    public boolean isCustomer() {
        return this.account instanceof Customer;
    }

    public boolean isStaff() {
        return this.account instanceof Staff;
    }

    /**
     * Attempt login with a given username and password.
     * @param username Username of the Account that is being logged into.
     * @param password Password of the Account that is being logged into.
     */
    public void login(String username, String password) throws Exception {
        logger.info("Logging in...");
        this.account = this.accountManager.authenticate(username, password);
    }

    /**
     * Logout the current Account.
     */
    public boolean logout() {
        logger.info("Logging out...");
        if (this.account == null) {
            logger.warn("No account currently signed in.");
        }
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
