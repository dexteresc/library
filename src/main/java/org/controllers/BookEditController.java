package org.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.library.admin.BookEditModel;
import org.library.admin.EditModel;
import org.library.media.Author;
import org.library.media.Book;

public class BookEditController implements EditController {
    private AdminController adminController;
    private BookEditModel bookEditModel;
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

    // Empty table

    @FXML
    private HBox emptyTableIndicator;

    @Override
    public void setAdminController(AdminController adminController) {
        this.adminController = adminController;
    }

    @Override
    public void setEditModel(EditModel editModel) {
        this.bookEditModel = (BookEditModel) editModel;
    }

    @Override
    public void configure() {
        this.book = this.bookEditModel.getBook();

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

        this.authorsTableView.setFixedCellSize(25);
        this.authorsTableView.prefHeightProperty().bind(authorsTableView.fixedCellSizeProperty().multiply(Bindings.size(authorsTableView.getItems()).add(1.15)));
        this.authorsTableView.minHeightProperty().bind(this.authorsTableView.prefHeightProperty());
        this.authorsTableView.maxHeightProperty().bind(this.authorsTableView.prefHeightProperty());

        // Hide table view if it has no items
        this.authorList.addListener((ListChangeListener<Author>) (change) -> {
            this.setVisible(this.authorsTableView, change.getList().size() > 0);
            this.setVisible(this.emptyTableIndicator, change.getList().size() < 1);
        });

        // Set if table view should be visible initially
        this.setVisible(this.authorsTableView, authorList.size() > 0);
        this.setVisible(this.emptyTableIndicator, authorList.size() < 1);
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

    public void editMediaItems() {
        this.adminController.editMediaItems(this.book);
    }
}
