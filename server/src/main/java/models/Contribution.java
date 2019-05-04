package models;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * General class for an activity or an achievement.
 */
public class Contribution {

    protected User user;
    protected LocalDate date;
    protected LocalTime time;

    /**
     * Encapsulate default constructor to enforce passing all attributes.
     */
    protected Contribution() {}

    /**
     * Creates a contribution object.
     * @param user user of the contribution
     */
    public Contribution(User user, LocalDate date, LocalTime time) {
        this.user = user;
        this.date = date;
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
