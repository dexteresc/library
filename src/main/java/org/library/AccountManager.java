package org.library;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class AccountManager {

    // Statements
    private static final String LOGIN_STATEMENT = "SELECT id, password_hash, EXISTS(SELECT * FROM customer WHERE account_id = id) AS is_customer, EXISTS(SELECT * FROM staff WHERE account_id = id) AS is_staff FROM account WHERE email = ? LIMIT 1";
    private static final String SELECT_ACCOUNT_BY_ID_STATEMENT = "SELECT * FROM account WHERE id = ? LIMIT 1";
    private static final String SELECT_CUSTOMER_BY_ID_STATEMENT = "SELECT * FROM account INNER JOIN customer ON customer.account_id = account.id INNER JOIN customer_type ON customer_type.id = customer.customer_type_id WHERE account.id = ? LIMIT 1";
    private static final String SELECT_STAFF_BY_ID_STATEMENT = "SELECT * FROM account INNER JOIN staff ON staff.account_id = account.id WHERE account.id = ? LIMIT 1";
    private static final String UPDATE_ACCOUNT_PASSWORD_STATEMENT = "UPDATE account SET password_hash = ? WHERE email = ? LIMIT 1";
    private static final String UPDATE_ACCOUNT_STATEMENT = "UPDATE account SET given_name = ?, family_name = ?, email = ?, phone = ? WHERE id = ? LIMIT 1";
    private static final String CREATE_ACCOUNT_STATEMENT = "INSERT INTO account (given_name, family_name, email, phone, password_hash) VALUES (?, ?, ?, ?, ?)";

    private final BCrypt.Hasher hasher = BCrypt.with(BCrypt.Version.VERSION_2B);
    private final BCrypt.Verifyer verifier = BCrypt.verifyer(BCrypt.Version.VERSION_2B);

    private final Database database;

    public AccountManager(Database database) {
        this.database = database;
    }

    /**
     * Attempts to authenticate an account.
     * @param email Email of the account.
     * @param password Password of the account.
     * @exception Exception thrown if an invalid username or password is provided.
     */
    public Account authenticate(String email, String password) throws Exception {
        try {
            return database.select(LOGIN_STATEMENT, Account.class)
                    .configure(preparedStatement -> { preparedStatement.setString(1, email); })
                    .fetch(resultSet -> {
                        // Check if the provided password is a match.
                        BCrypt.Result verificationResult = verifier.verify(
                                password.toCharArray(),
                                resultSet.getString("password_hash")
                        );

                        if (!verificationResult.verified) {
                            // The password was not a match, or there was an error related to hash formatting.
                            // Might be worth checking if verification result returns an invalid format message for debugging.
                            throw new Exception();
                        }

                        // Get the authenticated account.
                        Long accountId = resultSet.getLong("id");

                        if (resultSet.getBoolean("is_staff")) {
                            // Return staff account.
                            return this.getStaffById(accountId);
                        } else if (resultSet.getBoolean("is_customer")) {
                            // Return customer account.
                            return this.getCustomerById(accountId);
                        } else {
                            // Account is neither staff nor customer. This scenario should be avoided.
                            return this.getAccountById(accountId);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();

            // Throw new exception to mask the underlying cause.
            // This is to prevent exposing registered users through brute-force attacks.
            throw new Exception("Invalid username or password.");
        }
    }

    /**
     * Update password for account.
     * @param account Account to set password for.
     * @param password The new password.
     */
    public void updateAccountPassword(Account account, String password) throws Exception {
        String passwordHash = hasher.hashToString(12, password.toCharArray());
        database.update(UPDATE_ACCOUNT_PASSWORD_STATEMENT)
                .configure(passwordHash, account.getEmail())
                .execute();
    }

    public Account getAccountById(Long id) throws Exception {
        return database.select(SELECT_ACCOUNT_BY_ID_STATEMENT, Account.class)
                .configure(id)
                .fetch(Account::new);
    }

    public Account getCustomerById(Long id) throws Exception {
        return database.select(SELECT_CUSTOMER_BY_ID_STATEMENT, Customer.class)
                .configure(id)
                .fetch(Customer::new);
    }

    public Account getStaffById(Long id) throws Exception {
        return database.select(SELECT_STAFF_BY_ID_STATEMENT, Staff.class)
                .configure(id)
                .fetch(Staff::new);
    }

    /**
     * Create an account.
     * @param givenName account holder's given name.
     * @param familyName account holder's family name.
     * @param email account holder's email.
     * @param password account password.
     */
    public void createAccount(String givenName, String familyName, String email, String phoneNumber, String password) throws Exception {
        String passwordHash = hasher.hashToString(12, password.toCharArray());
        database.insert(CREATE_ACCOUNT_STATEMENT)
                .configure(givenName, familyName, email, phoneNumber, passwordHash)
                .execute();
    }


    public void updateAccount(Account account) throws Exception {
        database.insert(UPDATE_ACCOUNT_STATEMENT)
                .configure(account.getGivenName(), account.getFamilyName(), account.getEmail(), account.getPhoneNumber())
                .execute();
    }
    
}
