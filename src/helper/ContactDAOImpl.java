package helper;

import controller.ScheduleScreen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Contact;

import java.sql.*;
import java.util.Optional;

/**
 * Contact Data Access Object Implementation Class.
 */
public class ContactDAOImpl implements DAO<Contact> {

    /**
     * Gets all contacts from the database and puts them in an observable list.
     * @return An observable list of contacts.
     */
    public static ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> ctList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM contacts";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                Contact contactResult = new Contact(id, name, email);
                ctList.add(contactResult);
            }
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
        return ctList;
    }


    /**
     * Gets all results from a ResultSet contact object and retrieves the data from it.
     *
     * @param results The data extracted from each column of a contact object
     * @return A contact with all of its data loaded to each column.
     * @throws SQLException
     */
    private Contact extractFromResults(ResultSet results) throws SQLException {
        return new Contact(
                results.getInt("Contact_ID"),
                results.getString("Contact_Name"),
                results.getString("Email")
        );
    }

    /**
     * Gets a contact with a specific integer value.
     *
     * @param id ID of the specific contact being queried.
     * @return Either an empty contact or a contact containing a specific ID.
     */
    @Override
    public Optional<Contact> get(int id) {

        try {
            String sql = "GET * FROM contacts WHERE Contact_ID=" + id;
            Connection connection = JDBC.getConnection();
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            if (results.next()) {
                return Optional.of(extractFromResults(results));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Gets all contacts from the database.
     *
     * @return An ObservableList of contacts.
     */
    @Override
    public ObservableList<Contact> getAll() {
        try {
            String sql = "SELECT * FROM contacts";
            Connection connection = JDBC.getConnection();
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            ObservableList<Contact> contacts = FXCollections.observableArrayList();

            while (results.next()) {
                contacts.add(extractFromResults(results));
            }

            return contacts;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return FXCollections.observableArrayList();
    }

    /**
     * Inserts a contact into the database.
     *
     * @param contact Contact being inserted into the database.
     * @return If true, adds the contact, otherwise prints an error.
     */
    @Override
    public boolean insert(Contact contact) {
        return false;
    }

    /**
     * Updates a contact into the database.
     *
     * @param contact Contact being updated into the database.
     * @return If true, updates the contact, otherwise prints an error.
     */
    @Override
    public boolean update(Contact contact) {
        return false;
    }

    /**
     * Deletes a contact from Database.
     *
     * @param id ID of the contact being deleted.
     * @return If true, deletes the contact, otherwise prints an error.
     */
    @Override
    public boolean delete(int id) {
        return false;
    }
}


