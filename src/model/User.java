package model;

/**
 * User Class
 */
public class User {
    /**
     * ID of the user.
     */
    private int userId;
    /**
     * Name of the user.
     */
    private String userName;
    /**
     * Password of the user.
     */
    private String password;

    /**
     * User Constructor
     * @param userId ID of the user.
     * @param userName Name of the user.
     * @param password Password of the user.
     */
    public User(int userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }
    /**
     * Special User Constructor
     * @param userId ID of the user.
     * @param userName Name of the user.
     */
    public User(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    /**
     * Gets the ID of the user.
     * @return The ID of the user.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the user.
     * @param userId The ID of the user.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the name of the user.
     * @return The name of the user.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the name of the user.
     * @param userName The name of the user.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the password of the user.
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Allows the users ID and name to be a string within a combo box.
     * @return The users ID and name as a string within a combo box.
     */
    @Override
    public String toString(){
        return(userId + " " + userName);
    }
}
