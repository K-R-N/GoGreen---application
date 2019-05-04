package models;

import java.time.LocalDate;

import java.time.LocalTime;

public class Activity extends Contribution {

    private int logId;
    private ActivityType type;

    /**
     * Encapsulate default constructor to enforce passing all attributes.
     */
    private Activity() {}

    /**
     * Creates an activity object for a specific serverUser on a specific timestamp.
     *
     * @param id       id of the activity
     * @param type     the type of activity
     * @param user     activity owner
     * @param date     date of when the activity was submitted
     * @param time     time of when the activity was submitted
     */
    public Activity(int id,
                    ActivityType type,
                    User user,
                    LocalDate date,
                    LocalTime time) {

        super(user, date, time);
        this.logId = id;
        this.type = type;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return this.logId;
    }

    public void setId(int id) {
        this.logId = id;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }
}
