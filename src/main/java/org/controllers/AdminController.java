package org.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.library.admin.AdminModel;
import org.library.media.Book;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    private AdminModel adminModel;

    @FXML
    private Button backButton;

    @FXML
    private VBox childNode;

    @FXML
    private Button saveButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // get adminModel
        this.adminModel = App.getAppModel().getAdminModel();

        // Back Button
        backButton.setOnAction(actionEvent -> {
            try {
                App.setRoot("primary");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Configure editing mode
        if (adminModel.hasObject()) {
            this.adminModel.setEditingMode(EditingMode.UPDATE);
            this.configureChildNode();
        } else {
            this.adminModel.setEditingMode(EditingMode.CREATE);
        }
    }

    private Node getNode(String path) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(path + ".fxml"));
        return fxmlLoader.load();
    }

    private void setChild(String path) throws IOException {
        Node node = this.getNode(path);
        Object userData = node.getUserData();

        if (userData instanceof EditController) {
            EditController editController = (EditController) userData;
            editController.setObjectToEdit(this.adminModel.getObject());
            editController.configure();
        }

        this.childNode.getChildren().setAll(node);
    }

    private void configureChildNode() {
        Object object = adminModel.getObject();

        // Cancel configuration if media is null.
        if (object == null) { return; }

        try {
            if (object instanceof Book) {
                // Load BookEditController
                this.setChild("edit/book");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void newBook() {
        this.adminModel.setObject(new Book());
        this.configureChildNode();
    }

    public void save() throws Exception {
        this.adminModel.save();
    }

    public void delete() throws Exception {
        this.adminModel.delete();
    }
}
