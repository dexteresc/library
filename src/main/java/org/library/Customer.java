package org.library;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer extends Account {
    private CustomerType customerType;

    public Customer() {
        super();
    }

    public Customer(Account account, CustomerType customerType) {
        super(account.getId(), account.getGivenName(), account.getFamilyName(), account.getEmail(), account.getPhoneNumber());

        this.customerType = customerType;
    }

    public Customer(ResultSet resultSet) throws SQLException {
        super(resultSet.getLong("id"), resultSet.getString("given_name"), resultSet.getString("family_name"), resultSet.getString("email"), resultSet.getString("phone"));

        this.customerType = new CustomerType(resultSet);
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }
}
