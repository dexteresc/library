package org.example;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.library.account.Account;
import org.library.account.Customer;
import org.library.loan.Loan;
import org.library.loan.LoanManager;
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
    public VBox loanView;
    public Label loanViewTitle;

    private AuthenticationModel authenticationModel;
    private LoanModel loanModel;
    private LoanManager loanManager;
    private Account account;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (authenticationModel == null) {
            this.authenticationModel = App.getAppModel().getAuthenticationModel();
            this.account = authenticationModel.getAccount();
        }
        if (loanModel == null) {
            this.loanModel = App.getAppModel().getLoanModel();
        }
        if (loanManager == null) {
            this.loanManager = App.getAppModel().getLoanManager();
        }

        firstName.setText(account.getGivenName());
        lastName.setText(account.getFamilyName());
        phoneNumber.setText(account.getPhoneNumber());
        email.setText(account.getEmail());

        if (authenticationModel.isCustomer()) {
            BorderPane header = new BorderPane();
            header.setLeft(new Label("ID"));
            header.setRight(new Label("Return date"));
            header.setCenter(new Label("Title"));
            try {
                for (Loan loan :
                        loanManager.getActiveCustomerLoans(authenticationModel.getAccount().getId())) {
                    BorderPane borderPane = new BorderPane();
                    borderPane.setLeft(new Label(String.valueOf(loan.getId())));
                    borderPane.setRight(new Label(String.valueOf(loan.getReturnBy())));
                    // todo title
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (authenticationModel.isStaff()) {
            loanViewTitle.setText("Late loans");
            BorderPane header = new BorderPane();
            HBox headerHBox = new HBox();
            headerHBox.getChildren().add(new Label("Media ID"));
            headerHBox.getChildren().add(new Label("Customer ID"));
            HBox headerHBox2 = new HBox();
            headerHBox2.getChildren().add(new Label("Return date"));
            header.setLeft(headerHBox);
            header.setRight(headerHBox2);
            try {
                for (Loan lateLoan : loanManager.getLateLoans()) {
                    BorderPane borderPane = new BorderPane();
                    HBox hBox = new HBox();
                    hBox.getChildren().add(new Label(String.valueOf(lateLoan.getId())));
                    hBox.getChildren().add(new Label(String.valueOf(lateLoan.getCustomerId())));
                    HBox hBox2 = new HBox();
                    hBox2.getChildren().add(new Label(String.valueOf(lateLoan.getReturnBy())));
                    borderPane.setLeft(hBox);
                    borderPane.setRight(hBox2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
