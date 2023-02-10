package controller;

import helper.*;
import helper.Utilites.Alerts;
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
import model.Contact;
import model.Customer;
import model.User;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Add Appointment Screen Controller Class.
 * Gives the user the ability to add/schedule appointments.
 */
public class AddAppointmentScreen implements Initializable {
    /**
     * Gets the systems default locale area for internationalization and sets it to CURRENTLOCALE.
     */
    private final static Locale CURRENTLOCALE = Locale.getDefault();
    /**
     * Creates a Resource Bundle for internationalization named MESSAGES that checks the systems local for what language to use.
     */
    private final static ResourceBundle MESSAGES = ResourceBundle.getBundle("Resource/Nat", CURRENTLOCALE);

    @FXML
    public Label userNameLbl;
    @FXML
    public TextField appointmentIdTxt;
    @FXML
    public TextField titleTxt;
    @FXML
    public TextField locationTxt;
    @FXML
    public TextField typeTxt;
    @FXML
    public ComboBox<Contact> contactCb;
    @FXML
    public ComboBox<Customer> customerIdCb ;
    @FXML
    public ComboBox<User> userIdCb;
    @FXML
    public ComboBox<String> startTimeCb;
    @FXML
    public ComboBox<String> endTimeCb;
    @FXML
    public DatePicker startDateDp;
    @FXML
    public DatePicker endDateDp;
    @FXML
    public TextArea descriptionTa;
    @FXML
    public Button saveBtn;
    @FXML
    public Button backBtn;

    /**
     * Adds appointment to the database.
     * This method takes the values from the form fields (title, description, location, type, start time, end time, created date, created by, last update, last updated by, customer ID, user ID, and contact ID),
     * formats the start and end times into LocalTime objects.
     * Before adding the appointment into the database, it checks for overlapping times by using a stream/lambda expression.
     * The purpose of this lambda expression is to act as a filter so that no overlapping appointments are scheduled,
     * and to display an error message if there is one.
     * If there are no overlapping appointments,
     * the values are inserted into the appointments' table in the database using a SQL INSERT statement and a prepared statement.
     * @throws SQLException if a database error occurs.
     */
    public void addAppointment() {
        try {
            String startStrTime = startTimeCb.getValue();
            DateTimeFormatter startDateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            LocalTime startLocalTime = LocalTime.parse(startStrTime, startDateTimeFormatter);

            String endStrTime = endTimeCb.getValue();
            DateTimeFormatter endDateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            LocalTime endLocalTime = LocalTime.parse(endStrTime, endDateTimeFormatter);

            String sql = "INSERT INTO appointments VALUE(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            String title = titleTxt.getText();
            String description = descriptionTa.getText();
            String location = locationTxt.getText();
            String type = typeTxt.getText();
            LocalDateTime start = LocalDateTime.of(startDateDp.getValue(), startLocalTime);
            LocalDateTime end = LocalDateTime.of(endDateDp.getValue(), endLocalTime);
            LocalDateTime createdDate = LocalDateTime.of(LocalDate.now(), LocalTime.now());
            String createdBy = String.valueOf(userIdCb.getValue().getUserName());
            LocalDateTime lastUpdate = LocalDateTime.of(LocalDate.now(), LocalTime.now());
            String lastUpdatedBy = String.valueOf(userIdCb.getValue().getUserName());
            int customerId = customerIdCb.getValue().getCustomerId();
            int userId = userIdCb.getValue().getUserId();
            int contactId = contactCb.getValue().getId();

            if(AppointmentDAOImpl.getAllAppointments().stream().filter(appointment -> appointment.getCustomerId() == customerId)
                    .anyMatch(appointment ->
                    ((appointment.getStartDateTime().isEqual(start) || appointment.getStartDateTime().isBefore(start)) && (appointment.getEndDateTime().isAfter(start)))
                    || (appointment.getStartDateTime().isBefore(end) && ((appointment.getEndDateTime().isEqual(end) || appointment.getEndDateTime().isAfter(end))))
                    || ((appointment.getStartDateTime().isEqual(start) || appointment.getStartDateTime().isAfter(start)) && ((appointment.getEndDateTime().isEqual(end) || appointment.getEndDateTime().isBefore(end))))))
                    {
                Alerts.information((MESSAGES.getString("errorTitle") + "\n\n" + MESSAGES.getString("customerOverlap1") + "\n\n" + MESSAGES.getString("customerOverlap2")));
            }else {

                ps.setString(1, title);
                ps.setString(2, description);
                ps.setString(3, location);
                ps.setString(4, type);
                ps.setTimestamp(5, Timestamp.valueOf(start));
                ps.setTimestamp(6, Timestamp.valueOf(end));
                ps.setString(7, String.valueOf(createdDate));
                ps.setString(8, createdBy);
                ps.setString(9, String.valueOf(lastUpdate));
                ps.setString(10, lastUpdatedBy);
                ps.setInt(11, customerId);
                ps.setInt(12, userId);
                ps.setInt(13, contactId);

                ps.execute();
                AppointmentDAOImpl.getAllAppointments();
            }

        }catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Save the appointment to the database.
     * Formats the start and end times into LocalTime objects,
     * checks for exceptions,
     * if there are any present, displays an error message to the user,
     * if none are present,
     * calls the addAppointment method to add the appointment to the database.
     * Initializes and opens the Schedule Screen.
     * @param event The event that triggers this method (the save button being clicked)
     * @throws IOException If an I/O error occurs.
     */
    @FXML
    void onActionSaveBtn(ActionEvent event) throws  IOException {
        String startStrTime = startTimeCb.getValue();
        DateTimeFormatter startDateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        LocalTime startLocalDate = LocalTime.parse(startStrTime, startDateTimeFormatter);

        String endStrTime = endTimeCb.getValue();
        DateTimeFormatter endDateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        LocalTime endLocalDate = LocalTime.parse(endStrTime, endDateTimeFormatter);

        if (titleTxt.getText().isEmpty()) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("appointmentTextFieldEmpty1") + "\n" + MESSAGES.getString("appointmentTextFieldEmpty2"));
            error.showAndWait();
        } else if (locationTxt.getText().isEmpty()) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("appointmentTextFieldEmpty1") + "\n" + MESSAGES.getString("appointmentTextFieldEmpty2"));
            error.showAndWait();
        } else if (typeTxt.getText().isEmpty()) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("appointmentTextFieldEmpty1") + "\n" + MESSAGES.getString("appointmentTextFieldEmpty2"));
            error.showAndWait();
        } else if (contactCb.getSelectionModel().isEmpty()) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("appointmentTextFieldEmpty1") + "\n" + MESSAGES.getString("appointmentTextFieldEmpty2"));
            error.showAndWait();
        } else if (customerIdCb.getSelectionModel().isEmpty()) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("appointmentTextFieldEmpty1") + "\n" + MESSAGES.getString("appointmentTextFieldEmpty2"));
            error.showAndWait();
        } else if (userIdCb.getSelectionModel().isEmpty()) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("appointmentTextFieldEmpty1") + "\n" + MESSAGES.getString("appointmentTextFieldEmpty2"));
            error.showAndWait();
        } else if (startTimeCb.getSelectionModel().isEmpty()) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("appointmentTextFieldEmpty1") + "\n" + MESSAGES.getString("appointmentTextFieldEmpty2"));
            error.showAndWait();
        } else if (endTimeCb.getSelectionModel().isEmpty()) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("appointmentTextFieldEmpty1") + "\n" + MESSAGES.getString("appointmentTextFieldEmpty2"));
            error.showAndWait();
        } else if (startDateDp.getEditor().getText().isEmpty()) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("appointmentTextFieldEmpty1") + "\n" + MESSAGES.getString("appointmentTextFieldEmpty2"));
            error.showAndWait();
        } else if (endDateDp.getEditor().getText().isEmpty()) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("appointmentTextFieldEmpty1") + "\n" + MESSAGES.getString("appointmentTextFieldEmpty2"));
            error.showAndWait();
        } else if (descriptionTa.getText().isEmpty()) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("appointmentTextFieldEmpty1") + "\n" + MESSAGES.getString("appointmentTextFieldEmpty2"));
            error.showAndWait();
        } else if (startTimeCb.getValue().equals(endTimeCb.getValue())) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("startEndTimesSame"));
            error.showAndWait();
        } else if (endLocalDate.isBefore(startLocalDate)) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("endTimeBeforeStart"));
            error.showAndWait();
        } else if (startDateDp.getValue().isBefore(LocalDate.now())) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("AppointmentBeforeNow"));
            error.showAndWait();
        }else if(!Objects.equals(startDateDp.getEditor().getText(), endDateDp.getEditor().getText())) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("endStartDateConflict"));
            error.showAndWait();
        }
        else {
            addAppointment();
        }

        String string = userNameLbl.getText();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ScheduleScreen.fxml"));
        Parent newRoot = loader.load();

        ScheduleScreen controller = loader.getController();
        controller.initializeUser(string);

        Stage newStage = new Stage();
        newStage.setScene(new Scene(newRoot));
        newStage.setTitle("ScheduleScreen");
        newStage.setResizable(true);
        newStage.initStyle(StageStyle.DECORATED);
        newStage.setOnCloseRequest(windowEvent -> Platform.exit());

        Stage currentStage = (Stage) backBtn.getScene().getWindow();
        currentStage.close();

        newStage.show();
    }

    /**
     * The method onActionBackBtn is an event handler for the "back" button in the user interface.
     * When the button is clicked, the current stage is closed and a new stage is created, displaying
     * the ScheduleScreen. The user's name is passed to the ScheduleScreen and initialized in the ScheduleScreen controller.
     * @param event the ActionEvent that is triggered when the button is clicked
     * @throws IOException if the FXML file for the ScheduleScreen cannot be loaded
     */
    @FXML
    void onActionBackBtn(ActionEvent event) throws IOException {

        String string = userNameLbl.getText();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ScheduleScreen.fxml"));
        Parent newRoot = loader.load();

        ScheduleScreen controller = loader.getController();
        controller.initializeUser(string);

        Stage newStage = new Stage();
        newStage.setScene(new Scene(newRoot));
        newStage.setTitle("ScheduleScreen");
        newStage.setResizable(true);
        newStage.initStyle(StageStyle.DECORATED);
        newStage.setOnCloseRequest(windowEvent -> Platform.exit());

        Stage currentStage = (Stage) backBtn.getScene().getWindow();
        currentStage.close();

        newStage.show();
    }

    /**
     * This method initializes all AddAppointmentScreen fields.
     * The username label is set to the selected user, the contact, customer, and user comboboxes are populated with the data from the respective DAOImpl.
     * The start and end time comboboxes are populated with the time range between 8:00 AM and 10:00 PM, with increments of 15 minutes.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        userNameLbl.setText(ScheduleScreen.getSelectedUser());
        contactCb.setItems(ContactDAOImpl.getAllContacts());
        customerIdCb.setItems(CustomerDAOImpl.getAllCustomers());
        userIdCb.setItems(UserDAOImpl.getAllUsers());

        LocalTime open = LocalTime.of(8, 0);
        LocalTime close = LocalTime.of(22,1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

        while (open.isBefore(close.plusSeconds(1))) {
            String timeToEnter = open.format(formatter);
            startTimeCb.getItems().add(timeToEnter);
            endTimeCb.getItems().add(timeToEnter);
            open = open.plusMinutes(15);
        }
    }
}