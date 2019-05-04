package server;

import com.fasterxml.jackson.databind.ObjectMapper;
import database.DbRetrieve;

import models.User;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ServerUserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper mapper;

    private MockHttpSession testsession;
    private MockHttpSession testsession2;
    private UserController userController;
    private AccountController accountController = new AccountController();

    @BeforeEach
    public void sql() {
        AccountController.setSourceSql(DbRetrieve.retrieveTestDb());
        UserController.setSourceSql(DbRetrieve.retrieveTestDb());
    }

    @Test
    public void testGetUserInfo() throws Exception {
//        accountController.setSourceSql(source);

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
                get("/user/getUser")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        JSONObject resultJson = new JSONObject(result.getResponse().getContentAsString());
        assert(resultJson.has("firstname"));
    }

    @Test
    public void testgetUserInfoNoSessionAttribute() throws Exception {
//        accountController.setSourceSql(source);

        testsession = new MockHttpSession();

        MvcResult result = mockMvc.perform(
                get("/user/getUser")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();


        assertTrue(result.getResponse().getContentAsString().contains("null")) ;
    }

    @Test
    public void getLeaderBoardTest() throws Exception{
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
                get("/user/getLeaderboard")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("jakob_hand"));
    }

    @Test
    public void TestGetLeaderBoardsWithusernameNull() throws Exception{
        testsession2 = new MockHttpSession();
        MvcResult sendrequest = mockMvc.perform(
                get("/user/getLeaderboard")
                        .session(testsession2)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertEquals("",sendrequest.getResponse().getContentAsString());
    }

    @Test
    public void GetAllFriends()throws Exception{
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
                get("/user/getAllFriends")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        boolean response = result.getResponse().getContentAsString().contains("false");
        assertTrue(response);
    }

    @Test
    public void TestGetAllFriendsWithusernameNull() throws Exception{
        testsession2 = new MockHttpSession();
        MvcResult sendrequest = mockMvc.perform(
                get("/user/getAllFriends")
                        .session(testsession2)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertEquals("{\"users\":[]}",sendrequest.getResponse().getContentAsString());
    }

    @Test
    public void TestSendFriendRequest()throws Exception{
        testsession = new MockHttpSession();
        testsession2 = new MockHttpSession();
        User testuser = new User("jakob_hand",null,null,null);
        MvcResult resultlogin = mockMvc.perform(
                post("/login")
                        .session(testsession2)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"Wout1999\", \"password\": \"Sahsh5kab\"}")
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        MvcResult sendrequest = mockMvc.perform(
                post("/user/sendFriendRequest")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession2)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        MvcResult resultlogin2 = mockMvc.perform(
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
                get("/user/getFriendRequests?getOwn=" + false)
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Wout1999"));
    }
    @Test
    public void TestSendFriendRequestMultipleTimes()throws Exception{
        testsession = new MockHttpSession();
        testsession2 = new MockHttpSession();
        User testuser = new User("jakob_hand",null,null,null);
        MvcResult resultlogin = mockMvc.perform(
                post("/login")
                        .session(testsession2)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"Wout1999\", \"password\": \"Sahsh5kab\"}")
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        MvcResult sendrequest = mockMvc.perform(
                post("/user/sendFriendRequest")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession2)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        MvcResult sendreques2 = mockMvc.perform(
                post("/user/sendFriendRequest")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession2)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(sendreques2.getResponse().getContentAsString().contains("false"));
    }

    @Test
    public void TestSendRequestWithusernameNull() throws Exception{
        User testuser = new User("JackOfHearts","c00ldood","Thomas","jajal@gmail.com");
        testsession2 = new MockHttpSession();
        MvcResult sendrequest = mockMvc.perform(
                post("/user/sendFriendRequest")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession2)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(sendrequest.getResponse().getContentAsString().contains("false"));
    }

    @Test
    public void AcceptFriendRequests()throws Exception{
        testsession = new MockHttpSession();
        testsession2 = new MockHttpSession();
        User testuser = new User("jakob_hand",null,null,null);
        User testuser2 = new User("PandaCrazyBr",null,null,null);
        MvcResult resultloginsecondary = mockMvc.perform(
                post("/login")
                        .session(testsession2)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"PandaCrazyBr\", \"password\": \"Zeos2ae2\"}")
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        MvcResult sendrequest = mockMvc.perform(
                post("/user/sendFriendRequest")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession2)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        MvcResult segondarylogout = mockMvc.perform(
                post("/logout")
                        .session(testsession2)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        MvcResult resultloginmain = mockMvc.perform(
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
                post("/user/acceptFriendRequest")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser2))
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("true"));
    }

    @Test
    public void TestAcceptRequestWithusernameNull() throws Exception{
        User testuser = new User("JackOfHearts","c00ldood","Thomas","jajal@gmail.com");
        testsession2 = new MockHttpSession();
        MvcResult sendrequest = mockMvc.perform(
                post("/user/acceptFriendRequest")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession2)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(sendrequest.getResponse().getContentAsString().contains("false"));
    }

    @Test
    public void RejectFriendRequestTest()throws Exception{
        testsession = new MockHttpSession();
        User testuser = new User("Wout1999",null,null,null);
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
        MvcResult rejectrequest = mockMvc.perform(
                post("/user/declineFriendRequest")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        MvcResult result = mockMvc.perform(
                get("/user/getFriendRequests?getOwn=false")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(!result.getResponse().getContentAsString().contains("Wout1999"));
    }

    @Test
    public void RejectFriendRequestTestNotInTheList()throws Exception{
        testsession = new MockHttpSession();
        User testuser = new User("Wout",null,null,null);
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
        MvcResult rejectrequest = mockMvc.perform(
                post("/user/declineFriendRequest")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        assertFalse(rejectrequest.getResponse().getContentAsString().contains("false"));
    }


    @Test
    public void TestRejectRequestWithusernameNull() throws Exception{
        User testuser = new User("JackOfHearts","c00ldood","Thomas","jajal@gmail.com");
        testsession2 = new MockHttpSession();
        MvcResult sendrequest = mockMvc.perform(
                post("/user/declineFriendRequest")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession2)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(sendrequest.getResponse().getContentAsString().contains("false"));
    }


    @Test
    public void TestRemoveFriend()throws Exception{
        testsession = new MockHttpSession();
        User testuser = new User("PandaCrazyBr",null,null,null);


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
        MvcResult removeFriend = mockMvc.perform(
                post("/user/removeFriend")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession)

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        MvcResult result = mockMvc.perform(
                get("/user/getAllFriends")
                        .session(testsession)

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(!result.getResponse().getContentAsString().contains("PandaCrazyBr"));
    }

    @Test
    public void TestRemoveFriendWithusernameNull() throws Exception{
        User testuser = new User("JackOfHearts","c00ldood","Thomas","jajal@gmail.com");
        testsession2 = new MockHttpSession();
        MvcResult sendrequest = mockMvc.perform(
                post("/user/removeFriend")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession2)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(sendrequest.getResponse().getContentAsString().contains("false"));
    }


    @Test
    public void GetFriendRequests()throws Exception{
        testsession = new MockHttpSession();
        testsession2 = new MockHttpSession();
        User testuser = new User("jakob_hand",null,null,null);
        MvcResult resultloginsecondary = mockMvc.perform(
                post("/login")
                        .session(testsession2)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"PandaCrazyBr\", \"password\": \"Zeos2ae2\"}")
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        MvcResult sendrequest = mockMvc.perform(
                post("/user/sendFriendRequest")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession2)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

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
                get("/user/getFriendRequests?getOwn=" + false)
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("PandaCrazyBr"));

        MvcResult revoke = mockMvc.perform(
                post("/user/revokeFriendRequest")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession2)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(revoke.getResponse().getContentAsString().contains("true"));
    }
    @Test
    public void TestRevokeRequestWithoutSendingRequest()throws Exception{
        User testuser = new User("jakob_hand",null,null,null);
        testsession = new MockHttpSession();
        MvcResult resultloginsecondary = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"PandaCrazyBr\", \"password\": \"Zeos2ae2\"}")
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        MvcResult result = mockMvc.perform(
                post("/user/revokeFriendRequest")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertFalse(result.getResponse().getContentAsString().contains("true"));
    }
    @Test
    public void GetSuggestionsTest() throws Exception{
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
                post("/user/getFriendSuggestions")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"pan\"}")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("PandaCrazyBr"));
    }


    @Test
    public void TestGetRequestsWithusernameNull() throws Exception{
        testsession2 = new MockHttpSession();
        MvcResult sendrequest = mockMvc.perform(
                get("/user/getFriendRequests?getOwn=" + false)
                        .session(testsession2)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertEquals("",sendrequest.getResponse().getContentAsString());
    }
    @Test
    public void TestRevokeRequestNotLoggedIn() throws Exception{
        User testuser = new User("JackOfHearts","c00ldood","Thomas","jajal@gmail.com");
        testsession = new MockHttpSession();
        MvcResult result = mockMvc.perform(
                post("/user/revokeFriendRequest")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertFalse(result.getResponse().getContentAsString().contains("true"));
    }

    @Test
    public void TestRevokeRequest() throws Exception{
        User testuser = new User("fr1",null,null,null);
        testsession = new MockHttpSession();
        MvcResult resultloginsecondary = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"PandaCrazyBr\", \"password\": \"Zeos2ae2\"}")
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        MvcResult sendrequest = mockMvc.perform(
                post("/user/sendFriendRequest")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        MvcResult result = mockMvc.perform(
                post("/user/revokeFriendRequest")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("true"));
    }
    @Test
    public void getProfilePicTest() throws Exception{
        MvcResult result = mockMvc.perform(
                get("/user/getProfilePic")
                .param("username","jakob_hand")

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertEquals("",result.getResponse().getContentAsString());
    }
}