package org.library;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccountRepository extends Repository<Account> {

    public AccountRepository(Database database) {
        super(database);
    }

    @Override
    public Account loadFrom(ResultSet resultSet) throws SQLException {
        return new Account(
                resultSet.getInt("id"),
                resultSet.getString("given_name"),
                resultSet.getString("family_name"),
                resultSet.getString("email")
        );
    }

}
