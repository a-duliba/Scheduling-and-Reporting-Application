package controller;

import helper.JDBC;
import helper.UserDAOImpl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.User;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Login Screen Controller Class.
 * Gives the user the ability to log into the scheduling application.
 */
public class LoginScreen implements Initializable {
    /**
     * Gets the systems default locale area for internationalization and sets it to CURRENTLOCALE.
     */
    private final static Locale CURRENTLOCALE = Locale.getDefault();
    /**
     * Creates a Resource Bundle for internationalization named MESSAGES that checks the systems local for what language to use.
     */
    private final static ResourceBundle MESSAGES = ResourceBundle.getBundle("Resource/Nat", CURRENTLOCALE);
    @FXML
    public Label loginLbl;
    @FXML
    public Label userNameLbl;
    @FXML
    public TextField userNameTxt;
    @FXML
    public  Label passwordLbl;
    @FXML
    public TextField passwordTxt;
    @FXML
    public Label timeZoneLbl;
    @FXML
    public Label countryZoneLbl;
    @FXML
    public Button loginBtn;
    @FXML
    public Button closeBtn;
    @FXML
    public Label errorMessageTxt;

    /**
     * Retrieves a user object from the database based on their username and password.
     * @param username the username of the user to be retrieved
     * @param password the password of the user to be retrieved
     * @return the user object matching the provided username and password
     * @throws NoSuchElementException if no user with the provided username and password exists in the database
     */
    private User getUser(String username, String password) throws NoSuchElementException {
        UserDAOImpl userDao = new UserDAOImpl();
        Optional<User> user = userDao.getUserByUserNameAndPassword(username, password);

        return user.orElseThrow();
    }

    /**
     * The login method is responsible for logging in the user by verifying the entered username and password.
     * If the entered username and password match the ones in the database, the method opens the ScheduleScreen for the user.
     * If the entered username and password do not match, an error message is displayed.
     * @throws IOException if the FXMLLoader encounters an error while loading the ScheduleScreen.fxml file.
     */
    public void login() throws IOException {
        try {
            User user = getUser(userNameTxt.getText(), passwordTxt.getText());

            helper.Utilites.Logger.logSuccess(userNameTxt.getText(), true, "logged in at");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ScheduleScreen.fxml"));
            Parent newRoot = loader.load();

            ScheduleScreen controller = loader.getController();
            controller.initializeData(user);

            Stage newStage = new Stage();
            newStage.setScene(new Scene(newRoot));
            newStage.setTitle("ScheduleScreen");
            newStage.setResizable(true);
            newStage.initStyle(StageStyle.DECORATED);
            newStage.setOnCloseRequest(windowEvent -> Platform.exit());

            Stage currentStage = (Stage) loginBtn.getScene().getWindow();
            currentStage.close();

            newStage.show();

        } catch (NoSuchElementException ex) {
            helper.Utilites.Logger.logFailure(userNameTxt.getText(),false, "login attempt at");
            errorMessageTxt.setText(MESSAGES.getString("loginError"));
        }
    }

    /**
     * The initialize method sets the text for various labels and buttons in the login screen and establishes a database connection using JDBC.
     * @param url - the URL location of the FXML file
     * @param resourceBundle - the ResourceBundle for internationalization
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.getConnection();
        loginLbl.setText(MESSAGES.getString("loginLbl"));
        userNameLbl.setText(MESSAGES.getString("usernameLbl"));;
        passwordLbl.setText(MESSAGES.getString("passwordLbl"));;
        timeZoneLbl.setText(MESSAGES.getString("timeZoneLbl"));;
        countryZoneLbl.setText(ZoneId.systemDefault().toString());//HELP or fine?
        loginBtn.setText(MESSAGES.getString("loginBtn"));;
        closeBtn.setText(MESSAGES.getString("closeBtn"));

    }

    /**
     * Closes the connection to the database and closes the current window when the close button is clicked.
     * @param event the action event that triggered the call to this method
     * @throws IOException if an I/O error occurs while closing the connection
     */
    @FXML
    void onActionCloseBtn(ActionEvent event) throws IOException {
        JDBC.closeConnection();
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Attempts to log in the user and displays the schedule screen if successful.
     * @param event the action event that triggered the call to this method
     * @throws Exception if there is an error during the login process
     */
    @FXML
    void onActionLoginBtn(ActionEvent event) throws Exception {
        login();
    }
}
