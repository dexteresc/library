package org.example;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.library.Article;
import org.library.Book;
import org.library.LibraryOverseer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class PrimaryController {
    public Label homeButton;
    public Button registerButton;
    public Button loginButton;
    public ListView<String> categoriesView;
    public VBox libView;
    public StackPane libStackPane;
    public BorderPane mainPane;

    /**
     * Switches scene to login
     *
     * @throws IOException if fxml doesn't exist in resources
     */
    @FXML
    public void switchToLogin() throws IOException {
        App.setRoot("login");
    }

    public void initialize() {
        for (Article article : allArticles()) {
            BorderPane borderPane = new BorderPane();
            Label label = new Label(article.getTitle());
            Button button = new Button("Låna");
            borderPane.setLeft(label);
            BorderPane.setAlignment(label, Pos.CENTER_LEFT);
            borderPane.setRight(button);
            libView.getChildren().add(borderPane);


        }
    }
    // Lite hjälp med detta error. Kan inte ta bort module-info.java utan error :)
    // Går annars att lösa genom att dra in allt under example (?)

    /**
     * Get all articles in the library
     *
     * @return ArrayList of articles
     */
    public ArrayList<Article> allArticles() {
        ArrayList<Article> articles = new ArrayList<>();
        Connection conn = LibraryOverseer.createDBConnection();

        try {
            assert conn != null;
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("select * from artikel");
            while (rs.next()) {
                int id = rs.getInt("artikelID");
                String title = rs.getString("titel");
                int year = rs.getInt("ar");
                String isbn = rs.getString("ISBN");
                String[] authors = {"Not yet"};
                Double physical_location = rs.getDouble("fysiskPlats");
                int inStock = rs.getInt("antal");

                Book book = new Book(id, title, year, isbn, authors, physical_location, inStock);
                articles.add(book);
            }
        } catch (Exception e) {
            System.out.println("Something went wrong in allArticles()");
        }
        return articles;
    }
}
