package org.library;


import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {

    private Long id;
    private String givenName;
    private String familyName;
    private String email;
    private String phoneNumber;

    public Account() {}

    /**
     * Create a new account instance with id.
     * @param id account id.
     * @param givenName account holder's given name.
     * @param familyName account holder's family name.
     * @param email account holder's email.
     */
    public Account(Long id, String givenName, String familyName, String email, String phoneNumber) {
        this.id = id;
        this.givenName = givenName;
        this.familyName = familyName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Account(ResultSet resultSet) throws SQLException {
        this(resultSet.getLong("id"), resultSet.getString("given_name"), resultSet.getString("family_name"), resultSet.getString("email"), resultSet.getString("phone"));
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
