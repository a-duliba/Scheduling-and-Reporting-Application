package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.sql.*;
import java.util.Optional;

/**
 * User Data Access Object Implementation Class.
 */
public class UserDAOImpl implements DAO<User> {

    /**
     * Gets all users from the database and puts them in an observable list.
     * @return An observable list of users.
     */
    public static ObservableList<User> getAllUsers() {
        ObservableList<User> uList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM users";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int userid = rs.getInt("User_ID");
                String username = rs.getString("User_Name");
                String password = rs.getString("Password");
                User userResult = new User(userid, username, password);
                uList.add(userResult);
            }
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
        return uList;
    }

    /**
     * Gets all results from a ResultSet user object and retrieves the data from it.
     *
     * @param results The data extracted from each column of a user object
     * @return A user with all of its data loaded to each column.
     * @throws SQLException
     */
    private User extractFromResults(ResultSet results) throws SQLException {
        return new User(results.getInt("User_ID"), results.getString("User_Name"));
    }

    /**
     * Gets a user with a specific username and password.
     * @param username Username of the user.
     * @param password Password of the user.
     * @return Either an empty user or a user containing a specific username and password.
     */
    public Optional<User> getUserByUserNameAndPassword(String username, String password) {
        try {
            String sql = "SELECT User_ID, User_Name FROM users WHERE User_Name=? AND Password=?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet results = ps.executeQuery();

            if(results.next()) {
                return Optional.of(extractFromResults(results));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * Gets a user with a specific integer value.
     *
     * @param id ID of the specific user being queried.
     * @return Either an empty user or a user containing a specific ID.
     */
    @Override
    public Optional<User> get(int id) {
        try {
            String sql = "SELECT User_ID, User_Name FROM users WHERE id=" + id;
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
     * Gets all users from the database.
     *
     * @return An ObservableList of users.
     */
    @Override
    public ObservableList<User> getAll() {
        try {
            String sql = "SELECT User_ID, User_Name FROM users";
            Connection connection = JDBC.getConnection();
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            ObservableList<User> users = FXCollections.observableArrayList();

            while(results.next()) {
                User user = extractFromResults(results);
                users.add(user);
            }

            return users;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return FXCollections.observableArrayList();
    }

    /**
     * Inserts a user into the database.
     *
     * @param user User being inserted into the database.
     * @return If true, adds the user, otherwise prints an error.
     */
    @Override
    public boolean insert(User user) {
        return false;
    }

    /**
     * Updates a user into the database.
     *
     * @param user User being updated into the database.
     * @return If true, updates the user, otherwise prints an error.
     */
    @Override
    public boolean update(User user) {
        return false;
    }

    /**
     * Deletes a user from Database.
     *
     * @param id ID of the user being deleted.
     * @return If true, deletes the user, otherwise prints an error.
     */
    @Override
    public boolean delete(int id) {
        return false;
    }
}