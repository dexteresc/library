package org.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.library.admin.*;
import org.library.media.Book;
import org.library.media.Media;
import org.library.media.Movie;

public class AdminController implements Controller {
    private AdminModel adminModel;

    @FXML private VBox childNode;

    @FXML private Button cancelButton;

    @FXML private Button saveButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // get adminModel
        this.adminModel = App.getAppModel().getAdminModel();

        // Configure editing mode
        if (adminModel.hasEditModel()) {
            this.restoreChildNode();
        }
    }

    private void restoreChildNode() {
        if (this.adminModel.hasEditModel()) {
            EditModel editModel = this.adminModel.getEditModel();

            if (editModel instanceof BookEditModel) {
                this.configureChildNode("edit/book");
            } else if (editModel instanceof MovieEditModel) {
                this.configureChildNode("edit/movie");
            } else if (editModel instanceof MediaItemsEditModel) {
                this.configureChildNode("edit/mediaitems");
            }
        }
    }

    private void configureChildNode(String path) {
        try {
            Node node = this.getNode(path);
            Object userData = node.getUserData();

            if (userData instanceof EditController) {
                EditController editController = (EditController) userData;
                editController.setAdminController(this);
                editController.setEditModel(this.adminModel.getEditModel());
                editController.configure();
            }

            this.childNode.getChildren().setAll(node);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // New

    public void newBook() {
        this.edit(new Book());
    }

    public void newMovie() {
        this.edit(new Movie());
    }

    public void edit(Media media) {
        if (media instanceof Book) {
            this.adminModel.editBook((Book) media);
        } else if (media instanceof Movie) {
            this.adminModel.editMovie((Movie) media);
        }
        this.restoreChildNode();
    }

    public void editMediaItems(Media media) {
        this.adminModel.editMediaItems(media);
        this.restoreChildNode();
    }

    public void save() {
        try {
            this.adminModel.save();
        } catch (Exception exception) {
            this.receiveException(exception);
        }
    }

    public void delete() {
        try {
            this.adminModel.delete();
            this.adminModel.reset();
            App.setRoot("admin");
        } catch (Exception exception) {
            this.receiveException(exception);
        }
    }

    public void cancel() {
        try {
            this.adminModel.reset();
            App.setRoot("admin");
        } catch (Exception exception) {
            this.receiveException(exception);
        }
    }
}
