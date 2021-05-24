package org.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface Controller extends Initializable {
    Logger logger = LogManager.getLogger();

    // Initialization

    default void initialize(URL url, ResourceBundle resourceBundle) {
        AppModel appModel = App.getAppModel();

        this.initialize(appModel);
    }

    default void initialize(AppModel appModel) {}

    // Navigation

    default void navigateTo(Destination destination) {
        logger.info("Navigating to " + destination.name() + "...");

        try {
            App.getRootController().present(destination);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // JavaFX utility methods

    default void setVisible(Node node, Boolean visible) {
        node.setVisible(visible);
        node.setManaged(visible);
    }

    default Node getNode(String path) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(path + ".fxml"));
        return fxmlLoader.load();
    }

    default void alert(AlertBuilder alertBuilder) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alertBuilder.build(alert);
        alert.show();
    }

    // Exception indication

    default void receiveException(Exception exception) {
        logger.error(exception.getMessage());
        exception.printStackTrace();

        this.handleException(exception);
    }

    default void handleException(Exception exception) {
        this.alert(
                alert -> {
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setContentText(exception.getMessage());
                });
    }

    // Internal types

    interface AlertBuilder {
        void build(Alert alert);
    }
}
