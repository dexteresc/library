package org.library;

public class Customer extends Account {
    private CustomerType customerType;

    public Customer(Account account, CustomerType customerType) {
        super(account.getId(), account.getGivenName(), account.getFamilyName(), account.getEmail(), account.getPhoneNumber());

        this.customerType = customerType;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }
}
