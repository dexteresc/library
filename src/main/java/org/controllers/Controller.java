package org.controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface Controller extends Initializable {
    Logger logger = LogManager.getLogger();

    default void initialize(URL url, ResourceBundle resourceBundle) {
        AppModel appModel = App.getAppModel();

        this.initialize(appModel);
    }

    default void initialize(AppModel appModel) {

    }

    default void navigateTo(Destination destination) {
        logger.info("Navigating to " + destination.name() + "...");

        try {
            App.getRootController().present(destination);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    default void setVisible(Node node, Boolean visible) {
        node.setVisible(visible);
        node.setManaged(visible);
    }

    default Node getNode(String path) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(path + ".fxml"));
        return fxmlLoader.load();
    }
}
