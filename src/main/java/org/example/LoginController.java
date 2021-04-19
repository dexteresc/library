package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.library.Account;

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
    Label errorLabel = new Label("Username or password is wrong.");

    private final Account account = App.getAccount(); // Get user from App
    public VBox loginBox;

    @FXML
    public void goHome() throws IOException {
        App.setRoot("primary");
    }

    public void goRegister() throws IOException {
        App.setRoot("register");
    }

    public void initialize() {
        errorLabel.getStyleClass().add("errorLabel");
    }

    @FXML
    private void logIn() {
        String username = usernameField.textProperty().getValue();
        String password = passwordField.textProperty().getValue();

        /*
        if (username.strip().equals("") || password.strip().equals("")) { // Check if empty
            if (username.strip().equals("")) {
                usernameField.getStyleClass().add("fieldError"); // Add red border
            }
            if (password.strip().equals("")) {
                passwordField.getStyleClass().add("fieldError"); // Add red border
            }
            if (!(loginBox.getChildren().contains(errorLabel))) {
                loginBox.getChildren().add(errorLabel); // Add error label
            }
            passwordField.clear(); // Clear password field
        } else {
            passwordField.getStyleClass().remove("fieldError"); // Remove red border if condition is filled
            usernameField.getStyleClass().remove("fieldError"); // Remove red border if condition is filled
            if (username.equals(account.getUsername()) && password.equals(account.getPassword())) { // TODO: Check for username in db (LibraryOverseer)
                account.setLoggedIn(true);
            } else {
                if (!(loginBox.getChildren().contains(errorLabel))) {
                    loginBox.getChildren().add(errorLabel); // Add error label
                }
                passwordField.clear(); // Clear password field
            }
        }

         */
    }
}
