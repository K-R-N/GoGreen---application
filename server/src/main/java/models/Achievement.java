package models;

import javafx.scene.image.Image;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Achievement object for the application.
 */
public class Achievement extends Contribution {

    private int achievementId;
    private String title;
    private String desc;
    private boolean acquired;

    private String smallBadgePath;
    private String bigBadgePath;

    /**
     * Only used at runtime.
     */
    private Image smallBadgeImage;
    private Image bigBadgeImage;

    private Achievement() {}

    /**
     * Creates an achievement object for the timeline page.
     * @param id unique id of the achievement
     * @param title title of the achievement (e.g. 'professional transporter')
     * @param desc description of the achievement
     *             (e.g. 'for completing 10 activities from the 'Transportation' category!')
     */
    public Achievement(int id,
                       String title,
                       String desc,
                       String smallBadgePath,
                       String bigBadgePath,
                       User user,
                       LocalDate date,
                       LocalTime time) {

        super(user, date, time);

        this.achievementId = id;
        this.title = title;
        this.desc = desc;
        this.acquired = true;

        this.smallBadgePath = smallBadgePath;
        this.bigBadgePath = bigBadgePath;
    }

    /**
     * Creates an achievement object for the user profile and achievements page.
     * @param id unique id of the achievement
     * @param title title of the achievement (e.g. 'professional transporter')
     * @param desc description of the achievement
     * @param bigBadgePath the url of the badge of the achievement
     * @param acquired confirms if the current user achieved this achievement.
     */
    public Achievement(int id, String title, String desc, String bigBadgePath, boolean acquired) {
        super();

        this.achievementId = id;
        this.title = title;
        this.desc = desc;
        this.acquired = acquired;

        this.bigBadgePath = bigBadgePath;
    }

    public int getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(int achievementId) {
        this.achievementId = achievementId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isAcquired() {
        return acquired;
    }

    public void setAcquired(boolean acquired) {
        this.acquired = acquired;
    }

    public String getSmallBadgePath() {
        return smallBadgePath;
    }

    public void setSmallBadgePath(String smallBadgePath) {
        this.smallBadgePath = smallBadgePath;
    }

    public String getBigBadgePath() {
        return bigBadgePath;
    }

    public void setBigBadgePath(String bigBadgePath) {
        this.bigBadgePath = bigBadgePath;
    }

    public Image getSmallBadgeImage() {
        return smallBadgeImage;
    }

    public void setSmallBadgeImage(Image smallBadgeImage) {
        this.smallBadgeImage = smallBadgeImage;
    }

    public Image getBigBadgeImage() {
        return bigBadgeImage;
    }

    public void setBigBadgeImage(Image bigBadgeImage) {
        this.bigBadgeImage = bigBadgeImage;
    }
}
