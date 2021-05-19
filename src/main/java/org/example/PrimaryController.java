package org.example;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.library.*;
import org.library.loan.LoanModel;
import org.library.media.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PrimaryController implements Initializable {
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
    private LoanModel loanModel;
    private AdminModel adminModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        libView.getChildren().clear();

        // Configure authentication model
        if (this.authenticationModel == null) {
            this.authenticationModel = App.getAppModel().getAuthenticationModel();
        }

        if(this.adminModel == null) {
            this.adminModel = App.getAppModel().getAdminModel();
        }

        // Configure search model
        if (this.searchModel == null) {
            this.searchModel = App.getAppModel().getSearchModel();

            this.searchResults = this.searchModel.getSearchResultsList();
            this.searchResults.addListener((ListChangeListener<Media>) change -> {
                this.updateSearchResults();
            });
        }

        // Configure loan Manager
        if (this.loanModel == null) {
            this.loanModel = App.getAppModel().getLoanModel();
        }

        if(searchModel.getPreviousQuery() != null){
            updateSearchResults();
            searchBar.setText(searchModel.getPreviousQuery());
        } else {
            promptSearchDecor();
        }
        // Load Categories
        // TODO: Implement

    }

    @FXML
    public void searchResult() {
        // TODO: 5/13/2021 Add "No result for x"
        String query = searchBar.textProperty().getValue().toLowerCase().strip();

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
        Button borrowButton;
        if (true) { // USER IS ADMIN??
            borrowButton = new Button("Edit");
        } else {
            borrowButton = new Button("Låna");
        }


        if (media instanceof Book) {

            List<Author> authorList = ((Book) media).getAuthors();
            String authors;
            // TODO: 5/13/2021 Nån bättre lösning? Kommer inte på nån
            if (authorList.size() < 4) {
                authors = authorList.stream().map(author -> author.getGivenName() + " " + author.getFamilyName()).collect(Collectors.joining(", "));
            } else {
                authors = authorList.subList(0, 3).stream().map(author -> author.getGivenName() + " " + author.getFamilyName()).collect(Collectors.joining(", "));
            }
            Label authorLabel = new Label(authors);
            authorLabel.getStyleClass().add("authorLabel");
            String inStock = "Antal kvar: " + media.getNumberOfLoanableItems();
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
                borderPane.getChildren().clear();

                BorderPane borderPaneTop = new BorderPane();
                Button goBack = new Button("Return");
                goBack.setOnAction(actionEvent1 -> {
                    updateSearchResults();
                });
                Label errorLabel = new Label();
                errorLabel.getStyleClass().add("errorLabel");

                if (true) { // USER IS ADMIN?
                    Button editButton = new Button("Edit");
                    editButton.setOnAction(actionEvent1 -> {
                        adminModel.setMedia(media);
                        try {
                            App.setRoot("admin");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    borderPaneTop.setRight(editButton);
                } else if (media.hasItemsAvailableForLoan()) {
                    Button loanButton = new Button("Borrow");
                    loanButton.setOnAction(actionEvent1 -> {
                        try {
                            loanModel.add(media);
                            updateSearchResults();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            errorLabel.setText(exception.getMessage());
                            borderPaneTop.setBottom(errorLabel);
                        }
                    });
                    borderPaneTop.setRight(loanButton);
                }

                borderPaneTop.setLeft(goBack);
                borderPane.setTop(borderPaneTop);

                VBox mediaInformation = new VBox();
                Label descriptionLabel = new Label("Description");
                Label authorHeader = new Label();
                if (((Book) media).getAuthors().size() < 2) {
                    authorHeader.setText("Author");
                } else {
                    authorHeader.setText("Authors");
                }
                authorHeader.getStyleClass().add("h2");
                Label publisher = new Label("Publisher");
                publisher.getStyleClass().add("h2");

                // Title
                title.getStyleClass().add("h1");
                mediaInformation.getChildren().add(title); // Add title
                // Description
                descriptionLabel.getStyleClass().add("h2");
                mediaInformation.getChildren().add(descriptionLabel);
                mediaInformation.getChildren().add(new Label(media.getSummary()));

                // Authors
                mediaInformation.getChildren().add(authorHeader);
                for (Author author :
                        authorList) {
                    mediaInformation.getChildren().add(new Label(author.getGivenName() + " " + author.getFamilyName()));
                }

                // Publisher
                if (!(((Book) media).getPublisher() == null)) {
                    mediaInformation.getChildren().add(publisher);
                    mediaInformation.getChildren().add(new Label(((Book) media).getPublisher()));
                }
                borderPane.setCenter(mediaInformation);
                libView.getChildren().add(borderPane);
            });
        } else if (media instanceof AudioBook) {
            borderPane.setLeft(title);
            borderPane.setRight(borrowButton);
            libView.getChildren().add(borderPane);
        } else {
            promptSearchDecor("This is a media item.");
        }

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
