package org.library;

import java.sql.ResultSet;

public abstract class Repository<T> {
    // Produces general prepared statement query-strings.
    static class Statements {
        // Creates a get by id statement for the provided table name.
        static String getByID(String table) {
            return "SELECT * FROM " + table + " WHERE id = ? LIMIT 1";
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
     * Get an entity by it's ID.
     * @param id ID corresponding to an entity.
     * @param transformation A lambda expression for transforming a result set into the entity.
     * @return The entity with the provided ID.
     * @throws Exception If the entity was not found, the transformation failed, or a general database error occurred.
     */
    public T getByID(int id, Transformation<ResultSet, T> transformation) throws Exception {
        String statement = Statements.getByID(table);
        return database.query(statement, preparedStatement -> {
            preparedStatement.setInt(1, id);
        }, transformation);
    }
}
