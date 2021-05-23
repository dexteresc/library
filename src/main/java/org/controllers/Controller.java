package org.controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public interface Controller extends Initializable {

    default void initialize(URL url, ResourceBundle resourceBundle) {

    }

    default void setVisible(Node node, Boolean visible) {
        node.setVisible(visible);
        node.setManaged(visible);
    }

    default void hide(Node node) {
        this.setVisible(node, false);
    }

    default void show(Node node) {
        this.setVisible(node, true);
    }

    default Node getNode(String path) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(path + ".fxml"));
        return fxmlLoader.load();
    }
}
