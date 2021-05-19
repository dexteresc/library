package org.library.security;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.library.account.Account;
import org.library.account.AccountManager;
import org.library.account.Customer;
import org.library.account.Staff;
import org.library.util.Database;

/**
 * Keeps track of the application's authentication state.
 */
public class AuthenticationModel {
    private static final Logger logger = LogManager.getLogger();

    private static final String LOGIN_STATEMENT = "SELECT id, password_hash, EXISTS(SELECT * FROM customer WHERE account_id = id) AS is_customer, EXISTS(SELECT * FROM staff WHERE account_id = id) AS is_staff FROM account WHERE email = ? LIMIT 1";

    private final BCrypt.Hasher hasher = BCrypt.with(BCrypt.Version.VERSION_2B);
    private final BCrypt.Verifyer verifier = BCrypt.verifyer(BCrypt.Version.VERSION_2B);

    private Database database;
    private AccountManager accountManager;
    private Account account;

    /**
     * Creates a new instance of AuthenticationModel.
     * @param accountManager An instance of AccountManager for managing accounts.
     */
    public AuthenticationModel(Database database, AccountManager accountManager) {
        this.database = database;
        this.accountManager = accountManager;
    }

    /**
     * Attempt login with a given username and password.
     * @param email Username of the Account that is being logged into.
     * @param password Password of the Account that is being logged into.
     */
    public void login(String email, String password) throws Exception {
        logger.info("Logging in...");
        try {
            this.account = this.authenticate(email, password);
        } catch (Exception e) {
            e.printStackTrace();

            // Throw new exception to mask the underlying cause.
            // This is to prevent exposing registered users through brute-force attacks.
            throw new Exception("Invalid username or password.");
        }
    }

    private Account authenticate(String email, String password) throws Exception {
        return database.select(LOGIN_STATEMENT, Account.class)
                .configure(email)
                .fetch(resultSet -> {
                    // Check if the provided password is a match.
                    BCrypt.Result verificationResult = verifier.verify(
                            password.toCharArray(),
                            resultSet.getString("password_hash")
                    );

                    if (!verificationResult.verified) {
                        // The password was not a match, or there was an error related to hash formatting.
                        // Might be worth checking if verification result returns an invalid format message for debugging.
                        logger.error("Password could not be verified.");
                        throw new Exception();
                    }

                    logger.info("Account authentication successful.");

                    // Get the authenticated account.
                    Long accountId = resultSet.getLong("id");

                    if (resultSet.getBoolean("is_staff")) {
                        // Return staff account.
                        return this.accountManager.getStaffById(accountId);
                    } else if (resultSet.getBoolean("is_customer")) {
                        // Return customer account.
                        return this.accountManager.getCustomerById(accountId);
                    } else {
                        // Account is neither staff nor customer. This scenario should be avoided.
                        throw new Exception();
                    }
                });
    }

    /**
     * Logout the current Account.
     */
    public void logout() {
        logger.info("Logging out...");
        if (this.account == null) {
            logger.warn("No account currently signed in.");
        }
        this.account = null;
    }

    /**
     * @return The authenticated Account if authenticated, otherwise null.
     */
    public Account getAccount() {
        return this.account;
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

}
