package org.example;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.library.AuthenticationModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the login page
 */
public class LoginController implements Initializable {
    public Text homeButton;
    public Button registerButton;
    public Button loginButton;
    public BorderPane header;
    public PasswordField passwordField;
    public TextField usernameField;
    Label errorLabel = new Label("Username or password is wrong.");

    private AuthenticationModel authenticationModel;
    public VBox loginBox;

    public void goRegister() throws IOException {
        App.setRoot("register");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (this.authenticationModel == null) {
            this.authenticationModel = App.getAppModel().getAuthenticationModel();
        }

        // Check if user is already logged in
        if (this.authenticationModel.isAuthenticated()) {
            try {
                App.setRoot("internal:previous");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }

        errorLabel.getStyleClass().add("errorLabel");
    }

    @FXML
    private void logIn() {
        String username = usernameField.textProperty().getValue();
        String password = passwordField.textProperty().getValue();

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

            // Attempt login
            try {
                this.authenticationModel.login(username, password); // Throws error if unsuccessful
                // Login successful
                App.setRoot("internal:previous");
            } catch (Exception e) {
                e.printStackTrace();
                if (!this.loginBox.getChildren().contains(this.errorLabel)) {
                    this.loginBox.getChildren().add(errorLabel); // Add error label if it is not already present
                }
                this.errorLabel.setText(e.getMessage()); // Set the error message
                this.passwordField.clear();
            }
        }
    }
}
