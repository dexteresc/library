package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class RegisterController {
    public TextField firstNameField;

    @FXML
    public void goHome() throws IOException {
        App.setRoot("primary");
    }

    public void readTextFields() {
    }

}
