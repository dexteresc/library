package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.IOException;

/**
 * Controller for the login page
 */
public class LoginController {
    public Text homeButton;
    public Button registerButton;
    public Button loginButton;
    public BorderPane header;

    @FXML
    public void goHome() throws IOException {
        App.setRoot("primary");
    }
}
