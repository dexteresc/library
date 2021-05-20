package org.library.account;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Account representing a customer of the library system.
 *
 * @see Account
 */
public class Customer extends Account {
    private CustomerType customerType;
    private Long numberOfActiveLoans; // Pre-computed value

    /**
     * Creates a new, empty, customer instance.
     */
    public Customer() {
        super();
    }

    /**
     * Creates a new customer instance from a result set.
     *
     * @param resultSet A ResultSet instance.
     * @throws SQLException if the ResultSet instance methods throw an exception.
     */
    public Customer(ResultSet resultSet) throws SQLException {
        super(resultSet.getLong("id"), resultSet.getString("given_name"), resultSet.getString("family_name"), resultSet.getString("email"), resultSet.getString("phone"));

        this.customerType = new CustomerType(resultSet);
        this.numberOfActiveLoans = resultSet.getLong("active_loan_count");
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public Long getNumberOfActiveLoans() {
        return numberOfActiveLoans;
    }

    public void setNumberOfActiveLoans(Long numberOfActiveLoans) {
        this.numberOfActiveLoans = numberOfActiveLoans;
    }
}
