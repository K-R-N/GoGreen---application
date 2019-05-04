package server;

import database.DbAccess;
import database.DbRetrieve;
import models.Achievement;
import models.Activity;
import models.ActivityType;
import models.Contribution;
import models.User;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 * Class that manages the endpoint for manipulating activities of user.
 */
// Marks that this controller uses the REST API.
@RestController
// Set the base path that this class manages
@RequestMapping(ActivityController.classPath)
public class ActivityController {

    /**
     * Defines the base path of which all endpoints in this class are children of.
     */
    static final String classPath = "/activities";

    private static DataSource source;

    /**
     * setup the default database.
     */
    @PostConstruct
    public void setSourcePG() {
        this.source = DbRetrieve.retrievePgDb();
    }

    /**
     * setup SqlLite database for CI.
     */
    public static void setSourceSql( DataSource sourcesql) {
        source = sourcesql;
    }

    /**
     * Endpoint to put an activity via POST into the database.
     * @param activity the activity object in JSON format
     * @param session the session of the request to identify the user
     * */
    @PostMapping(value = "/put")
    public JsonBoolean putActivity(@RequestBody Activity activity,
                              HttpSession session)  {
        if (session.getAttribute("username") == null) {
            return new JsonBoolean(false);
        }

        String username = (String) session.getAttribute("username");
        DbAccess db = new DbAccess(source);

        System.out.println("Insert activity with id: "
                + activity.getId() + " for user " + username);

        db.insertUserActivity(username, activity.getId());

        return new JsonBoolean(true);
    }

    /**
     * Endpoint to get timeline for certain users.
     * @param session the session of the request to identify the user
     */
    @GetMapping(value = "/getTimeline")
    public Map<String , Object> getActivities(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("includeFriends") String includeFriends,
            @RequestParam(value = "isMainUser",defaultValue = "true") String isUserMain,
            HttpSession session
    ) {
        Map<String , Object> map = new HashMap<>();
        if (session.getAttribute("username") == null) {
            return map;
        }
        boolean include = includeFriends.contains("true");
        DbAccess db = new DbAccess(source);
        String username = (String) session.getAttribute("username");
        if (isUserMain.contains("true")) {
            List<Contribution> output =
                    db.getContribution(username, startDate, endDate, include, true);
            map.put("activities", output);
        } else {
            List<Contribution> notmain =
                    db.getContribution(isUserMain,startDate,endDate,false,false);
            map.put("activities",notmain);
        }
        return map;
    }

    /**
     * method that returns a list of all the achievments objects of the user.
     * the achievement object can have a boolean to indicate that the user has this achievement
     * (this has to be discussed)
     * @param session the session of the request to identify the user
     * @return list of achievements.(this might change depends on how we will model achievements).
     */
    @GetMapping(value = "/getAllAchievements")
    public Map<String , Object> getAllAchievements(HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        if (session.getAttribute("username") == null) {
            return map;
        }
        String username = (String) session.getAttribute("username");
        DbAccess db = new DbAccess(source);
        List<Achievement> achievements = db.retrieveAchievements(username);
        map.put("achievements", achievements);
        return map;
    }

    /**
     * method to return all the available activities.
     * @return list of all the posible activities a user can preform.
     */
    @GetMapping(value = "/getAvailableActivities")
     public Map<String , Object> getAvailableActivities() {
        Map<String , Object> map = new HashMap<>();

        DbAccess db = new DbAccess(source);
        List<ActivityType> response = db.retrieveActivityTypes(" ");
        map.put("activities",response);
        return map;
    }

    /**
     * method to delete an activity.
     * @param activity the activity object,
     * @param session to identify the user.
     * @return true if delete was succesfull,
     *      false if otherwise(activity id isn't in an activity of the user).
     */
    @PostMapping(value = "/deleteActivity")
    public JsonBoolean deleteActivity(@RequestBody Activity activity, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return new JsonBoolean(false);
        }
        String username = (String) session.getAttribute("username");
        DbAccess db = new DbAccess(source);
        boolean result = db.deleteActivity(username, activity.getId());
        JsonBoolean output = new JsonBoolean(result);
        return output;
    }

    /**
     * method that returns all the achievements of a friend,
     * this method should check if you are accually friends with this user.
     * @param username object with the name of the friend of who'm you want all the achievements
     * @param session the session of the request to identify the user
     * @return an activityList with achievements.
     */
    @PostMapping(value = "/getFriendsAchievements")
    public Map<String , Object> getFriendsAchievements(@RequestBody User username,
                                               HttpSession session) {
        Map<String, Object> map = new HashMap<>();

        if (session.getAttribute("username") == null) {
            return map;
        }

        String usernameUser = (String) session.getAttribute("username");
        DbAccess db = new DbAccess(source);
        List<Achievement> achievements = new ArrayList<>();
        List<User> friends = db.getFriends(usernameUser);
        for (int i = 0; i < friends.size();i++) {
            if (friends.get(i).getUsername().equals(username.getUsername())) {
                System.out.println(" friend is in list");

                achievements = db.retrieveAchievements(username.getUsername());
            }
        }
        map.put("achievements", achievements);
        return map;
    }


    /**
     * method that returns a list of all the badges objects of the user.
     * @param session the session of the request to identify the user
     * @return list of badges.(this might change depends on how we will model achievements).
     */
    @GetMapping(value = "/getAllBadges")
    public Map<String , Object> getAllBadges(HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        if (session.getAttribute("username") == null) {
            return map;
        }
        String username = (String) session.getAttribute("username");
        DbAccess db = new DbAccess(source);
        List<Achievement> achievements = db.retrieveAchievements(username);
        List<String> badgesurl = new ArrayList<>();
        for (Achievement a:achievements) {
            badgesurl.add(a.getBigBadgePath());
        }
        map.put("badges", badgesurl);
        return map;
    }

    /**
     * method that return the image of a badge or activity.
     * @param id the id of the category or category of which to return the image.
     * @param isCategory if the method should get the catergory image or not.
     * @param isBig if the method should return a big or small image.
     * @return byte array of a png image.
     */
    @RequestMapping(value = "/getImage", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(
            @RequestParam("id") String id,
            @RequestParam("isCategory") String isCategory,
            @RequestParam("isBig") String isBig
    ) {
        Integer.parseInt(id);
        boolean category = Boolean.parseBoolean(isCategory);
        boolean isbig = Boolean.parseBoolean(isBig);
        byte[] image;
        DbAccess db = new DbAccess(source);
        if ( ! category) {

            if (isbig) {
                image = db.getBadgeLarge(Integer.parseInt(id));
            } else {
                image = db.getBadgeSmall(Integer.parseInt(id));
            }

        } else {
            image = db.getCategoryImage(Integer.parseInt(id));
        }


        return image;
    }
}
