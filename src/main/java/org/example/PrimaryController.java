package org.example;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
import java.util.stream.Collectors;

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

    private AuthenticationModel authenticationModel;
    private SearchModel searchModel;
    private ObservableList<Media> searchResults;

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

        // Configure authentication model
        if (this.authenticationModel == null) {
            this.authenticationModel = App.getAppModel().getAuthenticationModel();
        }

        // Configure search model
        if (this.searchModel == null) {
            this.searchModel = App.getAppModel().getSearchModel();

            this.searchResults = this.searchModel.getSearchResultsList();
            this.searchResults.addListener((ListChangeListener<Media>) change -> {
                this.updateSearchResults();
            });
        }

        if (this.authenticationModel.isAuthenticated()) {
            headerButtonBox.getChildren().clear();
            Button myPage = new Button("Mina sidor");
            headerButtonBox.setCenter(myPage);
        }

        // Load Categories
        // TODO: Implement
        updateSearchResults();
    }

    @FXML
    public void searchResult() {
        String query = searchBar.textProperty().getValue().toLowerCase().strip();

        if (!(query.equals(""))) {
            searchModel.search(query);
        } else {
            promptSearchDecor();
        }
    }

    private void updateSearchResults() {
        libView.getChildren().clear();

        if (searchResults.size() < 1) {
            promptSearchDecor();
        }

        for (Media media : searchResults) {
            libModuleCreate(media);
        }
    }

    private void libModuleCreate(Media media) { // should find a better way to solve this.
        BorderPane borderPane = new BorderPane();
        Label title = new Label(media.getTitle());
        title.getStyleClass().add("titleLabel");
        Button borrowButton = new Button("Låna");

        if (media instanceof Book) {
            String authors = ((Book) media).getAuthors().stream().map(author -> { return author.getGivenName() + " " + author.getFamilyName(); }).collect(Collectors.joining(", "));
            Label authorLabel = new Label(authors);
            authorLabel.getStyleClass().add("authorLabel");
            String inStock = "Antal kvar: 0";// + ((Book) media).getInStock();
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
        } else if (media instanceof AudioBook) {
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
