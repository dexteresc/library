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
    private static String pathToChildNode;

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
        if (adminModel.hasEditModel()) {
            this.restoreChildNode();
        }
    }

    private Node getNode(String path) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(path + ".fxml"));
        return fxmlLoader.load();
    }

    private void restoreChildNode() {
        String path = AdminController.pathToChildNode;

        if (path != null) {
            this.configureChildNode(path);
        }
    }

    private void configureChildNode(String path) {
        try {
            Node node = this.getNode(path);
            Object userData = node.getUserData();

            if (userData instanceof EditController) {
                EditController editController = (EditController) userData;
                editController.setEditModel(this.adminModel.getEditModel());
                editController.configure();
            }

            this.childNode.getChildren().setAll(node);

            AdminController.pathToChildNode = path;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void newBook() {
        this.adminModel.editBook(new Book());
        this.configureChildNode("edit/book");
    }

    public void editBook(Book book) {
        this.adminModel.editBook(book);
        this.configureChildNode("edit/book");
    }

    public void save() throws Exception {
        this.adminModel.save();
    }

    public void delete() throws Exception {
        this.adminModel.delete();
    }
}
