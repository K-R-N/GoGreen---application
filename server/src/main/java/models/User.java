package models;

import javafx.scene.image.Image;

/**
 * Standard user object for the application.
 */
public class User {

    /**
     * Boolean that indicates whether user object is the main user of this application instance.
     */
    private boolean isMainUser;

    /**
     * General attributes for any user.
     */
    private int totalPoints;
    private String username;
    private String picturePath;

    /**
     * Attributes that will only have values when object is main user.
     */
    private String emailadress;
    private String firstname;
    private String privacy;

    /**
    * Attribute for the database object.
    */
    private String password;
    /**
     * Cached profile pictures automatically created on client.
     */
    private Image picture;

    /**
     * Encapsulate default constructor to enforce passing attributes.
     */
    private User() {}

    /** Creates a user object for the database.
     *
     * @param username username of the user
     * @param password the password of the user.
     * @param firstname first name of the main user
     * @param emailadress email address of the main user
     */
    public User(String username, String password, String firstname, String emailadress) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.emailadress = emailadress;
    }

    /**
     * Creates a non-main user (a friend).
     * @param totalPoints total points of the friend
     * @param username username of the friend
     * @param picturePath path of profile picture of the friend
     */
    public User(int totalPoints, String username, String picturePath) {
        this.isMainUser = false;
        this.totalPoints = totalPoints;
        this.username = username;
        this.picturePath = picturePath;
    }

    /**
     * Creates a main user.
     * @param totalPoints total points of the main user
     * @param username username of the main user
     * @param picturePath path of profile picture of the main user
     * @param emailadress email address of the main user
     * @param firstname first name of the main user
     */
    public User(int totalPoints, String username, String picturePath,
                String emailadress, String firstname) {
        this(totalPoints, username, picturePath);
        this.isMainUser = true;
        this.emailadress = emailadress;
        this.firstname = firstname;
    }

    /**
     * Creates a user object for the timeline.
     * @param username username of the friend
     * @param picturePath path of profile picture of the friend
     */
    public User(String username, String picturePath) {
        this.username = username;
        this.picturePath = picturePath;
    }

    public boolean isMainUser() {
        return isMainUser;
    }

    public void setMainUser(boolean mainUser) {
        isMainUser = mainUser;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getEmailadress() {
        return emailadress;
    }

    public void setEmailadress(String emailadress) {
        this.emailadress = emailadress;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Image getPicture() {
        return picture;
    }

    public void setPicture(Image picture) {
        this.picture = picture;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    @Override
    public String toString() {
        return "User{"
                + "totalPoints=" + totalPoints
                + ", username='" + username + '\''
                + ", picturePath='" + picturePath + '\''
                + ", emailadress='" + emailadress + '\''
                + ", firstname='" + firstname + '\''
                + ", password='" + password + '\''
                + '}';
    }
}

