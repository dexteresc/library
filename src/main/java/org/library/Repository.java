package org.library;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Facilitates persistence management for entities.
 * @param <T> Entity to manage.
 */
public abstract class Repository<T> {

    private final Database database;

    /**
     * Creates a new instance of repository.
     * @param database A database instance used to submit queries to the database.
     */
    public Repository(Database database) {
        this.database = database;
    }

    /**
     * @apiNote Avoid using this whenever possible.
     * @return An instance of database.
     */
    public Database getDatabase() {
        return database;
    }

    public abstract T loadFrom(ResultSet resultSet) throws SQLException;

    public ArrayList<T> loadAllFrom(ResultSet resultSet) throws SQLException {
        ArrayList<T> items = new ArrayList<>();

        // Add the first item, as it will be missed by the loop due to the database query implementation.
        items.add(loadFrom(resultSet));

        // Collect entities from result set.
        while (resultSet.next()) {
            items.add(loadFrom(resultSet));
        }

        return items;
    }

    /**
     * Executes a generic query against the database and returns an instance of the provided type (if possible).
     * @param statement A string containing a prepared statement.
     * @param configuration A lambda expression that configures the prepared statement.
     * @param transformation A lambda expression that is used to transform a result set into the expected output type O.
     * @param <O> The expected output type (e.g. Account).
     * @return An instance of the provided type O.
     * @throws Exception If there were no results, or if any database-related errors occur, or if the transformation throws an error.
     */
    public <O> O query(String statement, Database.Configuration<PreparedStatement> configuration, Transformation<ResultSet, O> transformation) throws Exception {
        return database.query(statement, configuration, transformation);
    }

    /**
     * Executes a generic query against the database.
     * @param statement A string containing a prepared statement.
     * @param configuration A lambda expression that configures the prepared statement.
     * @return A boolean indicating whether or not the query was successful.
     * @throws Exception If there were no results, or if any database-related errors occur, or if the transformation throws an error.
     */
    public boolean perform(String statement, Database.Configuration<PreparedStatement> configuration) throws Exception {
        return database.perform(statement, configuration);
    }

}
