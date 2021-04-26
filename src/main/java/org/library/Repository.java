package org.library;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Facilitates persistence management for entities.
 * @param <T> Entity to manage.
 */
public abstract class Repository<T> {
    // Produces general prepared statement query-strings.
    static class Statements {
        // Creates a get by id statement for the provided table name.
        static String find(String table, String attribute) {
            return "SELECT * FROM " + table + " WHERE " + attribute + " = ? LIMIT 1";
        }

        static String findAll(String table, String condition, int limit, int offset, String[] orderBy) {
            return "SELECT * FROM " + table + " WHERE " + condition + " ORDER BY " + String.join(", ", orderBy) + " OFFSET " + offset + " ROWS" + " LIMIT " + limit;
        }

        // Creates an insert into statement for the provided table name and attributes.
        static String create(String table, String[] attributes) {
            String[] parameters = new String[attributes.length];
            for (int i = 0; i < attributes.length; i++) {
                parameters[i] = "?";
            }
            return "INSERT INTO " + table + " (" + String.join(", ", attributes) + ") VALUES (" + String.join(", ", parameters) + ")";
        }
    }

    private final Database database;
    private final String table;

    /**
     * Creates a new instance of repository.
     * @param table Table name associated with the underlying resource (e.g. ”account”).
     * @param database A database instance used to submit queries to the database.
     */
    public Repository(String table, Database database) {
        this.table = table;
        this.database = database;
    }

    /**
     * @apiNote Avoid using this whenever possible.
     * @return An instance of database.
     */
    public Database getDatabase() {
        return database;
    }

    /**
     * Get an entity with a matching attribute.
     * @param attribute Attribute to match.
     * @param value Value of attribute to match.
     * @param transformation A lambda expression for transforming a result set into the entity.
     * @return The entity with the provided attribute value.
     * @throws Exception If the entity was not found, the transformation failed, or a general database error occurred.
     */
    public T find(String attribute, String value, Transformation<ResultSet, T> transformation) throws Exception {
        String statement = Statements.find(table, attribute);
        return database.query(statement, preparedStatement -> preparedStatement.setString(1, value), transformation);
    }

    /**
     * Get an entity with a matching attribute.
     * @param attribute Attribute to match.
     * @param value Value of attribute to match.
     * @param transformation A lambda expression for transforming a result set into the entity.
     * @return The entity with the provided attribute value.
     * @throws Exception If the entity was not found, the transformation failed, or a general database error occurred.
     */
    public T find(String attribute, int value, Transformation<ResultSet, T> transformation) throws Exception {
        String statement = Statements.find(table, attribute);
        return database.query(statement, preparedStatement -> preparedStatement.setInt(1, value), transformation);
    }

    public ArrayList<T> findAll(String condition, String[] orderBy, int limit, int offset, Database.Configuration<PreparedStatement> configuration, Transformation<ResultSet, T> transformation) throws Exception {
        String statement = Statements.findAll(table, condition, limit, offset, orderBy);
        return database.query(statement, configuration, rs -> {
            ArrayList<T> items = new ArrayList<>();

            // Add the first item, as it will be missed by the loop due to the database query implementation.
            items.add(transformation.materialize(rs));

            // Collect entities from result set.
            while (rs.next()) {
                items.add(transformation.materialize(rs));
            }

            return items;
        });
    }


    /**
     * Attempt to create an entity.
     * @param attributes A string array of entity attributes.
     * @param configuration A lambda expression that configures the prepared statement.
     * @return A boolean indicating whether or not the statement was executed successfully.
     * @throws Exception
     */
    public boolean create(String[] attributes, Database.Configuration<PreparedStatement> configuration) throws Exception {
        String statement = Statements.create(table, attributes);
        return database.perform(statement, configuration);
    }
}
