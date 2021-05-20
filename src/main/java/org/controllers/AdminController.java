package org.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.library.admin.AdminModel;
import org.library.media.Author;
import org.library.media.Book;
import org.library.media.BookManager;
import org.library.media.Media;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public Button saveButton;
    private AdminModel adminModel;
    private BookManager bookManager;

    private String title;
    private List<Author> authorList;
    private String description;
    String titleValue;
    String descriptionValue;
    String familyNameValue;
    String givenNameValue;

    @FXML
    private Button backButton;
    @FXML
    private VBox informationVBox;
    @FXML
    private TextArea titleArea;
    @FXML
    private VBox authorVBox;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField givenNameField;
    @FXML
    private TextField familyNameField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // get adminModel
        this.adminModel = App.getAppModel().getAdminModel();
        this.bookManager = App.getAppModel().getBookManager();

        informationVBox.getChildren().clear();

        // Back Button
        backButton.setOnAction(actionEvent -> {
            try {
                App.setRoot("primary");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Create module

        // Title
        titleArea = new TextArea();
        titleArea.setWrapText(true);
        HBox titleBox = new HBox();
        titleBox.getChildren().add(new Label("Title:"));
        titleBox.getChildren().add(titleArea);
        informationVBox.getChildren().add(titleBox);

        // Description
        HBox descriptionHBox = new HBox();
        descriptionHBox.getChildren().add(new Label("Description:"));
        descriptionArea = new TextArea();
        descriptionHBox.getChildren().add(descriptionArea);
        informationVBox.getChildren().add(descriptionHBox);

        // Authors

        HBox authorHBox = new HBox();
        authorVBox = new VBox();
        authorVBox.getChildren().add(new Label("Authors:"));
        authorHBox.getChildren().add(authorVBox);
        informationVBox.getChildren().add(authorHBox);
        VBox authorNewVBox = new VBox();
        HBox authorNewHBox = new HBox();
        authorNewVBox.getChildren().add(new Label("New author:"));
        authorNewVBox.getChildren().add(authorNewHBox);
        authorNewHBox.getChildren().add(new Label("Given name:"));
        givenNameField = new TextField();
        authorNewHBox.getChildren().add(givenNameField);
        authorNewHBox.getChildren().add(new Label("Family: name"));
        familyNameField = new TextField();
        authorNewHBox.getChildren().add(familyNameField);
        informationVBox.getChildren().add(authorNewVBox);


        // Save Button

        if (adminModel.getMedia() == null) {
            createMedia();
        } else {
            editMedia();
        }
    }

    public void editMedia() {

        Media media = adminModel.getMedia();
        title = media.getTitle();
        description = media.getSummary();
        titleArea.setText(title);
        descriptionArea.setText(media.getSummary());
        if (media instanceof Book) {
            authorList = ((Book) media).getAuthors();
            for (Author author :
                    authorList) {
                HBox authorBox = new HBox();
                Label authorName = new Label(author.getGivenName() + " " + author.getFamilyName());
                Button deleteAuthorButton = new Button("delete");
                deleteAuthorButton.setOnAction(actionEvent -> {
                    // TODO: 5/19/2021 Delete author
                });
                deleteAuthorButton.getStyleClass().add("coloredButton");
                authorBox.getChildren().add(authorName);
                authorBox.getChildren().add(deleteAuthorButton);
                authorVBox.getChildren().add(authorBox);
            }
            saveButton.setOnAction(actionEvent -> {
                // Variables
                titleValue = titleArea.textProperty().getValue();
                descriptionValue = descriptionArea.textProperty().getValue();
                familyNameValue = familyNameField.textProperty().getValue();
                givenNameValue = givenNameField.textProperty().getValue();
                if (!(descriptionArea.textProperty().getValue().equals(description))) {
                    media.setSummary(descriptionArea.textProperty().getValue());
                    System.out.println(descriptionArea.textProperty().getValue());
                }
                if (!(titleArea.textProperty().getValue().equals(title))) {
                    media.setTitle(titleArea.textProperty().getValue());
                    System.out.println(titleArea.textProperty().getValue());
                }
                if (!(givenNameValue.equals("")) || !(familyNameValue.equals(""))) {
                    // TODO: Add or create new author
                }
                try {
                    bookManager.updateBook((Book) media);
                    App.setRoot("primary");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void createMedia() {

        // assert adminModel.getMedia() == null;
        saveButton.setOnAction(actionEvent -> {
            // Variables
            titleValue = titleArea.textProperty().getValue();
            descriptionValue = descriptionArea.textProperty().getValue();
            familyNameValue = familyNameField.textProperty().getValue();
            givenNameValue = givenNameField.textProperty().getValue();
            System.out.println("this Ran");
            System.out.println(titleValue + " " + descriptionValue + " " + givenNameValue + " " + familyNameValue);
            if (!(titleValue.equals(""))
                    && (!(descriptionValue.equals("")))
                    && (!(givenNameValue.equals("")))
                    && (!(familyNameValue.equals("")))) {
                Book book = new Book();
                System.out.println("this Ran2");

                book.setTitle(titleValue);
                book.setSummary(descriptionValue);
                Author author = new Author();
                author.setGivenName(givenNameValue);
                author.setFamilyName(familyNameValue);
                authorList = new ArrayList<>();
                authorList.add(author);
                book.setAuthors(authorList);

                try {
                    bookManager.createBook(book);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
