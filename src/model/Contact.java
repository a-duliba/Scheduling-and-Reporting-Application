package model;

/**
 * Contact Class.
 */
public class Contact {
    /**
     * ID of the contact.
     */
    private  int id;
    /**
     * Name of the contact.
     */
    private String name;
    /**
     * Email of the contact.
     */
    private String email;

    /**
     * Contact constructor
     * @param id ID of the contact.
     * @param name Name of the contact.
     * @param email Email of the contact.
     */
    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * Gets the ID of the contact.
     * @return The ID of the contact.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the contact.
     * @param id The ID of the contact.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the Name of the contact.
     * @return The name of the contact.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the Name of the contact.
     * @param name The name of the contact.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email of the contact.
     * @return The email of the contact.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the contact.
     * @param email The email of the contact.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Allows the contact ID and name to be displayed in a combo box.
     * @return The contact ID and name of a contact as a string in a combo box.
     */
    @Override
    public String toString(){
        return(id + " " + name);
    }
}
