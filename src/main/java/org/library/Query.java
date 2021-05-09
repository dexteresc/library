package org.library;

import java.sql.*;

public class Query {
    /**
     * A lambda expression used to configure a reference-type (e.g. ResultSet).
     * @param <Input> The reference type to configure.
     */
    public interface Configuration<Input> {
        void apply(Input input) throws Exception;
    }

    protected String statement;
    protected Connection connection;

    protected Configuration<PreparedStatement> configuration;

    public Query(String statement, Connection connection) {
        this.statement = statement;
        this.connection = connection;
    }

    public Query configure(Configuration<PreparedStatement> configuration) {
        this.configuration = configuration;
        return this;
    }

    public Query configure(Object ...parameters) throws Exception {
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
                    case "string": preparedStatement.setString(parameterIndex, (String) parameter); break;
                    case "long":
                    case "integer": preparedStatement.setLong(parameterIndex, (Long) parameter); break;
                    case "float":
                    case "double": preparedStatement.setDouble(parameterIndex, (Double) parameter); break;
                    case "boolean": preparedStatement.setBoolean(parameterIndex, (Boolean) parameter); break;
                    case "localdate": preparedStatement.setObject(parameterIndex, parameter); break;
                    default: throw new Exception("Invalid parameter for prepared statement with value of type: " + parameterTypeName);
                }
            }
        });
        return this;
    }

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
}