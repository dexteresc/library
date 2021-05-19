package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.library.account.AccountModel;
import org.library.account.Customer;
import org.library.account.CustomerType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    public Button register;
    public VBox registerBox;
    public TextField passwordField;
    public TextField phoneField;
    public TextField emailField;
    public TextField lastNameField;
    public TextField firstNameField;
    public ChoiceBox<CustomerType> membershipChoice;
    private AccountModel accountModel;
    private ObservableList<CustomerType> customerTypes = FXCollections.observableArrayList();

    Label errorLabel = new Label("Error, please make sure that everything is answered and that it's answered correctly");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (this.accountModel == null) {
            this.accountModel = App.getAppModel().getAccountModel();
        }

        membershipChoice.setItems(customerTypes);

        try {
            this.customerTypes.setAll(this.accountModel.getCustomerTypes());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void switchToLogin() throws IOException { //behövs för att kunna nå register knappen
        App.setRoot("login");
    }

    @FXML
    public void register() throws Exception { // detta gör så vi sparar allt som läggs in i databasen
        String firstName = firstNameField.textProperty().getValue();
        String lastName = lastNameField.textProperty().getValue();
        String email = emailField.textProperty().getValue();
        String phone = phoneField.textProperty().getValue();
        String password = passwordField.textProperty().getValue();
        CustomerType customerType = membershipChoice.getValue();

        // STYLING
        if (firstName.matches(".*\\d.*") || firstName.equals("") ){
            firstNameField.getStyleClass().add("fieldError");
        } else {
            firstNameField.getStyleClass().remove("fieldError");
        }
        if (lastName.matches(".*\\d.*") || lastName.equals("")){
            lastNameField.getStyleClass().add("fieldError");
        } else {
            lastNameField.getStyleClass().remove("fieldError");
        }
        if (email.contains("@")|| email.equals("")) {
            emailField.getStyleClass().add("fieldError");
        } else {
            emailField.getStyleClass().remove("fieldError");
        }
        if (!phone.matches(".*\\d.*")|| phone.equals("")) {
            phoneField.getStyleClass().add("fieldError");
        } else {
            phoneField.getStyleClass().remove("fieldError");
        }
        if (password.equals("")){
            passwordField.getStyleClass().add("fieldError");
        } else {
            passwordField.getStyleClass().remove("fieldError");
        }


        // REGISTRATION
        if (!firstName.matches(".*\\d.*") // lite krav av vad som måste ske innan man kan gå vidare till login
                && !firstName.equals("")
                && !lastName.matches(".*\\d.*")
                && !lastName.equals("")
                && email.contains("@")
                && phone.matches(".*\\d.*")
                && !password.equals("")) {
            registerBox.getChildren().remove(errorLabel);
            Customer customer = new Customer();
            customer.setGivenName(firstName);
            customer.setFamilyName(lastName);
            customer.setEmail(email);
            customer.setPhoneNumber(phone);
            customer.setCustomerType(customerType);
            accountModel.createCustomerAccount(customer, password);
            App.setRoot("Login");
        } else {
            if (!registerBox.getChildren().contains(errorLabel)) {
                registerBox.getChildren().add(errorLabel);
            }
        }
    }
}
