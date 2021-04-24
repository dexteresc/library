package org.library;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountManager {

    private static final String LOGIN_STATEMENT = "SELECT * FROM users WHERE email = ? LIMIT 1";
    private static final String SET_PASSWORD_STATEMENT = "UPDATE users SET passwordHash = ? WHERE email = ? LIMIT 1";

    private final BCrypt.Hasher hasher = BCrypt.with(BCrypt.Version.VERSION_2B);
    private final BCrypt.Verifyer verifier = BCrypt.verifyer(BCrypt.Version.VERSION_2B);

    private AccountRepository accountRepository;

    public AccountManager(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Attempts to authenticate an account.
     * @param email Email of the account.
     * @param password Password of the account.
     * @exception Exception thrown if an invalid username or password is provided.
     */
    public Account authenticate(String email, String password) throws Exception {
        Database database = accountRepository.getDatabase();
        Account account;

        try {
            account = database.query(
                    LOGIN_STATEMENT,
                    ps -> {
                        ps.setString(1, email);
                    },
                    rs -> {
                        // Check if the provided password is a match.
                        BCrypt.Result verificationResult = verifier.verify(
                                password.toCharArray(),
                                rs.getString("passwordHash")
                        );

                        if (!verificationResult.verified) {
                            // The password was not a match, or there was an error related to hash formatting.
                            // Might be worth checking if verification result returns an invalid format message for debugging.
                            throw new Exception();
                        }

                        // Initialize the requested account.
                        return new Account(
                                rs.getString("givenName"),
                                rs.getString("familyName"),
                                rs.getString("email")
                        );
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();

            // Throw new exception to mask the underlying cause.
            // This is to prevent exposing registered users through brute-force attacks.
            throw new Exception("Invalid username or password.");
        }

        return account;
    }

    /**
     * Set password for account.
     * @param account Account to set password for.
     * @param password The new password.
     * @return Whether or not the new password could be set.
     * @throws Exception
     */
    public boolean setPassword(Account account, String password) throws Exception {
        String passwordHash = hasher.hashToString(12, password.toCharArray());
        return accountRepository.getDatabase().query(
                SET_PASSWORD_STATEMENT,
                ps -> {
                    ps.setString(1, passwordHash);
                    ps.setString(2, account.getEmail());
                },
                rs -> rs.rowUpdated()
        );
    }


    /**
     * Create an account.
     * @param givenName account holder's given name.
     * @param familyName account holder's family name.
     * @param email account holder's email.
     * @param password account password.
     */
    public boolean createAccount(String givenName, String familyName, String email, String password) throws Exception {
        String passwordHash = hasher.hashToString(12, password.toCharArray());
        return this.accountRepository.create(givenName, familyName, email, passwordHash);
    }

    // TODO: Update account
    
}
