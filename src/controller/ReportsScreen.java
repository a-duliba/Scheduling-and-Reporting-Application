package controller;

import helper.AppointmentDAOImpl;
import helper.ContactDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Reports Screen Controller class.
 * Gives the user the ability to see various types of reports.
 */
public class ReportsScreen implements Initializable {

    Stage stage;
    Parent scene;

    /**
     * The dateTimeFormat field is a DateTimeFormatter instance with pattern "MM/dd/yyyy h:mm a z".
     * It is used for formatting and parsing dates and times.
     */
    private final static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a z");

    @FXML
    public ComboBox<Contact> reportsContactCb;
    @FXML
    public TableView<Appointment> contactScheduleTableView;
    @FXML
    public TableColumn<Appointment, Integer> reportsAppointmentId;
    @FXML
    public TableColumn<Appointment, String> reportsTitle;
    @FXML
    public TableColumn<Appointment, String> reportsType;
    @FXML
    public TableColumn<Appointment, String> reportsDescription;
    @FXML
    public TableColumn<Appointment, String> reportsLocation;
    @FXML
    public TableColumn<Appointment, LocalDateTime> reportsStart;
    @FXML
    public TableColumn<Appointment, LocalDateTime> reportsEnd;
    @FXML
    public TableColumn<Appointment, Integer> reportsCustomerId;

    @FXML
    public TableView<Report> totalCustomerApptByMonthTypeTableView;
    @FXML
    public TableColumn<Report, Month> reportsByMonth;
    @FXML
    public TableColumn<Report, String> reportsByType;
    @FXML
    public TableColumn<Report, Integer> reportsByTotal;

    @FXML
    public TableView<Report> appointmentsByContactYearTableView;
    @FXML
    public TableColumn<Report, String> reportsContactCol;
    @FXML
    public TableColumn<Report, Year> reportsContactYearCol;
    @FXML
    public TableColumn<Report, Integer> reportsAppointmentsByYearTotalCol;
    @FXML
    public Button reportsBackBtn;

    /**
     * Declares an {@code ObservableList} of {@code Appointment}s.
     */
    private final ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    /**
     * Declares a filtered version of the {@code appointments} list.
     */
    private final FilteredList<Appointment> filteredAppointments = new FilteredList<>(appointments);
    /**
     Declares an {@code ObservableList} to store data for the type month report.
     */
    private final ObservableList typeMonthReport = FXCollections.observableArrayList();
    /**
     * Declares an {@code ObservableList} to store data for the contact year report.
     */
    private final ObservableList contactYearReport = FXCollections.observableArrayList();
    /**
     * Populates the {@code typeMonthReport} list with data generated from the {@code AppointmentDAOImpl} class.
     */
    private void TypeMonthReport() {
        typeMonthReport.addAll(AppointmentDAOImpl.generateTypeMonthReport());}
    /**
     * Populates the {@code contactYearReport} list with data generated from the {@code AppointmentDAOImpl} class.
     */
    private void ContactYearReport() {
        contactYearReport.addAll(AppointmentDAOImpl.generateContactYearReport());
    }
    /**
     * Sets the items for the {@code reportsContactCb} combobox with data from the {@code ContactDAOImpl} class.
     */
    private void ContactComboBox() {
        reportsContactCb.setItems(ContactDAOImpl.getAllContacts());
    }

    /**
     * Initializes the appointment table view.
     * It retrieves all the appointments from the database using the {@link AppointmentDAOImpl#getAllAppointments()} method,
     * and sets the appointment data to the appointment table view using the setItems method.
     * Sets the cell value factory for each column of the table view with the respective appointment property.
     * The purpose of the lambda expressions are as follows:
     * Sets the cell factory for the start and end date columns to format the date and time according to the pattern "MM/dd/yyyy h:mm a z".
     */
    private void AppointmentsTable() {
        appointments.addAll(AppointmentDAOImpl.getAllAppointments());
        contactScheduleTableView.setItems(filteredAppointments);
        reportsAppointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        reportsTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        reportsType.setCellValueFactory(new PropertyValueFactory<>("type"));
        reportsDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        reportsLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        reportsStart.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        reportsStart.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Appointment, LocalDateTime> call(TableColumn<Appointment, LocalDateTime> appointmentLocalDateTimeTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(LocalDateTime item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setText(null);
                        } else {
                            setText(item.atZone(ZoneId.systemDefault()).format(dateTimeFormat));
                        }
                    }
                };
            }
        });
        reportsEnd.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        reportsEnd.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Appointment, LocalDateTime> call(TableColumn<Appointment, LocalDateTime> appointmentLocalDateTimeTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(LocalDateTime item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setText(null);
                        } else {
                            setText(item.atZone(ZoneId.systemDefault()).format(dateTimeFormat));
                        }
                    }
                };
            }
        });
        reportsCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
    }

    /**
     * Initializes the GUI components for the application such as ContactComboBox, AppointmentsTable, TypeMonthReport and ContactYearReport.
     * It sets the items for the totalCustomerApptByMonthTypeTableView and appointmentsByContactYearTableView, and sets the cell value factories for each table column.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ContactComboBox();
        AppointmentsTable();
        TypeMonthReport();
        ContactYearReport();

        totalCustomerApptByMonthTypeTableView.setItems(typeMonthReport);
        reportsByType.setCellValueFactory(new PropertyValueFactory<>("type"));
        reportsByMonth.setCellValueFactory(new PropertyValueFactory<>("date"));
        reportsByTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        appointmentsByContactYearTableView.setItems(contactYearReport);
        reportsContactCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        reportsContactYearCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        reportsAppointmentsByYearTotalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    /**
     * When a contact is selected in the reportsContactCb combo box. It filters the AppointmentsTable based on the selected contact's ID using a lambda expression.
     * The purpose of this lambda expression is to filter the appointments' table by the selected contact in the combobox.
     */
    @FXML
    void onActionContactCb() {
        Contact selected = reportsContactCb.getSelectionModel().getSelectedItem();
        if(selected != null) {
            filteredAppointments.setPredicate(appointment -> appointment.getContactId() == selected.getId());
        }
    }

    /**
     * Handles the action of the "Back" button. It gets the current stage, loads the ScheduleScreen.fxml file, sets the scene to the newly loaded scene, and shows the stage.
     */
    @FXML
    void OnActionBackBtn(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ScheduleScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

}