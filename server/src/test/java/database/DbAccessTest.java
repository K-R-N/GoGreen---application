package database;

import models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DbAccessTest {
    private DbAccess db;
    private PGSimpleDataSource pg;
    private DbAccess dbf;

    @BeforeEach
    public void setUp() {
       db = new DbAccess(DbRetrieve.retrieveTestDb());
       pg = new PGSimpleDataSource();
       dbf = new DbAccess(pg);
    }

    @Test
    public void testGetDataSource() {
        DbAccess dbx = new DbAccess(null);
        PGSimpleDataSource test = new PGSimpleDataSource();
        dbx.setDataSource(test);
        assertEquals(test, dbx.getDataSource());
    }

    @Test
    public void testSetDataSource() {
        DbAccess dbx = new DbAccess(null);
        PGSimpleDataSource test = new PGSimpleDataSource();
        dbx.setDataSource(test);
        assertEquals(test, dbx.getDataSource());
    }

    @Test
    public void testGetUser(){
        assertEquals("TylerCCuevas@dayrep.com ", db.getUser("Encted", true).getEmailadress());
        assertNull(db.getUser("Encted", false).getEmailadress());
        assertNull(dbf.getUser("smkfmesk", true));
        assertNull(db.getUser(null, true));
    }

    @Test
    public void testRetrieveTotalPoints() {
        assertEquals(30,  db.retrieveTotalPoints("Encted"));
        assertEquals(0, dbf.retrieveTotalPoints("anything"));
    }

    @Test
    public void testGetPasswordHash() {
        assertEquals("ooZ9aePhae", db.getPasswordHash("Encted"));
        assertNull(dbf.getPasswordHash("anything"));
    }

    @Test
    public void testGetFriends() {
        assertEquals("dbunit_user", db.getFriends("test").get(0).getUsername());
        assertNull(dbf.getFriends("anything"));
        assertNull(db.getFriends(null));
    }
    @Test
    public void GetHashPasswordFalse(){
        String username = null;
        assertEquals(null,db.getPasswordHash(username));
    }

    @Test
    public void testGetRankingList() {
        assertNull(db.getRankingList(null));
        assertEquals("dbunit_user", db.getRankingList("test").get(1).getUsername());
        assertEquals(true, db.getRankingList("test").get(0).isMainUser());
        assertEquals("test", db.getRankingList("test").get(0).getUsername());
        assertNull(dbf.getRankingList("\uD83D\uDE02"));
    }

    @Test
    public void testGetContribution() {
        User test = new User("test", null);
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern( "yyyy-MM-dd");
        String timeColonPattern = "HH:mm:ss";
        DateTimeFormatter timeColonFormatter = DateTimeFormatter.ofPattern(timeColonPattern);
        LocalDate date = LocalDate.parse("2018-01-17", formatterDate);
        LocalTime time = LocalTime.parse("15:30:00", timeColonFormatter);
        Achievement achievement = new Achievement(25, "5_veg_name", "desc", null, null, test, date, time);
        assertEquals("test", db.getContribution("test", "2015-01-01", "2020-01-01",true, true).get(0).getUser().getUsername());
        assertEquals("test", db.getContribution("test", "2015-01-01", "2020-01-01",false, true).get(0).getUser().getUsername());
        Achievement a1 = (Achievement) db.getContribution("test", "2015-01-01", "2020-01-01",false, false).get(1);
        Achievement a2 = (Achievement) db.getContribution("test", "2015-01-01", "2020-01-01",true, true).get(1);
        assertEquals(achievement.getTitle(), a1.getTitle());
        assertEquals(achievement.getTitle(), a2.getTitle());
        Activity b1 = (Activity) db.getContribution("test", "2015-01-01", "2020-01-01",false, true).get(0);
        Activity b2 = (Activity) db.getContribution("test", "2015-01-01", "2020-01-01",true, true).get(0);
        assertEquals("transport", b1.getType().getCategory().getName());
        assertEquals("transport", b2.getType().getCategory().getName());
        assertNull(dbf.getContribution("test", "2015-01-01", "2020-01-01",true, true));
        assertNull(db.getContribution("","","",true, false));
    }

    @Test
    public void testInsertUserActivity() throws SQLException {
        int before1 = db.countRows("activity_log");
        db.insertUserActivity("dbunit_user", 1);
        int after1 = db.countRows("activity_log");
        assertEquals(before1, after1-1);
        int before2 = db.countRows("activity_log");
        db.insertUserActivity("dbunit_user", 3);
        int after2 = db.countRows("activity_log");
        assertEquals(before2, after2-1);
        int before3 = db.countRows("activity_log");
        db.insertUserActivity("dbunit_user", 20);
        int after3 = db.countRows("activity_log");
        assertEquals(before3, after3-1);
        int before4 = db.countRows("activity_log");
        db.insertUserActivity("dbunit_user", 12);
        int after4 = db.countRows("activity_log");
        assertEquals(before4, after4-1);
        int before5 = db.countRows("activity_log");
        db.insertUserActivity("dbunit_user", 23);
        int after5 = db.countRows("activity_log");
        assertEquals(before5, after5-1);
        int beforef = dbf.countRows("activity_log");
        dbf.insertUserActivity("darius", 1);
        int afterf = dbf.countRows("activity_log");
        DbRetrieve.retrieveTestDb().getConnection().prepareStatement("Delete From activity_log Where log_id is Null;").executeUpdate();
        assertEquals(beforef, afterf);
    }

    @Test
    public void testCountRows() {
        int expected = 22;
        assertEquals(expected, db.countRows("activity"));
        assertEquals(0, dbf.countRows("anything"));
    }

    @Test
    public void testChangePassword() {
        db.changePassword("test","test$123");
        assertEquals("test$123" ,db.getPasswordHash("test"));
        //change the password back to the original one.
        db.changePassword("test", "test1234");
        assertFalse(dbf.changePassword("",""));
    }

    @Test
    public void testChangeFirstName() {
        db.changeFirstName("test","Mockito");
        assertEquals("Mockito" ,db.getUser("test", true).getFirstname());
        //change the first name back to the original
        db.changeFirstName("test", "Test");
        assertFalse(dbf.changeFirstName("",""));
    }

    @Test
    public void testChangeEmail() {
        db.changeEmail("test","test@mockito.com");
        assertEquals("test@mockito.com" ,db.getUser("test", true).getEmailadress());
        //change the email back to the original
        db.changeEmail("test", "test@test.com");
        assertFalse(dbf.changeEmail("",""));
    }

    @Test
    public void testDeleteUser() throws SQLException {
        //count the rows before the deleteUser method is applied.
        int before = db.countRows("user_details");
        //delete the test user
        db.deleteUser("test");
        //count the rows after the deletion.
        int after = db.countRows("user_details");
        //reinsert the test user.
        DbRetrieve.retrieveTestDb().getConnection().prepareStatement("INSERT INTO user_details (username, "
                + "password, first_name, email_address, profile_picture) "
                + "VALUES ('test', 'test1234', 'Test', 'test@test.com', NULL);").executeUpdate();
        assertEquals(before, after+1);
        assertFalse(dbf.deleteUser(""));
    }

    @Test
    public void testCreateUser() {
        //count the rows before the createUser methods is applied
        int before = db.countRows("user_details");
        //insert the test user.
        db.createUser("test2", "hahaha", "looool", "lol@ed.com");
        //count the rows after the insertion.
        int after = db.countRows("user_details");
        //delete the test user.
        db.deleteUser("test2");
        assertEquals(before+1, after);
        assertFalse(dbf.createUser("","","",""));
    }

    @Test
    public void testSendRequest() throws SQLException {
        int before = db.countRows("friend_request");
        db.sendRequest("test", "darius");
        int after = db.countRows("friend_request");
        DbRetrieve.retrieveTestDb().getConnection().prepareStatement("Delete From friend_request Where from_username = 'test'").executeUpdate();
        assertEquals(before, after-1);
        assertFalse(dbf.sendRequest("",""));
    }

    @Test
    public void testDeleteFriend() throws SQLException {
        DbRetrieve.retrieveTestDb().getConnection().prepareStatement("Insert Into friends_with(username1, username2) Values('test', 'darius')").executeUpdate();
        int before = db.countRows("friends_with");
        db.deleteFriend("darius", "test");
        int after = db.countRows("friends_with");
        assertEquals(before, after+1);
        assertFalse(db.sendRequest("darius", "jakob_hand"));
        assertFalse(dbf.deleteFriend("",""));
    }

    @Test
    public void testRetrieveAchievements(){
        List<Achievement> expected = new ArrayList<>();
        Achievement ach1 = new Achievement(25, "5_veg_meal", null, null, true);
        Achievement ach2 = new Achievement(26, "10_veg_meal", null, null, false);
        expected.add(ach1);
        expected.add(ach2);
        List<Achievement> actual = db.retrieveAchievements("jakob_hand");
        assertEquals(expected.get(0).isAcquired(), actual.get(0).isAcquired());
        assertNull(dbf.retrieveAchievements(""));
    }

    @Test
    public void testRetrieveSuggestions() {
        List<String> suggestions = db.retrieveSuggestions("te");
        assertTrue(suggestions.contains("TestFriend"));
        assertNull(dbf.retrieveSuggestions(""));
    }

    @Test
    public void testRetrieveRequests() throws SQLException {
        DbRetrieve.retrieveTestDb().getConnection().prepareStatement("INSERT INTO friend_request(request_id, from_username, to_username) "
                + "VALUES (42, 'fr1', 'fr2');").executeUpdate();
        List<FriendRequest> fr = db.retrieveRequests("fr2");
        DbRetrieve.retrieveTestDb().getConnection().prepareStatement("DELETE FROM friend_request Where from_username = 'fr1'").executeUpdate();
        assertEquals("fr1", fr.get(0).getFromUser());
        assertNull(dbf.retrieveRequests("anything"));
    }

    @Test
    public void testAcceptRequest() throws SQLException {
        //create a new friend request with a known id.
        DbRetrieve.retrieveTestDb().getConnection().prepareStatement("INSERT INTO friend_request(request_id, from_username, to_username) "
                + "VALUES (42, 'test', 'Durnity');").executeUpdate();
        //count the number of rows.
        int before = db.countRows("friends_with");
        //accept the friend request
        db.acceptRequest("Durnity", 42);
        //count the number the number of rows after the request was accepted.
        int after = db.countRows("friends_with");
        assertEquals(before, after - 1);
        //undo the changes by removing the newly added friends.
        DbRetrieve.retrieveTestDb().getConnection().prepareStatement("DELETE FROM friends_with Where username1 = 'test'" +
                " And username2 = 'Durnity'").executeUpdate();
        assertFalse(dbf.acceptRequest("",0));

    }

    @Test
    public void testRejectRequest() throws SQLException {
        //create a new friend request with a known id.
        DbRetrieve.retrieveTestDb().getConnection().prepareStatement("INSERT INTO friend_request(request_id, from_username, to_username) "
                + "VALUES (42, 'test', 'Durnity');").executeUpdate();
        //count the number rows before.
        int before = db.countRows("friend_request");
        //reject the friend request.
        db.rejectRequest("Durnity", 42);
        //count the number of rows after.
        int after = db.countRows("friend_request");
        assertEquals(before, after+1);
        assertFalse(dbf.rejectRequest("",0));
    }

    @Test
    public void testRetrieveOwnRequests() throws SQLException {
        DbRetrieve.retrieveTestDb().getConnection().prepareStatement("INSERT INTO friend_request(request_id, from_username, to_username) "
                + "VALUES (42, 'fr1', 'fr2');").executeUpdate();
        List<FriendRequest> fr = db.retrieveOwnRequests("fr1");
        DbRetrieve.retrieveTestDb().getConnection().prepareStatement("DELETE FROM friend_request Where from_username = 'fr1'").executeUpdate();
        assertEquals("fr2", fr.get(0).getFromUser());
        assertNull(dbf.retrieveRequests("anything"));
    }

    @Test
    public void testRevokeRequest() throws SQLException {
        DbRetrieve.retrieveTestDb().getConnection().prepareStatement("INSERT INTO friend_request(request_id, from_username, to_username) "
                + "VALUES (42, 'test', 'Durnity');").executeUpdate();
        int before = db.countRows("friend_request");
        db.revokeRequest("test", 42);
        int after = db.countRows("friend_request");
        assertEquals(before, after + 1);
        assertFalse(dbf.rejectRequest("", 0));
    }

    @Test
    public void testRetrieveActivityTypes() {
        ActivityCategory expectedCategory = new ActivityCategory(1,"food", "A category for food related activities.", null);
        ActivityType expectedType = new ActivityType(1, "vegeterian_meal", 6, expectedCategory, "I ate a vegetarian meal today.");
        assertEquals(db.retrieveActivityTypes("").get(0).getName(), expectedType.getName());
        assertNull(dbf.retrieveActivityTypes(""));
    }

    @Test
    public void testDeleteActivity() throws SQLException {
        DbRetrieve.retrieveTestDb().getConnection().prepareStatement("INSERT INTO activity_log (log_id, username, activity_id, date, time)"
                + "VALUES (42, 'test', 1, '2019-01-21', '12:00:00');").executeUpdate();
        DbRetrieve.retrieveTestDb().getConnection().prepareStatement("INSERT INTO activity_log (log_id, username, activity_id, date, time)"
                + "VALUES (43, 'test', 5, '2019-01-21', '12:00:00');").executeUpdate();
        DbRetrieve.retrieveTestDb().getConnection().prepareStatement("INSERT INTO activity_log (log_id, username, activity_id, date, time)"
                + "VALUES (44, 'test', 20, '2019-01-21', '12:00:00');").executeUpdate();
        DbRetrieve.retrieveTestDb().getConnection().prepareStatement("INSERT INTO activity_log (log_id, username, activity_id, date, time)"
                + "VALUES (45, 'test', 15, '2019-01-21', '12:00:00');").executeUpdate();
        DbRetrieve.retrieveTestDb().getConnection().prepareStatement("INSERT INTO activity_log (log_id, username, activity_id, date, time)"
                + "VALUES (46, 'test', 23, '2019-01-21', '12:00:00');").executeUpdate();
        int before1 = db.countRows("activity_log");
        db.deleteActivity("test", 42);
        int after1 = db.countRows("activity_log");
        assertEquals(before1, after1+1);
        int before2 = db.countRows("activity_log");
        db.deleteActivity("test", 43);
        int after2 = db.countRows("activity_log");
        assertEquals(before2, after2+1);
        int before3 = db.countRows("activity_log");
        db.deleteActivity("test", 44);
        int after3 = db.countRows("activity_log");
        assertEquals(before3, after3+1);
        int before4 = db.countRows("activity_log");
        db.deleteActivity("test", 45);
        int after4 = db.countRows("activity_log");
        assertEquals(before4, after4+1);
        int before5 = db.countRows("activity_log");
        db.deleteActivity("test", 46);
        int after5 = db.countRows("activity_log");
        assertEquals(before5, after5+1);
        int before0 = db.countRows("activity_log");
        db.deleteActivity("test", 7023);
        int after0 = db.countRows("activity_log");
        assertEquals(before0, after0);
        assertFalse(dbf.deleteActivity("",0));
    }

    @Test
    public void testSetProfilePicture() throws IOException {
        File imgfile = new File("youtubelogo.png");
        FileInputStream fin = new FileInputStream(imgfile);
        byte[] image = fin.readAllBytes();
        db.setProfilePicture("dbunit_user", image);
        assertArrayEquals(image, db.getProfilePicture("dbunit_user"));
        db.setProfilePicture("dbunit_user", null);
        assertFalse(dbf.setProfilePicture("", null));
    }

    @Test
    public void testGetProfilePicture() throws IOException {
        File imgfile = new File("youtubelogo.png");
        FileInputStream fin = new FileInputStream(imgfile);
        assertArrayEquals(fin.readAllBytes(),db.getProfilePicture("Encted"));
        assertNull(dbf.getProfilePicture(""));
    }

    @Test
    public void testGetBadgeLarge() throws IOException {
        File imgfile = new File("youtubelogo.png");
        FileInputStream fin = new FileInputStream(imgfile);
        assertArrayEquals(fin.readAllBytes(), db.getBadgeLarge(26));
        assertNull(dbf.getBadgeLarge(0));
    }

    @Test
    public void testGetBadgeSmall() throws IOException {
        File imgfile = new File("youtubelogo.png");
        FileInputStream fin = new FileInputStream(imgfile);
        assertArrayEquals(fin.readAllBytes(), db.getBadgeSmall(26));
        assertNull(dbf.getBadgeSmall(0));
    }

    @Test
    public void testChangePrivacySettings() throws SQLException {
        db.changePrivacySettings("test", "private");
        Connection con = db.getDataSource().getConnection();
        ResultSet result = con.prepareStatement("SELECT privacy_settings FROM user_details WHERE username = 'test';").executeQuery();
        result.next();
        String actual = result.getString(1);
        con.close();
        db.changePrivacySettings("test", "public");
        assertEquals("private", actual);
        assertFalse(dbf.changePrivacySettings("",""));
    }

    @Test
    public void testGetCategoryImage() throws IOException {
        File imgfile = new File("youtubelogo.png");
        FileInputStream fin = new FileInputStream(imgfile);
        byte[] expected = fin.readAllBytes();
        byte [] actual = db.getCategoryImage(42);
        assertArrayEquals(expected, actual);
        assertNull(dbf.getCategoryImage(0));
    }
    @Test
    public void TestGetCategoryImage() throws IOException{
        int exception = 2134412312;
        assertEquals(null,dbf.getCategoryImage(exception));
    }
    @Test
    public void TestGetOwnRequestsFalse(){
        assertEquals(null,dbf.retrieveOwnRequests(null));
    }
    @Test
    public void RevokeRequest(){
        assertFalse(dbf.revokeRequest(null,411423428));
    }
}