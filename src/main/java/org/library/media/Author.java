package org.library.media;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Author representing an author of a book.
 */
public class Author extends Person {

    /**
     * Creates a new author instance from a result set.
     *
     * @param resultSet A ResultSet instance.
     * @throws SQLException if the ResultSet instance methods throw an exception.
     */
    public Author(ResultSet resultSet) throws SQLException {
        super(resultSet);
    }

}
