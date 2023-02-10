package controller;

import helper.*;
import helper.Utilites.Alerts;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import model.Country;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Add Customer Screen Controller Class.
 * Gives the user the ability to add customers.
 */
public class AddCustomerScreen implements Initializable {

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
    public TextField customerIdTxt;
    @FXML
    public TextField customerNameTxt;
    @FXML
    public TextField addressTxt;
    @FXML
    public TextField postalCodeTxt;
    @FXML
    public TextField phoneNumberTxt;
    @FXML
    public ComboBox<Country> countryCb;
    @FXML
    public ComboBox<Division> stateProvinceCb;
    @FXML
    public Button saveBtn;
    @FXML
    public Button backBtn;

    /**
     * The addCustomer method adds a new customer to the database.
     * It collects user input from all fields.
     * Before inserting the customer information into the database, it checks for duplicate customer names
     * using a stream/lambda expression.
     * The purpose of this lambda expression is to act as a filter for duplicate customer names.
     * If a duplicate customer name is found, an information alert is displayed.
     * If there are no duplicates, the customer information is inserted into the database using a PreparedStatement and the execute method.
     * @throws SQLException If there is an error executing the PreparedStatement
     */
    public void addCustomer() {
        try {
            String sql = "INSERT INTO customers VALUE(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            String name = customerNameTxt.getText();
            String address = addressTxt.getText();
            String postalCode = postalCodeTxt.getText();
            String phone = phoneNumberTxt.getText();
            LocalDateTime createdDate = LocalDateTime.of(LocalDate.now(), LocalTime.now());
            String createdBy = userNameLbl.getText();
            LocalDateTime lastUpdate = LocalDateTime.of(LocalDate.now(), LocalTime.now());
            String lastUpdatedBy = userNameLbl.getText();
            int divisionId = stateProvinceCb.getSelectionModel().getSelectedItem().getId();

            if(CustomerDAOImpl.getAllCustomers().stream().anyMatch(customer -> Objects.equals(customer.getCustomerName(), name))) {
                Alerts.information(MESSAGES.getString("customerNameDuplicate"));
            }
            else {
                ps.setString(1, name);
                ps.setString(2, address);
                ps.setString(3, postalCode);
                ps.setString(4, phone);
                ps.setString(5, String.valueOf(createdDate));
                ps.setString(6, createdBy);
                ps.setString(7, String.valueOf(lastUpdate));
                ps.setString(8, lastUpdatedBy);
                ps.setInt(9, divisionId);

                ps.execute();
                CustomerDAOImpl.getAllCustomers();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * The comboBoxSelection method initializes the items for two combo boxes stateProvinceCb and countryCb.
     * For each combo box, it sets the items with the values returned by DivisionDAOImpl.getAllDivisions() and CountryDAOImpl.getAllCountries() respectively.
     * The purpose of the lambda expressions are to set cell factories and button cells for the two combo boxes to display the name of the Division and Country objects in the combo box.
     * The countryCb combo box has an event handler attached to it to filter the items in the stateProvinceCb combo box based on the selected country.
     */
    public void comboBoxSelection() {
        stateProvinceCb.setItems(DivisionDAOImpl.getAllDivisions());
        stateProvinceCb.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Division> call(ListView<Division> divisionListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Division item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };
            }
        });
        stateProvinceCb.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Division item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    setText(item.getName());
                } else {
                    setText(null);
                }
            }
        });


        countryCb.setItems(CountryDAOImpl.getAllCountries());
        countryCb.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Country> call(ListView<Country> listView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Country item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };
            }
        });
        countryCb.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Country item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    setText(item.getName());
                } else {
                    setText(null);
                }
            }
        });
        countryCb.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int countryId = countryCb.getSelectionModel().getSelectedItem().getId();
                ObservableList<Division> filteredDivisions = FXCollections.observableArrayList();

                for (Division division : DivisionDAOImpl.getAllDivisions()) {
                    if (division.getCountryId() == countryId) {
                        filteredDivisions.add(division);
                    }
                }

                stateProvinceCb.setItems(filteredDivisions);
            }
        });
    }

    /**
     * Save the customer to the database.
     * Checks for exceptions,
     * if there are any present, displays an error message to the user,
     * if none are present,
     * calls the addCustomer method to add the customer to the database.
     * Initializes and opens the Schedule Screen.
     * @param event The event that triggers this method (the save button being clicked)
     * @throws IOException If an I/O error occurs.
     */
    @FXML
    void onActionSaveBtn(ActionEvent event) throws IOException {
        if(customerNameTxt.getText().isEmpty()) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("appointmentTextFieldEmpty1") + "\n" + MESSAGES.getString("appointmentTextFieldEmpty2"));
            error.showAndWait();
        }
        else if(addressTxt.getText().isEmpty()) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("appointmentTextFieldEmpty1") + "\n" + MESSAGES.getString("appointmentTextFieldEmpty2"));
            error.showAndWait();
        }
        else if(postalCodeTxt.getText().isEmpty()) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("appointmentTextFieldEmpty1") + "\n" + MESSAGES.getString("appointmentTextFieldEmpty2"));
            error.showAndWait();
        }
        else if(phoneNumberTxt.getText().isEmpty()) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("appointmentTextFieldEmpty1") + "\n" + MESSAGES.getString("appointmentTextFieldEmpty2"));
            error.showAndWait();
        }
        else if(countryCb.getSelectionModel().isEmpty()) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("appointmentTextFieldEmpty1") + "\n" + MESSAGES.getString("appointmentTextFieldEmpty2"));
            error.showAndWait();
        }
        else if(stateProvinceCb.getSelectionModel().isEmpty()) {
            Alert error = new Alert((Alert.AlertType.WARNING));
            error.setTitle(MESSAGES.getString("warningTitle"));
            error.setContentText(MESSAGES.getString("appointmentTextFieldEmpty1") + "\n" + MESSAGES.getString("appointmentTextFieldEmpty2"));
            error.showAndWait();
        }
        else {
            addCustomer();
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
     * Initializes the date picker with the all country/division options and sets the text of the username label to the selected user from the schedule screen.
     * @param url The URL of the FXML file.
     * @param resourceBundle The resource bundle that was specified in the FXML file.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBoxSelection();
        userNameLbl.setText(ScheduleScreen.getSelectedUser());
    }
}
