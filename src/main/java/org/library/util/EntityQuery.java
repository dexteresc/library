package org.library.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Entity query wrapper.
 *
 * <p>Wraps a database query that returns one or more entities.
 *
 * @param <T> The entity type that should be returned.
 */
public class EntityQuery<T> extends Query {
    protected EntityQuery(String statement, Connection connection) {
        super(statement, connection);
    }

    @Override
    @Deprecated
    public void execute() throws Exception {
        logger.warn("execute() should not be called for EntityQuery.");
    }

    /**
     * Explicitly configure query prepared statement.
     *
     * @param configuration Configuration lambda expression.
     */
    @Override
    public EntityQuery<T> configure(Configuration<PreparedStatement> configuration) {
        super.configure(configuration);
        return this;
    }

    /**
     * Implicitly configure query prepared statement.
     *
     * @param parameters Parameters to pass to the prepared statement (in order).
     * @implNote Supports the following data types: string, long, integer, float (discouraged),
     *     double, boolean, localdate and null.
     */
    @Override
    public EntityQuery<T> configure(Object... parameters) throws Exception {
        super.configure(parameters);
        return this;
    }

    /**
     * Executes the database query and passes the result into the provided transformation.
     *
     * @param transformation Transformation that consumes a ResultSet to return the requested entity
     *     type T.
     * @return Entity of type T.
     * @throws Exception if the transformation throws an error, or if a general database error
     *     occurs.
     */
    public T fetch(Transformation<ResultSet, T> transformation) throws Exception {
        if (!this.isAsync()) {
            logger.warn(
                    "Avoid running synchronous calls on the main thread. Use async methods if possible.");
        }

        // Declare and set to null to ensure that cleanup can occur, even if an exception is thrown.
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
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
     * Executes the database query and passes the result into the provided transformation.
     *
     * @param transformation Transformation that consumes a ResultSet to return the requested entity
     *     type T.
     * @return A list of entities of type T.
     * @throws Exception if the transformation throws an error, or if a general database error
     *     occurs.
     */
    public List<T> fetchAll(Transformation<ResultSet, T> transformation) throws Exception {
        if (!this.isAsync()) {
            logger.warn(
                    "Avoid running synchronous calls on the main thread. Use async methods if possible.");
        }

        // Declare and set to null to ensure that cleanup can occur, even if an exception is thrown.
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(statement);

            // Apply prepared statement configuration
            configuration.apply(preparedStatement);

            // Execute prepared statement
            resultSet = preparedStatement.executeQuery();

            // Collect entities from result set.
            ArrayList<T> items = new ArrayList<>();

            while (resultSet.next()) {
                items.add(transformation.materialize(resultSet));
            }

            return items;
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
     * Executes the database query asynchronously.
     *
     * @return Completable future that wraps the fetch operation.
     * @see EntityQuery#fetch(Transformation)
     */
    public CompletableFuture<T> asyncFetch(Transformation<ResultSet, T> transformation) {
        this.setAsync(true);
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return this.fetch(transformation);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        return null;
                    }
                });
    }

    /**
     * Executes the database query asynchronously.
     *
     * @return Completable future that wraps the fetchAll operation.
     * @see EntityQuery#fetchAll(Transformation)
     */
    public CompletableFuture<List<T>> asyncFetchAll(Transformation<ResultSet, T> transformation) {
        this.setAsync(true);
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return this.fetchAll(transformation);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        return List.of();
                    }
                });
    }

    /**
     * A lambda expression that transforms a value (commonly used as a method parameter).
     *
     * @param <Input> The input type (e.g. ResultSet).
     * @param <Output> The output type (e.g. Account).
     */
    public interface Transformation<Input, Output> {
        /**
         * Transforms the provided input value into the output type.
         *
         * @param input A value of the input type.
         * @return A value of the output type that was created using the provided input value.
         * @throws Exception If an exception is thrown during the transformation.
         */
        Output materialize(Input input) throws Exception;
    }
}
