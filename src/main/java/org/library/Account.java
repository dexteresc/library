package org.library;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {

    private String givenName;
    private String familyName;
    private String email;

    /**
     * Create a new account instance.
     * @param givenName account holder's given name.
     * @param familyName account holder's family name.
     * @param email account holder's email.
     */
    public Account(String givenName, String familyName, String email) {
        this.givenName = givenName;
        this.familyName = familyName;
        this.email = email;
    }

    /**
     * Create a new account instance using a result set.
     * @param resultSet ResultSet used to initialize account fields.
     */
    public Account(ResultSet resultSet) throws SQLException {
        this.email = resultSet.getString("email");
        this.givenName = resultSet.getString("givenName");
        this.familyName = resultSet.getString("familyName");
    }

    public String getEmail() {
        return email;
    }

}
