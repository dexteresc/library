package org.example;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextFlow;
import org.library.*;

import java.io.IOException;
import java.util.stream.Collectors;

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
    private String query;

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
        libView.getChildren().clear();
        promptSearchDecor();

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
       /* try {
            ObservableList<String> items = FXCollections.observableArrayList(articleRepository.getGenres());
            categoriesView.setItems(items);
            categoriesView.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
                libView.getChildren().clear();
                ArrayList<Article> articles = null;
                try {
                    articles = articleRepository.getGenreArticles(t1);
                    System.out.println(t1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(articles);
                assert articles != null;
                if (!articles.isEmpty()) {
                    for (Article article :
                            articles) {
                        libModuleCreate(article);
                    }
                } else {
                    promptSearchDecor("No articles in " + categoriesView.getSelectionModel().selectedItemProperty().getValue());
                }
            });} catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @FXML
    public void searchResult() {
        query = searchBar.textProperty().getValue().toLowerCase().strip();

        if (!(query.equals(""))) {
            searchModel.search(query);
        } else {
            libView.getChildren().clear();
            promptSearchDecor();
        }
    }

    private void updateSearchResults() {
        libView.getChildren().clear();

        if (searchResults.size() < 1) {
            promptSearchDecor();
        } else {
            for (Media media : searchResults) {
                libModuleCreate(media);
            }
        }
    }

    private void libModuleCreate(Media media) { // should find a better way to solve this.
        BorderPane borderPane = new BorderPane();
        Label title = new Label(media.getTitle());
        title.getStyleClass().add("titleLabel");
        Button borrowButton = new Button("Låna");
        // testing


        if (media instanceof Book) {
            String authors = ((Book) media).getAuthors().stream().map(author -> {
                return author.getGivenName() + " " + author.getFamilyName();
            }).collect(Collectors.joining(", "));
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

            borrowButton.setOnAction(actionEvent -> {
                libView.getChildren().clear();

                VBox mediaInformation = new VBox();
                Label mediaClassification = new Label(media.getClassification());
                Label mediaPublisher = new Label(((Book) media).getPublisher());
                Label descriptionLabel = new Label("Description");
                Label authorHeader = new Label();
                if (((Book) media).getAuthors().size() < 2) {
                    authorHeader.setText("Author");
                } else {
                    authorHeader.setText("Authors");
                }
                Label loanAuthorLabel = new Label(authors.replace(", ", "\n"));

                mediaInformation.getChildren().add(title); // Add title
                // Description
                descriptionLabel.getStyleClass().add("h1");
                mediaInformation.getChildren().add(new Label(media.getSummary()));

                // Authors

                authorHeader.getStyleClass().add("h1");
                mediaInformation.getChildren().add(authorHeader);
                mediaInformation.getChildren().add(loanAuthorLabel);

                mediaInformation.getChildren().add(mediaPublisher);
                mediaInformation.getChildren().add(mediaClassification);
                borderPane.setLeft(mediaInformation);
                Button loanButton = new Button("Borrow");

                borderPane.setRight();
                System.out.println("this ran");
                libView.getChildren().add(borderPane);
            });
        } else if (media instanceof AudioBook) {
            borderPane.setLeft(title);
            borderPane.setRight(borrowButton);
            libView.getChildren().add(borderPane);
        } else {
            promptSearchDecor();
        }

    }

    private EventHandler<ActionEvent> loanViewCreate(Media media) {
        mainPane.setCenter(null);
        mainPane.setLeft(null);

        return null;
    }

    private void promptSearchDecor() {
        BorderPane borderPane = new BorderPane();
        Label label = new Label("Search through the library.");
        borderPane.setCenter(label);
        libView.getChildren().add(borderPane);
    }

    private void promptSearchDecor(String text) {
        BorderPane borderPane = new BorderPane();
        Label label = new Label(text);
        borderPane.setCenter(label);
        libView.getChildren().add(borderPane);
    }
}
