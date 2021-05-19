package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.library.loan.ReturnsModel;

import java.net.URL;
import java.util.ResourceBundle;

public class ReturnsController implements Initializable {

    private ReturnsModel returnsModel;

    @FXML
    private TextField barcodeTextField;

    @FXML
    private Button submitButton;

    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (this.returnsModel == null) {
            this.returnsModel = App.getAppModel().getReturnsModel();
        }

        this.barcodeTextField.textProperty().addListener(change -> {
            this.submitButton.setDisable(
                    this.barcodeTextField.getText().isEmpty()
            );
        });

        this.setNodeVisible(this.errorLabel, false);
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
            this.returnsModel.returnById(mediaItemId);
            this.setNodeVisible(this.errorLabel, false);
        } catch (Exception exception) {
            this.errorLabel.setText("Failed to return item.");
            this.setNodeVisible(this.errorLabel, true);
        }
    }

}
