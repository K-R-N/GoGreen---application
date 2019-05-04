package controller;

import models.User;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JsonParsingTest {

    @Test
    public void testAttrExist() {
        String mockJsonStr = "{ 'attr1': 'val1' }";
        JSONObject mockJson = new JSONObject(mockJsonStr);

        boolean exists = JsonParsing.attrExists("attr1", mockJson);

        assertTrue(exists);
    }

    @Test
    public void testAttrExistEmpty() {
        String mockJsonStr = "{ 'attr1': '' }";
        JSONObject mockJson = new JSONObject(mockJsonStr);

        boolean exists = JsonParsing.attrExists("attr1", mockJson);

        assertTrue(exists);
    }




    @Test
    public void testParseContribuion() {

        String mockJsonStr = "{'date': '2019-03-27', 'time': '02:18:51', " +
                "'user': {'mainUser': 'false', 'totalPoints': '99', 'username': 'testusername', 'picturePath': " +
                "'/randowPath', 'emailadress': 'test@email.nl', 'firstname': 'testfirstname'}}";
        JSONObject mockJson = new JSONObject(mockJsonStr);

        var contribution = JsonParsing.parseContribution(mockJson);

        assertEquals (contribution.getDate().toString(), "2019-03-27" );
        assertEquals(contribution.getTime().toString(), "02:18:51");


    }

    @Test
    public void testParseContributionEmpty() {

        String mockJsonStr = "{'acquired': 'false'}";
        JSONObject mockJson = new JSONObject(mockJsonStr);

        var achievement = JsonParsing.parseAchievement(mockJson);

        assert (achievement.getAchievementId() == -1 );

    }




    @Test
    public void testParseAchievementEmpty() {

        String mockJsonStr = "{'acquired': 'false'}";
        JSONObject mockJson = new JSONObject(mockJsonStr);

        var achievement = JsonParsing.parseAchievement(mockJson);

        assert (achievement.getAchievementId() == -1 );

    }

    @Test
    public void testParseAchievement() {

        String mockJsonStr = "{'achievementId':'10','title':'testTitle','desc':'description'," +
                "'smallBadgePath':'/somepath','bigBadgePath':'/someOtherPart', 'acquired': 'false'}";

        JSONObject mockJson = new JSONObject(mockJsonStr);

        var achievement = JsonParsing.parseAchievement(mockJson);

        assert (achievement.getAchievementId() ==  10);
        assert (achievement.getTitle().equals("testTitle"));
        assert (achievement.getDesc().equals("description"));
        assert (achievement.getSmallBadgePath().equals("/somepath"));
        assert (achievement.getBigBadgePath().equals("/someOtherPart"));

    }

    @Test
    public void testParseActivity() {

        String mockJsonStr = "{'id': '5', 'type': {'id': '8', 'name': 'testname', 'description': 'test description', 'points': '69', 'category': { 'id': '9', 'name': 'test name', 'desc': 'test description', 'imagePath': '/testPath'}} }";

        JSONObject mockJson = new JSONObject(mockJsonStr);

        var activity = JsonParsing.parseActivity(mockJson);

        assert (activity.getId() == 5);
    }

    @Test
    public void testParseActivityEmpty() {
        String mockJsonStr = "{'nothing': ''}";

        JSONObject mockJson = new JSONObject(mockJsonStr);

        var activity = JsonParsing.parseActivity(mockJson);

        assert (activity.getId() == -1);
    }



    @Test
    public void  testParseActivityType() {

        String mockJsonStr = "{'id': '8', 'name': 'testname', 'description': 'test description', 'points': '69', 'category': { 'id': '9', 'name': 'test name', 'desc': 'test description', 'imagePath': '/testPath'}}";

        JSONObject mockJson = new JSONObject(mockJsonStr);

        var activityType = JsonParsing.parseActivityType(mockJson);

        assert (activityType.getId() == 8);
        assertEquals (activityType.getName(), "testname");
        assertEquals(activityType.getDescription(), "test description");
        assert (activityType.getPoints() == 69);
    }

    @Test
    public void testParseActivityTypeEmpty() {

        String mockJsonStr = "{'empty': ''}";

        JSONObject mockJson = new JSONObject(mockJsonStr);

        var activityType = JsonParsing.parseActivityType(mockJson);

        assert (activityType.getId() == -1);

    }


    @Test
    public void testParseUser() {

        String mockJsonStr = "{'mainUser': 'false', 'totalPoints': '99', 'username': 'testusername', " +
                "'picturePath': '/randowPath', 'emailadress': 'test@email.nl', 'firstname': 'testfirstname', 'privacy': 'public'}";

        JSONObject mockJson = new JSONObject(mockJsonStr);

        var user = JsonParsing.parseUser(mockJson);

        assert (!user.isMainUser());
        assert (user.getTotalPoints() == 99);
        assertEquals(user.getUsername(), "testusername");
        assertEquals(user.getPicturePath(), "/randowPath");
    }

    @Test
    public void testParseUserEmpty() {

        String mockJsonStr = "{'mainUser': 'false', 'username': 'test' }";

        JSONObject mockJson = new JSONObject(mockJsonStr);

        var user = JsonParsing.parseUser(mockJson);

        assert ( !user.isMainUser());
        assert (user.getTotalPoints() == -1);
    }

    @Test
    public void testParseUserCached() {
        DataAccess.cachedFriends.put("test username", new User(99, "test username", "/randowPath", "test@email.nl", "testfirstname") );

        String mockJsonStr = "{'mainUser': 'false', 'totalPoints': '99', 'username': 'test username', " +
                "'picturePath': '/randowPath', 'emailadress': 'test@email.nl', 'firstName':'testfirstname'}";


        JSONObject mockJson = new JSONObject(mockJsonStr);

        var user = JsonParsing.parseUser(mockJson);

        assert ( user.isMainUser());

    }

    @Test
    public void testParseUserCachedNullPoints() {
        DataAccess.cachedFriends.put("test username", new User(0, "test username", "/randowPath", "test@email.nl", "testfirstname") );

        String mockJsonStr = "{'mainUser': 'false', 'totalPoints': '0', 'username': 'test username', " +
                "'picturePath': '/randowPath', 'emailadress': 'test@email.nl', 'firstName':'testfirstname'}";


        JSONObject mockJson = new JSONObject(mockJsonStr);

        var user = JsonParsing.parseUser(mockJson);

        assert (! user.isMainUser());

    }




    @Test
    public void parseActivityCategory() {

        String mockJsonStr = "{ 'id': '9', 'name': 'test name', 'desc': 'test description', 'imagePath': '/testPath'}";

        JSONObject mockJson = new JSONObject(mockJsonStr);

        var activityCategory = JsonParsing.parseActivityCategory(mockJson);

        assert (activityCategory.getId() == 9);
        assertEquals(activityCategory.getName(), "test name");
        assertEquals(activityCategory.getDesc(), "test description");
    }

    @Test
    public void parseActivityCategoryEmpty() {
        String mockJsonStr = "{ 'nothing': ' '}";

        JSONObject mockJson = new JSONObject(mockJsonStr);

        var activity = JsonParsing.parseActivityCategory(mockJson);

        assert (activity.getId() == 0);
    }





    @Test
    public void testAttrExistBoolean() {
        String mockJsonStr = "{ 'attr1': false }";
        JSONObject mockJson = new JSONObject(mockJsonStr);

        boolean exists = JsonParsing.attrExists("attr1", mockJson);

        assertTrue(exists);
    }

    @Test
    public void testAttrExistNull() {
        String mockJsonStr = "{ 'attr1': null }";
        JSONObject mockJson = new JSONObject(mockJsonStr);

        boolean exists = JsonParsing.attrExists("attr1", mockJson);
        System.out.println(mockJson.get("attr1"));

        assertFalse(exists);
    }

    @Test
    public void testAttrExistNull_2() {
        String mockJsonStr = "{ 'attr1': 'val1' }";
        JSONObject mockJson = new JSONObject(mockJsonStr);

        boolean exists = JsonParsing.attrExists("attr2", mockJson);

        assertFalse(exists);
    }
}
