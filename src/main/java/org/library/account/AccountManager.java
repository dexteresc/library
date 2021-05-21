package org.library.account;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.library.util.Database;

import java.util.List;

/**
 * Manager for account types.
 */
public class AccountManager {
    private static final Logger logger = LogManager.getLogger();

    // Statements
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

    private final Database database;

    /**
     * Creates a new account manager instance.
     *
     * @param database A database instance.
     */
    public AccountManager(Database database) {
        this.database = database;
    }

    /**
     * Update password for account.
     *
     * @param account  Account to set password for.
     * @param password The new password.
     * @implNote This is a blocking operation.
     */
    public void updateAccountPassword(Account account, String password) throws Exception {
        logger.info("Updating account password...");
        String passwordHash = hasher.hashToString(12, password.toCharArray());
        database.update(UPDATE_ACCOUNT_PASSWORD_STATEMENT)
                .configure(passwordHash, account.getId())
                .execute();
    }

    /**
     * Get a customer by id.
     *
     * @return A customer instance (if one exists for the provided id)
     * @throws Exception if the customer was not found, or if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public Customer getCustomerById(Long id) throws Exception {
        logger.info("Getting customer by id " + id + "...");
        return database.select(SELECT_CUSTOMER_BY_ID_STATEMENT, Customer.class)
                .configure(id)
                .fetch(Customer::new);
    }

    /**
     * Get a staff member by id.
     *
     * @return A staff instance (if one exists for the provided id)
     * @throws Exception if the staff member was not found, or if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public Staff getStaffById(Long id) throws Exception {
        logger.info("Getting staff by id " + id + "...");
        return database.select(SELECT_STAFF_BY_ID_STATEMENT, Staff.class)
                .configure(id)
                .fetch(Staff::new);
    }

    /**
     * Creates an account.
     *
     * @param account  Account instance to create.
     * @param password Requested account password.
     * @throws Exception if the account violates unique constraints, or if a general database error occurs.
     * @implNote This is a blocking operation.
     * @implNote Modifies account instance by setting the generated id.
     */
    private void createAccount(Account account, String password) throws Exception {
        logger.info("Creating account...");
        String passwordHash = hasher.hashToString(12, password.toCharArray());
        Long id = database.insert(CREATE_ACCOUNT_STATEMENT)
                .configure(account.getGivenName(), account.getFamilyName(), account.getEmail(), account.getPhoneNumber(), passwordHash)
                .executeQuery();
        account.setId(id);
    }

    /**
     * Creates a customer account.
     *
     * @param customer A customer instance to create.
     * @param password A password to set for the customer account.
     * @throws Exception if the account violates unique constraints, or if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public void createCustomerAccount(Customer customer, String password) throws Exception {
        logger.info("Creating customer account...");
        this.createAccount(customer, password);

        if (customer.getCustomerType() == null) {
            logger.error("Customer type has not been set.");
            throw new Exception("Customer type has not been set.");
        }

        this.database.insert(CREATE_CUSTOMER_STATEMENT)
                .configure(customer.getId(), customer.getCustomerType().getId())
                .execute();
    }

    /**
     * Updates an existing account.
     *
     * @param account Account instance to update.
     * @throws Exception if the account instance violates database constraints, or if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public void updateAccount(Account account) throws Exception {
        logger.info("Updating account with id " + account.getId() + "...");
        database.update(UPDATE_ACCOUNT_STATEMENT)
                .configure(account.getGivenName(), account.getFamilyName(), account.getEmail(), account.getPhoneNumber(), account.getId())
                .execute();
    }

    /**
     * @return A list of customer types.
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public List<CustomerType> getCustomerTypes() throws Exception {
        logger.info("Getting customer types...");
        return database.select(SELECT_ALL_CUSTOMER_TYPES_STATEMENT, CustomerType.class)
                .fetchAll(CustomerType::new);
    }

    /**
     * Deletes an existing account.
     *
     * @param account Account instance to delete.
     * @throws Exception if the account cannot be found, or if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public void deleteAccount(Account account) throws Exception {
        logger.info("Deleting account with id " + account.getId() + "...");
        database.delete(DELETE_ACCOUNT_BY_ID_STATEMENT)
                .configure(account.getId())
                .execute();
    }
}
