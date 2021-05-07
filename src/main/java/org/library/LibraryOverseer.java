package org.library;

import java.sql.*;
import java.util.ArrayList;

/**
 *
 */
public class LibraryOverseer { // Database currently incomplete. Might rename class as well

    private static final String dbhost = "jdbc:mysql://localhost:3306/biblan3";
    private static final String username = "root";
    private static final String password = "kronberg";

    static ArrayList<Media> media = new ArrayList<>();

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

    /**
     * Get all articles in the library
     *
     * @return ArrayList of articles
     */
    public static ArrayList<Media> allArticles(Connection connection) {

        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from artikel");
            createArticleObject(media, rs);
            System.out.println(media);
        } catch (Exception e) {
            System.out.println("Something went wrong in allArticles()");
        }

        return media;
    }

    public static ArrayList<Media> searchArticle(String statement, Connection connection) {
        statement = "%" + statement + "%";
        ArrayList<Media> mediaArrayList = new ArrayList<>();
        try {
            // TODO: Search author name without specifying
            PreparedStatement preparedStatement = connection.prepareStatement("select * from artikel where titel like ?");
            preparedStatement.setString(1, statement);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            createArticleObject(mediaArrayList, resultSet);
            System.out.println(mediaArrayList);
            media = mediaArrayList;
            return media;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    private static void createArticleObject(ArrayList<Media> mediaList, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            Long id = resultSet.getLong("artikelID");
            String title = resultSet.getString("titel");
            int year = resultSet.getInt("ar");
            String isbn = resultSet.getString("ISBN");
            String[] authors = {"Not yet", "Just Testing Bruv"};
            Double physical_location = resultSet.getDouble("fysiskPlats");
            int inStock = resultSet.getInt("antal");

            Book book = new Book(id, title, year, isbn, authors, physical_location, inStock);
            mediaList.add(book);
        }
    } // Might not be necessary. Need help
}

