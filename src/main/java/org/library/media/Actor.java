package org.library.media;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Actor extends Person {

    public Actor(ResultSet resultSet) throws SQLException {
        super(resultSet);
    }

}
