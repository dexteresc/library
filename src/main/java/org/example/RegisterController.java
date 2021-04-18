package org.example;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class RegisterController {

    public Label homeButton;
    public Button loginButton;
    public Button registerButton;

    public void switchToLogin() throws IOException {
        App.setRoot("login");
    }
}
