package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Appointment Class.
 */
public class Appointment {
    /**
     * ID of appointment.
     */
    private int appointmentId;
    /**
     * Title of appointment.
     */
    private String title;
    /**
     * Description of appointment.
     */
    private String description;
    /**
     * Location of appointment.
     */
    private String location;
    /**
     * Type of appointment.
     */
    private String type;
    /**
     * Start date and time of appointment.
     */
    private LocalDateTime startDateTime;
    /**
     * End date and time of appointment.
     */
    private LocalDateTime endDateTime;
    /**
     * Date the appointment was created.
     */
    private LocalDateTime createdDate;
    /**
     * User who created the appointment.
     */
    private String createdBy;
    /**
     * Date the appointment was last updated.
     */
    private LocalDateTime lastUpdate;
    /**
     * User who last updated the appointment.
     */
    private String lastUpdateBy;
    /**
     * Customer ID of the appointment.
     */
    private int customerId;
    /**
     * Customer name of the appointment.
     */
    private String customer;
    /**
     * Contact ID of the appointment.
     */
    private int contactId;
    /**
     * contact name of the appointment.
     */
    private String contact;
    /**
     * User ID of the appointment.
     */
    private int userId;
    /**
     * Users name for the appointment.
     */
    private String user;

    /**
     * Empty Constructor
     */
    public Appointment() {

    }

    /**
     * Appointment Contstructor.
     * @param appointmentId ID of the appointment.
     * @param title Title of the appointment.
     * @param description Description of the appointment.
     * @param location Location of the appointment.
     * @param type Type of appointment.
     * @param startDateTime Start LocalDateTime of the appointment.
     * @param endDateTime End LocalDateTime of the appointment.
     * @param createdDate Date and Time the appointment was created.
     * @param createdBy User who Created the appointment.
     * @param lastUpdate Date and Time the appointment was last updated.
     * @param lastUpdateBy User who last updated the appointment.
     * @param customerId ID of Customer who is to attend the appointment.
     * @param customer Name of customer who is to attend the appointment.
     * @param contactId ID of the Contact who is to attend the appointment.
     * @param contact Name of the contact who is to attend the appointment.
     * @param userId ID of the user who made the appointment.
     * @param user Name of the user who made the appointment.
     */
    public Appointment(int appointmentId, String title, String description, String location, String type, LocalDateTime startDateTime, LocalDateTime endDateTime,
                       LocalDateTime createdDate, String createdBy, LocalDateTime lastUpdate, String lastUpdateBy, int customerId, String customer, int contactId,
                       String contact, int userId, String user) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.customerId = customerId;
        this.customer = customer;
        this.contactId = contactId;
        this.contact = contact;
        this.userId = userId;
        this.user = user;
    }

    /**
     * Custom Appointment Contstructor.
     * @param appointmentId ID of the appointment.
     * @param title Title of the appointment.
     * @param description Description of the appointment.
     * @param location Location of the appointment.
     * @param type Type of appointment.
     * @param startDateTime Start LocalDateTime of the appointment.
     * @param endDateTime End LocalDateTime of the appointment.
     * @param createdDate Date and Time the appointment was created.
     * @param createdBy User who Created the appointment.
     * @param lastUpdate Date and Time the appointment was last updated.
     * @param lastUpdateBy User who last updated the appointment.
     * @param customerId ID of Customer who is to attend appointment.
     * @param userId ID of the user who is supposed to attend the appointment.
     * @param contactId ID of the Contact who is to attend the appointment.
     * @param contact Name of the contact who is to attend the appointment.
     */
    public Appointment(int appointmentId, String title, String description, String location, String type, LocalDateTime startDateTime, LocalDateTime endDateTime,
                       LocalDateTime createdDate, String createdBy, LocalDateTime lastUpdate, String lastUpdateBy, int customerId, int userId, int contactId, String contact) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
        this.contact = contact;

    }

    /**
     * Gets the ID of the appointment.
     * @return the ID of the appointment.
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Sets the ID of the appointment.
     * @param appointmentId the ID of the appointment.
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Gets the title of the appointment.
     * @return The title of the appointment.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the appointment.
     * @param title The title of the appointment.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the description of the appointment.
     * @return The description of the appointment.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the appointment.
     * @param description The description of the appointment.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the location of the appointment.
     * @return The location of the appointment.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of tha appointment.
     * @param location The location of the appointment.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the type of the appointment.
     * @return The type of the appointment.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the appointment.
     * @param type The type of the appointment.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the start date and time of the appointment.
     * @return The start date and time of the appointment.
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Sets the start date and time of the appointment.
     * @param startDateTime the start date and time of the appointment.
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * Gets the end date and time of the appointment.
     * @return the end date and time of the appointment.
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Sets the end date and time of the appointment.
     * @param endDateTime The end date and time of the appointment.
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * Gets the date an appointment was created.
     * @return The date an appointment was created.
     */
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    /**
     * Sets the date an appointment was created.
     * @param createdDate The date the appointment was created.
     */
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Gets the User who created the appointment.
     * @return The name of the user who created the appointment.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the user who created the appointment.
     * @param createdBy The Name of the user who created the appointment.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the last date and time the appointment was updated.
     * @return The date and time the appointment was updated.
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Sets the last date and time the appointment was updated.
     * @param lastUpdate The date and time the appointment was updated.
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Gets the user who last updated the appointment.
     * @return The user who last updated the appointment.
     */
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    /**
     * Sets the user who last updated the appointment.
     * @param lastUpdateBy The user who last updated the appointment.
     */
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
///
    /**
     * Gets the customers ID for who is supposed to be at the appointment.
     * @return The ID of the customer who is supposed to be at the appointment.
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets the Customer ID for who is supposed to be at the appointment.
     * @param customerId The ID of the customer who is supposed to be at the appointment.
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the name of the customer who is supposed to be at the appointment.
     * @return The name of the customer who is supposed to be at the appointment.
     */
    public String getCustomer() {
        return customer;
    }

    /**
     * Sets the name of the customer who is supposed to be at the appointment.
     * @param customer The name of the customer who is supposed to be at the appointment.
     */
    public void setCustomer(String customer) {
        this.customer = customer;
    }

    /**
     * Gets the contact ID for who is supposed to be at the appointment.
     * @return The ID of the contact who is to attend.
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Sets the contact ID for who is supposed to be at the appointment.
     * @param contactId The ID of the contact who is supposed to be at the appointment.
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * Gets the contact name for who is supposed to be at the appointment.
     * @return The name of the contact who is supposed to be at the appointment.
     */
    public String getContact() {
        return contact;
    }

    /**
     * Sets the contact name for who is supposed to be at the appointment.
     * @param contact The name of the contact who is supposed to be at the appointment.
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * Gets the user ID for who is supposed to be at the appointment.
     * @return The ID of the user who is to attend.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user ID for who is supposed to be at the appointment.
     * @param userId The ID of the user who is supposed to be at the appointment.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the username for who is supposed to be at the appointment.
     * @return The name of the user who is supposed to be at the appointment.
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the username for who is supposed to be at the appointment.
     * @param user The name of the user who is supposed to be at the appointment.
     */
    public void setUser(String user) {
        this.user = user;
    }
}
