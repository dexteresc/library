package org.library.account;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer extends Account {
    private CustomerType customerType;
    private Long numberOfActiveLoans;

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
        this.numberOfActiveLoans = resultSet.getLong("active_loan_count");
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public Long getNumberOfActiveLoans() {
        return numberOfActiveLoans;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public void setNumberOfActiveLoans(Long numberOfActiveLoans) {
        this.numberOfActiveLoans = numberOfActiveLoans;
    }
}
