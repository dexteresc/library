package org.library.account;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Account representing a user of the library system.
 *
 * @see Customer
 * @see Staff
 */
public abstract class Account {
    private Long id;
    private String givenName;
    private String familyName;
    private String email;
    private String phoneNumber;

    /** Creates a new, empty, account instance. */
    public Account() {}

    /** Creates a new account instance. */
    protected Account(
            Long id, String givenName, String familyName, String email, String phoneNumber) {
        this.id = id;
        this.givenName = givenName;
        this.familyName = familyName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Creates a new account instance from a result set.
     *
     * @param resultSet A ResultSet instance.
     * @throws SQLException if the ResultSet instance methods throw an exception.
     */
    public Account(ResultSet resultSet) throws SQLException {
        this(
                resultSet.getLong("id"),
                resultSet.getString("given_name"),
                resultSet.getString("family_name"),
                resultSet.getString("email"),
                resultSet.getString("phone"));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
