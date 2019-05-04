package database;

import models.Achievement;
import models.Activity;
import models.ActivityCategory;
import models.ActivityType;
import models.Contribution;
import models.FriendRequest;
import models.User;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;

/**
 * Class used to send request to the database with JDBC driver.
 */
public class DbAccess implements DbTemplate {
    private DataSource source;

    /**
     * The constructor for the class DbAccess.
     * @param source the DataSource to be passed to the class.
     */
    public DbAccess(DataSource source) {
        this.source = source;
    }

    /**
     * Getter for the DataSource attribute.
     * @return the DataSource of the object.
     */
    public DataSource getDataSource() {
        return source;
    }

    /**
     * Setter for the Database attribute.
     * @param source the DataSource to set.
     */
    public void setDataSource(DataSource source) {
        this.source = source;
    }

    /**
     * Returns user object from database.
     * @param username username of the user
     * @param isMain boolean indicating whether user is main user
     * @return User object containing all data
     */
    public User getUser(String username, boolean isMain) {
        if (username == null) {
            System.out.println("FAIL: no username provided!");
            return null;
        }
        String sqlQuery = DbQueries.retrieve("get_user");
        User user = null;
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement(sqlQuery);
            pst.setString(1, username);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                if (isMain) {
                    user = new User(
                        result.getInt(4),
                        result.getString(1),
                        "/user/getProfilePic?username=" + result.getString(1),
                        result.getString(5),
                        result.getString(3)
                    );
                    user.setPrivacy(result.getString(6));
                } else {
                    user = new User(
                        result.getInt(4),
                        result.getString(1),
                        "/user/getProfilePic?username=" + result.getString(1)
                    );
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: exception raised when retrieving user!");
            return null;
        }

        return user;
    }

    /**
     * A method used to retrieve the total points of a user.
     * @param username username of a user
     * @return the total green points of the user
     */
    public int retrieveTotalPoints(String username) {
        int totalPoints = 0;
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement(DbQueries.retrieve("get_points"));
            pst.setString(1, username);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                totalPoints = result.getInt(1);
            }
            result.close();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Points couldn't be found");
            return 0;
        }
        return totalPoints;
    }


    /**
     * Returns password hash for a user.
     * @param username username
     * @return hash
     */
    public String getPasswordHash(String username) {
        if (username == null) {
            System.out.println("FAIL: no username provided!");
            return null;
        }

        String pwdHash = null;
        try {
            Connection connection = getDataSource().getConnection();

            // get user details
            String userQuery = DbQueries.retrieve("get_password");
            PreparedStatement userPrepared = connection.prepareStatement(userQuery);
            userPrepared.setString(1, username);
            ResultSet result = userPrepared.executeQuery();
            while (result.next()) {
                pwdHash = result.getString(1);
            }
            userPrepared.close();
            result.close();

            // close connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: exception raised when retrieving password hash!");
            return null;
        }

        return pwdHash;
    }

    /**
     * Gets all friends for a main user.
     * @param username username of the main user
     * @return list of users
     */
    public List<User> getFriends(String username) {
        if (username == null) {
            System.out.println("FAIL: no username provided!");
            return null;
        }

        List<String> friendStrList = new ArrayList<>();
        try {
            Connection connection = getDataSource().getConnection();

            // get friends
            String friendsQuery = DbQueries.retrieve("get_friends");
            PreparedStatement friendsPrepared = connection.prepareStatement(friendsQuery);
            friendsPrepared.setString(1, username);
            friendsPrepared.setString(2, username);
            ResultSet result = friendsPrepared.executeQuery();
            while (result.next()) {
                friendStrList.add(result.getString("friend_name"));
            }
            friendsPrepared.close();
            result.close();

            // close connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: exception raised when retrieving friends!");
            return null;
        }

        List<User> friendList = new ArrayList<>();
        for (String friendName: friendStrList) {
            friendList.add(getUser(friendName, false));
        }

        return friendList;
    }

    /**
     * Returns main user and his friends sorted on total points.
     * @param username username of the main user
     * @return sorted list of users
     */
    public List<User> getRankingList(String username) {
        if (username == null) {
            System.out.println("FAIL: no username provided!");
            return null;
        }
        List<User> users = new ArrayList<>();
        String sqlQuery = DbQueries.retrieve("get_rl");
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement(sqlQuery);
            pst.setString(1, username);
            pst.setString(2, username);
            pst.setString(3, username);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                User user = new User(
                        result.getInt(2),
                        result.getString(1),
                        "/user/getProfilePic?username=" + result.getString(1)
                );
                user.setMainUser(user.getUsername().equals(username));
                users.add(user);
            }
            result.close();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't retrieve the ranking list.");
            return null;
        }

        return users;
    }

    /**
     * Returns list of contributions for certain time range.
     * @param username username of main user
     * @param startDate startdate of timeline
     * @param endDate enddate of timeline
     * @param includeFriends boolean stating whether to include friends
     * @param isMain is the user making the request the main user
     * @return list of contributions
     */
    public List<Contribution> getContribution(String username,
                                              String startDate,
                                              String endDate,
                                              boolean includeFriends, boolean isMain) {
        if (!isMain && includeFriends) {
            System.out.println("FAIL: Wrong combination");
            return null;
        }
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern( "yyyy-MM-dd");
        String timeColonPattern = "HH:mm:ss";
        LocalDate start = LocalDate.parse(startDate, formatterDate);
        LocalDate end = LocalDate.parse(endDate, formatterDate);
        DateTimeFormatter timeColonFormatter = DateTimeFormatter.ofPattern(timeColonPattern);
        List<Contribution> contributions = new ArrayList<>();
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = null;
            //to retrieve the full timeline of a user with
            // their friends(the ones that have public profile).
            if (includeFriends) {
                String query = DbQueries.retrieve("get_timeline_fr");
                pst = connection.prepareStatement(query);
                pst.setString(1, username);
                pst.setString(2, username);
                pst.setString(3, username);
                pst.setString(4, username);
                pst.setString(5, username);
                pst.setString(6, username);
                /* This branch can't be tested because it is impossible
                 * to have an actual Postgres data source due to our local database.
                 */
                if (this.getDataSource() instanceof SQLiteDataSource) {
                    pst.setString(7, start.toString());
                    pst.setString(8, end.toString());
                } else {
                    pst.setDate(7, java.sql.Date.valueOf(start));
                    pst.setDate(8, java.sql.Date.valueOf(end));
                }
            } else {
                //Retrieve the timeline of a specific friend of the user.
                // Privacy settings must be public.
                String query = DbQueries.retrieve("get_timeline_single");
                //Retrieve the timeline of the main user.
                if (isMain) {
                    query = DbQueries.retrieve("get_timeline_main");
                }
                pst = connection.prepareStatement(query);
                pst.setString(1, username);
                pst.setString(2, username);
                /* This branch can't be tested because it is impossible
                 * to have an actual Postgres data source due to our local database.
                 */
                if (this.getDataSource() instanceof SQLiteDataSource) {
                    pst.setString(3, start.toString());
                    pst.setString(4, end.toString());
                } else {
                    pst.setDate(3, java.sql.Date.valueOf(start));
                    pst.setDate(4, java.sql.Date.valueOf(end));
                }
            }
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                String categoryName = result.getString("activity_category_name");
                if (categoryName == null) {
                    String userN = result.getString("user");
                    User user = new User(
                            retrieveTotalPoints(userN),
                            userN,
                            "/user/getProfilePic?username=" + userN
                    );
                    user.setMainUser(userN.equals(username));
                    Achievement ach = new Achievement(result.getInt("contribution_id"),
                            result.getString("contribution_name"),
                            result.getString("contribution_description"),
                            "/activities/getImage?id="
                                    + result.getInt("contribution_id")
                                    + "&isCategory=false&isBig=false",
                            "/activities/getImage?id="
                                    + result.getInt("contribution_id")
                                    + "&isCategory=false&isBig=true",
                            user,
                            LocalDate.parse(result.getString("date"), formatterDate),
                            LocalTime.parse(result.getString("time"), timeColonFormatter));
                    contributions.add(ach);
                } else {
                    String userN = result.getString("user");
                    User user = new User(
                            retrieveTotalPoints(userN),
                            userN,
                            "/user/getProfilePic?username=" + userN
                    );
                    user.setMainUser(userN.equals(username));
                    ActivityCategory category = new ActivityCategory(
                            result.getInt("activity_category_id"), categoryName,
                            result.getString("activity_category_description"), null);
                    ActivityType type = new ActivityType(result.getInt("contribution_id"),
                            result.getString("contribution_name"),
                            result.getInt("activity_points"),
                            category, result.getString("contribution_description"));
                    type.setSubcategoryName(result.getString("subcategory"));
                    Activity activity = new Activity(result.getInt("log_id"),
                            type, user,
                            LocalDate.parse(result.getString("date"), formatterDate),
                            LocalTime.parse(result.getString("time"), timeColonFormatter));
                    contributions.add(activity);
                }
            }
            result.close();
            pst.close();


            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't retrieve the timeline");
            return null;
        }
        return contributions;

    }


    /**
     * A method used to add a new activity done by a user.
     * @param username username of a user
     * @param activityId the activity to be added to the user's records
     * @return returns false if SQLException was thrown.
     */
    public boolean insertUserActivity(String username, int activityId) {
        List<Integer> solar = Arrays.asList(20, 21, 22);
        if (solar.contains(activityId)
                && DbActivity.solarCheck(this.getDataSource(), username, activityId)) {
            System.out.println("Limit for Solar panels has been exceeded");
            return false;
        }
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement("INSERT INTO activity_log "
                    + "(username, activity_id, date, time) VALUES (?, ?, ?, ?)");

            // set variables
            pst.setString(1, username);
            pst.setInt(2, activityId);
            ZoneId zone1 = ZoneId.of("UTC");
            LocalDate localDate = LocalDate.now(zone1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = localDate.format(formatter);
            String timeColonPattern = "HH:mm:ss";
            DateTimeFormatter timeColonFormatter = DateTimeFormatter.ofPattern(timeColonPattern);
            LocalTime localTime = LocalTime.now(zone1).truncatedTo(ChronoUnit.SECONDS);
            String formattedTime = localTime.format(timeColonFormatter);
            /* This branch can't be tested because it is impossible
             * to have an actual Postgres data source due to our local database.
             */
            if (this.getDataSource() instanceof SQLiteDataSource) {
                pst.setString(3, formattedDate);
                pst.setString(4, formattedTime);
            } else {
                pst.setDate(3, java.sql.Date.valueOf(formattedDate));
                pst.setTime(4, java.sql.Time.valueOf(formattedTime));
            }
            pst.executeUpdate();
            pst.close();
            connection.close();
            List<Integer> food = Arrays.asList(1,2);
            List<Integer> transport = Arrays.asList(3,4,5,6,7,8,9,10,11);
            List<Integer> temperature = Arrays.asList(12,13,14,15,16,17,18);
            List<Integer> energy = Arrays.asList(19,20,21,22);
            List<Integer> other = Arrays.asList(23,24);

            if (food.contains(activityId)) {
                DbAchievements.checkInsertFood(this.getDataSource(), username);
            } else if (transport.contains(activityId)) {
                DbAchievements.checkInsertTransport(this.getDataSource(),username);
            } else if (energy.contains(activityId)) {
                DbAchievements.checkInsertEnergy(this.getDataSource(), username);
            } else if (temperature.contains(activityId)) {
                DbAchievements.checkInsertTemperature(this.getDataSource(), username);
            } else if (other.contains(activityId)) {
                DbAchievements.checkInsertOther(this.getDataSource(), username);
            }
        } catch (SQLException  e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't add a new activity");
            return false;
        }
        return true;
    }

    /**
     * Counts the number of the rows. It is used for testing purposes.
     * @param tbName The name of the table
     * @return the number of rows
     */
    public int countRows(String tbName) {
        int numRows = 0;
        try {
            Connection connection = getDataSource().getConnection();
            //This statement is used for testing other methods so no risk of SQL Injection
            Statement st = connection.createStatement();
            ResultSet result = st.executeQuery("Select Count(*) as row From " + tbName);
            while (result.next()) {
                numRows = result.getInt(1);
            }
            st.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't count rows");
            return 0;
        }
        return numRows;
    }

    /**
     * A method used to change the password of a user.
     * @param username the username of the user
     * @param password the new password to set.
     * @return returns false if SQLException was thrown.
     */
    public boolean changePassword(String username, String password) {
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement("Update user_details "
                    + "Set password = ? Where username = ?;");
            pst.setString(1, password);
            pst.setString(2, username);
            pst.executeUpdate();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't change password");
            return false;
        }
        return true;
    }

    /**
     * A method used to change the first name of a user.
     * @param username the username of the user
     * @param name the first name to set
     * @return returns false if SQLException was thrown.
     */
    public boolean changeFirstName(String username, String name) {
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement("Update user_details "
                    + "Set first_name = ? Where username = ?;");
            pst.setString(1, name);
            pst.setString(2, username);
            pst.executeUpdate();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't change first name");
            return false;
        }
        return true;
    }

    /**
     * A method used to change the email address of a user.
     * @param username the username of the user
     * @param email the email address to set
     * @return returns false if SQLException was thrown.
     */
    public boolean changeEmail(String username, String email) {
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement("Update user_details "
                    + "Set email_address = ? Where username = ?;");
            pst.setString(1, email);
            pst.setString(2, username);
            pst.executeUpdate();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't change email address");
            return false;
        }
        return true;
    }

    /**
     * A method used to delete a user from the database.
     * @param username the username of the user
     * @return returns false if SQLException was thrown.
     */
    public boolean deleteUser(String username) {
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement("DELETE FROM user_details "
                    + "Where username = ?;");
            pst.setString(1, username);
            pst.executeUpdate();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't delete the user");
            return false;
        }
        return true;
    }

    /**
     * A method used to create a new user in the database.
     * @param username the username.
     * @param password the password
     * @param firstName the first name
     * @param email the email address
     * @return returns false if SQLException was thrown.
     */
    public boolean createUser(String username, String password, String firstName, String email) {
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement("INSERT INTO user_details "
                    + "(username, password, first_name, email_address, profile_picture) "
                    + "VALUES (?, ?, ?, ?, NULL);");
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, firstName);
            pst.setString(4, email);
            pst.executeUpdate();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't create a user");
            return false;
        }
        return true;
    }

    /**
     * A method used to send a friend request.
     * @param fromUsername the username of the user sending the request
     * @param toUsername the username of the user receiving the request
     * @return returns false if SQLException was thrown.
     */
    public boolean sendRequest(String fromUsername, String toUsername) {
        //check if the user is sending a friend to an exiting friend.
        //the database contains a check constraint to ensure
        //that a user can't send a request to themselves.
        if (getFriendsUserName(fromUsername).contains(toUsername)) {
            System.out.println("FAIL: Can't send a friend request to an existing friend.");
            return false;
        }
        String query  = "INSERT INTO friend_request(from_username, to_username) "
                + "VALUES (?, ?);";
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, fromUsername);
            pst.setString(2, toUsername);
            pst.executeUpdate();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't send a friend request.");
            return false;
        }
        return true;
    }

    /**
     * A method used to delete a friend of a user.
     * @param fromUsername the username of the user making the deletion request
     * @param toUsername the username of the friend to delete.
     * @return returns false if SQLException was thrown.
     */
    public boolean deleteFriend(String fromUsername, String toUsername) {
        String query = "DELETE FROM friends_with "
                + "WHERE (username1 = ? And username2 = ?) Or (username1 = ? And username2 = ?);";
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, fromUsername);
            pst.setString(2, toUsername);
            pst.setString(3, toUsername);
            pst.setString(4, fromUsername);
            pst.executeUpdate();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't delete a friend");
            return false;
        }
        return true;
    }

    /**
     * A method used to retrieve the achievements for profile page.
     * @param username the username of the user.
     * @return the list of achievements.
     */
    public List<Achievement> retrieveAchievements(String username) {
        List<Achievement> achievements = new ArrayList<>();
        String sqlQuery = DbQueries.retrieve("get_achievements");
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement(sqlQuery);
            pst.setString(1, username);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                Achievement ach = new Achievement(
                        result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        "/activities/getImage?id="
                                + result.getInt(1)
                                + "&isCategory=false&isBig=false",
                        "/activities/getImage?id="
                                + result.getInt(1)
                                + "&isCategory=false&isBig=true",
                        null,
                        null,
                        null
                );
                System.out.println("Acquired: " + result.getBoolean(4));
                ach.setAcquired(result.getBoolean(4));
                achievements.add(ach);
            }
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't retrieve the achievements");
            return null;
        }
        return achievements;
    }

    /**
     * A method used to retrieve a list of users.
     * @param pattern the first few characters of username
     * @return a list of 3 users
     */
    public List<String> retrieveSuggestions(String pattern) {
        List<String> users = new ArrayList<>();
        String query = "Select username "
                + "From user_details "
                + "Where username Like ?"
                + "Limit 3;";
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, pattern + "%");
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                users.add(result.getString(1));
            }
            result.close();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't retrieve friend suggestions");
            return null;
        }
        return users;
    }

    /**
     * A method used to retrieve the friend request of a user.
     * @param username the username of the user.
     * @return list of pending friend request.
     */
    public List<FriendRequest> retrieveRequests(String username) {
        List<FriendRequest> requests = new ArrayList<>();
        String query = "Select request_id, from_username "
                + "From friend_request "
                + "Where to_username = ?;";
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, username);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                FriendRequest request = new FriendRequest(result.getInt(1), result.getString(2));
                requests.add(request);
            }
            result.close();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't retrieve pending friend requests");
            return null;
        }
        return requests;
    }

    /**
     * A method used to accept a friend request.
     * @param username the username of the user who received the request.
     * @param id the friend request id.
     * @return returns false if SQLException was thrown.
     */
    public boolean acceptRequest(String username, int id) {
        String query1 = "INSERT INTO friends_with "
                + "(username1, username2) VALUES "
                + "((SELECT from_username FROM friend_request "
                + "Where to_username = ? AND request_id = ?), ?);";
        String query2 = "DELETE FROM friend_request "
                + "Where to_username = ? AND request_id = ?;";
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst1 = connection.prepareStatement(query1);
            pst1.setString(1, username);
            pst1.setInt(2, id);
            pst1.setString(3, username);
            PreparedStatement pst2 = connection.prepareStatement(query2);
            pst1.executeUpdate();
            pst1.close();
            pst2.setString(1, username);
            pst2.setInt(2, id);
            pst2.executeUpdate();
            pst2.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't accept a friend request");
            return false;
        }
        return true;
    }

    /**
     * A method used to accept a friend request.
     * @param username the username of the user who received the request.
     * @param id the friend request id.
     * @return returns false if SQLException was thrown.
     */
    public boolean rejectRequest(String username, int id) {
        String query = "Delete From friend_request "
                + "Where to_username = ? And request_id = ?;";
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, username);
            pst.setInt(2, id);
            pst.executeUpdate();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't reject a friend request");
            return false;
        }
        return true;
    }

    /**
     * A method used to retrieve the friend request sent by a user.
     * @param username the username of the user.
     * @return the list of own friend requests.
     */
    public List<FriendRequest> retrieveOwnRequests(String username) {
        List<FriendRequest> requests = new ArrayList<>();
        String query = "Select request_id, to_username "
                + "From friend_request "
                + "Where from_username = ?;";
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, username);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                FriendRequest request = new FriendRequest(result.getInt(1), result.getString(2));
                requests.add(request);
            }
            result.close();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't retrieve friend requests");
            return null;
        }
        return requests;
    }

    /**
     * A method used to revoke a friend request sent by a user.
     * @param username the username of the user.
     * @param id the id of the friend request.
     * @return returns false if SQLException was thrown.
     */
    public boolean revokeRequest(String username, int id) {
        String query = "Delete From friend_request "
                + "Where from_username = ? And request_id = ?;";
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, username);
            pst.setInt(2, id);
            pst.executeUpdate();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't revoke a friend request");
            return false;
        }
        return true;
    }

    /**
     * A method to retrieve the types of activities from the database.
     * @param username the username of the user
     * @return returns the list of activities types.
     */
    public List<ActivityType> retrieveActivityTypes(String username) {
        List<ActivityType> types = new ArrayList<>();
        String query = DbQueries.retrieve("get_activities");
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                ActivityCategory category = new ActivityCategory(result.getInt(4),
                        result.getString(5), result.getString(6), null);
                ActivityType type = new ActivityType(result.getInt(1), result.getString(2),
                        result.getInt(3), category, result.getString(8));
                type.setSubcategoryName(result.getString(7));
                types.add(type);
            }
            result.close();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't retrieve activities");
            return null;
        }
        return types;
    }

    /**
     * A method used to delete an activity from a user's history.
     * @param username the username of the user.
     * @param logId the log id of the activity.
     * @return returns false if SQLException was thrown.
     */
    public boolean deleteActivity(String username, int logId) {
        String query = "Delete From activity_log "
                + "Where username = ? And log_id = ?;";
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, username);
            pst.setInt(2, logId);
            int activityId = DbAchievements.getActivityTypeId(this.getDataSource(), logId);
            pst.executeUpdate();
            //This part deals with checking if the user can keep their achievement.
            List<Integer> food = Arrays.asList(1,2);
            List<Integer> transport = Arrays.asList(3,4,5,6,7,8,9,10,11);
            List<Integer> temperature = Arrays.asList(12,13,14,15,16,17,18);
            List<Integer> energy = Arrays.asList(19,20,21,22);
            List<Integer> other = Arrays.asList(23,24);
            if (food.contains(activityId)) {
                DbAchievements.checkDeleteFood(this.getDataSource(), username);
            } else if (transport.contains(activityId)) {
                DbAchievements.checkDeleteTransport(this.getDataSource(),username);
            } else if (energy.contains(activityId)) {
                DbAchievements.checkDeleteEnergy(this.getDataSource(), username);
            } else if (temperature.contains(activityId)) {
                DbAchievements.checkDeleteTemperature(this.getDataSource(), username);
            } else if (other.contains(activityId)) {
                DbAchievements.checkDeleteOther(this.getDataSource(), username);
            }
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't delete an activity");
            return false;
        }
        return true;
    }

    /**
     * A method used to updated the profile picture of a user.
     * @param username thee username of the user
     * @param pic the picture.
     * @return returns false if SQLException was thrown.
     */
    public boolean setProfilePicture(String username, byte[] pic) {
        String query = "UPDATE user_details SET profile_picture = ? WHERE username = ?;";
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(2, username);
            pst.setBytes(1, pic);
            pst.executeUpdate();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't update the profile picture");
            return false;
        }
        return true;
    }

    /**
     * A method used to retrieve the profile picture of a user.
     * @param username the username of the user.
     * @return the picture as binary array.
     */
    public byte[] getProfilePicture(String username) {
        String query = "Select profile_picture From user_details Where username = ?;";
        byte[] array = null;
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, username);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                array = result.getBytes(1);
            }
            result.close();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't get the profile picture");
            return null;
        }
        return array;
    }

    /**
     * A method used to retrieve the large badge of an achievement.
     * @param id the id of the achievement.
     * @return the image as binary array.
     */
    public byte[] getBadgeLarge(int id) {
        String query = "Select badge_large From achievement Where achievement_id = ?;";
        byte[] array = null;
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                array = result.getBytes(1);
            }
            result.close();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't get the large badge");
            return null;
        }
        return array;
    }

    /**
     * A method used to retrieve the small badge of an achievement.
     * @param id the id of the achievement.
     * @return the image as binary array.
     */
    public byte[] getBadgeSmall(int id) {
        String query = "Select badge_small From achievement Where achievement_id = ?;";
        byte[] array = null;
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                array = result.getBytes(1);
            }
            result.close();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't get the small badge");
            return null;
        }
        return array;
    }

    /**
     * A method to change the privacy settings of a user.
     * @param username the username of a user.
     * @param type the settings type.
     * @return returns false if SQLException was thrown.
     */
    public boolean changePrivacySettings(String username, String type) {
        String query = "UPDATE user_details SET privacy_settings = ? WHERE username = ?;";
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, type);
            pst.setString(2, username);
            pst.executeUpdate();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't change the privacy settings");
            return false;
        }
        return true;
    }

    /**
     * A method to retrieve the image of an activity category.
     * @param id the id of the category
     * @return the image as binary array.
     */
    public byte[] getCategoryImage(int id) {
        String query = "Select category_image From category Where category_id = ?;";
        byte[] array = null;
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                array = result.getBytes(1);
            }
            result.close();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't get the category image");
            return null;
        }
        return array;
    }

    /**
     * A method used to retrieve the usernames of the friends of a user.
     * @param username the username of the user.
     * @return the list of the usernames of the friends.
     */
    public List<String> getFriendsUserName(String username) {
        List<String> friends = new ArrayList<>();
        try {
            Connection connection = getDataSource().getConnection();
            PreparedStatement pst = connection.prepareStatement(DbQueries.retrieve("get_friends"));
            pst.setString(1, username);
            pst.setString(2, username);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                friends.add(result.getString(1));
            }
            result.close();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't get the category image");
            return new ArrayList<String>();
        }
        return friends;
    }
}
