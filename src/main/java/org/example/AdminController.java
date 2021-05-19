package org.example;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public Button saveButton;
    AdminModel adminModel;
    @FXML
    Button backButton;
    @FXML
    VBox informationVBox;

    String title;
    List<Author> authorList;
    TextArea titleArea;
    VBox authorVBox;
    TextArea descriptionArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // get adminModel
        adminModel = App.getAppModel().getAdminModel();
        informationVBox.getChildren().clear();

        // Back Button
        backButton.setOnAction(actionEvent -> {
            try {
                App.setRoot("primary");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        // Create module

        // Title
        titleArea = new TextArea();
        titleArea.setWrapText(true);
        HBox titleBox = new HBox();
        titleBox.getChildren().add(new Label("Title:"));
        titleBox.getChildren().add(titleArea);
        informationVBox.getChildren().add(titleBox);

        // Description
        HBox descriptionHBox = new HBox();
        descriptionHBox.getChildren().add(new Label("Description:"));
        descriptionArea = new TextArea();
        descriptionHBox.getChildren().add(descriptionArea);
        informationVBox.getChildren().add(descriptionHBox);

        // Authors

        authorVBox = new VBox();

        informationVBox.getChildren().add(new Label("Authors:"));
        HBox authorNewHBox = new HBox();
        informationVBox.getChildren().add(authorVBox);
        informationVBox.getChildren().add(new Label("New author:"));
        authorNewHBox.getChildren().add(new Label("Given name:"));
        TextField givenNameField = new TextField();
        authorNewHBox.getChildren().add(givenNameField);
        authorNewHBox.getChildren().add(new Label("Family: name"));
        TextField familyNameField = new TextField();
        authorNewHBox.getChildren().add(familyNameField);
        informationVBox.getChildren().add(authorNewHBox);

        // Save Button
        saveButton.setOnAction(actionEvent -> {
            if (!(titleArea.textProperty().getValue().equals(title))) {
                // TODO: 5/19/2021 Change title
            }

            if (!(givenNameField.textProperty().getValue().equals("")) || !(familyNameField.textProperty().getValue().equals(""))) {
                // TODO: Add or create new author
            }

        });

        if (adminModel.getMedia() == null) {
            createMedia();
        } else {
            editMedia();
        }
    }

    public void editMedia() {

        Media media = adminModel.getMedia();

        titleArea.setText(media.getTitle());
        descriptionArea.setText(media.getSummary());
        if (media instanceof Book) {
            authorList = ((Book) media).getAuthors();
            for (Author author :
                    authorList) {
                HBox authorBox = new HBox();
                Label authorName = new Label(author.getGivenName() + " " + author.getFamilyName());
                Button deleteAuthorButton = new Button("delete");
                deleteAuthorButton.setOnAction(actionEvent -> {
                    // TODO: 5/19/2021 Delete author
                });
                deleteAuthorButton.getStyleClass().add("coloredButton");
                authorBox.getChildren().add(authorName);
                authorBox.getChildren().add(deleteAuthorButton);
                authorVBox.getChildren().add(authorBox);
            }
        }
    }

    public void createMedia() {
    }

}
