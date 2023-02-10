package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.Division;

import java.sql.*;
import java.util.Optional;

/**
 * Division Data Access Object Implementation Class.
 */
public class DivisionDAOImpl implements DAO<Division> {

    /**
     * Gets all divisions from the database and puts them in an observable list.
     * @return An observable list of divisions.
     */
    public static ObservableList<Division> getAllDivisions() {
        ObservableList<Division> dList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM first_level_divisions";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("Division_ID");
                String name = rs.getString("Division");
                int countryId = rs.getInt("Country_Id");
                Division divisionResult = new Division(id, name, countryId);
                dList.add(divisionResult);
            }
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
        return dList;
    }

    /**
     * Gets all results from a ResultSet division object and retrieves the data from it.
     *
     * @param results The data extracted from each column of a division object
     * @return A division with all of its data loaded to each column.
     * @throws SQLException
     */
    private Division extractFromResults(ResultSet results) throws SQLException {
        return new Division(
                results.getInt("Division_ID"),
                results.getString("Division"),
                results.getInt("Country_ID")
        );
    }

    /**
     * Gets a division with a specific integer value.
     *
     * @param id ID of the specific division being queried.
     * @return Either an empty contact or a division containing a specific ID.
     */
    @Override
    public Optional<Division> get(int id) {
        try {
            String sql = "SELECT Division_ID, Division, Country_ID, Country FROM first_level_divisions JOIN countries on first_level_divisions.Country_ID=countries.Country_ID" +
                    " WHERE Division_ID=" + id;
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
     * Gets all divisions from the database.
     *
     * @return An ObservableList of divisions.
     */
    @Override
    public ObservableList<Division> getAll() {
        try {
            String sql = "SELECT first_level_divisions.*, countries.Country FROM first_level_divisions JOIN countries on first_level_divisions.Country_ID=countries.Country_ID";
            Connection connection = JDBC.getConnection();
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            ObservableList<Division> divisions = FXCollections.observableArrayList();

            while(results.next()) {
                Division division = extractFromResults(results);
                divisions.add(division);
            }

            return divisions;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return FXCollections.observableArrayList();
    }

    /**
     * Inserts a division into the database.
     *
     * @param division Division being inserted into the database.
     * @return If true, adds the division, otherwise prints an error.
     */
    @Override
    public boolean insert(Division division) {
        return false;
    }

    /**
     * Updates a division into the database.
     *
     * @param division division being updated into the database.
     * @return If true, updates the division, otherwise prints an error.
     */
    @Override
    public boolean update(Division division) {
        return false;
    }

    /**
     * Deletes a division from Database.
     *
     * @param id ID of the division being deleted.
     * @return If true, deletes the division, otherwise prints an error.
     */
    @Override
    public boolean delete(int id) {
        return false;
    }
}

