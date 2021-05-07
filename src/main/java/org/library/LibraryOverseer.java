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

    static ArrayList<Article> articles = new ArrayList<>();

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
    public static ArrayList<Article> allArticles(Connection connection) {

        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from artikel");
            createArticleObject(articles, rs);
            System.out.println(articles);
        } catch (Exception e) {
            System.out.println("Something went wrong in allArticles()");
        }

        return articles;
    }

    public static ArrayList<Article> searchArticle(String statement, Connection connection) {
        statement = "%" + statement + "%";
        ArrayList<Article> articleArrayList = new ArrayList<>();
        try {
            // TODO: Search author name without specifying
            PreparedStatement preparedStatement = connection.prepareStatement("select * from artikel where titel like ?");
            preparedStatement.setString(1, statement);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            createArticleObject(articleArrayList, resultSet);
            System.out.println(articleArrayList);
            articles = articleArrayList;
            return articles;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    private static void createArticleObject(ArrayList<Article> articleList, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            Long id = resultSet.getLong("artikelID");
            String title = resultSet.getString("titel");
            int year = resultSet.getInt("ar");
            String isbn = resultSet.getString("ISBN");
            String[] authors = {"Not yet", "Just Testing Bruv"};
            Double physical_location = resultSet.getDouble("fysiskPlats");
            int inStock = resultSet.getInt("antal");

            Book book = new Book(id, title, year, isbn, authors, physical_location, inStock);
            articleList.add(book);
        }
    } // Might not be necessary. Need help
}

