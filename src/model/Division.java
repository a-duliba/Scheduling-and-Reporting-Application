package model;

/**
 * Division Class.
 */
public class Division {
    /**
     * ID of the division.
     */
    private int id;
    /**
     * Name of the division.
     */
    private String name;
    /**
     * Country ID of the division.
     */
    private int countryId;

    /**
     * Division constructor
     * @param id ID of the division.
     * @param name Name of the division.
     * @param countryId Country ID of the division.
     */
    public Division(int id, String name, int countryId) {
        this.id = id;
        this.name = name;
        this.countryId = countryId;
    }

    /**
     * Gets the ID of the division.
     * @return the ID of the division.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the division.
     * @param id The ID of the division.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the division.
     * @return The name of the division.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the division.
     * @param name The name of the division.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the country ID of the division.
     * @return The country ID of the division.
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Sets the country ID of the division.
     * @param countryId The country ID of the division.
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * Allows the name of the division to be a string within a combo box.
     * @return The name of the division as a string within a combo box.
     */
    @Override
    public String toString(){
        return(name);
    }
}
