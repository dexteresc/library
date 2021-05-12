package org.library;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
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
