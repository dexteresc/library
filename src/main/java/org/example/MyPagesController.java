package org.example;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import org.library.*;

import java.io.IOException;

public class MyPagesController {
    public Label firstName;
    public Label lastName;
    public Label phoneNumber;
    public Label email;
    public VBox loanView;

    private AuthenticationModel authenticationModel;
    private Account account;
    private LoanModel loanModel;

    public void initialize() {
        if (authenticationModel == null) {
            this.authenticationModel = App.getAppModel().getAuthenticationModel();
            this.account = authenticationModel.getAccount();
        }
        if (loanModel == null) {
            this.loanModel = App.getAppModel().getLoanModel();
        }
        firstName.setText(account.getGivenName());
        lastName.setText(account.getFamilyName());
        phoneNumber.setText(account.getPhoneNumber());
        email.setText(account.getEmail());

        for (MediaItem mediaItem :
                loanModel.getMediaItemList()) {
            System.out.println("ran");
            loanModuleCreate(mediaItem);
        }
    }

    private void loanModuleCreate(MediaItem mediaItem) {
        BorderPane borderPane = new BorderPane();
        Label title = new Label(mediaItem.getMedia().getTitle());
        borderPane.setLeft(title);
    }

    public void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}
