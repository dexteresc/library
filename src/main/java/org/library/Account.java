package org.library;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {

    private static final String LOGIN_STATEMENT = "SELECT * FROM account WHERE email = ? LIMIT 1";
    private static final String SET_PASSWORD_STATEMENT = "UPDATE account SET passwordHash = ? WHERE id = ? LIMIT 1";
    private static final String UPDATE_ACCOUNT_STATEMENT = "UPDATE account SET givenName = ?, familyName = ?, email = ? WHERE id = ? LIMIT 1";

    private static BCrypt.Hasher getHasher() {
        return BCrypt.with(BCrypt.Version.VERSION_2B);
    }

    private static BCrypt.Verifyer getVerifier() {
        return BCrypt.verifyer(BCrypt.Version.VERSION_2B);
    }

    private int id;
    private String givenName;
    private String familyName;
    private String email;

    /**
     * Create a new account instance.
     */
    public Account(String givenName, String familyName, String email) {
        this.givenName = givenName;
        this.familyName = familyName;
        this.email = email;
    }

    /**
     * Attempts to authenticate an account.
     * @param email Email of the account.
     * @param password Password of the account.
     * @param connection A valid database connection.
     * @exception Exception thrown if an invalid username or password is provided.
     */
    public static Account attemptLogin(String email, String password, Connection connection) throws Exception {
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

        if (resultSet.next() == false) {
            // User not found
            throw new Exception("Invalid username or password.");
        }

        String passwordHash;
        try {
            passwordHash = resultSet.getString("passwordHash");
        } catch (SQLException e) {
            throw new Exception("Invalid username or password.");
        }

        BCrypt.Result result = getVerifier().verify(password.toCharArray(), passwordHash);
        if (result.verified) {
            return new Account(resultSet);
        }

        throw new Exception("Invalid username or password.");
    }

    /**
     * Create a new account instance using a result set.
     * @param resultSet Used to initialize account fields.
     */
    private Account(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.email = resultSet.getString("email");
        this.givenName = resultSet.getString("givenName");
        this.familyName = resultSet.getString("familyName");
    }

    /**
     * Set password for account.
     * @param password The new password.
     * @param connection A valid database connection.
     * @return Whether or not the new password could be set.
     * @throws Exception
     */
    public boolean setPassword(String password, Connection connection) throws Exception {
        if (connection == null) {
            throw new Exception("Failed to retrieve database connection.");
        }

        if (password.length() < 8) {
            throw new Exception("Password too short.");
        }

        BCrypt.Hasher hasher = getHasher();
        String passwordHash = hasher.hashToString(12, password.toCharArray());
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SET_PASSWORD_STATEMENT);
            preparedStatement.setString(1, passwordHash);
            preparedStatement.setString(2, this.id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new Exception("Failed to query database.");
        }
    }

    public boolean save(Connection connection) throws Exception {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ACCOUNT_STATEMENT);
            preparedStatement.setString(1, this.givenName);
            preparedStatement.setString(2, this.familyName);
            preparedStatement.setString(3, this.email);
            preparedStatement.setInt(4, this.id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new Exception("Failed to query database.");
        }
    }

}
