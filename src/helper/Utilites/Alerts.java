package helper.Utilites;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Alert Class
 */
public class Alerts {
    /**
     * Gets the systems default locale area for internationalization and sets it to CURRENTLOCALE.
     */
    private final static Locale CURRENTLOCALE = Locale.getDefault();
    /**
     * Creates a Resource Bundle for internationalization named MESSAGES that checks the systems local for what language to use.
     */
    private final static ResourceBundle MESSAGES = ResourceBundle.getBundle("Resource/Nat", CURRENTLOCALE);

    /**
     * Holds alerts as local variable.
     */
    private static Alert alert;

    /**
     * EXIT, CANCEL, or Delete as confirmation types.
     */
    public enum ConfirmType {EXIT, CANCEL, DELETE}

    /**
     * Creates an alert display for the user to verify whether they want to proceed with their choice.
     * @param confirmType Gives user prompt to confirm choice.
     * @return boolean.
     */
    public static boolean confirmation(ConfirmType confirmType) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        String title = "confirmTitle" + confirmType;/////
        String message = "confirmMessage" + confirmType;/////

        alert.setHeaderText(MESSAGES.getString(title));
        alert.setContentText(MESSAGES.getString(message));
        Optional<ButtonType> result = alert.showAndWait();

        return (result.isPresent() && result.get() == ButtonType.OK);
    }

    /**
     * Creates a custom alert display for the user to verify whether they want to proceed with their choice.
     * @param title Title of the alert.
     * @param message The message the user sees.
     * @return Confirmation that the ok button was pressed by the user which exits the alert screen.
     */
    public static boolean customConfirmation(String title, String message) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();

        return (result.isPresent() && result.get() == ButtonType.OK);
    }

    /**
     * Alerts the user if there is an error display.
     * @param errorMessage Displays a message to user that an error as occurred.
     */
    public static void error(String errorMessage) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(MESSAGES.getString("errorTitle"));////
        alert.setContentText(errorMessage);
        alert.show();
    }

    /**
     * Alerts the user with an instructional information display.
     * @param infoMessage Instructs the user with information.
     */
    public static void information(String infoMessage) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(infoMessage);
        alert.showAndWait();
    }
}
