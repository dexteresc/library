package org.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.library.account.Account;
import org.library.loan.Loan;
import org.library.loan.LoanManager;
import org.library.loan.LoanModel;
import org.library.security.AuthenticationModel;

import java.net.URL;
import java.util.ResourceBundle;

public class MyPagesController implements Controller {
    public Label firstName;
    public Label lastName;
    public Label phoneNumber;
    public Label email;
    public VBox rightVBox;
    public VBox loanView;
    public Label loanViewTitle;
    HBox mainHBox;
    VBox idVBox;
    VBox returnDateVBox;
    VBox titleVBox;
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
            mainHBox = new HBox();
            mainHBox.getStyleClass().add("mainHBox");
            idVBox = new VBox();
            titleVBox = new VBox();
            returnDateVBox = new VBox();
            idVBox.getChildren().add(new Label("ID"));
            titleVBox.getChildren().add(new Label("Title"));
            returnDateVBox.getChildren().add(new Label("Return date"));

            mainHBox.getChildren().add(idVBox);
            mainHBox.getChildren().add(titleVBox);
            mainHBox.getChildren().add(returnDateVBox);
            loanView.getChildren().add(mainHBox);
            try {
                for (Loan loan :
                        loanManager.getActiveCustomerLoans(authenticationModel.getAccount().getId())) {
                    idVBox.getChildren().add(new Label(String.valueOf(loan.getId())));
                    returnDateVBox.getChildren().add(new Label(String.valueOf(loan.getReturnBy())));
                    // todo title titleVBox.getChildren().add(x)
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (authenticationModel.isStaff()) {
            loanViewTitle.setText("Late loans");
            mainHBox = new HBox();
            mainHBox.getStyleClass().add("mainHBox");

            idVBox = new VBox();
            VBox customerIDVBox = new VBox();
            titleVBox = new VBox();
            returnDateVBox = new VBox();
            idVBox.getChildren().add(new Label("Media ID"));
            customerIDVBox.getChildren().add(new Label("Customer ID"));
            returnDateVBox.getChildren().add(new Label("Return date"));
            mainHBox.getChildren().add(idVBox);
            mainHBox.getChildren().add(titleVBox);
            mainHBox.getChildren().add(customerIDVBox);
            mainHBox.getChildren().add(returnDateVBox);
            loanView.getChildren().add(mainHBox);
            try {
                System.out.println(loanManager.getLateLoans());
                for (Loan lateLoan : loanManager.getLateLoans()) {
                    idVBox.getChildren().add(new Label(String.valueOf(lateLoan.getId())));
                    // TODO: 5/20/2021 Title vbox add
                    customerIDVBox.getChildren().add(new Label(String.valueOf(lateLoan.getCustomerId())));
                    returnDateVBox.getChildren().add(new Label(String.valueOf(lateLoan.getReturnBy())));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
