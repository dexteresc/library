package org.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 */
public class LibraryOverseer { // Database currently incomplete. Might rename class as well

    private static final String dbhost = "jdbc:mysql://localhost:3306/biblan3";
    private static final String username = "root";
    private static final String password = test.getPwd();

    /**
     * @return Connection from variables in LibraryOverseer
     */
    public static Connection createDBConnection() {
        try {
            Connection connection = DriverManager.getConnection(
                    dbhost, username, password);
            System.out.println("Connected");
            return connection;
        } catch (SQLException e) {
            System.out.println("Cannot create database connection");
            e.printStackTrace();
            return null;
        }
    }
}

