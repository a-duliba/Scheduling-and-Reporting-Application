package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Report;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Appointment Data Access Object Implementation Class.
 */
public class AppointmentDAOImpl implements DAO<Appointment> {

    /**
     * Gets all appointments from the database and puts them in an observable list.
     * @return An observable list of appointments.
     */
    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try{
            String sql = "SELECT appointments.*, contacts.Contact_ID, contacts.Contact_Name FROM appointments, contacts WHERE appointments.Contact_ID=contacts.Contact_ID";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                LocalDateTime createdDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int customer_id = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");
                String contact = rs.getString("Contact_Name");
                Appointment appointmentResult = new Appointment(id, title, description, location, type, start, end, createdDate, createdBy, lastUpdate, lastUpdatedBy,
                        customer_id, userId, contactID, contact);
                appointments.add(appointmentResult);
            }
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
        return appointments;
    }

    /**
     * Deletes a selected appointment.
     * @param selectedAppointment The appointment that was selected on the Schedule Screen.
     * @return Deletes the selected appointment from the database.
     */
    public static boolean deleteAppointment(Appointment selectedAppointment) {
        return getAllAppointments().remove(selectedAppointment);
    }

    /**
     * Gets all results from a ResultSet appointment object and retrieves the data from it.
     * @param results The data extracted from each column of an appointment object
     * @return An appointment with all of its data loaded to each column.
     * @throws SQLException
     */
    private static Appointment extractFromResults(ResultSet results) throws SQLException {
        return new Appointment(
                results.getInt("Appointment_ID"),
                results.getString("Title"),
                results.getString("Description"),
                results.getString("Location"),
                results.getString("Type"),
                results.getTimestamp("Start").toLocalDateTime(),
                results.getTimestamp("End").toLocalDateTime(),
                results.getTimestamp("Create_Date").toLocalDateTime(),
                results.getString("Created_By"),
                results.getTimestamp("Last_Update").toLocalDateTime(),
                results.getString("Last_Updated_By"),
                results.getInt("Customer_ID"),
                results.getString("Customer_Name"),
                results.getInt("Contact_ID"),
                results.getString("Contact_Name"),
                results.getInt("User_ID"),
                results.getString("User_Name")
        );
    }

    /**
     * Gets an appointment with a specific integer value.
     * @param id ID of the specific appointment being queried.
     * @return Either an empty appointment or an appointment containing a specific ID.
     */
    public Optional<Appointment> get(int id) {
        try{
            String sql = "SELECT appointments.*, User_Name, Contact_Name, Customer_Name FROM appointments, customers, contacts, users WHERE " +
                    "appointments.Customer_ID=customers.Customer_ID and appointments.Contact_ID=contacts.Contact_ID and appointments.User_ID=users.User_ID and " +
                    "appointments.Appointments_ID=" + id;
            Connection connection = JDBC.getConnection();
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            if(results.next()) {
                return Optional.of(extractFromResults(results));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Gets all appointments from the database.
     * @return An ObservableList of appointments.
     */
    public ObservableList<Appointment> getAll() {
        try{
            String sql = "SELECT appointments.*, contacts.Contact_ID, contacts.Contact_Name FROM appointments, contacts WHERE appointments.Contact_ID=contacts.Contact_ID";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            ObservableList<Appointment> appointments = FXCollections.observableArrayList();

            while (rs.next()) {
                appointments.add(extractFromResults(rs));
            }
            return appointments;
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
        return FXCollections.observableArrayList();
    }

    /**
     * Inserts an appointment into the database.
     * @param appointment Appointment being inserted into the database.
     * @return If true, adds the appointment, otherwise prints an error.
     */
    public boolean insert(Appointment appointment) {
        try {
            String sql = "INSERT INTO appointments VALUE(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; //null is autogen aptID PK
            Connection connection = JDBC.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); //generates in appointments table an empty row in table to be filled in with bind variables above later entered by user
            ps.setString(1, appointment.getTitle()); //1 is the first question mark
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType());
            ps.setTimestamp(5, Timestamp.valueOf(appointment.getStartDateTime()));
            ps.setTimestamp(6, Timestamp.valueOf(appointment.getEndDateTime()));
            ps.setTimestamp(7, Timestamp.valueOf(appointment.getCreatedDate()));
            ps.setString(8, appointment.getCreatedBy());
            ps.setTimestamp(9, Timestamp.valueOf(appointment.getLastUpdate()));
            ps.setString(10, appointment.getLastUpdateBy());
            ps.setString(11, String.valueOf(appointment.getCustomerId()));
            ps.setString(12, String.valueOf(appointment.getUserId()));
            ps.setString(13, String.valueOf(appointment.getContactId()));
            //fills in generated row with data in appointments
            int result = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys(); //gets a result set from above with all the values plus new one we inserted

            if(result == 1 & rs.next()){ //rs.next() advances to the first row in result set
                appointment.setAppointmentId(rs.getInt(1)); //rs.getInt(1) gets info out of the first row puts in appointment object
                //creates from appointment class an int appointmentId object to have data inserted into
                return true; //returns inserted object
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    /**
     * Updates an appointment in database.
     * @param appointment Appointment being updated in the database.
     * @return If true, updates the appointment, otherwise prints an error.
     */
    public boolean update(Appointment appointment){
        try {
            String sql = "UPDATE appointments SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, Create_Date=?, Created_By=?, Last_Update=?, Last_Updated_By=?," +
                    " Customer_ID=?, Contact_ID=?, User_ID=? WHERE Appointment_ID=?";
            Connection connection = JDBC.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, appointment.getTitle());
            statement.setString(2, appointment.getDescription());
            statement.setString(3, appointment.getLocation());
            statement.setString(4, appointment.getType());
            statement.setTimestamp(5, Timestamp.valueOf(appointment.getStartDateTime()));
            statement.setTimestamp(6, Timestamp.valueOf(appointment.getEndDateTime()));
            statement.setTimestamp(7, Timestamp.valueOf(appointment.getCreatedDate()));
            statement.setString(8, appointment.getCreatedBy());
            statement.setTimestamp(9, Timestamp.valueOf(appointment.getLastUpdate()));
            statement.setString(10, appointment.getLastUpdateBy());
            statement.setString(11, String.valueOf(appointment.getCustomer()));
            statement.setString(12, String.valueOf(appointment.getContact()));
            statement.setString(13, String.valueOf(appointment.getUser()));
            statement.setString(14, String.valueOf(appointment.getCustomerId()));
            int result = statement.executeUpdate();

            if(result == 1) return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    /**
     * Deletes an appointment from Database.
     * @param id ID of the Appointment being deleted.
     * @return If true, deletes the appointment, otherwise prints an error.
     */
    public boolean delete(int id) {
        try {
            String sql = "DELETE FROM appointments WHERE Appointment_ID=" + id;
            Connection connection = JDBC.getConnection();
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(sql);

            if(result == 1) return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    /**
     * Uses a customers customerId to delete all their appointments from the database.
     * @param customerId ID of the Customer whos appointments are being deleted.
     * @return If true, deletes all the appointments, otherwise prints an error.
     */
    public boolean deleteAll(int customerId) {
        try {
            String sql = "DELETE FROM appointments WHERE Customer_ID=" + customerId;
            Connection connection = JDBC.getConnection();
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(sql);

            if(result > 0) return true;

        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    /**
     * Selects all appointments by their LocalDateTime values and creates an array list filtered by type, month, and total.
     * @return An array list of appointments that have been filtered by type, month, and total.
     */
    public static ArrayList generateTypeMonthReport() {
        try {
            String sql = "Select month(start) as month, type, count(*) as total from appointments GROUP BY month, type;";
            Connection connection = JDBC.getConnection();
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            ArrayList<Report> typeMonthReport = new ArrayList<>();

            while(results.next()) {
                Report row = new Report(results.getLong("month"), results.getString("type"), results.getLong("total"));
                typeMonthReport.add(row);
            }

            return typeMonthReport;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return new ArrayList<>();
    }

    /**
     * Selects all appointments by their LocalDateTime values and creates an array list filtered by contact name, year, and total.
     * @return An array list of appointments that have been filtered by contact name, year, and total.
     */
    public static ArrayList generateContactYearReport() {
        try {
            String sql = "Select year(start) as year, contacts.Contact_Name as Contact, count(*) as total from appointments JOIN contacts on " +
                    "contacts.Contact_ID=appointments.Contact_ID GROUP BY year, contact;";
            Connection connection = JDBC.getConnection();
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            ArrayList<Report> typeMonthReport = new ArrayList<>();

            while(results.next()) {
                Report row = new Report(results.getLong("year"), results.getString("Contact"), results.getLong("total"));
                typeMonthReport.add(row);
            }

            return typeMonthReport;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return new ArrayList<>();
    }

}
