package org.controllers;

import javafx.beans.InvalidationListener;
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
import org.library.admin.EditModel;
import org.library.admin.MovieEditModel;
import org.library.media.Actor;
import org.library.media.Movie;

public class MovieEditController implements EditController {
    private MovieEditModel movieEditModel;
    private Movie movie;
    private ObservableList<Actor> actorList;

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
    private TextField ageRatingField;

    @FXML
    private TextField productionCountryField;

    @FXML
    private TextField directorField;

    @FXML
    private TableView<Actor> actorsTableView;

    // New actor

    @FXML
    private TextField actorGivenNameField;

    @FXML
    private TextField actorFamilyNameField;

    @Override
    public void setEditModel(EditModel editModel) {
        this.movieEditModel = (MovieEditModel) editModel;
    }

    @Override
    public void configure() {
        this.movie = this.movieEditModel.getMovie();

        // Make sure movie is not null.
        if (this.movie == null) {
            return;
        }

        // Set initial values
        this.titleField.setText(this.movie.getTitle());
        this.summaryField.setText(this.movie.getSummary());
        this.classificationField.setText(this.movie.getClassification());
        this.locationField.setText(this.movie.getLocation());
        this.publishingDatePicker.setValue(this.movie.getPublishingDate());
        this.ageRatingField.setText(this.movie.getAgeRating());
        this.productionCountryField.setText(this.movie.getProductionCountry());
        this.directorField.setText(this.movie.getDirector());

        // Add listeners
        this.titleField.textProperty().addListener((o, v, value) -> this.movie.setTitle(value));
        this.summaryField.textProperty().addListener((o, v, value) -> this.movie.setSummary(value));
        this.classificationField.textProperty().addListener((o, v, value) -> this.movie.setClassification(value));
        this.locationField.textProperty().addListener((o, v, value) -> this.movie.setLocation(value));
        this.publishingDatePicker.valueProperty().addListener((o, v, value) -> this.movie.setPublishingDate(value));
        this.ageRatingField.textProperty().addListener((o, v, value) -> this.movie.setAgeRating(value));
        this.productionCountryField.textProperty().addListener((o, v, value) -> this.movie.setProductionCountry(value));
        this.directorField.textProperty().addListener((o, v, value) -> this.movie.setDirector(value));

        // Make author list observable
        this.actorList = FXCollections.observableList(this.movie.getActors());

        // Configure table
        this.configureTableView();
    }

    private void configureTableView() {
        this.actorsTableView.setItems(actorList);

        TableColumn<Actor, String> givenNameColumn = new TableColumn<>("Given name");
        givenNameColumn.setCellValueFactory(new PropertyValueFactory<>("givenName"));

        TableColumn<Actor, String> familyNameColumn = new TableColumn<>("Family name");
        familyNameColumn.setCellValueFactory(new PropertyValueFactory<>("familyName"));

        this.actorsTableView.getColumns().add(givenNameColumn);
        this.actorsTableView.getColumns().add(familyNameColumn);

        this.actorsTableView.setFixedCellSize(25);
        this.actorsTableView.prefHeightProperty().bind(actorsTableView.fixedCellSizeProperty().multiply(Bindings.size(actorsTableView.getItems()).add(1.15)));
        this.actorsTableView.minHeightProperty().bind(this.actorsTableView.prefHeightProperty());
        this.actorsTableView.maxHeightProperty().bind(this.actorsTableView.prefHeightProperty());

        // Hide table view if it has no items
        this.actorList.addListener((ListChangeListener<Actor>) (change) -> {
            this.setVisible(this.actorsTableView, change.getList().size() > 0);
        });

        // Set if table view should be visible initially
        this.setVisible(this.actorsTableView, actorList.size() > 0);
    }

    public void removeActor() {
        if (!this.actorsTableView.getSelectionModel().isEmpty()) {
            this.actorList.remove(this.actorsTableView.getSelectionModel().getFocusedIndex());
        }
    }

    public void addActor() {
        Actor actor = new Actor();
        actor.setGivenName(this.actorGivenNameField.getText());
        actor.setFamilyName(this.actorFamilyNameField.getText());

        this.actorList.add(actor);

        // Clear actor fields
        this.clearNewActorFields();
    }

    private void clearNewActorFields() {
        this.actorGivenNameField.setText("");
        this.actorFamilyNameField.setText("");
    }
}
