package org.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.library.admin.EditModel;
import org.library.admin.MediaItemsEditModel;
import org.library.media.MediaItem;
import org.library.media.MediaType;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MediaItemEditController implements EditController {
    private AdminController adminController;
    private MediaItemsEditModel mediaItemsEditModel;
    private ObservableList<MediaItem> mediaItemList;
    private ObservableList<MediaType> mediaTypeList;
    private MediaItem editingMediaItem;

    @FXML
    private TableView<MediaItem> mediaItemsTableView;

    @FXML
    private ChoiceBox<MediaType> mediaTypeChoiceBox;

    @FXML
    private ChoiceBox<MediaItem.Status> mediaItemStatusChoiceBox;

    @FXML
    private Button addMediaItemButton;

    @FXML
    private Button saveMediaItemButton;

    // Empty table

    @FXML
    private HBox emptyTableIndicator;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setEditing(false);
    }

    @Override
    public void setAdminController(AdminController adminController) {
        this.adminController = adminController;
    }

    @Override
    public void setEditModel(EditModel editModel) {
        this.mediaItemsEditModel = (MediaItemsEditModel) editModel;
    }

    @Override
    public void configure() {
        try {
            this.mediaItemList = mediaItemsEditModel.getMediaItemList();
            this.mediaTypeList = mediaItemsEditModel.getMediaTypeList();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        // Configure table view
        this.configureTableView();

        // Configure media type choice box
        this.configureMediaTypeChoiceBox();

        // Configure media item status choice box
        this.configureMediaItemStatusChoiceBox();
    }

    private void configureTableView() {
        this.mediaItemsTableView.setItems(this.mediaItemList);

        TableColumn<MediaItem, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getId() == null ? "-" : cellData.getValue().getId().toString()));

        TableColumn<MediaItem, String> mediaTypeColumn = new TableColumn<>("Media type");
        mediaTypeColumn.setCellValueFactory(new PropertyValueFactory<>("mediaType"));

        TableColumn<MediaItem, String> onLoanColumn = new TableColumn<>("On loan");
        onLoanColumn.setCellValueFactory(new PropertyValueFactory<>("currentlyOnLoan"));

        TableColumn<MediaItem, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<MediaItem, String> loanPeriodColumn = new TableColumn<>("Loan period");
        loanPeriodColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getMediaType().getLoanPeriod().toString() + " day(s)"));

        this.mediaItemsTableView.getColumns().add(idColumn);
        this.mediaItemsTableView.getColumns().add(mediaTypeColumn);
        this.mediaItemsTableView.getColumns().add(onLoanColumn);
        this.mediaItemsTableView.getColumns().add(statusColumn);
        this.mediaItemsTableView.getColumns().add(loanPeriodColumn);

        this.mediaItemsTableView.setFixedCellSize(25);
        this.mediaItemsTableView.prefHeightProperty().bind(mediaItemsTableView.fixedCellSizeProperty().multiply(Bindings.size(mediaItemsTableView.getItems()).add(1.15)));
        this.mediaItemsTableView.minHeightProperty().bind(this.mediaItemsTableView.prefHeightProperty());
        this.mediaItemsTableView.maxHeightProperty().bind(this.mediaItemsTableView.prefHeightProperty());

        // Hide table view if it has no items
        this.mediaItemList.addListener((ListChangeListener<MediaItem>) (change) -> {
            this.setVisible(this.mediaItemsTableView, change.getList().size() > 0);
            this.setVisible(this.emptyTableIndicator, change.getList().size() < 1);
        });

        // Set if table view should be visible initially
        this.setVisible(this.mediaItemsTableView, mediaItemList.size() > 0);
        this.setVisible(this.emptyTableIndicator, mediaItemList.size() < 1);
    }

    private void configureMediaTypeChoiceBox() {
        this.mediaTypeChoiceBox.setItems(this.mediaTypeList);

        if (this.mediaTypeList.size() > 0) {
            this.mediaTypeChoiceBox.setValue(this.mediaTypeList.get(0));
        }
    }

    private void configureMediaItemStatusChoiceBox() {
        this.mediaItemStatusChoiceBox.setItems(FXCollections.observableList(List.of(MediaItem.Status.values())));
        this.mediaItemStatusChoiceBox.setValue(MediaItem.Status.NONE);
    }

    public void removeMediaItem() {
        if (!this.mediaItemsTableView.getSelectionModel().isEmpty()) {
            this.mediaItemList.remove(this.mediaItemsTableView.getSelectionModel().getSelectedItem());
        }
    }

    public void editMediaItem() {
        if (!this.mediaItemsTableView.getSelectionModel().isEmpty()) {
            MediaItem mediaItem = this.mediaItemsTableView.getSelectionModel().getSelectedItem();
            this.editingMediaItem = mediaItem;
            this.mediaTypeChoiceBox.setValue(mediaItem.getMediaType());
            this.mediaItemStatusChoiceBox.setValue(mediaItem.getStatus());

            this.setEditing(true);
        }
    }

    public void addMediaItem() {
        MediaItem mediaItem = new MediaItem();
        mediaItem.setMedia(this.mediaItemsEditModel.getMedia());
        mediaItem.setMediaType(this.mediaTypeChoiceBox.getValue());
        mediaItem.setStatus(this.mediaItemStatusChoiceBox.getValue());

        this.mediaItemList.add(mediaItem);
    }

    public void saveMediaItem() {
        this.editingMediaItem.setMediaType(this.mediaTypeChoiceBox.getValue());
        this.editingMediaItem.setStatus(this.mediaItemStatusChoiceBox.getValue());
        this.mediaItemList.set(this.mediaItemList.indexOf(this.editingMediaItem), this.editingMediaItem);

        this.editingMediaItem = null;

        if (this.mediaTypeList.size() > 0) {
            this.mediaTypeChoiceBox.setValue(this.mediaTypeList.get(0));
        }
        this.mediaItemStatusChoiceBox.setValue(MediaItem.Status.NONE);

        this.setEditing(false);
    }

    private void setEditing(Boolean active) {
        this.setVisible(this.addMediaItemButton, !active);
        this.setVisible(this.saveMediaItemButton, active);
    }

    public void backToMedia() {
        this.adminController.edit(this.mediaItemsEditModel.getMedia());
    }
}
