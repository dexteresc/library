package org.library;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {

    private String givenName;
    private String familyName;
    private String email;

    public Account() {}

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
