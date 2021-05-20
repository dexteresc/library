package org.library.media;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Actor representing an actor in a movie.
 *
 * @see org.library.media.Movie
 */
public class Actor extends Person {

    /**
     * Creates a new actor instance from a result set.
     *
     * @param resultSet A ResultSet instance.
     * @throws SQLException if the ResultSet instance methods throw an exception.
     */
    public Actor(ResultSet resultSet) throws SQLException {
        super(resultSet);
    }

}
