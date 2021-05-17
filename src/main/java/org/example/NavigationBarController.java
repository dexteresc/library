package org.example;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.library.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Navigation Bar Controller
 *
 * Provides a shared navigation bar that is state-aware.
 */
public class NavigationBarController implements Initializable {
    private static final Logger logger = LogManager.getLogger();

    private RootController rootController;
    private Destination activeDestination = Destination.HOME;

    private AuthenticationModel authenticationModel;
    private LoanModel loanModel;

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

    @FXML
    private Button loanButton;

    @FXML
    private Button previousButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Get the authentication model
        this.authenticationModel = App.getAppModel().getAuthenticationModel();

        // Get the loan model
        this.loanModel = App.getAppModel().getLoanModel();

        // Listen to changes made to loan model
        this.configureLoanListener();
    }

    private void configureLoanListener() {
        this.loanModel.getMediaItemList().addListener((ListChangeListener<MediaItem>) change -> {
            this.updateLoanButton(true);
        });
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

    /**
     * Used to update node visibility.
     */
    private void setNodeVisible(Node node, boolean visible) {
        node.setVisible(visible);
        node.setManaged(visible);
    }

    /**
     * Updates the actions available to the user based on the current application state.
     */
    private void updateAvailableActions() {
        logger.debug("Updating available actions...");

        boolean isAuthenticated = this.authenticationModel.isAuthenticated();
        boolean isCustomer = this.authenticationModel.isCustomer();
        boolean isStaff = this.authenticationModel.isStaff();
        boolean onlyPrevious = activeDestination == Destination.LOGIN || activeDestination == Destination.REGISTER;

        this.setNodeVisible(this.registerButton, !isAuthenticated && !onlyPrevious);
        this.setNodeVisible(this.loginButton, !isAuthenticated && !onlyPrevious);
        this.setNodeVisible(this.myPagesButton, isAuthenticated && isCustomer && !onlyPrevious);
        this.setNodeVisible(this.adminButton, isAuthenticated && isStaff && !onlyPrevious);
        this.setNodeVisible(this.logoutButton, isAuthenticated && !onlyPrevious);
        this.setNodeVisible(this.previousButton, onlyPrevious);

        this.updateLoanButton(!onlyPrevious);
    }

    /**
     * Updates the loan button based on the current loan state.
     */
    private void updateLoanButton(boolean canBeVisible) {
        List<MediaItem> mediaItemList = this.loanModel.getMediaItemList();

        if (mediaItemList.size() > 0) {
            this.loanButton.setText("New Loan (" + mediaItemList.size() + ")");
        }

        this.setNodeVisible(loanButton, mediaItemList.size() > 0 && canBeVisible);
    }

    // NOTE: Should only be called from RootController.
    public void setRootController(RootController rootController) {
        this.rootController = rootController;
    }

    // NOTE: Should only be called from RootController.
    // Called by RootController after present is called.
    public void willSetActiveDestination(Destination destination) {
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
    public void navigateToNewLoan() {
        this.navigateTo(Destination.NEW_LOAN);
    }

    @FXML
    public void navigateToPrevious() {
        this.navigateTo(Destination.PREVIOUS);
    }

    @FXML
    public void logout() {
        this.authenticationModel.logout();
        this.loanModel.setCustomer(null);
        this.navigateToHome();
    }

}
