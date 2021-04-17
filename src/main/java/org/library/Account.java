package org.library;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {

    private String givenName;
    private String familyName;
    private String email;

    public Account() {}

    /**
     * Create a new account instance using a ResultSet
     * @param resultSet ResultSet used to initialize account fields.
     */
    public static Account fromResultSet(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.email = resultSet.getString("email");
        account.givenName = resultSet.getString("givenName");
        account.familyName = resultSet.getString("familyName");
        return account;
    }

}
