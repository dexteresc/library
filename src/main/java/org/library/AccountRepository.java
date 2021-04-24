package org.library;

public class AccountRepository extends Repository<Account> {

    public AccountRepository(Database database) {
        super("account", database);
    }

    public Account getByID(int id) throws Exception {
        return super.getByID(id, rs -> new Account(
                rs.getString("givenName"),
                rs.getString("familyName"),
                rs.getString("email")
        ));
    }
}
