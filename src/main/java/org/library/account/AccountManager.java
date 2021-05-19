package org.library.account;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.library.util.Database;

import java.util.List;

public class AccountManager {
    private static final Logger logger = LogManager.getLogger();

    // Statements
    private static final String SELECT_ACCOUNT_BY_ID_STATEMENT = "SELECT * FROM account WHERE id = ? LIMIT 1";
    private static final String SELECT_CUSTOMER_BY_ID_STATEMENT = "SELECT *, (SELECT COUNT(*) FROM loan WHERE loan.customer_id = account.id AND returned_at IS NULL) AS active_loan_count FROM account INNER JOIN customer ON customer.account_id = account.id INNER JOIN customer_type ON customer_type.id = customer.customer_type_id WHERE account.id = ? LIMIT 1";
    private static final String SELECT_STAFF_BY_ID_STATEMENT = "SELECT * FROM account INNER JOIN staff ON staff.account_id = account.id WHERE account.id = ? LIMIT 1";
    private static final String UPDATE_ACCOUNT_PASSWORD_STATEMENT = "UPDATE account SET password_hash = ? WHERE id = ? LIMIT 1";
    private static final String UPDATE_ACCOUNT_STATEMENT = "UPDATE account SET given_name = ?, family_name = ?, email = ?, phone = ? WHERE id = ? LIMIT 1";
    private static final String CREATE_ACCOUNT_STATEMENT = "INSERT INTO account (given_name, family_name, email, phone, password_hash) VALUES (?, ?, ?, ?, ?)";
    private static final String CREATE_CUSTOMER_STATEMENT = "INSERT INTO customer (account_id, customer_type_id) VALUES (?, ?)";
    private static final String DELETE_ACCOUNT_BY_ID_STATEMENT = "DELETE FROM account WHERE id = ? LIMIT 1";
    private static final String DELETE_ACCOUNT_BY_EMAIL_STATEMENT = "DELETE FROM account WHERE email = ? LIMIT 1";
    private static final String SELECT_ALL_CUSTOMER_TYPES_STATEMENT = "SELECT * FROM customer_type";

    private final BCrypt.Hasher hasher = BCrypt.with(BCrypt.Version.VERSION_2B);
    private final BCrypt.Verifyer verifier = BCrypt.verifyer(BCrypt.Version.VERSION_2B);

    private final Database database;

    public AccountManager(Database database) {
        this.database = database;
    }

    /**
     * Update password for account.
     * @param account Account to set password for.
     * @param password The new password.
     */
    public void updateAccountPassword(Account account, String password) throws Exception {
        logger.info("Updating account password...");
        String passwordHash = hasher.hashToString(12, password.toCharArray());
        database.update(UPDATE_ACCOUNT_PASSWORD_STATEMENT)
                .configure(passwordHash, account.getId())
                .execute();
    }

    public Account getAccountById(Long id) throws Exception {
        logger.info("Getting account by id...");
        return database.select(SELECT_ACCOUNT_BY_ID_STATEMENT, Account.class)
                .configure(id)
                .fetch(Account::new);
    }

    public Customer getCustomerById(Long id) throws Exception {
        logger.info("Getting customer by id...");
        return database.select(SELECT_CUSTOMER_BY_ID_STATEMENT, Customer.class)
                .configure(id)
                .fetch(Customer::new);
    }

    public Staff getStaffById(Long id) throws Exception {
        logger.info("Getting staff by id...");
        return database.select(SELECT_STAFF_BY_ID_STATEMENT, Staff.class)
                .configure(id)
                .fetch(Staff::new);
    }

    /**
     * Create an account.
     * @param account Account to create.
     * @param password the requested account password.
     */
    private void createAccount(Account account, String password) throws Exception {
        logger.info("Creating account...");
        String passwordHash = hasher.hashToString(12, password.toCharArray());
        Long id = database.insert(CREATE_ACCOUNT_STATEMENT)
                .configure(account.getGivenName(), account.getFamilyName(), account.getEmail(), account.getPhoneNumber(), passwordHash)
                .executeQuery();
        account.setId(id);
    }

    public void createCustomerAccount(Customer customer, String password) throws Exception {
        logger.info("Creating customer account...");
        this.createAccount(customer, password);

        if (customer.getCustomerType() == null) {
            logger.warn("Customer type is null, defaulting to customer type 1.");
            customer.setCustomerType(new CustomerType((long) 1, "", 0));
        }

        this.database.insert(CREATE_CUSTOMER_STATEMENT)
                .configure(customer.getId(), customer.getCustomerType().getId())
                .execute();
    }

    public void updateAccount(Account account) throws Exception {
        logger.info("Updating account...");
        database.update(UPDATE_ACCOUNT_STATEMENT)
                .configure(account.getGivenName(), account.getFamilyName(), account.getEmail(), account.getPhoneNumber(), account.getId())
                .execute();
    }

    public List<CustomerType> getCustomerTypes() throws Exception {
        return database.select(SELECT_ALL_CUSTOMER_TYPES_STATEMENT, CustomerType.class)
                .fetchAll(CustomerType::new);
    }

    public void deleteAccount(Account account) throws Exception {
        logger.info("Deleting account...");
        database.delete(DELETE_ACCOUNT_BY_ID_STATEMENT)
                .configure(account.getId())
                .execute();
    }

    public void deleteAccountByEmail(String email) throws Exception {
        logger.info("Deleting account by email...");
        database.delete(DELETE_ACCOUNT_BY_EMAIL_STATEMENT)
                .configure(email)
                .execute();
    }
}