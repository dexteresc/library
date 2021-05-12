package org.library;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;

public class Database {
    private ComboPooledDataSource pooledDataSource;
    private static final Logger logger = LogManager.getLogger();

    private final String host;
    private final String user;
    private final String password;

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

    // Used when T can't be determined implicitly.
    public <T> EntityQuery<T> select(String statement, Class<T> _class) throws Exception {
        return new EntityQuery<>(statement, this.getConnection());
    }

    public <T> EntityQuery<T> select(String statement) throws Exception {
        return new EntityQuery<>(statement, this.getConnection());
    }

    public Query insert(String statement) throws Exception {
        return new Query(statement, this.getConnection());
    }

    public Query update(String statement) throws Exception {
        return new Query(statement, this.getConnection());
    }

    public Query delete(String statement) throws Exception {
        return new Query(statement, this.getConnection());
    }
}
