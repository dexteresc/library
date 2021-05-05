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
    private AuthenticationModel authenticationModel;

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
    public void switchToRegister() throws IOException              {
        App.setRoot("register");
    }

    public void initialize() {
        connection = LibraryOverseer.createDBConnection(); // Create db connection

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
        // TODO: Implement

    }

    @FXML
    public void searchResult() {
        libView.getChildren().clear();
        if (!(searchBar.textProperty().getValue().strip().equals(""))) {
            for (Article article : Objects.requireNonNull(LibraryOverseer.searchArticle(searchBar.textProperty().getValue().toLowerCase().strip(), connection))) {
                libModuleCreate(article);
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
            String[] authors = ((Book) article).getAuthors();
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
        } else {
            promptSearchDecor();
        }
    }

    private void promptSearchDecor() {
        BorderPane borderPane = new BorderPane();
        Label label = new Label("Search through the library.");
        borderPane.setCenter(label);
        libView.getChildren().add(borderPane);
    }
}
