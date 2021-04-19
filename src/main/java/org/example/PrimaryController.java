package org.example;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.library.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
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
    public BorderPane headerButtonBox;


    private Connection connection;
    Account account = App.getAccount();

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
        connection = LibraryOverseer.createDBConnection(); // Create db connection

        promptSearchDecor("Search through the library.");
        if (!(account == null)) {
            headerButtonBox.getChildren().clear();
            Button myPage = new Button("Mina sidor");
            headerButtonBox.setCenter(myPage);
        }

        // Load Categories
        // TODO: Implement

    }

    @FXML
    public void searchResult() {
        libView.getChildren().clear();
        String searchInput = searchBar.textProperty().getValue().strip();
        if (!(searchInput.equals(""))) {
            ArrayList<Article> searchArray = Objects.requireNonNull(LibraryOverseer.searchArticle(searchBar.textProperty().getValue().toLowerCase().strip(), connection));
            if (searchArray.isEmpty()) {
                promptSearchDecor("No result for: \"" + searchInput + "\"");
            } else {
                for (Article article : searchArray) {
                    libModuleCreate(article);
                }
            }
        } else {
            promptSearchDecor("Search through the library.");
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
}
