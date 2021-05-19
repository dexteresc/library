package org.library.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EntityQuery<T> extends Query {
    /**
     * A lambda expression that transforms a value (commonly used as a method parameter).
     * @param <Input> The input type (e.g. ResultSet).
     * @param <Output> The output type (e.g. Account).
     */
    public interface Transformation<Input, Output> {
        /**
         * Transforms the provided input value into the output type.
         * @param input A value of the input type.
         * @return A value of the output type that was created using the provided input value.
         * @throws Exception If an exception is thrown during the transformation.
         */
        Output materialize(Input input) throws Exception;
    }

    public EntityQuery(String statement, Connection connection) {
        super(statement, connection);
    }

    @Override
    @Deprecated
    public void execute() throws Exception {
        logger.warn("execute() should not be called for EntityQuery.");
    }

    @Override
    public EntityQuery<T> configure(Configuration<PreparedStatement> configuration) {
        super.configure(configuration);
        return this;
    }

    @Override
    public EntityQuery<T> configure(Object ...parameters) throws Exception {
        super.configure(parameters);
        return this;
    }

    public T fetch(Transformation<ResultSet, T> transformation) throws Exception {
        if (!this.isAsync()) {
            logger.warn("Avoid running synchronous calls on the main thread. Use async methods if possible.");
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

    public List<T> fetchAll(Transformation<ResultSet, T> transformation) throws Exception {
        if (!this.isAsync()) {
            logger.warn("Avoid running synchronous calls on the main thread. Use async methods if possible.");
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

    public CompletableFuture<T> asyncFetch(Transformation<ResultSet, T> transformation) {
        this.setAsync(true);
        return  CompletableFuture.supplyAsync(() -> {
            try {
                return this.fetch(transformation);
            } catch (Exception exception) {
                exception.printStackTrace();
                return null;
            }
        });
    }

    public CompletableFuture<List<T>> asyncFetchAll(Transformation<ResultSet, T> transformation) {
        this.setAsync(true);
        return  CompletableFuture.supplyAsync(() -> {
            try {
                return this.fetchAll(transformation);
            } catch (Exception exception) {
                exception.printStackTrace();
                return List.of();
            }
        });
    }
}
