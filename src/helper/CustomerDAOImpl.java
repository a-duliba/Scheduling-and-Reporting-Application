package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Customer Data Access Object Implementation Class.
 */
public class CustomerDAOImpl implements DAO<Customer> {

    /**
     * Gets all customers from the database and puts them in an observable list.
     * @return An observable list of customers.
     */
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> cList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT customers.*, first_level_divisions.Division_ID, first_level_divisions.Division, first_level_divisions.COUNTRY_ID, countries.Country_ID," +
                    " countries.Country FROM customers, first_level_divisions, countries WHERE customers.Division_ID=first_level_divisions.Division_ID AND " +
                    "first_level_divisions.COUNTRY_ID = countries.Country_ID"; //sql bootcamp2
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                LocalDateTime createdDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                int countryId = rs.getInt("Country_ID");
                String country = rs.getString("Country");
                int divisionId = rs.getInt("Division_ID");
                String division = rs.getString("Division");// how to get country from countries table via division ID foreign key
                Customer customerResult = new Customer(id, name, address, postalCode, phone, createdDate, countryId, country, divisionId, division);
                cList.add(customerResult);
            }
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
        return cList;
    }

    /**
     * Gets all results from a ResultSet customer object and retrieves the data from it.
     *
     * @param results The data extracted from each column of a customer object
     * @return A customer with all of its data loaded to each column.
     * @throws SQLException
     */
    private Customer extractFromResults(ResultSet results) throws SQLException {
        return new Customer(
                results.getInt("Customer_ID"),
                results.getString("Customer_Name"),
                results.getString("Address"),
                results.getString("Postal_Code"),
                results.getString("Phone"),
                results.getTimestamp("Create_Date").toLocalDateTime(),
                results.getString("Created_By"),
                results.getString("Last_Update"),
                results.getString("Last_Updated_By"),
                results.getInt("Division_ID"),
                results.getString("Division"),
                results.getInt("Country_ID"),
                results.getString("Country")
        );
    }

    /**
     * Gets a customer with a specific integer value.
     *
     * @param id ID of the specific customer being queried.
     * @return Either an empty contact or a customer containing a specific ID.
     */
    @Override
    public Optional<Customer> get(int id) {
        try {
            String sql = "SELECT customers.*, first_level_divisions.Division, first_level_divisions.COUNTRY_ID, countries.Country FROM customers," +
                    " first_level_divisions, countries WHERE customers.Division_ID=first_level_divisions.Division_ID and " +
                    "first_level_divisions.COUNTRY_ID = countries.Country_ID and customer.Customer_ID=" + id;
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
     * Gets all customers from the database.
     *
     * @return An ObservableList of customers.
     */
    @Override
    public ObservableList<Customer> getAll() {
        try {
            String sql = "SELECT customers.*, first_level_divisions.Division, first_level_divisions.COUNTRY_ID, countries.Country FROM customers, first_level_divisions," +
                    " countries WHERE customers.Division_ID=first_level_divisions.Division_ID and first_level_divisions.COUNTRY_ID = countries.Country_ID";
            Connection connection = JDBC.getConnection();
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            ObservableList<Customer> customerList = FXCollections.observableArrayList();

            while(results.next()) {
                Customer customer = extractFromResults(results);
                customerList.add(customer);
            }

            return customerList;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return FXCollections.observableArrayList();
    }

    /**
     * Inserts a customer into the database.
     *
     * @param customer Customer being inserted into the database.
     * @return If true, adds the customer, otherwise prints an error.
     */
    @Override
    public boolean insert(Customer customer) {
        try {
            String sql = "INSERT INTO customers VALUE (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            Connection connection = JDBC.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, customer.getCustomerName());
            statement.setString(2, customer.getAddress());
            statement.setString(3, customer.getPostalCode());
            statement.setString(4, customer.getPhoneNumber());
            statement.setTimestamp(5, Timestamp.valueOf(customer.getCreatedDate()));
            statement.setString(6, customer.getCreatedBy());
            statement.setTimestamp(7, Timestamp.valueOf(customer.getLastUpdated()));
            statement.setString(8, customer.getLastUpdatedBy());
            statement.setInt(9, customer.getDivisionId());

            int result = statement.executeUpdate();
            ResultSet results = statement.getGeneratedKeys();

            if (result == 1 & results.next()) {
                customer.setCustomerId(results.getInt(1));
                return true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    /**
     * Updates a customer into the database.
     *
     * @param customer customer being updated into the database.
     * @return If true, updates the customer, otherwise prints an error.
     */
    @Override
    public boolean update(Customer customer) {
        try {
            String sql = "UPDATE customers SET Customer_Name=?,  Address=?, Division_ID=?, Postal_Code=?, Phone=?, Create_Date=?, Created_By=?, Last_Update=?," +
                    " Last_Updated_By=? WHERE Customer_ID=?";
            Connection connection = JDBC.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, customer.getCustomerName());
            statement.setString(2, customer.getAddress());
            statement.setInt(3, customer.getDivisionId());
            statement.setString(4, customer.getPostalCode());
            statement.setString(5, customer.getPhoneNumber());
            statement.setTimestamp(6, Timestamp.valueOf(customer.getCreatedDate()));
            statement.setString(7, customer.getCreatedBy());
            statement.setTimestamp(8, Timestamp.valueOf(customer.getLastUpdated()));
            statement.setString(9, customer.getLastUpdatedBy());
            statement.setInt(10, customer.getCustomerId());

            int result = statement.executeUpdate();

            if(result == 1) return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    /**
     * Deletes a customer from Database.
     *
     * @param id ID of the customer being deleted.
     * @return If true, deletes the customer, otherwise prints an error.
     */
    @Override
    public boolean delete(int id) {
        try {
            String sql = "DELETE FROM customers WHERE Customer_ID=" + id;
            Connection connection = JDBC.getConnection();
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(sql);

            if(result == 1) return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }


}
