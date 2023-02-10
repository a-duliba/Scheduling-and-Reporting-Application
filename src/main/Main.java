package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class
 */
public class Main extends Application {
    /**
     * Starts the C195 performance assessment scheduling application.
     * @param stage Stage which starts the scheduling application
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    /**
     * Main method which starts the application.
     * @param args Arguments.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
