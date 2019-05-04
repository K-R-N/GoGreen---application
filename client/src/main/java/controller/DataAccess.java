package controller;

import gui.session.SessionIO;
import javafx.scene.image.Image;
import models.Achievement;
import models.Activity;
import models.Contribution;
import models.User;
import network.NetworkManager;
import network.NetworkResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAccess {

    /**
     * Used to authenticate user to the server.
     */
    public static String sessionId = "tempValue";

    public static String host = "https://localhost:8080";
    
    public static User currentUser;

    public static NetworkManager manager = new NetworkManager(5000);

    public static Map<String, Image> cachedImages = new HashMap<>();

    protected static Map<String, User> cachedFriends = new HashMap<>();

    /**
     * Gets a picture from an url using caching.
     * @param url url of the picture
     * @param width optional width of the picture
     * @param height optional height of the picture
     * @return JavaFX Image, null if not found on server
     */
    public static Image getImageForUrl(String url, Double width, Double height) {

        if (url.length() > 100) {
            System.out.println("ERROR: image path didn't got converted from a byte array yet!");
            return null;
        }

        if (cachedImages.containsKey(url)) {
            return cachedImages.get(url);
        } else {
            NetworkResponse<Image> imageResponse = manager.getImage(url, width, height);
            if (!imageResponse.wasSuccess()) {
                System.out.println("Couldn't retrieve image at '" + url + "':");
                System.out.println(imageResponse.getStatus());
                return null;
            }

            cachedImages.put(url, imageResponse.getData());

            return imageResponse.getData();
        }
    }

    /**
     * Fetches all the user data from the logged in user.
     * @return an object containing user data
     */
    public static User getUserData() {

        // retrieve User data from server.
        NetworkResponse<JSONObject> result = manager.getJson(host + "/user/getUser", sessionId);

        // check for failure
        if (!result.wasSuccess()) {
            System.out.println(
                "Couldn't retrieve main user data: "
                + result.getStatus().toString()
            );
            return null;
        }

        // gets data from the response
        JSONObject userJson = result.getData();

        // parses the json into a user object, and allow the user cache to be overridden
        JsonParsing.cache = false;
        User user = JsonParsing.parseUser(userJson);
        JsonParsing.cache = true;

        currentUser = user;

        return user;
    }

    /**
     * Fetches the friends list of the user.
     * @return a list of friend objects
     */
    public static List<User> getFriends() {

        // retrieve friends from server
        NetworkResponse<JSONObject> result = manager.getJson(host
                + "/user/getAllFriends", sessionId);

        // check for failure
        if (!result.wasSuccess()) {
            System.out.println(
                "Couldn't retrieve friends: "
                + result.getStatus().toString()
            );
            return null;
        }

        List<User> friendsList = new ArrayList<>();

        // convert json to list of Users
        JSONArray retrievedfriendList = result.getData().getJSONArray("users");
        for (int i = 0; i < retrievedfriendList.length(); i++) {
            JSONObject retrievedFriend = retrievedfriendList.getJSONObject(i);
            User friend = JsonParsing.parseUser(retrievedFriend);

            friendsList.add(friend);
        }

        return friendsList;
    }

    /**
     * Sends friend request to a user.
     * @param user the user to send the request to
     */
    public static Boolean sendFriendRequest(User user) {

        JSONObject friendJson = new JSONObject();
        friendJson.put("username", user.getUsername());

        // send the friend request to the server
        NetworkResponse result = manager.sendJson(host
                + "/user/sendFriendRequest", friendJson, sessionId);

        // check for failure
        if (!result.wasSuccess()) {
            System.out.println("Couldn't retrieve session id: " + result.getStatus().toString());
            return null;
        }

        JSONObject resultJson = new JSONObject((String) result.getData());

        return resultJson.getBoolean("requestSucces");
    }

    /**
     * Gets all your pending friend requests.
     * @return list of users
     */
    public static List<User> getFriendRequests(boolean getOwn) {

        // retrieve friends from server.
        NetworkResponse<JSONObject> result = manager.getJson(host
                + "/user/getFriendRequests?getOwn=" + getOwn, sessionId);

        // check for failure
        if (!result.wasSuccess()) {
            System.out.println(
                "Couldn't retrieve friend requests: "
                + result.getStatus().toString()
            );
            return null;
        }

        List<User> friendsList = new ArrayList<>();

        // parses the response data into user objects
        JSONArray retrievedfriendList = result.getData().getJSONArray("requests");
        for (int i = 0; i < retrievedfriendList.length(); i++) {
            JSONObject retrievedFriend = retrievedfriendList.getJSONObject(i);

            // add json data, to make the parsing work without errors, with correct picture paths
            JSONObject friendObject = new JSONObject();
            friendObject.put("username", retrievedFriend.get("fromUser"));
            friendObject.put("mainUser", false);
            friendObject.put("picturePath", "/user/getProfilePic?username="
                    + retrievedFriend.get("fromUser"));
            User friend = JsonParsing.parseUser(friendObject);

            friendsList.add(friend);
        }

        return friendsList;
    }

    /**
     * Accepts friend request.
     * @param user user object
     */
    public static Boolean acceptFriend(User user) {

        JSONObject friendJson = new JSONObject();
        friendJson.put("username", user.getUsername());

        // send the friend request to the server
        NetworkResponse result = manager.sendJson(host
                + "/user/acceptFriendRequest", friendJson, sessionId);

        // check for failure
        if (!result.wasSuccess()) {
            System.out.println("Couldn't retrieve session id: " + result.getStatus().toString());
            return null;
        }

        JSONObject resultJson = new JSONObject((String) result.getData());

        return resultJson.getBoolean("requestSucces");
    }

    /**
     * Declines friend request.
     * @param user user object
     */
    public static Boolean declineFriend(User user) {

        JSONObject friendJson = new JSONObject();
        friendJson.put("username", user.getUsername());

        // send the friend request to the server
        NetworkResponse result = manager.sendJson(host
                + "/user/declineFriendRequest", friendJson, sessionId);

        // check for failure
        if (!result.wasSuccess()) {
            System.out.println("Couldn't retrieve session id: " + result.getStatus().toString());
            return null;
        }

        JSONObject resultJson = new JSONObject((String) result.getData());

        return resultJson.getBoolean("requestSucces");
    }

    /**
     * Deletes friend.
     * @param friend friend object
     */
    public static Boolean deleteFriend(User friend) {

        JSONObject friendJson = new JSONObject();
        friendJson.put("username", friend.getUsername());

        // send the name of the friend to be removed to the server
        NetworkResponse result = manager.sendJson(host
                + "/user/removeFriend", friendJson, sessionId);

        // check for failure
        if (!result.wasSuccess()) {
            System.out.println("Couldn't retrieve session id: " + result.getStatus().toString());
            return null;
        }

        JSONObject resultJson = new JSONObject((String) result.getData());

        return resultJson.getBoolean("requestSucces");
    }

    /**
     * Gets user suggestions from pattern.
     * @param pattern pattern to match to
     * @return list of users
     */
    public static List<User> getUserSuggestions(String pattern) {

        JSONObject jsonPattern = new JSONObject();
        jsonPattern.put("username", pattern.toLowerCase());

        // send the string pattern to return friends with usernames that are similar to that patter
        NetworkResponse result = manager.sendJson(host
                + "/user/getFriendSuggestions", jsonPattern, sessionId);

        // check for failure
        if (!result.wasSuccess()) {
            System.out.println("Couldn't retrieve session id: " + result.getStatus().toString());
            return null;
        }

        List<User> friendsList = new ArrayList<>();

        // parses the response data, into a list of user objects
        JSONObject resultJson = new JSONObject((String) result.getData());
        JSONArray retrievedfriendList = resultJson.getJSONArray("suggested");
        for (int i = 0; i < retrievedfriendList.length(); i++) {
            JSONObject retrievedFriend = retrievedfriendList.getJSONObject(i);
            User friend = new User(
                    retrievedFriend.getInt("totalPoints"),
                    retrievedFriend.getString("username"),
                    " "
            );
            friendsList.add(friend);
        }

        return friendsList;
    }

    /**
     * Fetches the available types of activities from the server.
     * This way, the list on the 'add activity' can be dynamically manipulated via the server
     * @return the list of available activities
     */
    public static List<Activity> fetchActivityTypes() {

        // retrieve all available activities from server.
        NetworkManager manager = new NetworkManager(5000);
        NetworkResponse<JSONObject> result = manager.getJson(host
                + "/activities/getAvailableActivities", sessionId);

        // check for failure
        if (!result.wasSuccess()) {
            System.out.println(
                "Couldn't retrieve available activities: "
                + result.getStatus().toString()
            );
            return null;
        }

        List<Activity> availableActivities = new ArrayList<>();

        JSONArray retrievedActivityList = result.getData().getJSONArray("activities");

        for (int i = 0; i < retrievedActivityList.length(); i++) {

            Activity activity = new Activity(
                    retrievedActivityList.getJSONObject(i).getInt("id"),
                    JsonParsing.parseActivityType(retrievedActivityList.getJSONObject(i)),
                    new User(0, "",""),
                    null,
                    null
            );

            availableActivities.add(activity);
        }

        return availableActivities;

    }

    /**
     * Fetches the complete timeline of activities from the server.
     * These should be both of the user and his friends.
     * @return a list of activities
     */
    public static List<Contribution> getActivityHistory(
            boolean includeFriends,
            LocalDate startDate,
            LocalDate endDate,
            String getFriendsActivities
    ) {
        String strStartDate = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String strEndDate = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (getFriendsActivities == null) {
            getFriendsActivities = "true";
        }

        // retrieve the complete activityHistory from server.
        NetworkResponse<JSONObject> result = manager.getJson(
            host + "/activities/getTimeline"
                    + "?startDate=" + strStartDate
                    + "&endDate=" + strEndDate
                    + "&includeFriends=" + includeFriends
                    + "&isMainUser=" + getFriendsActivities,
            sessionId
        );

        // check for failure
        if (!result.wasSuccess()) {
            System.out.println(
                "Couldn't retrieve available activities: "
                + result.getStatus().toString()
            );
            return null;
        }

        // convert contribution list to their actual models
        List<Contribution> contributionList =  new ArrayList<>();
        JSONArray contributionListJson = result.getData().getJSONArray("activities");
        for (int i = 0; i < contributionListJson.length(); i++) {
            JSONObject activityOrAchievement = contributionListJson.getJSONObject(i);

            if (JsonParsing.attrExists("achievementId", activityOrAchievement)) {
                contributionList.add(JsonParsing.parseAchievement(activityOrAchievement));
            } else {
                contributionList.add(JsonParsing.parseActivity(activityOrAchievement));
            }
        }
        return contributionList;
    }

    /**
     * Gets a user's own achievements.
     * @return list of achievements
     */
    public static List<Achievement> getOwnAchievements() {

        // retrieve the achievements of the user from server.
        NetworkManager manager = new NetworkManager(5000);
        NetworkResponse<JSONObject> result = manager.getJson(host
                + "/activities/getAllAchievements", sessionId);

        // check for failure
        if (!result.wasSuccess()) {
            System.out.println(
                    "Couldn't retrieve all achievements: "
                            + result.getStatus().toString()
            );
            return null;
        }

        List<Achievement> achievementList = new ArrayList<>();

        JSONArray jsonAchievementList = result.getData().getJSONArray("achievements");
        for (int i = 0; i < jsonAchievementList.length(); i++) {
            Achievement achievement = JsonParsing.parseAchievement(
                    jsonAchievementList.getJSONObject(i));

            achievementList.add(achievement);
        }

        return achievementList;

    }

    /**
     * Gets all achievements of a friend.
     * @param friend friend object of whom to get the achievements
     * @return list of achievements
     */
    public static List<Achievement> getFriendsAchievements(User friend) {

        JSONObject friendJson = new JSONObject();
        friendJson.put("username", friend.getUsername());

        // send the friend request to the server
        NetworkManager manager = new NetworkManager(5000);
        NetworkResponse result = manager.sendJson(host
                + "/activities/getFriendsAchievements", friendJson, sessionId);

        // check for failure
        if (!result.wasSuccess()) {
            System.out.println("Couldn't retrieve  friends achievements: "
                    + result.getStatus().toString());
            return null;
        }

        List<Achievement> achievementList = new ArrayList<>();
        var dataObject = new JSONObject((String) result.getData());

        // parses the json into achievements
        JSONArray jsonAchievementList = dataObject.getJSONArray("achievements");
        for (int i = 0; i < jsonAchievementList.length(); i++) {
            JSONObject jsonAchievement = jsonAchievementList.getJSONObject(i);
            Achievement achievement = JsonParsing.parseAchievement(jsonAchievement);
            achievement.setAcquired(jsonAchievement.getBoolean("acquired"));

            achievementList.add(achievement);
        }

        return achievementList;

    }

    /**
     * Logs the user in by fetching session id from the server.
     * @param username username of the user
     * @param password password of the user
     */
    public static Boolean login(String username, String password) {

        // create login JSON object
        JSONObject accountJson = new JSONObject();
        accountJson.put("username", username);
        accountJson.put("password", password);

        // send the login data to the server
        NetworkManager manager = new NetworkManager(5000);
        NetworkResponse result = manager.sendJson(host + "/login", accountJson, sessionId);

        // check for failure
        if (!result.wasSuccess()) {
            System.out.println("Couldn't retrieve session id: " + result.getStatus().toString());
            return false;
        }

        // store the session id locally
        if (result.getCookie() != null) {
            sessionId = result.getCookie();
            SessionIO.write(sessionId);
        }

        var dataObject = new JSONObject((String) result.getData());

        return dataObject.getBoolean("requestSucces");
    }

    /**
     * Logs out an user if they logged in previously.
     */
    public static Boolean logout() {
        // send the session id to the server to invalidate it
        NetworkManager manager = new NetworkManager(5000);
        NetworkResponse<JSONObject> result = manager.getJson(host + "/logout", sessionId);

        SessionIO.write(null);

        return result.getData().getBoolean("requestSucces");

    }

    /**
     * Sends an added activity to the server, where it's stored.
     * E.g. when the user clicks 'Ate local produce', this should be sent to the server
     * @param activity the activity object containing information about the added activity
     */
    public static Boolean addActivity(Activity activity) {
        // create a JSON object for the activity
        JSONObject activityJson = new JSONObject();
        activityJson.put("id", activity.getId());

        // send the object to the server
        NetworkManager manager = new NetworkManager(5000);
        NetworkResponse result = manager.sendJson(host
                + "/activities/put", activityJson, sessionId);

        // check for failure
        if (!result.wasSuccess()) {
            System.out.println("Couldn't submit activity: " + result.getStatus().toString());
            return false;
        }

        var dataObject = new JSONObject((String) result.getData());

        return dataObject.getBoolean("requestSucces");
    }

    /**
     * Fetches the leaderboard from the server.
     * @return an object containing leaderboard data
     */
    public static List<User> getLeaderboard() {

        // retrieve activity history from server
        NetworkManager manager = new NetworkManager(5000);
        NetworkResponse<JSONObject> result = manager.getJson(host
                + "/user/getLeaderboard", sessionId);

        // check for failure
        if (!result.wasSuccess()) {
            System.out.println(
                    "Couldn't retrieve leaderboard: "
                            + result.getStatus().toString()
            );
            return null;
        }

        // create list to put users in
        List<User> leaderboard = new ArrayList<>();

        JsonParsing.cache = false;

        JSONArray friendsJson = result.getData().getJSONArray("ranking");
        for (int i = 0; i < friendsJson.length(); i++) {
            User friend = JsonParsing.parseUser(friendsJson.getJSONObject(i));

            leaderboard.add( friend );


        }

        JsonParsing.cache = true;

        return leaderboard;
    }

    /**
     * method that send a users profile picture to the server.
     * @param path the path of the image to be send to the server.
     * @return true if it was a succes, false otherwise.
     */
    public static boolean sendImage(String path) {

        // set the timeout to 5 seconds
        NetworkManager manager = new NetworkManager(5000);

        // send image to webpage
        NetworkResponse response = manager.sendImage(host + "/setProfilePic", path);

        if (!response.wasSuccess()) {
            System.out.println("couldn't upload image: " + response.getStatus().toString());
            return false;
        }

        var dataObject = new JSONObject(response.getData());

        return true;
    }

    /**
     * method that creates an account with the given data.
     * @param username the username of the account to be created.
     * @param password the password of the account.
     * @param firstname the firstname of the account.
     * @param emailaddress the emailaddress of the account.
     * @return true if account creation was successful, false otherwise.
     */
    public static boolean createAccount(String username,
                                        String password,
                                        String firstname,
                                        String emailaddress) {

        // create account JSON object
        JSONObject accountJson = new JSONObject();
        accountJson.put("username", username);
        accountJson.put("password", password);
        accountJson.put("emailadress", emailaddress);
        accountJson.put("firstname", firstname);

        // send the login data to the server
        NetworkManager manager = new NetworkManager(5000);
        NetworkResponse result = manager.sendJson(host + "/createAccount", accountJson, sessionId);

        // check for failure
        if (!result.wasSuccess()) {
            System.out.println("Couldn't get a response: " + result.getStatus().toString());
            return false;
        }

        var dataObject = new JSONObject((String) result.getData());

        return dataObject.getBoolean("requestSucces");

    }

    /**
     * method that sends the id of a method to be removed from a user activity history.
     * @param activityId the Id of the activity to be removed.
     * @return true if the removal was a succes, false if not.
     */
    public static boolean removeActivity(int activityId) {
        // create login JSON object
        JSONObject accountJson = new JSONObject();
        accountJson.put("logId", activityId);

        // send the login data to the server
        NetworkManager manager = new NetworkManager(5000);
        NetworkResponse result = manager.sendJson(host
                + "/activities/deleteActivity", accountJson, sessionId);

        // check for failure
        if (!result.wasSuccess()) {
            System.out.println("Couldn't get a response with the server: "
                    + result.getStatus().toString());
            return false;
        }

        // store the session id locally
        if (result.getCookie() != null) {
            sessionId = result.getCookie();
        }

        var dataObject = new JSONObject((String) result.getData());

        return dataObject.getBoolean("requestSucces");
    }

    /**
     * method that sends the password that will replace the current password.
     * @param newPassword the new password provided by the user.
     * @return returns true if changepassword was successful, else false.
     */
    public static Boolean changePassword(String newPassword) {

        // create login JSON object
        JSONObject accountJson = new JSONObject();
        accountJson.put("password", newPassword);

        // send the login data to the server
        NetworkManager manager = new NetworkManager(5000);
        NetworkResponse result = manager.sendJson(host + "/changePassword", accountJson, sessionId);

        // check for failure
        if (!result.wasSuccess()) {
            System.out.println("Couldn't retrieve a response from the server: "
                    + result.getStatus().toString());
            return false;
        }

        // store the session id locally
        if (result.getCookie() != null) {
            sessionId = result.getCookie();
        }

        var dataObject = new JSONObject((String) result.getData());

        return dataObject.getBoolean("requestSucces");
    }

    /**
     * method that sends the email that will replace the current email.
     * @param emailAddress the new email provided by the user.
     * @return returns true if changeEmail was successful, else false.
     */
    public static Boolean changeEmaiAddress(String emailAddress) {

        // create login JSON object
        JSONObject accountJson = new JSONObject();
        accountJson.put("emailadress", emailAddress);

        // send the login data to the server
        NetworkManager manager = new NetworkManager(5000);
        NetworkResponse result = manager.sendJson(host + "/changeEmail", accountJson, sessionId);

        // check for failure
        if (!result.wasSuccess()) {
            System.out.println("Couldn't retrieve a response from the server, it might be ofline: "
                    + result.getStatus().toString());
            return false;
        }

        // store the session id locally
        if (result.getCookie() != null) {
            sessionId = result.getCookie();
        }

        var dataObject = new JSONObject((String) result.getData());

        return dataObject.getBoolean("requestSucces");
    }
    /**
     * method that sends the firstname that will replace the current firstname.
     * @param firstname the new firstname provided by the user.
     * @return returns true if changeFirstname was successful, else false.
     */

    public static Boolean changeFirstname(String firstname) {

        // create login JSON object
        JSONObject accountJson = new JSONObject();
        accountJson.put("firstname", firstname);

        // send the login data to the server
        NetworkManager manager = new NetworkManager(5000);
        NetworkResponse result = manager.sendJson(host
                + "/changeFirstname", accountJson, sessionId);

        // check for failure
        if (!result.wasSuccess()) {
            System.out.println("Couldn't retrieve a response from the server: "
                    + result.getStatus().toString());
            return false;
        }

        // store the session id locally
        if (result.getCookie() != null) {
            sessionId = result.getCookie();
        }

        var dataObject = new JSONObject((String) result.getData());

        return dataObject.getBoolean("requestSucces");
    }

    /**
     * method that delete certain account.
     * @return true if it is deleted successfully, false if not
     */
    public static boolean deleteAccount() {

        // retrieve User data from server.
        NetworkManager manager1 = new NetworkManager(5000);
        NetworkResponse<JSONObject> result = manager1.getJson(host + "/deleteAccount", sessionId);

        // check for failure
        if (!result.wasSuccess()) {
            System.out.println(
                    "Couldn't retrieve User Data: "
                            + result.getStatus().toString()
            );
            return false;
        }

        return result.getData().getBoolean("requestSucces");
    }

    /**
     * Change privacy settings for user from public to private or vice versa.
     * @return retruns true if change was successful else returns false.
     */
    public static boolean setPrivacySetting() {

        // send request to change the privacy settings
        NetworkManager manager = new NetworkManager(5000);
        NetworkResponse<JSONObject> result = manager.getJson(host + "/changePrivacy", sessionId);

        return result.getData().getBoolean("requestSucces");
    }


    /**
     * Revokes friend request that the main user has sent to another user.
     * @param username the name of the user whom received the request
     * @return returns if it revoking was successful or not by returning a boolean.
     */
    public static boolean revokeFriendRequest(String username) {

        // create username object
        JSONObject accountJson = new JSONObject();
        accountJson.put("username", username);

        // send the username data to the server
        NetworkManager manager = new NetworkManager(5000);
        NetworkResponse result = manager.sendJson(host + "/user/revokeFriendRequest",
                accountJson, sessionId);
        // check for failure
        if (!result.wasSuccess()) {
            System.out.println("Couldn't retrieve session id: " + result.getStatus().toString());
            return false;
        }

        // store the session id locally
        if (result.getCookie() != null) {
            sessionId = result.getCookie();
        }

        var dataObject = new JSONObject((String) result.getData());

        return dataObject.getBoolean("requestSucces");
    }

    /**
     * method to check if you are logged in.
     * @return true or false dependent on if your session is logged in.
     */
    public static boolean amIloggedIn() {

        // send request to change the privacy settings
        NetworkManager manager = new NetworkManager(5000);
        NetworkResponse<JSONObject> result = manager.getJson(host + "/amIloggedIn", sessionId);

        // check for failure
        if (!result.wasSuccess()) {
            System.out.println("Couldn't retrieve session id: " + result.getStatus().toString());
            return false;
        }

        return result.getData().getBoolean("requestSucces");

    }
}
