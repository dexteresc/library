package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.library.User;

import java.io.IOException;

/**
 * Controller for the login page
 */
public class LoginController {
    public Text homeButton;
    public Button registerButton;
    public Button loginButton;
    public BorderPane header;
    public PasswordField passwordField;
    public TextField usernameField;

    private final User user = App.getUser();
    public VBox loginBox;

    @FXML
    public void goHome() throws IOException {
        App.setRoot("primary");
    }


    public void goRegister(ActionEvent actionEvent) {

    }

    @FXML
    private void logIn() {
        String username = usernameField.textProperty().getValue();
        String password = passwordField.textProperty().getValue();
        System.out.println(username + " " + password);
        if (username.strip().equals("")) {
            usernameField.getStyleClass().add("fieldError");
        } else if (password.strip().equals("")) {
            passwordField.getStyleClass().add("fieldError");
        } else {
            if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                user.setLoggedIn(true);
            } else {
                Label errorLabel = new Label("Username or password is wrong.");
                errorLabel.getStyleClass().add("errorLabel");
                loginBox.getChildren().add(errorLabel);
                passwordField.clear();
            }
        }
    }
}
