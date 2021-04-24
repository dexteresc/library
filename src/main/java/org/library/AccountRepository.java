package org.library;

public class AccountRepository extends Repository<Account> {

    public AccountRepository(Database database) {
        super("account", database);
    }

    /**
     * Get an account by id.
     * @param id Account id.
     * @return Account with the provided id.
     * @throws Exception If no account with the provided id exists.
     */
    public Account getByID(int id) throws Exception {
        return super.getByID(id, rs -> new Account(
                rs.getString("givenName"),
                rs.getString("familyName"),
                rs.getString("email")
        ));
    }

    /**
     * Attempts to create a new account.
     * @param givenName A valid given name.
     * @param familyName A valid family name.
     * @param email A valid email.
     * @param passwordHash A bcrypt password hash.
     * @return A boolean indicating if the operation was successful.
     * @throws Exception
     */
    public boolean create(String givenName, String familyName, String email, String passwordHash) throws Exception {
        return super.create(new String[]{"givenName", "familyName", "email", "passwordHash"}, configuration -> {
            configuration.setString(1, givenName);
            configuration.setString(2, familyName);
            configuration.setString(3, email);
            configuration.setString(4, passwordHash);
        });
    }

    // TODO: Get account by email

    // TODO: Get all accounts

}
