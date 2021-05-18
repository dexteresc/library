package org.example;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.library.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewLoanController implements Initializable {

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
            Account account = authenticationModel.getAccount();
            if (account instanceof Customer) {
                this.loanModel.setCustomer((Customer) account);
            }
        }
        // Add col
        loanItemsBox.add(new Label("Title"), 0, 0);
        loanItemsBox.add(new Label("Return date"), 1, 0);
        ObservableList<MediaItem> mediaItems = loanModel.getMediaItemList();
        for (int i = 0; i < mediaItems.size(); i++) {
            loanItemsBox.addRow(i+1, new Label(mediaItems.get(i).getMedia().getTitle()), new Label(mediaItems.get(i).getStatus().getRawValue()));
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
