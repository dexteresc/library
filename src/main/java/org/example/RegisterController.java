package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.library.AccountManager;
import org.library.AuthenticationModel;

import java.io.IOException;

public class RegisterController {

    public Label homeButton;
    public Button loginButton;
    public Button registerButton;
    public Button register;
    public VBox registerBox;
    public TextField passwordField;
    public TextField phoneField;
    public TextField emailField;
    public TextField lastNameField;
    public TextField firstNameField;
    private AuthenticationModel authenticationModel;
    Label errorLabel = new Label("Error, please make sure that everything is answered and that it's answered correctly");

    public void initialize() {
            if (this.authenticationModel == null) {
        this.authenticationModel = App.getAppModel().getAuthenticationModel();
    }
    }
    public void switchToLogin() throws IOException { //behövs för att kunna nå register knappen
        App.setRoot("login");
    }

    @FXML
    public void register() throws Exception { // detta gör så vi sparar allt som läggs in i databasen
        String firstName = firstNameField.textProperty().getValue();
        System.out.println(firstName);
        String lastName= lastNameField.textProperty().getValue();
        System.out.println(lastName);
        String email = emailField.textProperty().getValue();
        System.out.println(email);
        String phone = phoneField.textProperty().getValue();
        System.out.println(phone);
        String password = passwordField.textProperty().getValue();
        System.out.println(password);

        if (!firstName.matches(".*\\d.*") // lite krav av vad som måste ske innan man kan gå vidare till login
                && !firstName.equals("")
                && !lastName.matches(".*\\d.*")
                && !lastName.equals("")
                && email.contains("@")
                && phone.matches(".*\\d.*")
                && !password.equals("")){
            registerBox.getChildren().remove(errorLabel);
            authenticationModel.getAccountManager().createAccount(firstName, lastName,email, phone, password);
            App.setRoot("Login");
        }
        else {
            registerBox.getChildren().add(errorLabel);
        }
        }
    }
