package org.example;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.library.Student;

import java.io.IOException;

public class PrimaryController {
    public Label homeButton;
    public Button registerButton;
    public Button loginButton;
    public ListView<String> categoriesView;
    public VBox libView;
    public StackPane libStackPane;
    public ScrollPane scrollPane;
    public BorderPane mainPane;

    @FXML
    public void switchToLogin() throws IOException {
        App.setRoot("login");
    }

    @FXML
    public void initialize() {
        scrollPane.setFitToWidth(true);
        //libStackPane.minWidthProperty().bind(Bindings.createDoubleBinding(() ->
        //      scrollPane.getViewportBounds().getWidth(), scrollPane.viewportBoundsProperty()));
        //    borderSomething.minWidthProperty().bind(libStackPane.widthProperty());
        Student student = new Student();
        categoriesView.setItems(student.something()); // TEST
        for (int i = 0; i < 10; i++) {
            BorderPane borderPane = new BorderPane();
            Label label = new Label("Hej" + i);
            Button button = new Button("Låna");
            borderPane.setLeft(label);
            BorderPane.setAlignment(label, Pos.CENTER_LEFT);
            borderPane.setRight(button);
            libView.getChildren().add(borderPane);
        }
    }

    @FXML
    public void libModules() {

        Article[] books = new Article[10];

    }
}
