package org.example;

import javafx.scene.control.Label;
import org.library.Account;
import org.library.AuthenticationModel;

public class MyPagesController {
    public Label firstName;
    public Label lastName;
    public Label phoneNumber;
    public Label Email;

    private AuthenticationModel authenticationModel;
    private Account account;
    public void initialize() {
        if(authenticationModel == null){
            this.authenticationModel = App.getAppModel().getAuthenticationModel();
            this.account = authenticationModel.getAccount();
        }
        firstName.setText(account.getGivenName());
    }

}
