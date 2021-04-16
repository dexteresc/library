package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.library.Article;
import org.library.LibraryOverseer;

import java.io.IOException;
import java.sql.Connection;
import java.util.Objects;

public class PrimaryController {
    public Label homeButton;
    public Button registerButton;
    public Button loginButton;
    public ListView<String> categoriesView;
    public VBox libView;
    public StackPane libStackPane;
    public BorderPane mainPane;
    public TextField searchBar;
    public Button searchButton;
    private Connection connection;

    /**
     * Switches scene to login
     *
     * @throws IOException if fxml doesn't exist in resources
     */
    @FXML
    public void switchToLogin() throws IOException {
        App.setRoot("login");
    }
    @FXML
    public void switchToRegister() throws IOException {
        App.setRoot("register");
    }

    public void initialize() {
        connection = LibraryOverseer.createDBConnection();
        for (Article article : LibraryOverseer.allArticles(connection)) {
            libModuleCreate(article);
        }
    }

    @FXML
    public void search() {
        libView.getChildren().clear();
        for (Article article : Objects.requireNonNull(LibraryOverseer.searchArticle(searchBar.textProperty().getValue().toLowerCase().strip(), connection))) {
            libModuleCreate(article);
        }
    }

    private void libModuleCreate(Article article) { // might be a better way to solve this.
        BorderPane borderPane = new BorderPane();
        Label label = new Label(article.getTitle());
        Button button = new Button("Låna");
        borderPane.setLeft(label);
        BorderPane.setAlignment(label, Pos.CENTER_LEFT);
        borderPane.setRight(button);
        libView.getChildren().add(borderPane);
    }


}
