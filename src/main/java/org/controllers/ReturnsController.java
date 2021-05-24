package org.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.library.loan.ReturnsModel;

import java.net.URL;
import java.util.ResourceBundle;

public class ReturnsController implements Controller {

    private ReturnsModel returnsModel;

    @FXML
    private TextField barcodeTextField;

    @FXML
    private Button submitButton;

    @FXML
    private Label errorLabel;

    @FXML
    private Label confirmationLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.returnsModel = App.getAppModel().getReturnsModel();

        this.barcodeTextField.textProperty().addListener(change -> this.submitButton.setDisable(
                this.barcodeTextField.getText().isEmpty()
        ));

        this.setNodeVisible(this.errorLabel, false);
        this.setNodeVisible(this.confirmationLabel, false);
    }

    /**
     * Used to update node visibility.
     */
    private void setNodeVisible(Node node, boolean visible) {
        node.setVisible(visible);
        node.setManaged(visible);
    }

    public void returnItem() {
        Long mediaItemId = Long.parseLong(this.barcodeTextField.getText());

        try {
            this.setNodeVisible(this.errorLabel, false);
            this.confirmationLabel.setText(this.returnsModel.returnById(mediaItemId));
            this.setNodeVisible(this.confirmationLabel, true);
        } catch (Exception exception) {
            exception.printStackTrace();
            this.errorLabel.setText("Failed to return item. Please scan the item or enter id manually.");
            this.setNodeVisible(this.errorLabel, true);
            this.setNodeVisible(this.confirmationLabel, false);
        }
    }

}
