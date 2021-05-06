package org.library;

import java.sql.ResultSet;
import java.util.ArrayList;

public class AccountRepository extends Repository<Account> {

    public AccountRepository(Database database) {
        super("account", database);
    }

    // Create an account instance from a result set.
    private Account accountFrom(ResultSet resultSet) throws Exception {
        return new Account(
                resultSet.getInt("id"),
                resultSet.getString("given_name"),
                resultSet.getString("family_name"),
                resultSet.getString("email")
        );
    }

    /**
     * Get an account by id.
     * @param id Account id.
     * @return Account with the provided id.
     * @throws Exception If no account with the provided id exists.
     */
    public Account getByID(int id) throws Exception {
        return super.find("id", id, rs -> new Account(
                rs.getString("given_name"),
                rs.getString("family_name"),
                rs.getString("email")
        ));
    }

    /**
     * Get all accounts (paginated).
     * @param page page number to retrieve.
     * @param pageSize number of accounts to retrieve per page.
     * @return An array consisting of accounts for the given page.
     * @throws Exception
     */
    public ArrayList<Account> getAll(int page, int pageSize) throws Exception {
        return super.findAll("id", pageSize, page * pageSize, this::accountFrom);
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
        return super.create(new String[]{"given_name", "family_name", "email", "password_hash"}, configuration -> {
            configuration.setString(1, givenName);
            configuration.setString(2, familyName);
            configuration.setString(3, email);
            configuration.setString(4, passwordHash);
        });
    }

}
