package database;

import models.Achievement;
import models.ActivityType;
import models.Contribution;
import models.FriendRequest;
import models.User;

import java.util.List;

public interface DbTemplate {

    //A method used to retrieve the details of a user.
    User getUser(String username, boolean isMain);

    //A method used to retrieve the total points of a user.
    int retrieveTotalPoints(String username);

    //A method used to retrieve the hashed password of a user.
    String getPasswordHash(String username);

    //A method used to get the friend list of a user
    List<User> getFriends(String username);

    //A method used to get the ranking list of a user.
    List<User> getRankingList(String username);

    //A method used to get the timeline of user with or without their friends.
    List<Contribution> getContribution(String username,
                                       String startDate, String endDate,
                                       boolean includeFriends, boolean isMain);

    //A method used to add a new activity done by a user.
    boolean insertUserActivity(String username, int activityId);

    //Counts the number of the rows.
    int countRows(String tbName);

    //change the password for the user
    boolean changePassword(String username, String password);

    //change first name
    boolean changeFirstName(String username, String name);

    //change email address
    boolean changeEmail(String username, String email);

    //delete a user
    boolean deleteUser(String username);

    //create a user
    boolean createUser(String username, String password, String firstName, String email);

    //send a friend request
    boolean sendRequest(String fromUsername, String toUsername);

    //delete friend
    boolean deleteFriend(String fromUsername, String toUsername);

    //retrieve achievements for a user
    List<Achievement> retrieveAchievements(String username);

    //retrieve friend suggestion
    List<String> retrieveSuggestions(String pattern);

    //retrieve the pending friend requests
    List<FriendRequest> retrieveRequests(String username);

    //accept a friend request
    boolean acceptRequest(String username, int id);

    //reject a friend request
    boolean rejectRequest(String username, int id);

    //retrieve the friend request sent by the user.
    List<FriendRequest> retrieveOwnRequests(String username);

    //revoke a friend request sent by a user.
    boolean revokeRequest(String username, int id);

    //retrieve all the activity types from the database
    List<ActivityType> retrieveActivityTypes(String username);

    //delete an activity from a user's history
    boolean deleteActivity(String username, int logId);

    //sets the profile picture of a user.
    boolean setProfilePicture(String username, byte[] pic);

    //set the profile picture of a user.
    byte[] getProfilePicture(String username);

    //get the badge of an achievement in large size.
    byte[] getBadgeLarge(int id);

    //get the badge of an achievement in small size.
    byte[] getBadgeSmall(int id);

    //change the privacy settings of a user.
    boolean changePrivacySettings(String username, String type);

    //retrieve the image of an activity category.
    byte[] getCategoryImage(int id);

}
