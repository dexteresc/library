package org.library;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationModel {

    private static final String LOGIN_STATEMENT = "SELECT * FROM users WHERE email = ? LIMIT 1";

    // The account that is currently authenticated.
    private Account account = null;

    /**
     * Creates a new AuthenticationModel instance.
     */
    public AuthenticationModel() {}

    /**
     * @return Authenticated account (if set, otherwise null).
     */
    public Account getAccount() {
        return this.account;
    }

    /**
     * Attempts to authenticate an account.
     * @param email Email of the account.
     * @param password Password of the account.
     * @exception Exception thrown if an invalid username or password is provided.
     */
    public boolean attemptLogin(String email, String password) throws Exception {
        Connection connection = this.getConnection();

        if (connection == null) {
            throw new Exception("Failed to retrieve database connection.");
        }

        ResultSet resultSet;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(LOGIN_STATEMENT);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new Exception("Failed to query database.");
        }

        String passwordHash;
        try {
             passwordHash = resultSet.getString("passwordHash");
        } catch (SQLException e) {
            throw new Exception("Invalid username or password.");
        }

        if (this.verify(password, passwordHash)) {
            this.account = Account.fromResultSet(resultSet);
            return true;
        }

        throw new Exception("Invalid username or password.");
    }

    /**
     * Attempt to sign out the authenticated account.
     */
    public void attemptLogout() {
        this.account = null;
    }

    // Get a valid database connection.
    private Connection getConnection() {
        return null;
    }

    /**
     * Checks if a password matches the stored password hash.
     * @param password The entered password.
     * @param passwordHash The stored password hash.
     * @return A boolean indicating whether or not the password could be verified.
     */
    private boolean verify(String password, String passwordHash) {
        BCrypt.Verifyer verifier = BCrypt.verifyer(BCrypt.Version.VERSION_2B);
        BCrypt.Result result = verifier.verify(password.toCharArray(), passwordHash);
        return result.verified;
    }

}
