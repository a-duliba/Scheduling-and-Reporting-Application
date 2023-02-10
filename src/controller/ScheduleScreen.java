package controller;

import helper.*;
import helper.Utilites.Alerts;
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
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.User;

/**
 * Schedule Screen Controller class.
 * Gives the user the ability to see all appointments by week or month,
 * additionally the ability to navigate to forms that allow the user to add, update, delete, and see appointment reports.
 */
public class ScheduleScreen implements Initializable {
    Stage stage;
    Parent scene;
    /**
     * Holds the selected appointment in the appointment management.
     */
    private static Appointment selectedAppointment;

    /**
     * Holds the selected customer in the customer management.
     */
    private static Customer selectedCustomer;

    /**
     * Holds the selected user in the user management.
     */
    private static String selectedUser;

    /**
     * Holds the current user of the application.
     */
    private User user;

    /**
     * A formatter for dates in the format "MM/dd/yyyy".
     */
    private final static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /**
     * A formatter for dates and times in the format "MM/dd/yyyy h:mm a z", where "z" is the UTC time offset.
     */
    private final static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a z");

    /**
     * An observable list of appointments.
     */
    private final ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    /**
     * An observable list of customers.
     */
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();

    /**
     * A filtered list of appointments.
     */
    private FilteredList<Appointment> filteredAppointments = new FilteredList<>(appointments);

    /**
     * An instance of the CustomerDAOImpl class.
     */
    private final CustomerDAOImpl customerDao = new CustomerDAOImpl();

    /**
     * An instance of the AppointmentDAOImpl class.
     */
    private final AppointmentDAOImpl appointmentDao = new AppointmentDAOImpl();

    /**
     * The current locale of the application.
     */
    private final static Locale CURRENT_LOCALE = Locale.getDefault();

    /**
     * The messages resource bundle for the current locale.
     */
    private final static ResourceBundle MESSAGES = ResourceBundle.getBundle("Resource/Nat", CURRENT_LOCALE);

    /**
     * Gets the selected appointment.
     * @return The selected appointment.
     */
    public static Appointment getSelectedAppointment() {
        return selectedAppointment;
    }

    /**
     * Gets the selected customer.
     * @return The selected customer.
     */
    public static Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    /**
     * Gets the selected user.
     * @return The selected user.
     */
    public static String getSelectedUser() { return selectedUser; }


    @FXML
    public Label userNameLbl;
    @FXML
    public Label ScheduleApptLbl;
    @FXML
    public Label countryZoneLbl;
    @FXML
    public ToggleGroup ScheduleTimeOptions;
    @FXML
    public RadioButton ScheduleViewAllRb;
    @FXML
    public RadioButton ScheduleViewWeekRb;
    @FXML
    public RadioButton ScheduleViewMonthRb;

    @FXML
    public TableView<Appointment> AppointmentTableView;
    @FXML
    public TableColumn<Appointment, Integer> ScheduleApptIDCol;
    @FXML
    public TableColumn<Appointment, String> ScheduleTitleCol;
    @FXML
    public TableColumn<Appointment, String> ScheduleDescriptionCol;
    @FXML
    public TableColumn<Appointment, String> ScheduleLocationCol;
    @FXML
    public TableColumn<Appointment, Integer> ScheduleContactIdCol;
    @FXML
    public TableColumn<Appointment, String> ScheduleTypeCol;
    @FXML
    public TableColumn<Appointment, LocalDateTime> ScheduleStartTimeCol;
    @FXML
    public TableColumn<Appointment, LocalDateTime> ScheduleEndTimeCol;
    @FXML
    public TableColumn<Appointment, Integer> ScheduleCustomerIDCol;
    @FXML
    public TableColumn<Appointment, Integer> ScheduleUserIDCol;
    @FXML
    public TableColumn<Appointment, String> ScheduleContactCol;

    @FXML
    public Button ScheduleAddApptBtn;
    @FXML
    public Button ScheduleUpdateApptBtn;
    @FXML
    public Button ScheduleDeleteApptBtn;

    @FXML
    public TableView<Customer> CustomerTableView;
    @FXML
    public TableColumn<Customer, Integer> ScheduleCustomerCustomerIDCol;
    @FXML
    public TableColumn<Customer, String> ScheduleCustomerNameCol;
    @FXML
    public TableColumn<Customer, String> ScheduleCustomerAddressCol;
    @FXML
    public TableColumn<Customer, String> ScheduleCustomerPostalCodeCol;
    @FXML
    public TableColumn<Customer, String> ScheduleCustomerPhoneNumberCol;
    @FXML
    public TableColumn<Customer, String> ScheduleCustomerCountryCol;
    @FXML
    public TableColumn<Customer, String> ScheduleCustomerStateProvinceCol;
    @FXML
    public TableColumn<Customer,String> ScheduleCustomerDivisionIDCol;
    @FXML
    public TableColumn<Customer,String> ScheduleCustomerCountryIDCol;


    @FXML
    public Button ScheduleAddCustomerBtn;
    @FXML
    public Button ScheduleUpdateCustomerBtn;
    @FXML
    public Button ScheduleDeleteCustomerBtn;

    @FXML
    public Button ScheduleReportsBtn;
    @FXML
    public Button ScheduleLogoutBtn;

    /**
     * Initializes the data for the user. If there is an upcoming appointment,
     * it displays a warning, otherwise, it displays an information alert.
     * @param user the user for which data is to be initialized
     */
    public void initializeData(User user) {
        this.user = user;
        userNameLbl.setText(this.user.getUserName());
        ObservableList<Appointment> getAllAppointments = AppointmentDAOImpl.getAllAppointments();
        LocalDateTime currentTimeMinus15Min = LocalDateTime.now().minusMinutes(15);
        LocalDateTime currentTimePlus15Min = LocalDateTime.now().plusMinutes(15);
        LocalDateTime startTime;
        int getAppointmentID = 0;
        LocalDateTime displayTime = null;
        boolean appointmentWithin15Min = false;

        for (Appointment appointment: getAllAppointments) {
            startTime = appointment.getStartDateTime();
            if ((startTime.isAfter(currentTimeMinus15Min) || startTime.isEqual(currentTimeMinus15Min)) && (startTime.isBefore(currentTimePlus15Min) || (startTime.isEqual(currentTimePlus15Min)))) {
                getAppointmentID = appointment.getAppointmentId();
                displayTime = startTime;
                appointmentWithin15Min = true;
            }
        }
        if (appointmentWithin15Min !=false) {
            Alert warning = new Alert((Alert.AlertType.WARNING));
            warning.setTitle(MESSAGES.getString("warningTitle"));
            warning.setContentText(MESSAGES.getString("appointmentUpcoming") + "\n" + MESSAGES.getString("appointmentID") + getAppointmentID + "\n" +  MESSAGES.getString("appointmentDateTime") + displayTime);
            warning.showAndWait();
            System.out.println("There is an appointment within 15 minutes");
        }
        else {
            Alert warning = new Alert((Alert.AlertType.INFORMATION));
            warning.setTitle(MESSAGES.getString("alertTitle"));
            warning.setContentText(MESSAGES.getString("appointmentNotUpcoming"));
            warning.showAndWait();
            System.out.println("no upcoming appointments");
        }
    }

    /**
     * Initializes the user. Sets the username label text to the selected user.
     * @param string string representing the selected user
     */
    public void initializeUser(String string) {
        userNameLbl.setText(getSelectedUser());
    }

    /**
     * Sets the values for the appointment and customer tables.
     * The purpose of the lambda expressions are as follows:
     * Populates the start and end time cells in the dataTimeFormat of pattern "MM/dd/yyyy h:mm a z".
     */
    public void setTables() {
        try {
            AppointmentTableView.setItems(AppointmentDAOImpl.getAllAppointments());
            ScheduleApptIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            ScheduleTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            ScheduleDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            ScheduleLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            ScheduleContactIdCol.setCellValueFactory(new PropertyValueFactory<>("contactId")); //from contact observable list
            ScheduleTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            ScheduleStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
            ScheduleStartTimeCol.setCellFactory(new Callback<>() {
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
            ScheduleEndTimeCol.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
            ScheduleEndTimeCol.setCellFactory(new Callback<>() {
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
            ScheduleCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            ScheduleUserIDCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
            ScheduleContactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));

            CustomerTableView.setItems(CustomerDAOImpl.getAllCustomers());
            ScheduleCustomerCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            ScheduleCustomerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            ScheduleCustomerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
            ScheduleCustomerPostalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            ScheduleCustomerPhoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            ScheduleCustomerCountryIDCol.setCellValueFactory(new PropertyValueFactory<>("countryId"));
            ScheduleCustomerCountryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
            ScheduleCustomerDivisionIDCol.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
            ScheduleCustomerStateProvinceCol.setCellValueFactory(new PropertyValueFactory<>("division")); //make this grab name from division
        } catch (Exception ex) {
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method deletes an appointment from the appointment table view
     * by first checking if an appointment has been selected, then confirming
     * the deletion, and then executing a delete statement in the database.
     * The table view is updated to reflect the changes.
     * @throws SQLException if there is an error executing the delete statement
     */
    public void deleteAppointment() {
        try {
            selectedAppointment = AppointmentTableView.getSelectionModel().getSelectedItem();

            if (selectedAppointment == null) {
                Alert error = new Alert((Alert.AlertType.WARNING));
                error.setTitle(MESSAGES.getString("warningTitle"));
                error.setContentText(MESSAGES.getString("appointmentNotSelected"));
                error.showAndWait();
            } else {
                int selectedAppointmentID = selectedAppointment.getAppointmentId();
                String selectedAppointmentType = selectedAppointment.getType();

                String deleteStatement = "DELETE FROM appointments WHERE Appointment_ID = '" + selectedAppointmentID + "'";
                PreparedStatement ps = JDBC.getConnection().prepareStatement(deleteStatement);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle(MESSAGES.getString("deleteAppointment"));
                alert.setContentText(MESSAGES.getString("confirmMessageDELETE") + "\n" + MESSAGES.getString("appointmentID") + selectedAppointmentID + "\n" +
                        MESSAGES.getString("appointmentType") + selectedAppointmentType);
                Optional<ButtonType> answer = alert.showAndWait();
                if (answer.isPresent() && answer.get() == ButtonType.OK) {
                    ps.execute(deleteStatement);
                    AppointmentDAOImpl.deleteAppointment(selectedAppointment);
                    Alert alert1 = new Alert(Alert.AlertType.WARNING);
                    alert1.setTitle(MESSAGES.getString("warningTitle"));
                    alert1.setContentText(MESSAGES.getString("deletedAppointment"));
                    alert1.showAndWait();
                    AppointmentTableView.setItems(AppointmentDAOImpl.getAllAppointments());
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method deletes a customer from the customer table view by first checking
     * if a customer has been selected, then confirming the deletion. This is done by using a lambda expression.
     * The purpose of the lambda expressions is the following: To filter the customers to see if there are
     * any appointments linked to the customer and delete all appointments associated with the customer if the customer is deleted.
     * The method confirms the deletion and The table views are updated to reflect the changes.
     */
    void deleteCustomer() {
        if (!Alerts.confirmation(Alerts.ConfirmType.DELETE))
            return;

        Customer customer = CustomerTableView.getSelectionModel().getSelectedItem();

        if (customer == null) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("customerNotSelected"));
            error.showAndWait();
            return;
        }

        if (appointments.stream().anyMatch(appointment -> appointment.getCustomerId() == customer.getCustomerId())) {
            if (!Alerts.customConfirmation((MESSAGES.getString("deleteCustomerAppointments")), (MESSAGES.getString("linkedCustomerAppointmentsVerification1") + "\n" + MESSAGES.getString("linkedCustomerAppointmentsVerification2"))))
                return;
            if (!appointmentDao.deleteAll(customer.getCustomerId())) {
                Alerts.error(MESSAGES.getString("deleteCustomerError"));
                return;
            }
            appointments.removeIf(appointment -> appointment.getCustomerId() == customer.getCustomerId());
        }

        if (!customerDao.delete(customer.getCustomerId()))
            Alerts.error(MESSAGES.getString("deleteCustomerError"));

        customers.remove(customer);
        CustomerTableView.setItems(CustomerDAOImpl.getAllCustomers());
        AppointmentTableView.setItems(AppointmentDAOImpl.getAllAppointments());
    }

    /**
     * Filters the appointment table based on selected radio button. This is done by using a lambda expression.
     * The purpose of the lambda expressions is the following:
     * If ScheduleViewAllRb is selected, the table will display all appointments.
     * If ScheduleViewWeekRb is selected, the table will display all appointments happening in the next 7 days.
     * If ScheduleViewMonthRb is selected, the table will display all appointments happening in the current month.
     * The lambda expression does this by filtering each selection by a list of appointments and their start times.
     * The table view is updated with the relevant appointments and cell value factories are set for each column.
     */
    public void filterTable() {
        if (ScheduleViewAllRb.isSelected()) {
            ScheduleViewWeekRb.setSelected(false);
            ScheduleViewMonthRb.setSelected(false);
            AppointmentTableView.setItems(AppointmentDAOImpl.getAllAppointments());
            ScheduleApptIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            ScheduleTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            ScheduleDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            ScheduleLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            ScheduleContactIdCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
            ScheduleTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            ScheduleStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
            ScheduleEndTimeCol.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
            ScheduleCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            ScheduleUserIDCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
            ScheduleContactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        }
        if (ScheduleViewWeekRb.isSelected()) {
            ScheduleViewMonthRb.setSelected(false);
            ScheduleViewAllRb.setSelected(false);
            LocalDateTime startLDT = LocalDateTime.now();
            LocalDateTime endLDT = LocalDateTime.now().plusDays(7);
            ObservableList<Appointment> allAppointments = AppointmentDAOImpl.getAllAppointments();
            FilteredList<Appointment> appointmentsThisWeek = new FilteredList<>(allAppointments, i -> i.getStartDateTime().isAfter(startLDT) && i.getStartDateTime().isBefore(endLDT));
            AppointmentTableView.setItems(appointmentsThisWeek);
            ScheduleApptIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            ScheduleTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            ScheduleDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            ScheduleLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            ScheduleContactIdCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
            ScheduleTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            ScheduleStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
            ScheduleEndTimeCol.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
            ScheduleCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            ScheduleUserIDCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
            ScheduleContactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        }
        if (ScheduleViewMonthRb.isSelected()) {
            ScheduleViewAllRb.setSelected(false);
            ScheduleViewWeekRb.setSelected(false);
            int currentMonth = LocalDate.now().getMonthValue();
            ObservableList<Appointment> allAppointments = AppointmentDAOImpl.getAllAppointments();
            FilteredList<Appointment> appointmentsThisMonth = new FilteredList<>(allAppointments, i -> i.getStartDateTime().getMonthValue() == currentMonth);
            AppointmentTableView.setItems(appointmentsThisMonth);
            ScheduleApptIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            ScheduleTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            ScheduleDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            ScheduleLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            ScheduleContactIdCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
            ScheduleTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            ScheduleStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
            ScheduleEndTimeCol.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
            ScheduleCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            ScheduleUserIDCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
            ScheduleContactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        }
    }

    /**
     * Initialize the ScheduleScreen with the current zone, date and populates the tables with data.
     * @param url - the URL of the FXML file
     * @param resourceBundle - the resource bundle to use for localizing the view
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryZoneLbl.setText(ZoneId.systemDefault() + "  |  " + LocalDate.now().format(dateFormat));
        setTables();
    }

    /**
     * Filters the table by all times.
     * @param event - the event that triggered the method
     * @throws IOException - if an I/O error occurs
     */
    @FXML
    void onActionFilterByAll(ActionEvent event) throws IOException {
        filterTable();
    }

    /**
     * Filter the table by week.
     * @param event - the event that triggered the method
     * @throws IOException - if an I/O error occurs
     */
    @FXML
    void onActionFilterByWeek(ActionEvent event) throws IOException {
        filterTable();
    }

    /**
     * Filter the table by month.
     * @param event - the event that triggered the method
     * @throws IOException - if an I/O error occurs
     */
    @FXML
    void onActionFilterByMonth(ActionEvent event) throws IOException {
        filterTable();
    }

    /**
     * Opens the AddAppointmentScreen for the user to create a new appointment.
     * @param event - the event that triggered the method
     * @throws IOException - if an I/O error occurs
     */
    @FXML
    void onActionAddAppointmentBtn(ActionEvent event) throws IOException {
        selectedUser = userNameLbl.getText();

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddAppointmentScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Opens the UpdateAppointmentScreen for the user to update an existing appointment.
     * If no appointment is selected an alert will prompt the user to select an appointment.
     * @param event - the event that triggered the method
     * @throws IOException - if an I/O error occurs
     */
    @FXML
    void onActionUpdateAppointmentBtn(ActionEvent event) throws IOException {
        selectedAppointment = AppointmentTableView.getSelectionModel().getSelectedItem();
        selectedUser = userNameLbl.getText();

        if (selectedAppointment == null) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("appointmentNotSelected"));
            error.showAndWait();
        } else {

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/UpdateAppointmentScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * This method is called when the "Delete Appointment" button is clicked.
     * It calls the deleteAppointment() method.
     * Deletes the selected appointment
     * @param event the action event that triggered this method
     */
    @FXML
    void OnActionAppointmentDeleteBtn(ActionEvent event) {
        deleteAppointment();
    }

    /**
     * Opens the AddCustomerScreen for the user to create a new customer.
     * @param event - the event that triggered the method
     * @throws IOException - if an I/O error occurs
     */
    @FXML
    void onActionAddCustomerBtn(ActionEvent event) throws IOException {
        selectedUser = userNameLbl.getText();

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddCustomerScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Opens the UpdateCustomerScreen for the user to update an existing customer.
     * If no customer is selected an alert will prompt the user to select an customer.
     * @param event - the event that triggered the method
     * @throws IOException - if an I/O error occurs
     */
    @FXML
    void onActionUpdateCustomerBtn(ActionEvent event) throws IOException {
        selectedCustomer = CustomerTableView.getSelectionModel().getSelectedItem();
        selectedUser = userNameLbl.getText();

        if (selectedCustomer == null) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("customerNotSelected"));
            error.showAndWait();
        } else {

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/UpdateCustomerScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * This method is called when the "Delete Customer" button is clicked.
     * It calls the deleteCustomer() method.
     * Deletes the selected customer
     * @param event the action event that triggered this method
     */
    @FXML
    void OnActionCustomerDeleteBtn(ActionEvent event) {
        deleteCustomer();
    }

    /**
     * This method is called when the "Reports" button is clicked.
     * It loads the Reports screen as the scene for the current window.
     * @param event the action event that triggered this method
     * @throws IOException if an input/output error occurs while loading the scene
     */
    @FXML
    void onActionReportsBtn(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ReportsScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method is called when the "Logout" button is clicked.
     * It loads the Login screen as the scene for the current window.
     * @param event the action event that triggered this method
     * @throws IOException if an input/output error
     */
    @FXML
    void onActionLogoutBtn(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}