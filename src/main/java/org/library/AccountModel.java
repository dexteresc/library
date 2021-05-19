package org.library;

import java.util.List;

public class AccountModel {

    private AccountManager accountManager;

    public AccountModel(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public List<CustomerType> getCustomerTypes() throws Exception {
        return accountManager.getCustomerTypes();
    }

    public void createCustomerAccount(Customer customer, String password) throws Exception {
        this.accountManager.createCustomerAccount(customer, password);
    }

}
