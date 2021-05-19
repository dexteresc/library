package org.example;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.library.AdminModel;
import org.library.Author;
import org.library.Book;
import org.library.Media;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable{
    AdminModel adminModel;
    @FXML
    Button backButton;
    @FXML
    VBox informationVBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // get adminModel
        this.adminModel = App.getAppModel().getAdminModel();
        Media media = adminModel.getMedia();

        informationVBox.getChildren().clear();

        // Create module

        // Title
        TextArea titleArea = new TextArea();
        titleArea.setWrapText(true);
        titleArea.setText(media.getTitle());
        HBox titleBox = new HBox();
        titleBox.getChildren().add(new Label("Title:"));
        titleBox.getChildren().add(titleArea);
        informationVBox.getChildren().add(titleBox);

/*
        // Authors
        if (media instanceof Book){
            VBox authorVBox = new VBox();
            List<Author> authorList = ((Book) media).getAuthors();
            for (Author author :
                    authorList) {
                HBox authorBox = new HBox();
                TextField givenName = new TextField(author.getGivenName());
                TextField familyName = new TextField(author.getFamilyName());
                authorBox.getChildren().add(givenName);
                authorBox.getChildren().add(familyName);
                authorVBox.getChildren().add(authorBox);
            }
            informationVBox.getChildren().add(new Label("Authors:"));
            informationVBox.getChildren().add(authorVBox);
        }

 */





    }
}
