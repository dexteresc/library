package org.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.library.util.Database;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static final Logger logger = LogManager.getLogger();
    private static final AppModel appModel = new AppModel(new Database("jdbc:mysql://ec2-23-20-145-129.compute-1.amazonaws.com:3306/library", "admin", "cbq6LQzci9c"));
    private static Scene scene;
    private static RootController rootController;

    static void setRoot(String fxml) throws IOException {
        logger.warn("Using static navigation method.");
        rootController.present(Destination.resolve(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    public static RootController getRootController() {
        return App.rootController;
    }

    public static void setRootController(RootController rootController) {
        App.rootController = rootController;
    }

    public static AppModel getAppModel() {
        return appModel;
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("root"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }
}