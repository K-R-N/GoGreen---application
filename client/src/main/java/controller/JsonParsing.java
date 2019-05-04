package controller;

import models.Achievement;
import models.Activity;
import models.ActivityCategory;
import models.ActivityType;
import models.Contribution;
import models.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class JsonParsing {


    static Boolean cache = true;
    private static String datePattern = "yyyy-MM-dd";
    private static String timePattern = "HH:mm:ss";

    /**
     * Safely checks if JSON has value for an attribute.
     * true iff attribute exists and it is not null
     * @param attr name of the attribute
     * @param json json to look in
     * @return boolean indicating existence
     */
    public static boolean attrExists(String attr, JSONObject json) {
        try {
            Object result = json.get(attr);
            return result != JSONObject.NULL;
        } catch (JSONException e) {
            return false;
        }
    }

    /**
     * Parses Contribution.
     * @param json json to parse
     * @return Contribution object
     */
    public static Contribution parseContribution(JSONObject json) {

        // parse date
        LocalDate date = null;
        if (attrExists("date", json)) {
            date = LocalDate.parse(
                    json.getString("date"),
                    DateTimeFormatter.ofPattern(datePattern)
            );
        }

        // parse time
        LocalTime time = null;
        if (attrExists("time", json)) {
            time = LocalTime.parse(
                    json.getString("time"),
                    DateTimeFormatter.ofPattern(timePattern)
            );
        }

        User user = null;
        if (attrExists("user", json)) {
            user = parseUser(json.getJSONObject("user"));
        }

        return new Contribution(user, date, time);
    }

    /**
     * Parses Achievement.
     * @param json json to parse
     * @return Achievement object
     */
    public static Achievement parseAchievement(JSONObject json) {

        int achievementId = -1;
        if (attrExists("achievementId", json)) {
            achievementId = json.getInt("achievementId");
        }

        String title = null;
        if (attrExists("title", json)) {
            title = json.getString("title");
        }

        String desc = null;
        if (attrExists("desc", json)) {
            desc = json.getString("desc");
        }

        String smallBadgePath = null;
        if (attrExists("smallBadgePath", json)) {
            smallBadgePath = json.getString("smallBadgePath");
        }

        String bigBadgePath = null;
        if (attrExists("bigBadgePath", json)) {
            bigBadgePath = json.getString("bigBadgePath");
        }

        Contribution contribution = parseContribution(json);

        Achievement achievement = new Achievement(
            achievementId,
            title,
            desc,
            smallBadgePath,
            bigBadgePath,
            contribution.getUser(),
            contribution.getDate(),
            contribution.getTime()
        );

        achievement.setAcquired(json.getBoolean("acquired"));

        if (achievement.getSmallBadgePath() != null) {
            achievement.setSmallBadgeImage(
                DataAccess.getImageForUrl(
                DataAccess.host + achievement.getSmallBadgePath(), 50.0, 50.0
                )
            );
        }
        if (achievement.getBigBadgePath() != null) {
            achievement.setBigBadgeImage(
                DataAccess.getImageForUrl(
                DataAccess.host + achievement.getBigBadgePath(), 95.0, 95.0
                )
            );
        }

        return achievement;
    }

    /**
     * Parses Activity.
     * @param json json to parse
     * @return Activity object
     */
    public static Activity parseActivity(JSONObject json) {

        Contribution contribution = parseContribution(json);

        int activityId = -1;
        if (attrExists("id", json)) {
            activityId = json.getInt("id");
        }

        ActivityType activityType = null;
        if (attrExists("type", json)) {
            activityType = parseActivityType(json.getJSONObject("type"));
        }

        return new Activity(
            activityId,
            activityType,
            contribution.getUser(),
            contribution.getDate(),
            contribution.getTime()
        );
    }

    /**
     * Parses Activity Type.
     * @param json json to parse
     * @return Activity Type object
     */
    public static ActivityType parseActivityType(JSONObject json) {

        int activityId = -1;
        if (attrExists("id", json)) {
            activityId = json.getInt("id");
        }

        String title = null;
        if (attrExists("name", json)) {
            title = json.getString("name");
        }

        String desc = null;
        if (attrExists("description", json)) {
            desc = json.getString("description");
        }

        int points = -1;
        if (attrExists("points", json)) {
            points = json.getInt("points");
        }

        ActivityCategory category = null;
        if (attrExists("category", json)) {
            category = parseActivityCategory(json.getJSONObject("category"));
        }

        return new ActivityType(
            activityId,
            title,
            points,
            category,
            desc
        );
    }

    /**
     * Parses Activity Category.
     * @param json json to parse
     * @return Activity Category object
     */
    public static ActivityCategory parseActivityCategory(JSONObject json) {

        int categoryId = 0;
        if (attrExists("id", json)) {
            categoryId = json.getInt("id");
        }

        String name = null;
        if (attrExists("name", json)) {
            name = json.getString("name");
        }

        String desc = null;
        if (attrExists("desc", json)) {
            desc = json.getString("desc");
        }

        String imagePath = null;
        if (attrExists("imagePath", json)) {
            imagePath = json.getString("imagePath");
        }

        ActivityCategory activityCategory = new ActivityCategory(categoryId, name, desc, imagePath);

        activityCategory.setImage(
                DataAccess.getImageForUrl(
                    DataAccess.host + "/activities/getImage?id="
                        + categoryId + "&isCategory=true&isBig=true",
                        null,
                        100.0
                )
        );

        return activityCategory;
    }

    /**
     * Parses User.
     * @param json json to parse
     * @return User object
     */
    public static User parseUser(JSONObject json) {

        int totalPoints = -1;
        if (attrExists("totalPoints", json)) {
            totalPoints = json.getInt("totalPoints");
        }

        String picturePath = null;
        if (attrExists("picturePath", json)) {
            picturePath = json.getString("picturePath");
        }

        String emailAddress = null;
        if (attrExists("emailadress", json)) {
            emailAddress = json.getString("emailadress");
        }

        String firstName = null;
        if (attrExists("firstname", json)) {
            firstName = json.getString("firstname");
        }

        String username = json.getString("username");

        if (cache && DataAccess.cachedFriends.containsKey(username)) {
            User usr = DataAccess.cachedFriends.get(username);
            if (usr.getTotalPoints() > 0) {
                return usr;
            }
        }

        Boolean isMain = json.getBoolean("mainUser");
        User user = null;

        if (isMain) {
            user = new User(
                totalPoints,
                username,
                picturePath,
                emailAddress,
                firstName
            );
            user.setPrivacy(parsePrivacy(json));

        } else {
            user = new User(
                totalPoints,
                username,
                picturePath
            );
        }

        if (user.getPicturePath() != null) {
            user.setPicture(DataAccess.getImageForUrl(
                    DataAccess.host + user.getPicturePath(), null, 100.0)
            );
        }

        DataAccess.cachedFriends.put(user.getUsername(), user);

        return user;
    }

    /**
     * parces the json object to get the privacy part.
     * @param jsonObject the isput to parse for the privacy.
     * @return the value of the privacy.
     */
    public static String parsePrivacy(JSONObject jsonObject) {

        String privacy = null;
        if (attrExists("privacy", jsonObject)) {
            privacy = jsonObject.getString("privacy");
        }

        return privacy;
    }


}
