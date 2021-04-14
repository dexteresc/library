package org.library;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class LibraryOverseer { // Database currently incomplete. Might rename class as well

    private static final String dbhost = "jdbc:mysql://localhost:3306/biblan3";
    private static final String username = "root";
    private static final String password = "root";
    private static Connection conn;

    public static Connection createDBConnection() {
        try {
            conn = DriverManager.getConnection(
                    dbhost, username, password);
            System.out.println("Connected");
            return conn;
        } catch (SQLException e) {
            System.out.println("Cannot create database connection");
            e.printStackTrace();
            return null;
        }
    }
}

