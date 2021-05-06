package org.library;

import java.sql.*;

public class Database {
    /**
     * A lambda expression used to configure a reference-type (e.g. ResultSet).
     * @param <Input> The reference type to configure.
     */
    public interface Configuration<Input> {
        void apply(Input input) throws Exception;
    }

    private final String host;
    private final String username;
    private final String password;

    public Database(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    private Connection getConnection() throws Exception {
        return DriverManager.getConnection(host, username, password);
    }

    /**
     * Executes a generic query against the database and returns an instance of the provided type (if possible).
     * @param statement A string containing a prepared statement.
     * @param configuration A lambda expression that configures the prepared statement.
     * @param transformation A lambda expression that is used to transform a result set into the expected output type T.
     * @param <T> The expected output type (e.g. Account).
     * @return An instance of the provided type T.
     * @throws Exception If there were no results, or if any database-related errors occur, or if the transformation throws an error.
     */
    public <T> T query(String statement, Configuration<PreparedStatement> configuration, Transformation<ResultSet, T> transformation) throws Exception {
        // Declare and set to null to ensure that cleanup can occur, even if an exception is thrown.
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            // Apply prepared statement configuration
            configuration.apply(preparedStatement);

            // Execute prepared statement
            resultSet = preparedStatement.executeQuery();

            // Move result set cursor to the first item
            while (resultSet.isBeforeFirst()) {
                if (!resultSet.next()) {
                    // No results
                    throw new Exception("Query did not return any results.");
                }
            }

            // Transform result set into generic type T and return
            return transformation.materialize(resultSet);
        } finally {
            // Clean up
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
            }

            if (preparedStatement != null && !preparedStatement.isClosed()) {
                preparedStatement.close();
            }

            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }

    /**
     * Executes a generic query against the database.
     * @param statement A string containing a prepared statement.
     * @param configuration A lambda expression that configures the prepared statement.
     * @return A boolean indicating whether or not the query was successful.
     * @throws Exception If there were no results, or if any database-related errors occur, or if the transformation throws an error.
     */
    public boolean perform(String statement, Configuration<PreparedStatement> configuration) throws Exception {
        // Declare and set to null to ensure that cleanup can occur, even if an exception is thrown.
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            // Apply prepared statement configuration
            configuration.apply(preparedStatement);

            // Execute prepared statement
            return preparedStatement.execute();
        } finally {
            // Clean up
            if (preparedStatement != null && !preparedStatement.isClosed()) {
                preparedStatement.close();
            }

            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }
}
