package org.library;

import javafx.beans.value.ObservableValue;

import java.sql.*;
import java.util.ArrayList;

/**
 *
 */
public class LibraryOverseer { // Database currently incomplete. Might rename class as well

    private static final String dbhost = "jdbc:mysql://localhost:3306/biblan3";
    private static final String username = "root";
    private static final String password = test.getPwd();

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

    public static ArrayList<Article> searchArticle(String statement, Connection connection) {
        statement = "%" + statement + "%";
        ArrayList<Article> articleArrayList = new ArrayList<>();
        try {
            // TODO: Search author name without specifying
            PreparedStatement preparedStatement = connection.prepareStatement("select a.artikelID, namn, titel, ar, ISBN, fysiskPlats, antal\n" +
                    "from artikel_forfattare\n" +
                    "join artikel a on a.artikelID = artikel_forfattare.artikelID\n" +
                    "join forfattare f on f.forfattareID = artikel_forfattare.forfattareID\n" +
                    "where a.artikelID in (select a.artikelID\n" +
                    "                    from artikel_forfattare\n" +
                    "                        join artikel a on a.artikelID = artikel_forfattare.artikelID\n" +
                    "                        join forfattare f on artikel_forfattare.forfattareID = f.forfattareID\n" +
                    "                    where (titel like ? or namn like ?))");
            preparedStatement.setString(1, statement);
            preparedStatement.setString(2, statement);
            ResultSet resultSet = preparedStatement.executeQuery();
            createArticleObject(articleArrayList, resultSet);
            System.out.println(articleArrayList);
            articles = articleArrayList;
            return articles;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void createArticleObject(ArrayList<Article> articleList, ResultSet resultSet) throws SQLException {
        ArrayList<Integer> idList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("artikelID");
            if (idList.contains(id)) {
                for (Article article :
                        articleList) {
                    if (id == article.getId() && article instanceof Book) {
                        ((Book) article).getAuthors().add(resultSet.getString("namn"));
                    }
                }
            } else {
                ArrayList<String> authors = new ArrayList<>();
                authors.add(resultSet.getString("namn"));
                String title = resultSet.getString("titel");
                int year = resultSet.getInt("ar");
                String isbn = resultSet.getString("ISBN");
                Double physical_location = resultSet.getDouble("fysiskPlats");
                int inStock = resultSet.getInt("antal");
                idList.add(id);
                Book book = new Book(id, title, year, isbn, authors, physical_location, inStock);
                articleList.add(book);
            }

        }
        resultSet.close(); // idfk
    }

    public static ArrayList<String> getGenres(Connection connection) {
        ArrayList<String> genreList = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from genre");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                genreList.add(resultSet.getString("namn"));
            }
            return genreList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Article> selectGenre(String genreName, Connection connection) {
        System.out.println(genreName);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select a.artikelID, namn, titel, ar, ISBN, fysiskPlats, antal\n" +
                    "from artikel a\n" +
                    "join artikel_genre ag on a.artikelID = ag.artikelID\n" +
                    "join artikel_forfattare af on a.artikelID = af.artikelID\n" +
                    "join forfattare f on f.forfattareID = af.forfattareID\n" +
                    "where ag.genreID = (select genreID from genre where namn = ?)");
            preparedStatement.setString(1, genreName);
            ArrayList<Article> articleArrayList = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            createArticleObject(articleArrayList, resultSet);
            articles = articleArrayList;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return articles;
    }
}

