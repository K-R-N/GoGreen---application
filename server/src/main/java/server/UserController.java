package server;

import database.DbAccess;
import database.DbRetrieve;
import models.FriendRequest;
import models.User;
import models.UserList;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 * Class that manages the endpoint for manipulating users.
 */
// Marks that this controller uses the REST API.
@RestController
// Set the base path that this class manages
@RequestMapping(UserController.classPath)
public class UserController {

    /**
     * Defines the base path of which all endpoints in this class are children of.
     */
    static final String classPath = "/user";

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
    public static void setSourceSql(DataSource sourcesql) {
        source = sourcesql;
    }

    /**
     * Endpoint to information for a certain user.
     *
     * @param session the session of the request to identify the user
     */
    @GetMapping(value = "/getUser")
    public User getUserInfo(HttpSession session) {
        if (session.getAttribute("username") == null) {
            return new User(0, null, null);
        }

        String username = (String) session.getAttribute("username");
        DbAccess db = new DbAccess(source);

        User output = db.getUser(username, true);

        return output;

    }

    /**
     * Endpoint to information for a certain user.
     *
     * @param session the session of the request to identify the user
     */
    @GetMapping(value = "/getLeaderboard")
    public Map<String, Object> getLeaderboard(HttpSession session) {
        if (session.getAttribute("username") == null) {
            return null;
        }

        String username = (String) session.getAttribute("username");

        DbAccess db = new DbAccess(source);
        Map<String, Object> map = new HashMap<>();
        map.put("ranking", db.getRankingList(username));
        return map;
    }

    /**
     * method that returns a list of all the friends of the user.
     * * almost the same as getting friends in getUserInfo,
     * only and rfl into string of dbacces method
     *
     * @param session the session of the request to identify the user
     * @return a rankingList, which isn't sorted of points.
     */

    @GetMapping(value = "/getAllFriends")
    public UserList getAllFriends(HttpSession session) {
        List<User> friends = new ArrayList<User>();

        if (session.getAttribute("username") == null) {
            return new UserList(friends);
        }

        String username = (String) session.getAttribute("username");

        DbAccess db = new DbAccess(source);
        friends = db.getFriends(username);

        return new UserList(friends);
    }




    /*
    For the adding friend methods, I am not certain if this is the way we are going to this.
    For now this seems like a good way do this.
    If with we decide on a a way than these methods, might change.
     */

    /**
     * method for sending a friend request.
     *
     * @param username the username of the friend to be added.
     * @param session  the session of the request to identify the user.
     * @return true if it was succesfull, other wise false.
     */
    @PostMapping(value = "/sendFriendRequest")
    public JsonBoolean sendfriendRequest(@RequestBody User username, HttpSession session) {

        if (session.getAttribute("username") == null) {
            return new JsonBoolean(false);
        }
        String usernameUser = (String) session.getAttribute("username");
        DbAccess db = new DbAccess(source);
        int tempid = 0;
        List<Integer> doublecheck = new ArrayList<>();
        List<FriendRequest> req = db.retrieveRequests(username.getUsername());

        for (int i = 0; i < req.size(); i++) {
            if (req.get(i).getFromUser().equals(usernameUser)) {
                doublecheck.add(req.get(i).getRequestId());
                tempid = req.get(i).getRequestId();
            }
        }
        if (doublecheck.size() >= 1) {
            return new JsonBoolean(false);
        }


        JsonBoolean output = new JsonBoolean(db.sendRequest(usernameUser, username.getUsername()));

        return output;

    }

    /**
     * method that accept friend requests.
     *
     * @param username the user of which the friend request originated.
     * @param session  the session of the request to identify the user.
     * @return true if succesfull, false otherwise.
     */
    @PostMapping(value = "/acceptFriendRequest")
    public JsonBoolean acceptFriendRequest(@RequestBody User username,
                                           HttpSession session) {
        if (session.getAttribute("username") == null) {
            return new JsonBoolean(false);
        }
        int tempid = 0;
        String usernameUser = (String) session.getAttribute("username");
        DbAccess db = new DbAccess(source);
        List<FriendRequest> req = db.retrieveRequests(usernameUser);

        for (int x = 0; x < req.size(); x++) {
            if (req.get(x).getFromUser().equals(username.getUsername())) {
                tempid = req.get(x).getRequestId();
            }
        }
        JsonBoolean output = new JsonBoolean(db.acceptRequest(usernameUser, tempid));
        return output;
    }

    /**
     * method that denies friend requests.
     *
     * @param username the user of which the friend request originated.
     * @param session  the session of the request to identify the user
     * @return true if succesfull, false otherwise.
     */
    @PostMapping(value = "/declineFriendRequest")
    public JsonBoolean declineFriendRequest(@RequestBody User username,
                                            HttpSession session) {
        if (session.getAttribute("username") == null) {
            return new JsonBoolean(false);
        }
        int tempid = 0;
        String usernameUser = (String) session.getAttribute("username");
        DbAccess db = new DbAccess(source);

        List<FriendRequest> req = db.retrieveRequests(usernameUser);
        for (int i = 0; i < req.size(); i++) {
            if (req.get(i).getFromUser().equals(username.getUsername())) {
                tempid = req.get(i).getRequestId();
            }
        }
        boolean output = db.rejectRequest(usernameUser, tempid);
        return new JsonBoolean(output);
    }


    /**
     * method that deletes friends.
     *
     * @param username the username of the user with wich to end the friendship.
     * @param session  the session of the request to identify the user
     * @return true if succesfull, false otherwise.
     */
    @PostMapping(value = "/removeFriend")
    public JsonBoolean removeFriend(@RequestBody User username, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return new JsonBoolean(false);
        }

        String usernameUser = (String) session.getAttribute("username");
        DbAccess db = new DbAccess(source);

        JsonBoolean output = new JsonBoolean(db.deleteFriend(usernameUser, username.getUsername()));
        return output;
    }

    /**
     * method that returns a list of all people that have send request to the user.
     *
     * @param session the session of the request to identify the user
     * @return rankinglist with only the friendlist.
     */
    @GetMapping(value = "/getFriendRequests")
    public Map<String, Object> getFriendRequests(
            HttpSession session,
            @RequestParam("getOwn") boolean getOwn
    ) {
        if (session.getAttribute("username") == null) {
            return null;
        }
        String username = (String) session.getAttribute("username");
        DbAccess db = new DbAccess(source);
        List<FriendRequest> req = null;
        if (getOwn) {
            req = db.retrieveOwnRequests(username);
        } else {
            req = db.retrieveRequests(username);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("requests", req);
        return map;
    }

    /**
     * method that returns a list of friendObjects which begin with the a certain pattern.
     *
     * @param pattern with which the username of the returned friends should begin with
     * @param session the session of the request to identify the user
     * @return rankinglist with only the friendlist.
     */
    @PostMapping(value = "/getFriendSuggestions")
    public Map<String, Object> getFriendSuggestions(@RequestBody User pattern,
                                                    HttpSession session) {


        DbAccess db = new DbAccess(source);
        List<User> suggested = new ArrayList<User>();
        List<String> suggestednames = db.retrieveSuggestions(pattern.getUsername());

        for (int i = 0; i < suggestednames.size(); i++) {
            suggested.add(db.getUser(suggestednames.get(i), false));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("suggested", suggested);
        return map;

    }

    /**
     * method that send an image to the client.
     *
     * @param username name of user which to return a profile picture.
     * @return a byte array of an image.
     */
    @GetMapping(value = "/getProfilePic", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getProfilePic(
            @RequestParam("username") String username
    ) {
        System.out.println("GET profile pic for " + username);
        InputStream inputStream;
        byte[] image;

        DbAccess db = new DbAccess(source);

        image = db.getProfilePicture(username);

        return image;
    }

    /**
     *  Method revokes the friend request sent to another user.
     * @param user the name of the user who received the request.
     * @param session the session of the user who is removing the request
     * @return returns a JsonBoolean object with the values true if it was successful else false.
     */
    @PostMapping(value = "/revokeFriendRequest")
    public JsonBoolean revokeRequest(@RequestBody User user, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return new JsonBoolean(false);
        }
        String username = (String) session.getAttribute("username");
        DbAccess db = new DbAccess(source);
        List<FriendRequest> req = db.retrieveOwnRequests(username);
        JsonBoolean revoke = new JsonBoolean(false);
        for (int i = 0; i < req.size(); i++) {
            if (req.get(i).getFromUser().equals(user.getUsername())) {
                int tempid = req.get(i).getRequestId();
                revoke.setRequestSucces(db.revokeRequest(username, tempid));
            }
        }
        return revoke;
    }
}
