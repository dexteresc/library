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

    private static final String dbhost = "jdbc:mysql://localhost:3306/biblan3";
    private static final String username = "root";
    private static final String password = "";

    public Database() { }

    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(dbhost, username, password);
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
        Connection connection = this.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(statement);

        // Apply prepared statement configuration
        configuration.apply(preparedStatement);

        // Execute prepared statement
        ResultSet resultSet = preparedStatement.executeQuery();

        // Move result set cursor to the first item
        while (resultSet.isBeforeFirst()) {
            if (!resultSet.next()) {
                // No results
                throw new Exception("Query did not return any results.");
            }
        }

        // Transform result set into generic type T
        T value = transformation.materialize(resultSet);

        // Clean up
        if (!resultSet.isClosed()) {
            resultSet.close();
        }

        if (!preparedStatement.isClosed()) {
            preparedStatement.close();
        }

        if (!connection.isClosed()) {
            connection.close();
        }

        return value;
    }
}
