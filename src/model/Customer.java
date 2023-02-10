package model;

import java.time.LocalDateTime;

/**
 * Customer Class.
 */
public class Customer {
    /**
     * ID of the customer.
     */
    private int customerId;
    /**
     * Name of the customer.
     */
    private String customerName;
    /**
     * Address of the customer.
     */
    private String address;
    /**
     * Postal code of the customer.
     */
    private String postalCode;
    /**
     * Phone number of the customer.
     */
    private String phoneNumber;
    /**
     * Date the customer was created.
     */
    private LocalDateTime createdDate;
    /**
     * User who created the customer.
     */
    private String createdBy;
    /**
     * Date the customer was last updated.
     */
    private String lastUpdated; //string
    /**
     * User who last updated the customer.
     */
    private String lastUpdatedBy;
    /**
     * ID of the division where the customer lives.
     */
    private int divisionId;
    /**
     * Name of the division where the customer lives.
     */
    private String division;
    /**
     * ID of the country where the customer lives.
     */
    private int countryId;
    /**
     * Name of the country where the customer lives.
     */
    private String country;

    /**
     * Customer Constructor
     * @param customerId ID of customer.
     * @param customerName Name of customer.
     * @param address Address of customer.
     * @param postalCode Postal code of customer.
     * @param phoneNumber Phone number of customer.
     * @param createdDate Date customer was created.
     * @param createdBy User who created the customer.
     * @param lastUpdated Date the customer was last updated.
     * @param lastUpdatedBy User who last updated the customer.
     * @param divisionId ID of the division where the customer lives.
     * @param division Name of the division where the customer lives.
     * @param countryId ID of the country where the customer lives.
     * @param country Name of the country where the customer lives.
     */
    public Customer(int customerId, String customerName, String address, String postalCode, String phoneNumber, LocalDateTime createdDate, String createdBy,
                    String lastUpdated, String lastUpdatedBy, int divisionId, String division, int countryId, String country) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated; //string
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;
        this.division = division;
        this.countryId = countryId;
        this.country = country;
    }

    /**
     *  Special Customer Constructor
     * @param customerId ID of customer.
     * @param customerName Name of customer.
     * @param address Address of customer.
     * @param postalCode Postal code of customer.
     * @param phoneNumber Phone number of customer.
     * @param createdDate Date customer was created.
     * @param division Name of the division where the customer lives.
     * @param country Name of the country where the customer lives.
     */
    public Customer(int customerId, String customerName, String address, String postalCode, String phoneNumber, LocalDateTime createdDate, int countryId, String country, int divisionId,
                     String division) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.createdDate = createdDate;
        this.countryId = countryId;
        this.country = country;
        this.divisionId = divisionId;
        this.division = division;

    }

    /**
     * Empty Constructor
     */
    public Customer() {

    }

    /**
     * Gets the ID of the customer.
     * @return The ID of the customer.
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets the ID of the customer.
     * @param id The ID of the Customer.
     */
    public void setCustomerId(int id) {
        this.customerId = id;
    }

    /**
     * Gets the name of the customer.
     * @return The name of the customer.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the name of the customer.
     * @param customerName The name of the customer.
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Gets the address of the customer.
     * @return The address of the customer.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the customer.
     * @param address The address of the customer.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the postal code of the customer.
     * @return The postal code of the customer.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the postal code of the customer.
     * @param postalCode The postal code of the customer.
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Gets the phone number of the customer.
     * @return The phone number of the customer.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the customer.
     * @param phoneNumber The phone number of the customer.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the date the customer was created.
     * @return The date the customer was created.
     */
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    /**
     * Sets the date the customer was created.
     * @param createdDate The date the customer was created.
     */
    public void setCreatedDate( LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Gets the user who created the customer.
     * @return The user who created the customer.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the user who created the customer.
     * @param createdBy The user who created the customer.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the date the customer was last updated.
     * @return The date the customer was last updated.
     */
    public String getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Sets the date the customer was last updated.
     * @param lastUpdated The date the customer was last updated.
     */
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * Gets the user who last updated the customer.
     * @return The user who last updated the customer.
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Sets the user who last updated the customer.
     * @param lastUpdatedBy The user who last updated the customer.
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Gets the division ID of the customer.
     * @return The division ID of the customer.
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Sets the division ID of the customer.
     * @param divisionId The division ID of the customer.
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * Gets the division name of the customer.
     * @return The division name of the customer.
     */
    public String getDivision() {
        return division;
    }

    /**
     * Sets the division name of the customer.
     * @param division The division name of the customer.
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * Gets the country ID of the customer.
     * @return The country ID of the customer.
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Sets the country ID of the customer.
     * @param countryId The country ID of the customer.
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * Gets the country name of the customer.
     * @return The country name of the customer.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country name of the customer.
     * @param country The country name of the customer.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Allows the customer ID and name to be a string value displayed in a combo box.
     * @return The customer ID and name as a string value displayed in a combo box.
     */
    @Override
    public String toString() {
        return(customerId + " " + customerName);
    }

}
