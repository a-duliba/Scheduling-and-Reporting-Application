package helper.Utilites;

import java.io.*;
import java.time.Instant;

/**
 * Logger Class
 */
public class Logger {

    /**
     * Whenever a user successfully logs into the application, the user and time are displayed in the terminal.
     * The output to the terminal is then taken and put into a txt file which will be used to track all login attempts.
     * @param user Name of the user who attempted to log in.
     * @param success Boolean value for whether the login attempt was successful or a failure.
     * @param message The message that displays after the user and boolean values.
     */
    public static void logSuccess(String user, boolean success, String message) {
        String FILENAME = "login_activity.txt";
        try (FileWriter fw = new FileWriter(FILENAME, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {
            pw.println(user + (success ? " successfully" : " failed") + " " + message + " " + Instant.now().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Whenever a user fails to log into the application, the time is displayed in the terminal.
     * The output to the terminal is then taken and put into a txt file which will be used to track all login attempts.
     * @param success Boolean value for whether the login attempt was successful or a failure.
     * @param message the message that displays after the boolean value.
     */
    public static void logFailure(String user, boolean success, String message) {
        String FILENAME = "login_activity.txt";
        try (FileWriter fw = new FileWriter(FILENAME, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {
            pw.println(user + (success ? "successful" : " failed") + " " + message + " " + Instant.now().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


