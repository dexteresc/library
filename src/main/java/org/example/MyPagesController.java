package org.example;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.library.account.Account;
import org.library.AuthenticationModel;

import java.net.URL;
import java.util.ResourceBundle;

public class MyPagesController implements Initializable {
    public Label firstName;
    public Label lastName;
    public Label phoneNumber;
    public Label email;

    private AuthenticationModel authenticationModel;
    private Account account;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (authenticationModel == null) {
            this.authenticationModel = App.getAppModel().getAuthenticationModel();
            this.account = authenticationModel.getAccount();
        }

        firstName.setText(account.getGivenName());
        lastName.setText(account.getFamilyName());
        phoneNumber.setText(account.getPhoneNumber());
        email.setText(account.getEmail());
    }

}
