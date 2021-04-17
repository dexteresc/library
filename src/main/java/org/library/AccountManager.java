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

    public AccountManager() {}

    private Connection getConnection() {
        return null;
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

        BCrypt.Result result = verifier.verify(password.toCharArray(), passwordHash);
        if (result.verified) {
            return new Account(resultSet);
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
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new Exception("Failed to query database.");
        }
    }
}