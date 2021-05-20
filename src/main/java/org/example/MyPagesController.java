package org.example;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.library.account.Account;
import org.library.account.Customer;
import org.library.loan.LoanModel;
import org.library.security.AuthenticationModel;

import java.net.URL;
import java.util.ResourceBundle;

public class MyPagesController implements Initializable {
    public Label firstName;
    public Label lastName;
    public Label phoneNumber;
    public Label email;
    public VBox rightVBox;
    public BorderPane mainPane;
    public ScrollPane scrollPane;

    private AuthenticationModel authenticationModel;
    private LoanModel loanModel;
    private Account account;
    private Customer customer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (authenticationModel == null) {
            this.authenticationModel = App.getAppModel().getAuthenticationModel();
            this.account = authenticationModel.getAccount();
        }
        if(loanModel == null){
            this.loanModel = App.getAppModel().getLoanModel();
            customer = loanModel.getCustomer();
        }

        firstName.setText(account.getGivenName());
        lastName.setText(account.getFamilyName());
        phoneNumber.setText(account.getPhoneNumber());
        email.setText(account.getEmail());


    }

}
