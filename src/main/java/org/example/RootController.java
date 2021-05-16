package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {

    @FXML
    private NavigationBarController navigationBarController;

    @FXML
    private BorderPane content;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub

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

    private void setContent(Node node) {
        this.content.setCenter(node);
        BorderPane.setMargin(node, new Insets(0));
    }

    // Sets the FXML-file with the provided resource name to the active content.
    public void present(Destination destination) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(destination.getResourceName()));
        this.setContent(fxmlLoader.load());

        if (this.navigationBarController != null) {
            // Notify the navigation bar controller that a new resource was presented.
            this.navigationBarController.setActiveDestination(destination);
        }
    }

}
