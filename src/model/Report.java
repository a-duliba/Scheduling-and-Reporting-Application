package model;

/**
 * Report Class.
 */
public class Report {
    /**
     * Month of the appointment.
     */
    long date;
    /**
     * Type of appointment.
     */
    String type;
    /**
     * Total number of appointments.
     */
    long total;

    /**
     * Report Constructor
     * @param date Month of the appointment.
     * @param type Type of appointment.
     * @param total Total number of appointments.
     */
    public Report(long date, String type, long total) {
        this.date = date;
        this.type = type;
        this.total = total;
    }

    /**
     * Gets the month of the appointment.
     * @return The month of the appointment.
     */
    public long getDate() {
        return date;
    }

    /**
     * Sets the month of the appointment.
     * @param date The month of the appointment.
     */
    public void setDate(long date) {
        this.date = date;
    }

    /**
     * Gets the type of the appointment.
     * @return The type of the appointment.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of appointment.
     * @param type The type of appointment.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the total number of appointments.
     * @return the total number of appointments.
     */
    public long getTotal() {
        return total;
    }

    /**
     * Sets the total number of appointments.
     * @param total The total number of appointments.
     */
    public void setTotal(long total) {
        this.total = total;
    }
}
