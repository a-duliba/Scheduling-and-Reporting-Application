package model;

/**
 * Country Class.
 */
public class Country {
    /**
     * ID of the country.
     */
    private int id;
    /**
     * Name of the country.
     */
    private String name;

    /**
     * Country constructor
     * @param id ID of the country.
     * @param name Name of the country.
     */
    public Country(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the ID of the country.
     * @return The ID of the country.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the country.
     * @param id The ID of the country.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of a country.
     * @return The name of a country.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of a country.
     * @param name The name of a country.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Displays contact name in a combo box.
     * @return The names of all contacts in a combo box.
     */
    @Override
    public String toString(){
        return(name);
    }
}
