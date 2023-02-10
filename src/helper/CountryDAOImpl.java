package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import model.Country;

import java.sql.*;
import java.util.Optional;

/**
 * Country Data Access Object Implementation Class.
 */
public class CountryDAOImpl implements DAO<Country> {

    /**
     * Gets all Countries from the database and puts them in an observable list.
     * @return An observable list of countries.
     */
        public static ObservableList<Country> getAllCountries() {
            ObservableList<Country> ctyList = FXCollections.observableArrayList();
            try {
                String sql = "SELECT * FROM countries";
                PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("Country_ID");
                    String name = rs.getString("Country");
                    Country countryResult = new Country(id, name);
                    ctyList.add(countryResult);
                }
            }
            catch(SQLException ex) {
                ex.printStackTrace();
            }
            return ctyList;
        }

    /**
     * Gets all results from a ResultSet country object and retrieves the data from it.
     *
     * @param results The data extracted from each column of a country object
     * @return A country with all of its data loaded to each column.
     * @throws SQLException
     */
    private Country extractFromResults(ResultSet results) throws SQLException {
        return new Country(results.getInt("Country_ID"), results.getString("Country"));
    }

    /**
     * Gets a country with a specific integer value.
     *
     * @param id ID of the specific country being queried.
     * @return Either an empty country or a country containing a specific ID.
     */
    @Override
    public Optional<Country> get(int id) {
        try {
            String sql = "SELECT Country_ID, Country FROM countries WHERE Country_ID=" + id;
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
     * Gets all countries from the database.
     *
     * @return An ObservableList of countries.
     */
    @Override
    public ObservableList<Country> getAll() {
        try {
            String sql = "SELECT Country_ID, Country FROM countries";
            Connection connection = JDBC.getConnection();
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            ObservableList<Country> countries = FXCollections.observableArrayList();

            while(results.next()) {
                Country country = extractFromResults(results);
                countries.add(country);
            }

            return countries;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return FXCollections.observableArrayList();
    }

    /**
     * Inserts a country into the database.
     *
     * @param country Country being inserted into the database.
     * @return If true, adds the country, otherwise prints an error.
     */
    @Override
    public boolean insert(Country country) {
        return false;
    }

    /**
     * Updates a country into the database.
     *
     * @param country country being updated into the database.
     * @return If true, updates the country, otherwise prints an error.
     */
    @Override
    public boolean update(Country country) {
        return false;
    }

    /**
     * Deletes a country from Database.
     *
     * @param id ID of the country being deleted.
     * @return If true, deletes the country, otherwise prints an error.
     */
    @Override
    public boolean delete(int id) {
        return false;
    }

}

