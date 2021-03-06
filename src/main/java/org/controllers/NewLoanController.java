package org.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.library.account.Customer;
import org.library.loan.LoanModel;
import org.library.media.MediaItem;
import org.library.security.AuthenticationModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewLoanController implements Controller {

    private LoanModel loanModel;

    @FXML
    private GridPane loanItemsBox;

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
            if (authenticationModel.isCustomer()) {
                this.loanModel.setCustomer((Customer) authenticationModel.getAccount());
            }
        }
        // Add col
        // Initialize column labels.
        ObservableList<MediaItem> mediaItems = loanModel.getMediaItemList();
        loanItemsBox.getChildren().clear();
        for (int i = 0; i < mediaItems.size(); i++) { // Add mediaItems to rows.
            // TODO: 5/18/2021 Add return date and other necessary information
            loanItemsBox.addRow(i + 1, new Label(mediaItems.get(i).getMedia().getTitle()), new Label(mediaItems.get(i).getStatus().getRawValue()));
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
