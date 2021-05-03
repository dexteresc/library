package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.library.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class PrimaryController {
    public Button registerButton;
    public Button loginButton;
    public ListView<String> categoriesView;
    public VBox libView;
    public StackPane libStackPane;
    public BorderPane mainPane;
    public TextField searchBar;
    public Button searchButton;
    public BorderPane headerButtonBox;


   // private Connection connection;
    private AuthenticationModel authenticationModel;
    private ArticleRepository articleRepository = new ArticleRepository(new Database());

    /**
     * Switches scene to login
     *
     * @throws IOException if fxml doesn't exist in resources
     */
    @FXML
    public void switchToLogin() throws IOException, SQLException {
        App.setRoot("login");
    }

    public void initialize() {
      //  connection = LibraryOverseer.createDBConnection(); // Create db connection

        promptSearchDecor();
        // Configure authentication model
        if (this.authenticationModel == null) {
            this.authenticationModel = App.getAuthenticationModel();
        }

        if (this.authenticationModel.isAuthenticated()) {
            headerButtonBox.getChildren().clear();
            Button myPage = new Button("Mina sidor");
            headerButtonBox.setCenter(myPage);
        }

        // Load Categories
        /*
        ObservableList<String> items = FXCollections.observableArrayList(LibraryOverseer.getGenres(connection));
        categoriesView.setItems(items);
        categoriesView.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            libView.getChildren().clear();
            ArrayList<Article> articles = LibraryOverseer.selectGenre(t1, connection);
            System.out.println(articles);
            if (!articles.isEmpty()) {
                for (Article article :
                        articles) {
                    libModuleCreate(article);
                }
            } else {
                promptSearchDecor("No articles in " + categoriesView.getSelectionModel().selectedItemProperty().getValue());
            }
        });

         */
    }

    @FXML
    public void searchResult() throws Exception {
        libView.getChildren().clear();
        if (!(searchBar.textProperty().getValue().strip().equals(""))) {
            ArrayList<Article> articles = articleRepository.articleSearch(searchBar.textProperty().getValue().toLowerCase().strip());
            if (articles.isEmpty()) {
                promptSearchDecor();
            } else {
                for (Article article : articles) {
                    libModuleCreate(article);
                }
            }
        } else {
            promptSearchDecor();
        }
    }

    private void libModuleCreate(Article article) { // should find a better way to solve this.
        BorderPane borderPane = new BorderPane();
        Label title = new Label(article.getTitle());
        title.getStyleClass().add("titleLabel");
        Button borrowButton = new Button("Låna");

        if (article instanceof Book) {
            String[] authors = ((Book) article).getAuthors().toArray(new String[0]); // FIX
            String authorString = Arrays.toString(authors);
            authorString = authorString.replaceAll("\\[", "").replaceAll("]", "");
            Label authorLabel = new Label(authorString);
            authorLabel.getStyleClass().add("authorLabel");
            String inStock = "Antal kvar: " + ((Book) article).getInStock();
            Label inStockLabel = new Label(inStock);
            VBox leftVBox = new VBox();
            VBox rightVBox = new VBox();
            leftVBox.getChildren().add(title);
            leftVBox.getChildren().add(authorLabel);
            leftVBox.setAlignment(Pos.CENTER_LEFT);
            rightVBox.getChildren().add(inStockLabel);
            rightVBox.getChildren().add(borrowButton);
            rightVBox.setAlignment(Pos.CENTER_RIGHT);
            borderPane.setLeft(leftVBox);
            borderPane.setRight(rightVBox);
            libView.getChildren().add(borderPane);
        } else if (article instanceof AudioBook) {
            borderPane.setLeft(title);
            borderPane.setRight(borrowButton);
            libView.getChildren().add(borderPane);
        }
    }

    private void promptSearchDecor(String text) {
        BorderPane borderPane = new BorderPane();
        Label label = new Label(text);
        borderPane.setCenter(label);
        libView.getChildren().add(borderPane);
    }

    private void promptSearchDecor() {
        BorderPane borderPane = new BorderPane();
        Label label = new Label("Search through the library.");
        borderPane.setCenter(label);
        libView.getChildren().add(borderPane);
    }
}
