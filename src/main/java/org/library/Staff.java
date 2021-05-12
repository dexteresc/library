package org.library;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Staff extends Account {
    private String role;

    public Staff(Account account, String role) {
        super(account.getId(), account.getGivenName(), account.getFamilyName(), account.getEmail(), account.getPhoneNumber());

        this.role = role;
    }

    public Staff(ResultSet resultSet) throws SQLException {
        super(resultSet.getLong("id"), resultSet.getString("given_name"), resultSet.getString("family_name"), resultSet.getString("email"), resultSet.getString("phone"));

        this.role = resultSet.getString("role");
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
