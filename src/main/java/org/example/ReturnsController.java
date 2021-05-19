package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
    }

    public void returnItem() {
        Long mediaItemId = Long.getLong(this.barcodeTextField.getText());
        // TODO: Return item
    }

}
