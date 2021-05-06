package org.library;

public class Customer extends Account {
    private CustomerType customerType;

    public Customer(Account account, CustomerType customerType) {
        super(account.getID(), account.getGivenName(), account.getFamilyName(), account.getEmail());

        this.customerType = customerType;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }
}
