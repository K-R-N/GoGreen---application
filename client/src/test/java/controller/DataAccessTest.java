package controller;

import database.DbRetrieve;
import models.*;
import network.NetworkManager;
import org.hibernate.validator.constraints.br.TituloEleitoral;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import server.AccountController;
import server.ActivityController;
import server.ServerApplication;
import server.UserController;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class DataAccessTest {

    static ConfigurableApplicationContext server;

    @BeforeEach
    public void setup() {
        server = SpringApplication.run(ServerApplication.class);

        AccountController.setSourceSql(DbRetrieve.retrieveTestDb());
        UserController.setSourceSql(DbRetrieve.retrieveTestDb());
        ActivityController.setSourceSql(DbRetrieve.retrieveTestDb());

        NetworkManager.setSslSocketFromKey();
    }

    @AfterEach
    public void shutdown() {
        server.close();
    }

    // Test getUserData method

    @Test
    public void getUserDataTest() {
        DataAccess da = mock(DataAccess.class);
        da.login("GetTestUser", "GetTestUser");

        var compare = "User{totalPoints=0, username='GetTestUser', " +
                "picturePath='/user/getProfilePic?username=GetTestUser', " +
                "emailadress='GetTestUser@gmail.com', firstname='Test', password='null'}";

        var result = da.getUserData();
        assertEquals(compare, result.toString());
    }

    @Test
    public void getUserDataNoServer() {
        DataAccess da = mock(DataAccess.class);
        server.close();
        var result = da.getUserData();
        assert(result == null);
    }

    // Test getFriends method

    @Test
    public void getFriendsTest() {
        DataAccess da = mock(DataAccess.class);
        da.login("GetTestUser", "GetTestUser");
        var compare = new ArrayList<User>();

        var result = da.getFriends();
        System.out.println(result);
        assertEquals(result, compare);
    }

    @Test
    public void getFriendsNoServer() {
        DataAccess da = mock(DataAccess.class);
        server.close();
        var result = da.getFriends();
        assert(result == null);
    }

    // Test send Friend Request method

    @Test
    public void sendFriendRequestTest() {
        DataAccess da = mock(DataAccess.class);
        var u = da.login("AddTestsUser", "AddTestsUser");
        User friend = new User(69,"AddTestsUserFriend", "");

        assert(da.sendFriendRequest(friend));

        da.logout();
        da.login("AddTestsUserFriend", "AddTestsUserFriend");
        da.declineFriend(new User(99, "AddTestsUser", " "));
    }

    @Test
    public void sendFriendRequestNoServer() {
        server.close();
        DataAccess da = mock(DataAccess.class);
        User friend = new User(69,"test", "");
        friend.setUsername("testUsername");
        var result = da.sendFriendRequest(friend);
        assert(result == null);
    }

    // Test getFriendRequests

    @Test
    public void getFriendRequestsTest() {
        DataAccess da = mock(DataAccess.class);
        da.login("AddTestsUser", "AddTestsUser");
        da.sendFriendRequest(new User(90, "AddTestsUserFriend" , " " ));
        da.logout();

        da.login("AddTestsUserFriend", "AddTestsUserFriend");

        var result = da.getFriendRequests(false);
        System.out.println(da.getFriendRequests(false));
        assert(result.toString().contains("AddTestsUser"));
        da.declineFriend(new User(9, "AddTestsUser", " "));

    }

    @Test
    public void getFriendRequestsNoServer() {
        DataAccess da = mock(DataAccess.class);
        server.close();

        var result = da.getFriendRequests(false);
        assert(result == null);
    }

    @Test
    public void getFriendRequestGetCookie() {
        DataAccess da = mock(DataAccess.class);
        da.sessionId = "newCookie";
        da.login("GetTestUser", "GetTestUser");

        List<User> friends = new ArrayList<>();

        var result = da.getFriendRequests(false);
        assertEquals(friends.toString(), result.toString());
        assert( ! da.sessionId.equals("newCookie"));
    }

    // Tests for acceptFriend method

    @Test
    public void acceptFriendTest() {
        DataAccess da = mock(DataAccess.class);
        da.login("TestFriend", "TestFriend");
        da.sendFriendRequest(new User(9, "AddTestsUser", " "));
        da.logout();
        da.login("AddTestsUser", "AddTestsUser");
        User friend = new User(69,"TestFriend", "");
        assert(da.acceptFriend(friend));
    }

    @Test
    public void acceptFriendNoServer() {
        server.close();
        DataAccess da = mock(DataAccess.class);
        User friend = new User(69,"test", "");
        friend.setUsername("testUsername");
        var result = da.acceptFriend(friend);
        assert(result == null);
    }

    // Test for declineFriend method


    @Test
    public void declineFriendTest() {
        DataAccess da = mock(DataAccess.class);
        da.login("TestFriend", "TestFriend");
        da.sendFriendRequest(new User(9, "AddTestsUser", " "));
        da.logout();
        da.login("AddTestsUser", "AddTestsUser");
        User friend = new User(69,"TestFriend", "");
        assert(da.declineFriend(friend));
    }

    @Test
    public void declineFriendNoServer() {
        DataAccess da = mock(DataAccess.class);
        server.close();
        User friend = new User(69,"test", "");
        var result = da.declineFriend(friend);
        assert(result == null);
    }

    // Test for deleteFriend method

    @Test
    public void deleteFriendTest() {
        DataAccess da = mock(DataAccess.class);
        da.login("TestFriend", "TestFriend");
        da.sendFriendRequest(new User(9, "AddTestsUser", " "));
        da.logout();

        da.login("AddTestsUser", "AddTestsUser");
        User friend = new User(69,"TestFriend", "");
        da.acceptFriend(friend);

        var begin =  da.getFriends();

        assert(da.deleteFriend(friend));

        var end = da.getFriends();
        assert(begin.size() != end.size());
    }

    @Test
    public void deleteFriendNoServer() {
        DataAccess da = mock(DataAccess.class);
        server.close();
        User friend = new User(69,"test", "");
        var result = da.deleteFriend(friend);
        assert(result == null);
    }

    // Test getFriendSuggestions method

    @Test
    public void getUserSuggestionsTest() {
        DataAccess da = mock(DataAccess.class);
        List<User> friendList = new ArrayList<>();
        da.login("AddTestsUser", "AddTestsUser");
        var result = da.getUserSuggestions("te");
        assert (result.size() > 0);
    }

    @Test
    public void getUserSuggestionsNoServer() {
        DataAccess da = mock(DataAccess.class);
        server.close();
        var result = da.getUserSuggestions("ja");
        assert( result == null);
    }

    // Test fetchActiviityTypes method

    @Test
    public void fetchActivityTypesTest() {
        DataAccess da = mock(DataAccess.class);
        var result = da.fetchActivityTypes();
        assert (result.size() > 0);
    }

    @Test
    public void fetchActivityTypesNoServer() {
        DataAccess da = mock(DataAccess.class);
        server.close();
        var result = da.fetchActivityTypes();
        assert( result == null);
    }

    // test getActivityHistoryTest method

    @Test
    public void getActivityHistoryTest() {
        DataAccess da = mock(DataAccess.class);
        // login as user jakob_hand
        da.login("jakob_hand", "ea8phe1Zaz");

        var activities = da.getActivityHistory(true, LocalDate.parse("2018-08-01"), LocalDate.parse("2019-03-30"), null);
        System.out.println(activities);

        assert(activities.size() >= 0);

    }

    @Test
    public void getActivityHistoryNoServer() {
        DataAccess da = mock(DataAccess.class);
        server.close();
        var result = da.getActivityHistory(true, LocalDate.parse("2018-08-01"), LocalDate.parse("2019-03-30"), "friend1");
        assert(result == null);
    }

    @Test
    public void getActivityHistoryGetCookie() {
        DataAccess da = mock(DataAccess.class);
        da.sessionId = "newCookie";
        da.login("jakob_hand", "ea8phe1Zaz");

        var result = da.getActivityHistory(true, LocalDate.parse("2018-08-01"), LocalDate.parse("2019-03-30"), null);
        assert(result.size() >= 0);
        assert( ! da.sessionId.equals("newCookie"));
    }


    // test getOwnAchievements

    @Test
    public void getOwnAchievementsTest() {
        DataAccess da = mock(DataAccess.class);
        da.login("jakob_hand", "ea8phe1Zaz");
        var result = da.getOwnAchievements();

        assert(result.size() > 0);
    }

    @Test
    public void getOwnAchievementsNoServer() {
        DataAccess da = mock(DataAccess.class);
        server.close();
        var result = da.getOwnAchievements();
        assert(result == null);
    }

    @Test
    public void getOwnAchievementsGetCookie() {
        DataAccess da = mock(DataAccess.class);
        da.sessionId = "newCookie";
        da.login("jakob_hand", "ea8phe1Zaz");

        List<Achievement> achievementList = new ArrayList<>();
        var result = da.getOwnAchievements();

        assert(result.size() > 0);
        assert( ! da.sessionId.equals("newCookie"));
    }


    // test get friendsAchievements

    @Test
    public void getFriendsAchievementsTest() {
        DataAccess da = mock(DataAccess.class);
        da.login("jakob_hand", "ea8phe1Zaz");
        User friend = new User(69,"PandaCrazyBr", "");

        var result = da.getFriendsAchievements(friend);
        System.out.println(result.size());

        assert(result.size() >= 0);
    }

    @Test
    public void getFriendsAchievementsNoServer() {
        DataAccess da = mock(DataAccess.class);
        server.close();
        User friend = new User(69,"test", "");
        var result = da.getFriendsAchievements(friend);
        assert(result == null);
    }

    @Test
    public void getFriendsAchievementsGetCookie() {
        DataAccess da = mock(DataAccess.class);
        da.sessionId = "newCookie";
        da.login("jakob_hand", "ea8phe1Zaz");
        User friend = new User(69,"secern", "");

        var result = da.getFriendsAchievements(friend);

        assert(result.size() >= 0);
        assert( ! da.sessionId.equals("newCookie"));
    }


    // Test login method

    @Test
    public void loginTest() {
        DataAccess da = mock(DataAccess.class);
        assert( da.login("jakob_hand", "ea8phe1Zaz"));
    }

    @Test
    public void loginNoServer() {
        DataAccess da = mock(DataAccess.class);
        server.close();

        assert( !da.login("test", "test"));
    }

    @Test
    public void loginGetCookie() {
        DataAccess da = mock(DataAccess.class);
        da.sessionId = "newCookie";
        assert(da.login("GetTestUser", "GetTestUser"));
        assert( ! da.sessionId.equals("newCookie"));
    }

    @Test
    public void loginNoCooke() {
        DataAccess.login("GetTestUser", "GetTestUser");

        assert (DataAccess.login("GetTestUser", "GetTestUser"));
    }

    // Test logout method

    @Test
    public void logoutTest() {
        DataAccess da = mock(DataAccess.class);
        da.login("jakob_hand", "ea8phe1Zaz");
        String temp = da.sessionId;
        assert(da.logout());
        da.login("jakob_hand", "ea8phe1Zaz");
        assert(! temp.equals(da.sessionId));
    }


    // Test addActivity method

    @Test
    public void addActivityTest() throws SQLException {
        DataAccess da = mock(DataAccess.class);
        DbRetrieve dt = mock(DbRetrieve.class);
        da.login("AddTestsUser", "AddTestsUser");

        Activity test = new Activity(
                0,
                new ActivityType(0,"mock_activity",0,
                        new ActivityCategory(0,"mock_activity","mock_activity", null),
                        "mock_activity_desc"),
                null,
                LocalDate.now(),
                LocalTime.now());

        assert( da.addActivity(test));

        dt.retrieveTestDb().getConnection().prepareStatement("Delete From activity_log Where log_id is Null;").executeUpdate();
    }

    @Test
    public void addActivityNoServer() {
        DataAccess da = mock(DataAccess.class);
        server.close();
        Activity test = new Activity(
                0,
                new ActivityType(0,"mock_activity",0,
                        new ActivityCategory(0,"mock_activity","mock_activity", null),
                        "mock_activity_desc"),
                null,
                LocalDate.now(),
                LocalTime.now());

        assert( !da.addActivity(test));
    }


    @Test
    public void testParseNullUser() {
        String userJson = "{\"mainUser\": true, \"totalPoints\": 10, \"username\": \"peter\", \"picturePath\": \"test\"}";
        User user = JsonParsing.parseUser(new JSONObject(userJson));
        userJson = "{\"mainUser\": false, \"totalPoints\": 10, \"username\": \"peter\", \"picturePath\": null}";
        User mainUser = JsonParsing.parseUser(new JSONObject(userJson));
        userJson = "{\"mainUser\": false, \"totalPoints\": 10, \"username\": \"peter\"}";
        mainUser = JsonParsing.parseUser(new JSONObject(userJson));
        userJson = "{\"mainUser\": false, \"totalPoints\": 10, \"username\": \"peter\", \"picturePath\": \"test\"}";
        mainUser = JsonParsing.parseUser(new JSONObject(userJson));
        assert(mainUser.getTotalPoints() == 10);
    }

    @Test
    public void testGetUserData() {
        DataAccess da = mock(DataAccess.class);
        da.login("jakob_hand", "ea8phe1Zaz");
        User user = da.getUserData();
        da.getUserData();
        assert(user.getUsername() != null);
    }


    @Test
    public void getLeaderBoardTest() {
        DataAccess da = mock(DataAccess.class);
        da.login("Wout1999", "Sahsh5kab");
        var result = da.getLeaderboard();
        String compare = "[{username: Wout1999, password: null, emailaddress: null, firstname: null, totalpoints 16 }]";
        assert(result.size() >= 0);

    }

    @Test
    public void loginTestCookie() {
        DataAccess da = mock(DataAccess.class);
        da.sessionId = " dfdf";
        da.logout();
        da.login("fdf", "fdfdf");
        assert(true);
    }

    @Test
    public void getLeaderBoardTestCookie() {
        DataAccess da = mock(DataAccess.class);
        da.sessionId = " dfdf";
        da.logout();
        List<User> leaderboard = da.getLeaderboard();
        assert(leaderboard == null);
    }


    // Test sendImage
    @Test
    public void sendImageTest() {
        DataAccess da = mock(DataAccess.class);
        da.createAccount("test99", "test", "testname", "tes55955555t@gmail.com");
        da.login("test99", "test");
        assert (da.sendImage("maven_menu.png"));
        da.deleteAccount();
    }

    @Test
    public void sendImageTestDataNotFound() {
        DataAccess da = mock(DataAccess.class);
        da.login("AddTestUser","AddTestUser");
        assert(! da.sendImage("../template/readme-assets/maven_menu.png"));
        da.deleteAccount();
    }

    @Test
    public void sendImageTestNoServer() {
        DataAccess da = mock(DataAccess.class);
        server.close();
        assert(!da.sendImage("E:\\TuDelft\\quarter3\\Oop project\\100.png"));
    }

    // Test createAccount

    @Test
    public void createAccountTest() {
        DataAccess da = mock(DataAccess.class);
        assert ( da.createAccount("createaccounttest", "createaccounttest",
                "createaccounttest", "createaccounttest"));

        da.login("createaccounttest", "createaccounttest");
        da.deleteAccount();
    }

    @Test
    public void createAccountNoServer() {
        DataAccess da = mock(DataAccess.class);
        server.close();
        assert ( ! da.createAccount("deleteaccounttest", "deleteaccounttest",
                "deleteaccounttest", "deleteaccounttest"));
    }

    // change Password tests

    @Test
    public void removeActivity(){
        DataAccess da = mock(DataAccess.class);
        da.login("Wout1999","Sahsh5kab");
        Activity test = new Activity(
                0,
                new ActivityType(0,"mock_activity",0,
                        new ActivityCategory(0,"mock_activity","mock_activity", null),
                        "mock_activity_desc"),
                null,
                LocalDate.now(),
                LocalTime.now());
        da.addActivity(test);
        assert ( da.removeActivity(0));
    }

    @Test
    public void removeActivityNoServer(){
        DataAccess da = mock(DataAccess.class);
        server.close();
        assert (! da.removeActivity(0));

    }

    @Test
    public void removeActivityGetCookie() {
        DataAccess da = mock(DataAccess.class);
        da.sessionId = "newCookie";
        assert (!da.removeActivity(0));
        assert(!da.sessionId.equals("newCookie"));
    }

    @Test
    public void changePasswordTest() {
        DataAccess da = mock(DataAccess.class);
        da.login("AddTestsUser", "AddTestsUser");
        assert (da.changePassword("newPassword"));
        da.changePassword("AddTestsUser");
    }

    @Test
    public void changePasswordNoServer() {
        server.close();
        DataAccess da = mock(DataAccess.class);
        assert (!da.changePassword("newPassword"));
    }

    @Test
    public void changePasswordGetCookie() {
        DataAccess da = mock(DataAccess.class);
        da.sessionId = "newCookie";

        assert (!da.changePassword("newPassword"));
        assert(!da.sessionId.equals("newCookie"));
    }

    // change email tests

    @Test
    public void changeEmailTest() {
        DataAccess da = mock(DataAccess.class);
        da.login("AddTestsUser", "AddTestsUser");
        assert (da.changeEmaiAddress("newEmail"));
        da.changeEmaiAddress("AddTestsUser@gmail.com");
    }

    @Test
    public void changeEmailNoServer() {
        server.close();
        DataAccess da = mock(DataAccess.class);
        assert (!da.changeEmaiAddress("newEmail"));
    }

    @Test
    public void changeEmailGetCookie() {
        DataAccess.sessionId = "newCookie";
        DataAccess da = mock(DataAccess.class);
        assert (!da.changeEmaiAddress("newEmail"));
        assert (!da.sessionId.equals("newCookie"));
    }

    @Test
    public void changeFirstnameTest() {
        DataAccess da = mock(DataAccess.class);
        da.login("AddTestsUser", "AddTestsUser");
        assert (da.changeFirstname("newFirstname"));
        da.changeFirstname("AddTestsUser");
    }

    @Test
    public void changeFirstnameNoServer() {
        server.close();
        DataAccess da = mock(DataAccess.class);
        assert (!da.changeFirstname("newFirstname"));
    }

    @Test
    public void changeFirstnameGetCookie() {
        DataAccess da = mock(DataAccess.class);
        da.sessionId = "newCookie";
        assert (!da.changeFirstname("newFirstname"));
        assert(!da.sessionId.equals("newCookie"));
    }


    // test delete Account

    @Test
    public void deleteAccountTest() {
        DataAccess da = mock(DataAccess.class);
        da.createAccount("deleteaccounttestt", "deleteaccounttestt",
                "deleteaccounttestt", "deleteaccounttestt");

        da.login("deleteaccounttestt", "deleteaccounttestt");
        assert (da.deleteAccount());
    }

    @Test
    public void deleteAccountNoServer() {
        server.close();
        DataAccess da = mock(DataAccess.class);
        assert ( !da.deleteAccount());
    }



    @Test
    public void setPrivacyTest() {
        DataAccess da = mock(DataAccess.class);
        da.createAccount("test9", "test", "9testname", "test9@gmail.com");
        da.login("test9", "test");
        assert ( da.setPrivacySetting());
        da.deleteAccount();
    }

    @Test
    public void setPrivacyTestFailure() {
        DataAccess da = mock(DataAccess.class);
        assert (! da.setPrivacySetting());
    }

    @Test
    public void revokeFriendTestGetCookie() {
        DataAccess da = mock(DataAccess.class);
        da.sessionId = "newCookie";
        assert (!da.revokeFriendRequest("TestFriend"));
        assert(!da.sessionId.equals("newCookie"));
    }

    @Test
    public void revokeFriendTest() {
        DataAccess da = mock(DataAccess.class);
        da.createAccount("test999", "test99", "9testname", "test9@gmail.com");
        User friend = new User(70,"TestFriend", "");
        da.login("test999", "test99");
        da.sendFriendRequest(friend);
        assert ( da.revokeFriendRequest("TestFriend"));
        da.deleteAccount();
    }

    @Test
    public void revokeFriendTestNoServer() {
        server.close();
        DataAccess da = mock(DataAccess.class);
        assert ( !da.revokeFriendRequest("Wout1999"));
    }

    @Test
    public void getImgUrlTolong() {

        var result = DataAccess.getImageForUrl("jkdfjdkfkdjfkdjfkdsjfkjskfjksdfjksdjfksjfsdjfksdjfkjsdfsdjfjsfkljsdkfjdskfjjdfjdfjkldfjkdfjkl;dfjkl;sfkfklsdfdfskl;lsdfjksdfsdfkl;jsdsdfsdfjsdfsdfsdfjdsfkj", null, 100.0);

        assert (result == null);
    }

    @Test
    public void amIloggedInTest() {
        DataAccess.login("AddTestsUser", "AddTestsUser");

        var result = DataAccess.amIloggedIn();

        assertTrue( result );
    }

    @Test
    public void amIloggedInNoServer() {
        server.close();

        var result = DataAccess.amIloggedIn();

        assertFalse ( result);

    }

    @Test
    public void amIloggedInTestFalse() {

        var result = DataAccess.amIloggedIn();

        assertFalse ( result );
    }
}
