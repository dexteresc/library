package org.library;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountManager {

    private static final String LOGIN_STATEMENT = "SELECT * FROM users WHERE email = ? LIMIT 1";
    private static final String SET_PASSWORD_STATEMENT = "UPDATE users SET passwordHash = ? WHERE email = ? LIMIT 1";
    private static final String CREATE_ACCOUNT_STATEMENT = "INSERT INTO users (givenName, familyName, email) VALUES (?, ?, ?)";

    private final BCrypt.Hasher hasher = BCrypt.with(BCrypt.Version.VERSION_2B);
    private final BCrypt.Verifyer verifier = BCrypt.verifyer(BCrypt.Version.VERSION_2B);

    public AccountManager() {}

    private Connection getConnection() {
        return LibraryOverseer.createDBConnection();
    }

    /**
     * Attempts to authenticate an account.
     * @param email Email of the account.
     * @param password Password of the account.
     * @exception Exception thrown if an invalid username or password is provided.
     */
    public Account authenticate(String email, String password) throws Exception {
        Connection connection = this.getConnection();

        if (connection == null) {
            throw new Exception("Failed to retrieve database connection.");
        }

        ResultSet resultSet;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(LOGIN_STATEMENT);
            preparedStatement.setString(1, email);
            preparedStatement.closeOnCompletion();
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new Exception("Failed to query database.");
        } finally {
            if (!connection.isClosed()) {
                connection.close();
            }
        }

        if (resultSet.next() == false) {
            // User not found
            resultSet.close();
            throw new Exception("Invalid username or password.");
        }

        String passwordHash;
        try {
            passwordHash = resultSet.getString("passwordHash");
        } catch (SQLException e) {
            throw new Exception("Invalid username or password.");
        }

        BCrypt.Result result = verifier.verify(password.toCharArray(), passwordHash);
        if (result.verified) {
            return new Account(
                resultSet.getString("givenName"),
                resultSet.getString("familyName"),
                resultSet.getString("email")
            );
        }

        throw new Exception("Invalid username or password.");
    }

    /**
     * Set password for account.
     * @param account Account to set password for.
     * @param password The new password.
     * @return Whether or not the new password could be set.
     * @throws Exception
     */
    public boolean setPassword(Account account, String password) throws Exception {
        Connection connection = this.getConnection();

        if (connection == null) {
            throw new Exception("Failed to retrieve database connection.");
        }

        String passwordHash = hasher.hashToString(12, password.toCharArray());
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SET_PASSWORD_STATEMENT);
            preparedStatement.setString(1, passwordHash);
            preparedStatement.setString(2, account.getEmail());
            preparedStatement.closeOnCompletion();
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new Exception("Failed to query database.");
        } finally {
            if (!connection.isClosed()) {
                connection.close();
            }
        }
    }


    /**
     * Create an account.
     * @param givenName account holder's given name.
     * @param familyName account holder's family name.
     * @param email account holder's email.
     * @param password account password.
     */
    public Account createAccount(String givenName, String familyName, String email, String password) throws Exception {
        Connection connection = this.getConnection();

        if (connection == null) {
            throw new Exception("Failed to retrieve database connection.");
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_ACCOUNT_STATEMENT);
            preparedStatement.setString(1, givenName);
            preparedStatement.setString(2, familyName);
            preparedStatement.setString(3, email);
            preparedStatement.closeOnCompletion();
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new Exception("Failed to query database.");
        } finally {
            if (!connection.isClosed()) {
                connection.close();
            }
        }

        Account account = new Account(givenName, familyName, email);

        this.setPassword(account, password);

        return account;
    }

    // TODO: Get account by email

    // TODO: Get all accounts

    // TODO: Update account
    
}
