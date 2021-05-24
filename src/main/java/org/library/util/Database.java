package org.library.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.sql.Connection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Database utility for managing database connection and building database queries.
 *
 * @see Query
 * @see EntityQuery
 */
public class Database {
    private static final Logger logger = LogManager.getLogger();

    private final String host;
    private final String user;
    private final String password;
    private ComboPooledDataSource pooledDataSource;

    /**
     * Creates a new database instance.
     *
     * @param host Hostname of the remote database server.
     * @param user Username for the remote database server.
     * @param password Password for authenticating the provided username with the remote server.
     */
    public Database(String host, String user, String password) {
        this.host = host;
        this.user = user;
        this.password = password;

        try {
            this.configureDataSource();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("Failed to configure database.");
        }
    }

    private void configureDataSource() throws Exception {
        this.pooledDataSource = new ComboPooledDataSource();
        this.pooledDataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
        this.pooledDataSource.setJdbcUrl(this.host);
        this.pooledDataSource.setUser(this.user);
        this.pooledDataSource.setPassword(this.password);
    }

    private Connection getConnection() throws Exception {
        return this.pooledDataSource.getConnection();
    }

    /**
     * Creates a new entity query builder instance for a select statement.
     *
     * @param statement Statement to pass to the new entity query builder instance.
     * @implNote Used when generic parameter T cannot be determined implicitly.
     */
    public <T> EntityQuery<T> select(String statement, Class<T> _class) throws Exception {
        logger.debug("Getting entity query for select statement.");
        return new EntityQuery<>(statement, this.getConnection());
    }

    /**
     * Creates a new entity query builder instance for a select statement.
     *
     * @param statement Statement to pass to the new entity query builder instance.
     */
    public <T> EntityQuery<T> select(String statement) throws Exception {
        logger.debug("Getting entity query for select statement.");
        return new EntityQuery<>(statement, this.getConnection());
    }

    /**
     * Creates a new query builder instance for an insert statement.
     *
     * @param statement Statement to pass to the new query builder instance.
     */
    public Query insert(String statement) throws Exception {
        logger.debug("Getting entity query for insert statement.");
        return new Query(statement, this.getConnection());
    }

    /**
     * Creates a new query builder instance for an update statement.
     *
     * @param statement Statement to pass to the new query builder instance.
     */
    public Query update(String statement) throws Exception {
        logger.debug("Getting entity query for update statement.");
        return new Query(statement, this.getConnection());
    }

    /**
     * Creates a new query builder instance for a delete statement.
     *
     * @param statement Statement to pass to the new query builder instance.
     */
    public Query delete(String statement) throws Exception {
        logger.debug("Getting entity query for delete statement.");
        return new Query(statement, this.getConnection());
    }
}
