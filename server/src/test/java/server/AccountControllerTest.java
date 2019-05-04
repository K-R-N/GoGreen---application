package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.DbRetrieve;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;

import static java.lang.Boolean.parseBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper mapper;

    private MockHttpSession testsession;
    private AccountController accountController = new AccountController();

    @BeforeEach
    public void sql() {
        AccountController.setSourceSql(DbRetrieve.retrieveTestDb());
    }


    @Test
    public void testCorrectLogin() throws Exception {
//        accountController.setSourceSql(source);

        String payload = "{\"username\":\"jakob_hand\", \"password\": \"ea8phe1Zaz\"}";

        MvcResult result = mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(payload)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("true"));
    }

    @Test
    public void testNoExistUserLogin() throws Exception {
        String payload = "{\"username\":null, \"password\": \"ea8phe1Zau\"}";


        MvcResult result = mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(payload)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        assert(! parseBoolean(result.getResponse().getContentAsString()));
    }

    @Test
    public void testIncorrectLogin() throws Exception {
//        accountController.setSourceSql(source);
        String payload = "{\"username\":\"jakob_hand\", \"password\": \"wrong\"}";


        MvcResult result = mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(payload)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        assert(! parseBoolean(result.getResponse().getContentAsString()));

    }

    @Test
        public void testLogout() throws Exception {
//        accountController.setSourceSql(source);

            String payload = "{\"username\":\"jakob_hand\", \"password\": \"ea8phe1Zaz\"}";

            testsession = new MockHttpSession();

            MvcResult result = mockMvc.perform(
                    post("/login")
                            .session(testsession)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(payload)
            ).andDo(
                    print()
            ).andExpect(
                    status().isOk()
            ).andReturn();

        assert(!testsession.isInvalid());

        result = mockMvc.perform(
                get("/logout").session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(payload)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        assert(testsession.isInvalid());

    }
    @Test
    public void testWrongLogout() throws Exception {
//        accountController.setSourceSql(source);



        testsession = new MockHttpSession();


       MvcResult result = mockMvc.perform(
                get("/logout")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("false"));

    }

    @Test
    public void TestCorrectCreateAccount() throws Exception {
        User testuser = new User("johan", "c00", "Tom", "jaja@gmail.com");
        testsession = new MockHttpSession();
        MvcResult result = mockMvc.perform(
                post("/createAccount")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("true"));

        String payload = "{\"username\":\"johan\", \"password\": \"c00\"}";
        MvcResult login = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(payload)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        MvcResult result2 = mockMvc.perform(
                post("/deleteAccount")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

    }


    @Test
    public void TestUsernameAlreadyTaken() throws Exception {
        User testuser = new User("jakob_hand", "c00ldood", "Thomas", "jajal@gmail.com");
        testsession = new MockHttpSession();
        MvcResult result = mockMvc.perform(
                post("/createAccount")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("false"));
    }

    @Test
    public void TestCorrectDeleteAccount() throws Exception {

        User testuser = new User("johan", "c00", "Tom", "jaja@gmail.com");
        testsession = new MockHttpSession();
        MvcResult result = mockMvc.perform(
                post("/createAccount")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        String payload = "{\"username\":\"johan\", \"password\": \"c00\"}";
        MvcResult login = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(payload)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        MvcResult result2 = mockMvc.perform(
                post("/deleteAccount")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        assertTrue(result2.getResponse().getContentAsString().contains("true"));
    }

    @Test
    public void TestDeleteWithNonLoggedInUser() throws Exception {
        testsession = new MockHttpSession();
        MvcResult result = mockMvc.perform(
                post("/deleteAccount")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("false"));
    }

    @Test
    public void TestChangePasswordWithNonLoggedInUser() throws Exception {
        User temp = new User("temp","password1243",null,null);
        testsession = new MockHttpSession();
        MvcResult result = mockMvc.perform(
                post("/changePassword")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(temp))
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("false"));
    }
    @Test
    public void TestEmailWithNonLoggedInUser() throws Exception {
        User temp = new User("temp","password1243",null,"banana@hotmail.com");
        testsession = new MockHttpSession();
        MvcResult result = mockMvc.perform(
                post("/changeEmail")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(temp))
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("false"));
    }
    @Test
    public void TestFirstNamePasswordWithNonLoggedInUser() throws Exception {
        User temp = new User("temp","password1243","Jake",null);
        testsession = new MockHttpSession();
        MvcResult result = mockMvc.perform(
                post("/changeFirstname")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(temp))
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("false"));
    }
    @Test
    public void TestCreateChangePasswordAndDeleteAccount() throws Exception {
        User testuser = new User("JackOfHearts","c00ldood","Thomas","jajal@gmail.com");
        User temp = new User("temp","password1243",null,null);
        testsession = new MockHttpSession();
        MvcResult result = mockMvc.perform(
                post("/createAccount")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("true"));


        String payload = "{\"username\":\"jackofhearts\", \"password\": \"c00ldood\"}";


        MvcResult login = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(payload)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        MvcResult changepassword = mockMvc.perform(
                post("/changePassword")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(temp))
                        .session(testsession)

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
                    assertTrue(changepassword.getResponse().getContentAsString().contains("true"));

        MvcResult result3 = mockMvc.perform(
                post("/deleteAccount")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("true"));
    }

    @Test
    public void TestCreateChangePasswordAndDeleteAccountFailure() throws Exception {
        User testuser = new User("JackOfHearts","c00ldood","Thomas","jajal@gmail.com");

        testsession = new MockHttpSession();
        MvcResult result = mockMvc.perform(
                post("/createAccount")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("true"));


        String payload = "{\"username\":\"jackofhearts\", \"password\": \"c00ldood\"}";

        MvcResult login = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(payload)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        MvcResult changepassword = mockMvc.perform(
                post("/changePassword")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession)

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertFalse(changepassword.getResponse().getContentAsString().contains("true"));

        MvcResult result3 = mockMvc.perform(
                post("/deleteAccount")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("true"));
    }

    @Test
    public void TestCreateChangeEmailAndDeleteAccount() throws Exception {
        User testuser = new User("JackOfHearts","c00ldood","Thomas","jajal@gmail.com");
        User temp = new User("JackOfHearts","c00ldood","Thomas","jajal@hotmail.com");
        testsession = new MockHttpSession();
        MvcResult result = mockMvc.perform(
                post("/createAccount")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("true"));


        String payload = "{\"username\":\"jackofhearts\", \"password\": \"c00ldood\"}";


        MvcResult login = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(payload)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        MvcResult changeemail = mockMvc.perform(
                post("/changeEmail")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(temp))
                        .session(testsession)

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(changeemail.getResponse().getContentAsString().contains("true"));

        MvcResult result3 = mockMvc.perform(
                post("/deleteAccount")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("true"));
    }

    @Test
    public void TestCreateChangeEmailAndDeleteAccountFailure() throws Exception {
        User testuser = new User("JackOfHearts","c00ldood","Thomas","jajal@gmail.com");
        testsession = new MockHttpSession();
        MvcResult result = mockMvc.perform(
                post("/createAccount")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("true"));


        String payload = "{\"username\":\"jackofhearts\", \"password\": \"c00ldood\"}";


        MvcResult login = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(payload)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        MvcResult changeemail = mockMvc.perform(
                post("/changeEmail")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession)

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertFalse(changeemail.getResponse().getContentAsString().contains("true"));

        MvcResult result3 = mockMvc.perform(
                post("/deleteAccount")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("true"));
    }

    @Test
    public void TestCreateChangeFirstNameAndDeleteAccount() throws Exception {
        User testuser = new User("JackOfHearts","c00ldood","Thomas","jajal@gmail.com");
        User temp = new User("JackOfHearts","c00ldood","Bob","jajal@gmail.com");
        testsession = new MockHttpSession();
        MvcResult result = mockMvc.perform(
                post("/createAccount")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("true"));


        String payload = "{\"username\":\"jackofhearts\", \"password\": \"c00ldood\"}";


        MvcResult login = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(payload)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        MvcResult changefirstname = mockMvc.perform(
                post("/changeFirstname")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(temp))
                        .session(testsession)

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(changefirstname.getResponse().getContentAsString().contains("true"));

        MvcResult result3 = mockMvc.perform(
                post("/deleteAccount")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("true"));
    }

    @Test
    public void TestCreateChangeFirstNameAndDeleteAccountFailure() throws Exception {
        User testuser = new User("JackOfHearts","c00ldood","Thomas","jajal@gmail.com");
        testsession = new MockHttpSession();
        MvcResult result = mockMvc.perform(
                post("/createAccount")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("true"));


        String payload = "{\"username\":\"jackofhearts\", \"password\": \"c00ldood\"}";


        MvcResult login = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(payload)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        MvcResult changefirstname = mockMvc.perform(
                post("/changeFirstname")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(testuser))
                        .session(testsession)

        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertFalse(changefirstname.getResponse().getContentAsString().contains("true"));

        MvcResult result3 = mockMvc.perform(
                post("/deleteAccount")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("true"));
    }

    		    @Test
		    public void TestSetProfilePic() throws Exception {
		        User testuser = new User("JackOfHearts","c00ldood","Thomas","jajal@gmail.com");
		        testsession = new MockHttpSession();
		        MvcResult result = mockMvc.perform(
		                post("/createAccount")
		                        .contentType(MediaType.APPLICATION_JSON_UTF8)
		                        .content(mapper.writeValueAsString(testuser))
		                        .session(testsession)
		        ).andDo(
		                print()
		        ).andExpect(
		                status().isOk()
		        ).andReturn();
		        assertTrue(result.getResponse().getContentAsString().contains("true"));


		        String payload = "{\"username\":\"jackofhearts\", \"password\": \"c00ldood\"}";


		        MvcResult login = mockMvc.perform(
		                post("/login")
		                        .session(testsession)
		                        .contentType(MediaType.APPLICATION_JSON_UTF8)
		                        .content(payload)
		        ).andDo(
		                print()
		        ).andExpect(
		                status().isOk()
		        ).andReturn();

//                    DataOutputStream outputStream = new DataOutputStream();
		        MultipartFile testimage = new MockMultipartFile("testimage",new FileInputStream(
		                new File("youtubelogo.png")));

                    byte[] byteArr = testimage.getBytes();

                    System.out.println("byte size " + byteArr.length);
		        MockMultipartFile image = new MockMultipartFile("image", " ", "multipart/form-data", byteArr);

		        MvcResult result3 = mockMvc.perform(
		                MockMvcRequestBuilders.fileUpload("/setProfilePic").file(image)
                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
		                        .session(testsession)
		        ).andDo(
		                print()
		        ).andExpect(
		                status().isOk()
		        ).andReturn();

		        assertTrue(result3.getResponse().getContentAsString().contains("true"));

                    MvcResult result4 = mockMvc.perform(
                            post("/deleteAccount")
                                    .session(testsession)
                    ).andDo(
                            print()
                    ).andExpect(
                            status().isOk()
                    ).andReturn();

                    assertTrue(result4.getResponse().getContentAsString().contains("true"));
		    }
    @Test
    public void TestSetProfilePicWithoutLogin() throws Exception {
        testsession = new MockHttpSession();

        MultipartFile testimage = new MockMultipartFile("testimage",new FileInputStream(
                new File("youtubelogo.png")));

        byte[] byteArr = testimage.getBytes();

        System.out.println("byte size " + byteArr.length);
        MockMultipartFile image = new MockMultipartFile("image", " ", "multipart/form-data", byteArr);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.fileUpload("/setProfilePic").file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("false"));
    }
    @Test
    public void TestSetPrivacySettingsToPrivate() throws Exception{
        testsession = new MockHttpSession();
        String payload = "{\"username\":\"jakob_hand\", \"password\": \"ea8phe1Zaz\"}";


        MvcResult login = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(payload)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();


        MvcResult firstChange = mockMvc.perform(
                get("/changePrivacy")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();



        MvcResult result = mockMvc.perform(
                get("/changePrivacy")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        boolean response = result.getResponse().getContentAsString().contains("true");
        assertTrue(response);
    }
    @Test
    public void TestSetPrivacySettingsToPublic() throws Exception{
        testsession = new MockHttpSession();
        String payload = "{\"username\":\"jakob_hand\", \"password\": \"ea8phe1Zaz\"}";

        MvcResult login = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(payload)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();


        MvcResult SettingtoPublicFromNull = mockMvc.perform(
                get("/changePrivacy")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        MvcResult SettingFromPublicToPrivate = mockMvc.perform(
                get("/changePrivacy")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();

        MvcResult result = mockMvc.perform(
                get("/changePrivacy")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        boolean response = result.getResponse().getContentAsString().contains("true");
        assertTrue(response);
    }
    @Test
    public void TestsChangePrivacyWithoutASession() throws Exception{
        testsession = new MockHttpSession();
        MvcResult result = mockMvc.perform(
                get("/changePrivacy")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("false"));
    }

    @Test
    public void TestChangePrivacyWithItBeingNull() throws Exception{
        testsession = new MockHttpSession();
        String payload = "{\"username\":\"jakob_hand\", \"password\": \"ea8phe1Zaz\"}";
        MvcResult login = mockMvc.perform(
                post("/login")
                        .session(testsession)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(payload)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        MvcResult change = mockMvc.perform(
                get("/changePrivacy")
                        .session(testsession)
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
        assertTrue(change.getResponse().getContentAsString().contains("true"));
    }
    }

