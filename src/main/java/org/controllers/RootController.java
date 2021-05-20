package org.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Application Root Controller
 * <p>
 * Wraps the active content controller (e.g. PrimaryController) to include a shared navigation bar.
 */
public class RootController implements Initializable {

    private Destination previousDestination;

    @FXML
    private NavigationBarController navigationBarController;

    @FXML
    private BorderPane content;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Set RootController in App
        App.setRootController(this);

        // Set RootController in NavigationBarController
        this.navigationBarController.setRootController(this);

        try {
            // Set initial controller
            this.present(Destination.HOME);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Sets the active content controller.
     *
     * @param node A JavaFX node instance.
     */
    private void setContent(Node node) {
        this.content.setCenter(node);
        BorderPane.setMargin(node, new Insets(0));
    }

    /**
     * Sets the FXML-file with the provided resource name to the active content.
     */
    public void present(Destination destination) throws IOException {
        // Enable navigation to previous destination
        if (destination == Destination.PREVIOUS && this.previousDestination != null) {
            this.present(previousDestination);
            return;
        }

        if (this.navigationBarController != null) {
            // Notify the navigation bar controller that a new resource was presented.
            this.navigationBarController.willSetActiveDestination(destination);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(destination.getResourceName()));
        this.setContent(fxmlLoader.load());

        // Set new destination as previous destination
        if (destination != Destination.LOGIN && destination != Destination.REGISTER && destination != Destination.RETURNS) {
            this.previousDestination = destination;
        }
    }

}
