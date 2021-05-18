package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.library.Account;
import org.library.AuthenticationModel;
import org.library.Customer;
import org.library.LoanModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewLoanController implements Initializable {

    private LoanModel loanModel;

    @FXML
    private VBox loanItemsBox;

    @FXML
    private Button startLoanButton;

    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.loanModel = App.getAppModel().getLoanModel();

        // Configure customer
        AuthenticationModel authenticationModel = App.getAppModel().getAuthenticationModel();
        if (!this.loanModel.hasCustomer() && authenticationModel.isAuthenticated()) {
            Account account = authenticationModel.getAccount();
            if (account instanceof Customer) {
                this.loanModel.setCustomer((Customer) account);
            }
        }

        this.updateStartLoanButton();
    }

    /**
     * Used to update node visibility.
     */
    private void setNodeVisible(Node node, boolean visible) {
        node.setVisible(visible);
        node.setManaged(visible);
    }

    private void updateStartLoanButton() {
        try {
            this.loanModel.validate();
            this.startLoanButton.setDisable(false);
            this.setNodeVisible(this.errorLabel, false);
        } catch (Exception exception) {
            this.setNodeVisible(this.errorLabel, true);
            this.errorLabel.setText(exception.getMessage());
            this.startLoanButton.setDisable(true);
        }
    }

    public void startLoan() {
        try {
            this.loanModel.registerLoan();
            this.loanModel.reset();
            this.navigateToHome(); // TODO: Display receipt.
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void cancelLoan() {
        this.loanModel.reset();
        this.navigateToHome();
    }

    public void navigateToHome() {
        try {
            App.setRoot("primary");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
