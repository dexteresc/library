package org.library;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Author extends Person {

    public Author(ResultSet resultSet) throws SQLException {
        super(resultSet);
    }

}
