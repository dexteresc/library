package org.example;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.library.AdminModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public Button backButton;
    public VBox libView;
    AdminModel adminModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.adminModel = App.getAppModel().getAdminModel();
        libView.getChildren().add(new Label(adminModel.getMedia().getTitle()));
        backButton.setOnAction(actionEvent -> {
            try {
                App.setRoot("internal:previous");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
