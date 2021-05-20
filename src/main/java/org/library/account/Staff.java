package org.library.account;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Account representing a staff of the library system.
 *
 * @see Account
 */
public class Staff extends Account {
    private String role;

    /**
     * Creates a new, empty, staff instance.
     */
    public Staff() {
        super();
    }

    /**
     * Creates a new staff instance from a result set.
     *
     * @param resultSet A ResultSet instance.
     * @throws SQLException if the ResultSet instance methods throw an exception.
     */
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
