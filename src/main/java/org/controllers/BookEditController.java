package org.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.library.media.Author;
import org.library.media.Book;

import java.net.URL;
import java.util.ResourceBundle;

public class BookEditController implements EditController {
    private Book book;
    private ObservableList<Author> authorList;

    @FXML
    private TextField titleField;

    @FXML
    private TextField summaryField;

    @FXML
    private TextField classificationField;

    @FXML
    private TextField locationField;

    @FXML
    private DatePicker publishingDatePicker;

    @FXML
    private TextField isbnField;

    @FXML
    private TextField publisherField;

    @FXML
    private TableView<Author> authorsTableView;

    // New author

    @FXML
    private TextField authorGivenNameField;

    @FXML
    private TextField authorFamilyNameField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void setObjectToEdit(Object object) {
        this.book = (Book) object;
    }

    @Override
    public void configure() {
        // Make sure book is not null.
        if (this.book == null) {
            return;
        }

        // Set initial values
        this.titleField.setText(this.book.getTitle());
        this.summaryField.setText(this.book.getSummary());
        this.classificationField.setText(this.book.getClassification());
        this.locationField.setText(this.book.getLocation());
        this.publishingDatePicker.setValue(this.book.getPublishingDate());
        this.isbnField.setText(this.book.getIsbn());
        this.publisherField.setText(this.book.getPublisher());

        // Add listeners
        this.titleField.textProperty().addListener((o, v, value) -> this.book.setTitle(value));
        this.summaryField.textProperty().addListener((o, v, value) -> this.book.setSummary(value));
        this.classificationField.textProperty().addListener((o, v, value) -> this.book.setClassification(value));
        this.locationField.textProperty().addListener((o, v, value) -> this.book.setLocation(value));
        this.publishingDatePicker.valueProperty().addListener((o, v, value) -> this.book.setPublishingDate(value));
        this.isbnField.textProperty().addListener((o, v, value) -> this.book.setIsbn(value));
        this.publisherField.textProperty().addListener((o, v, value) -> this.book.setPublisher(value));

        // Make author list observable
        this.authorList = FXCollections.observableList(this.book.getAuthors());

        // Configure table
        this.configureTableView();
    }

    private void configureTableView() {
        this.authorsTableView.setItems(authorList);

        TableColumn<Author, String> givenNameColumn = new TableColumn<>("Given name");
        givenNameColumn.setCellValueFactory(new PropertyValueFactory<>("givenName"));

        TableColumn<Author, String> familyNameColumn = new TableColumn<>("Family name");
        familyNameColumn.setCellValueFactory(new PropertyValueFactory<>("familyName"));

        this.authorsTableView.getColumns().add(givenNameColumn);
        this.authorsTableView.getColumns().add(familyNameColumn);
    }

    public void removeAuthor() {
        if (!this.authorsTableView.getSelectionModel().isEmpty()) {
            this.authorList.remove(this.authorsTableView.getSelectionModel().getFocusedIndex());
        }
    }

    public void addAuthor() {
        Author author = new Author();
        author.setGivenName(this.authorGivenNameField.getText());
        author.setFamilyName(this.authorFamilyNameField.getText());

        this.authorList.add(author);

        // Clear author fields
        this.clearNewAuthorFields();
    }

    private void clearNewAuthorFields() {
        this.authorGivenNameField.setText("");
        this.authorFamilyNameField.setText("");
    }
}
