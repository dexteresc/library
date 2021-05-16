package org.example;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.library.AuthenticationModel;

import java.net.URL;
import java.util.ResourceBundle;

public class NavigationBarController implements Initializable {
    private static Logger logger = LogManager.getLogger();

    private RootController rootController;
    private Destination activeDestination = Destination.HOME;

    private AuthenticationModel authenticationModel;

    @FXML
    private Button registerButton;

    @FXML
    private Button loginButton;

    @FXML
    private Button myPagesButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button adminButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub

        this.authenticationModel = App.getAppModel().getAuthenticationModel();

        this.updateAvailableActions();
    }

    private void navigateTo(Destination destination) {
        logger.info("Navigating to " + destination.name() + "...");

        try {
            this.rootController.present(destination);
            this.updateAvailableActions();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void setNodeVisible(Node node, boolean visible) {
        node.setVisible(visible);
        node.setManaged(visible);
    }

    private void updateAvailableActions() {
        logger.debug("Updating available actions...");

        boolean isAuthenticated = this.authenticationModel.isAuthenticated();
        boolean isCustomer = this.authenticationModel.isCustomer();
        boolean isStaff = this.authenticationModel.isStaff();

        this.setNodeVisible(this.registerButton, !isAuthenticated && activeDestination != Destination.LOGIN  && activeDestination != Destination.REGISTER);
        this.setNodeVisible(this.loginButton, !isAuthenticated && activeDestination != Destination.LOGIN && activeDestination != Destination.REGISTER);
        this.setNodeVisible(this.myPagesButton, isAuthenticated && isCustomer);
        this.setNodeVisible(this.adminButton, isAuthenticated && isStaff);
        this.setNodeVisible(this.logoutButton, isAuthenticated);
    }

    public void setRootController(RootController rootController) {
        this.rootController = rootController;
    }

    // Called by RootController after present is called.
    public void setActiveDestination(Destination destination) {
        logger.info("Received new active destination " + destination.name() + ".");

        this.activeDestination = destination;
        this.updateAvailableActions();
    }

    @FXML
    public void navigateToLogin() {
        this.navigateTo(Destination.LOGIN);
    }

    @FXML
    public void navigateToRegister() {
        this.navigateTo(Destination.REGISTER);
    }

    @FXML
    public void navigateToHome() {
        this.navigateTo(Destination.HOME);
    }

    @FXML
    public void navigateToMyPages() {
        this.navigateTo(Destination.MY_PAGES);
    }

    @FXML
    public void navigateToAdmin() {
        this.navigateTo(Destination.ADMIN);
    }

    @FXML
    public void logout() {
        this.authenticationModel.logout();
        this.navigateToHome();
    }

}
