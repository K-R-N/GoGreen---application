package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


//import org.json.JSONException;
//import org.json.JSONObject;
import database.DbAccess;
import database.DbRetrieve;

import models.Activity;
import models.ActivityCategory;
import models.ActivityType;
import models.User;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;


import static java.lang.Boolean.parseBoolean;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@SpringBootTest
@AutoConfigureMockMvc
public class ActivityControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper mapper;
    @Autowired WebApplicationContext wac;
    private User testuser;
    private Activity testActivity;
    private ActivityController activityController = new ActivityController();
    private DbAccess testdb;
    private AccountController testaccount;
    private MockHttpSession testsession;

    @BeforeEach
    public void sql() {
        ActivityController.setSourceSql(DbRetrieve.retrieveTestDb());
        AccountController.setSourceSql(DbRetrieve.retrieveTestDb());
//        activityController.setSourceSql(source);
        }


    @Test
    public void testPutActivity() throws Exception {

        testsession = new MockHttpSession();

        MvcResult resultlogin = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"jakob_hand\", \"password\": \"ea8phe1Zaz\"}")
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        Activity testactivity = new Activity(
                0,new ActivityType(0,"mock_activity",0,
                new ActivityCategory(0,"mock_activity","mock_activity", null),
                "unauthorized_user"),
                null,
                LocalDate.of(1, 1, 1),
                LocalTime.of(1, 1, 1, 1)
        );

        MvcResult result = mockMvc.perform(
                post("/activities/put")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testactivity))
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        assert((result.getResponse().getContentAsString()).contains("true"));
        DbRetrieve.retrieveTestDb().getConnection().prepareStatement("Delete From activity_log Where log_id is Null;").executeUpdate();
    }

    @Test
    public void testPutActivityNoAuthentication()throws Exception{
        Activity testactivity = new Activity(
                0,
                new ActivityType(0,"mock_activity",0,
                new ActivityCategory(0,"mock_activity","mock_activity", null),
                "unauthorized_user"),
                null,
                LocalDate.of(1, 1, 1),
                LocalTime.of(1, 1, 1, 1)
        );

        testsession = new MockHttpSession();

        MvcResult result = mockMvc.perform(
                post("/activities/put")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testactivity))
                .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        assert(! parseBoolean(result.getResponse().getContentAsString()));
    }

    @Test
    public void TestGetFriendsActivitieswithoutBeingTheirFriend() throws Exception{
        testsession = new MockHttpSession();
        MvcResult resultlogin = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"jakob_hand\", \"password\": \"ea8phe1Zaz\"}")
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        MvcResult result = mockMvc.perform(
                get("/activities/getTimeline")
                       .param("startDate","2018-03-01")
                        .param("endDate","2019-03-31")
                        .param("includeFriends","true")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("darius"));
    }
    @Test
    public void TestGetFriendsActivities() throws Exception{
        testsession = new MockHttpSession();

        MvcResult resultlogin = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"jakob_hand\", \"password\": \"ea8phe1Zaz\"}")
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        MvcResult result = mockMvc.perform(
                get("/activities/getTimeline")
                        .param("startDate","2019-03-02")
                        .param("endDate","2019-03-31")
                        .param("includeFriends","true")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("darius"));
    }
    @Test
    public void TestDeleteActivities() throws Exception{
        testsession = new MockHttpSession();
        MvcResult resultlogin = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"jakob_hand\", \"password\": \"ea8phe1Zaz\"}")
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        Activity testactivity = new Activity(
                0,new ActivityType(0,"mock_activity",0,
                new ActivityCategory(0,"mock_activity","mock_activity", null),
                "unauthorized_user"),
                null,
                LocalDate.of(1, 1, 1),
                LocalTime.of(1, 1, 1, 1)
        );
        MvcResult resultput = mockMvc.perform(
                post("/activities/put")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testactivity))
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        MvcResult result = mockMvc.perform(
                post("/activities/deleteActivity")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testactivity))
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        assert((result.getResponse().getContentAsString()).contains("true"));
    }
    @Test
    public void TestDeleteActivityNoAthountication() throws Exception{
        testsession = new MockHttpSession();
        Activity testactivity = new Activity(
                0,new ActivityType(0,"mock_activity",0,
                new ActivityCategory(0,"mock_activity","mock_activity", null),
                "unauthorized_user"),
                null,
                LocalDate.of(1, 1, 1),
                LocalTime.of(1, 1, 1, 1)
        );
        MvcResult result = mockMvc.perform(
                post("/activities/deleteActivity")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testactivity))
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        assert((result.getResponse().getContentAsString()).contains("false"));
    }


    @Test
    public void testGetActivity() throws Exception {
        DbRetrieve.retrieveTestDb().getConnection().prepareStatement("Delete From activity_log Where log_id is Null;").executeUpdate();
        testsession = new MockHttpSession();

        MvcResult resultlogin = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"Wout1999\", \"password\": \"Sahsh5kab\"}")
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();


        MvcResult result = mockMvc.perform(
                get("/activities/getTimeline")
                        .param("startDate","2015-02-25")
                        .param("endDate","2019-03-31")
                        .param("includeFriends","true")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        
        assertTrue(result.getResponse().getContentAsString().contains("local_produce"));

    }

    @Test
    public void testGetActivityNoAuthentication() throws Exception{
        testsession = new MockHttpSession();

        MvcResult result = mockMvc.perform(
                get("/activities/getTimeline")
                .param("startDate","2019-02-25")
                        .param("endDate","2019-03-31")
                        .param("includeFriends","true")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();


        assertEquals("{}", result.getResponse().getContentAsString());
    }

    @Test
    public void TestGetAllActivities() throws Exception{
        testsession = new MockHttpSession();

        MvcResult resultlogin = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"jakob_hand\", \"password\": \"ea8phe1Zaz\"}")
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        MvcResult result = mockMvc.perform(
                get("/activities/getTimeline")
                        .param("startDate","2019-02-25")
                        .param("endDate","2019-03-31")
                        .param("includeFriends","true")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("jakob_hand"));
    }

    @Test
    public void testGetAllActivityNoAuthentication() throws Exception{
        testsession = new MockHttpSession();

        MvcResult result = mockMvc.perform(
                get("/activities/getTimeline")
                        .param("startDate","2019-02-25")
                        .param("endDate","2019-03-31")
                        .param("includeFriends","true")
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertEquals("{}", result.getResponse().getContentAsString());
    }

    @Test
    public void GetNonMainUsersActivities() throws Exception{
        testsession = new MockHttpSession();

        MvcResult resultlogin = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"jakob_hand\", \"password\": \"ea8phe1Zaz\"}")
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        MvcResult result = mockMvc.perform(
                get("/activities/getTimeline")
                        .param("startDate","2019-02-25")
                        .param("endDate","2019-03-31")
                        .param("includeFriends","false")
                        .param("isMainUser","darius")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("darius"));
    }
    @Test
    public void TestGetAllOwnActivitiesWithoutLogin() throws Exception{
        testsession = new MockHttpSession();
        MvcResult result = mockMvc.perform(
                get("/activities/getTimeline")
                        .param("startDate","2019-02-25")
                        .param("endDate","2019-03-31")
                        .param("includeFriends","false")

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertEquals("{}",result.getResponse().getContentAsString());
    }


    @Test
    public void TestTotalActivityHistory() throws Exception{
        testsession = new MockHttpSession();

        MvcResult resultlogin = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"jakob_hand\", \"password\": \"ea8phe1Zaz\"}")
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        MvcResult result = mockMvc.perform(
                get("/activities/getTimeline")
                        .param("startDate","2019-03-02")
                        .param("endDate","2019-03-31")
                        .param("includeFriends","true")
                        .session(testsession)

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("darius"));
    }

    @Test
    public void TestGetTotalActivityHistoryWithoutLogin() throws Exception{
        testsession = new MockHttpSession();
        MvcResult result = mockMvc.perform(
                get("/activities/getTimeline")
                        .param("startDate","2019-02-25")
                        .param("endDate","2019-03-31")
                        .param("includeFriends","true")
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertEquals("{}",result.getResponse().getContentAsString());
    }

    @Test
    public void TestGetAllAchievements() throws Exception{
        testsession = new MockHttpSession();

        MvcResult resultlogin = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"jakob_hand\", \"password\": \"ea8phe1Zaz\"}")
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        MvcResult result = mockMvc.perform(
                get("/activities/getAllAchievements")
                        .session(testsession)

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("10_veg_name"));
    }

    @Test
    public void TestGetAllAchievementsWithoutLogin() throws Exception{
        testsession = new MockHttpSession();
        MvcResult result = mockMvc.perform(
                get("/activities/getAllAchievements")
                        .session(testsession)

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertEquals("{}",result.getResponse().getContentAsString());
    }

    @Test
    public void GetFriendsAchievements() throws Exception{
        testsession = new MockHttpSession();

        MvcResult resultlogin = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"jakob_hand\", \"password\": \"ea8phe1Zaz\"}")
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        MvcResult result = mockMvc.perform(
                post("/activities/getFriendsAchievements")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"darius\"}")
                        .session(testsession)

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("5_veg_name"));
    }
    @Test
    public void GetFriendsAchievementsNotFriendsWith() throws Exception{
        testsession = new MockHttpSession();

        MvcResult resultlogin = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"jakob_hand\", \"password\": \"ea8phe1Zaz\"}")
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        MvcResult result = mockMvc.perform(
                post("/activities/getFriendsAchievements")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"test_user\"}")
                        .session(testsession)

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertEquals("{\"achievements\":[]}",result.getResponse().getContentAsString());
    }
    @Test
    public void TestGetAllFriendAchievementsWithoutLogin() throws Exception{
        testsession = new MockHttpSession();
        MvcResult result = mockMvc.perform(
                post("/activities/getFriendsAchievements")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"bob\"}")
                        .session(testsession)

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertEquals("{}",result.getResponse().getContentAsString());
    }

    @Test
    public void TestGetAllBadges() throws Exception{
        testsession = new MockHttpSession();

        MvcResult resultlogin = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"jakob_hand\", \"password\": \"ea8phe1Zaz\"}")
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        MvcResult result = mockMvc.perform(
                get("/activities/getAllBadges")
                        .session(testsession)

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(!result.getResponse().getContentAsString().contains("null"));
    }

    @Test
    public void TestGetAllBadgesWithoutLogin() throws Exception{
        testsession = new MockHttpSession();
        MvcResult result = mockMvc.perform(
                get("/activities/getAllBadges")
                        .session(testsession)

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertEquals("{}",result.getResponse().getContentAsString());
    }

    @Test
    public void GetAllAvailableActivities()throws Exception{
        testsession = new MockHttpSession();

        MvcResult resultlogin = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"jakob_hand\", \"password\": \"ea8phe1Zaz\"}")
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();


        MvcResult result = mockMvc.perform(
                get("/activities/getAvailableActivities")
                        .session(testsession)

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("vegeterian_meal"));
    }
    @Test
    public void GetAllAvailableActivitiesWithoutLogin()throws Exception{
        testsession = new MockHttpSession();


        MvcResult result = mockMvc.perform(
                get("/activities/getAvailableActivities")
                        .session(testsession)

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        assert ( !result.getResponse().getContentAsString().equals("{\"activities\":[]}"));
    }

    @Test
    public void TestGetImageIsCategoryFalse() throws Exception{
        MvcResult result = mockMvc.perform(
                get("/activities/getImage")
                        .param("id","2")
                        .param("isCategory","false")
                        .param("isBig","false")

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertEquals("",result.getResponse().getContentAsString());
    }
    @Test
    public void TestGetImageIsBigFalse() throws Exception{
        MvcResult result = mockMvc.perform(
                get("/activities/getImage")
                        .param("id","2")
                        .param("isCategory","true")
                        .param("isBig","false")

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertEquals("",result.getResponse().getContentAsString());
    }

    @Test
    public void TestGetImage() throws Exception{
        MvcResult result = mockMvc.perform(
                get("/activities/getImage")
                        .param("id","1")
                        .param("isCategory","false")
                        .param("isBig","true")

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertEquals("",result.getResponse().getContentAsString());
    }
}