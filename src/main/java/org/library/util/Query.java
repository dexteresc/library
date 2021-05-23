package org.library.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * Database query wrapper.
 *
 * Wraps a database query that returns does not return anything (except for a generated value, if requested).
 */
public class Query {
    protected static final Logger logger = LogManager.getLogger();

    protected String statement;
    protected Connection connection;
    protected Configuration<PreparedStatement> configuration = preparedStatement -> {
    };
    private Boolean async = false;

    protected Query(String statement, Connection connection) {
        this.statement = statement;
        this.connection = connection;
    }

    protected void setAsync(Boolean async) {
        this.async = async;
    }

    protected Boolean isAsync() {
        return this.async;
    }

    /**
     * Explicitly configure query prepared statement.
     *
     * @param configuration Configuration lambda expression.
     */
    public Query configure(Configuration<PreparedStatement> configuration) {
        this.configuration = configuration;
        return this;
    }

    /**
     * Implicitly configure query prepared statement.
     *
     * @param parameters Parameters to pass to the prepared statement (in order).
     * @implNote Supports the following data types: string, long, integer, float (discouraged), double, boolean, localdate and null.
     */
    public Query configure(Object... parameters) throws Exception {
        this.configure(preparedStatement -> {
            for (int i = 0; i < parameters.length; i++) {
                Object parameter = parameters[i];
                int parameterIndex = i + 1;

                if (parameter == null) {
                    preparedStatement.setNull(parameterIndex, Types.NULL);
                    continue;
                }

                String parameterTypeName = parameter.getClass()
                        .getName()
                        .replace("java.lang.", "")
                        .replace("java.time.", "")
                        .toLowerCase();

                switch (parameterTypeName) {
                    case "string":
                        preparedStatement.setString(parameterIndex, (String) parameter);
                        break;
                    case "long":
                        preparedStatement.setLong(parameterIndex, (Long) parameter);
                        break;
                    case "integer":
                        preparedStatement.setInt(parameterIndex, (Integer) parameter);
                        break;
                    case "float":
                        preparedStatement.setFloat(parameterIndex, (Float) parameter);
                        break;
                    case "double":
                        preparedStatement.setDouble(parameterIndex, (Double) parameter);
                        break;
                    case "boolean":
                        preparedStatement.setBoolean(parameterIndex, (Boolean) parameter);
                        break;
                    case "localdate":
                        preparedStatement.setObject(parameterIndex, parameter);
                        break;
                    default:
                        throw new Exception("Invalid parameter for prepared statement with value of type: " + parameterTypeName);
                }
            }
        });
        return this;
    }

    /**
     * Executes the database query.
     *
     * @throws Exception if a general database error occurs.
     */
    public void execute() throws Exception {
        PreparedStatement preparedStatement = null;

        try {
            // Create prepared statement
            preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

            // Apply prepared statement configuration
            configuration.apply(preparedStatement);

            // Execute prepared statement
            preparedStatement.execute();
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

    /**
     * Executes the database query and returns a generated id.
     *
     * @throws Exception if a general database error occurs.
     * @implNote Used when query returns a generated id.
     */
    public Long executeQuery() throws Exception {
        PreparedStatement preparedStatement = null;

        try {
            // Create prepared statement
            preparedStatement = connection.prepareStatement(statement, new String[]{"id"});

            // Apply prepared statement configuration
            configuration.apply(preparedStatement);

            // Execute prepared statement
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                return resultSet.getLong(1);
            } else {
                throw new Exception("No generated key.");
            }
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

    /**
     * A lambda expression used to configure a reference-type (e.g. ResultSet).
     *
     * @param <Input> The reference type to configure.
     */
    public interface Configuration<Input> {
        void apply(Input input) throws Exception;
    }
}