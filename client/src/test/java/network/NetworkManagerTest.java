package network;

// import jUnit
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.FieldSetter;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import server.ServerApplication;

// import Mockito
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NetworkManagerTest {

    static ConfigurableApplicationContext server;

    @BeforeAll
    private static void setup() {
        server = SpringApplication.run(ServerApplication.class);
    }

    @AfterAll
    private static void shutdown() {
        server.close();
    }

    @Test
    public void testGetData_success() {
        NetworkResponse response = mock(NetworkResponse.class);

        try {
            NetworkManager.setSslSocketFromKey();

            // try to reach Google, which should always succeed
            NetworkManager manager = new NetworkManager(5000);
            response = manager.getData("https://localhost:8080/amIloggedIn");

        } catch(Exception e) {}

        assertTrue(response.wasSuccess());
    }

    @Test
    public void testGetData_successCookie() {
        NetworkResponse response = mock(NetworkResponse.class);

        try {
            NetworkManager.setSslSocketFromKey();
            // try to reach Google, which should always succeed
            NetworkManager manager = new NetworkManager(5000);
            response = manager.getData("https://localhost:8080/amIloggedIn", "");

        } catch(Exception e) {}

        assertTrue(response.wasSuccess());
    }

    @Test
    public void testGetData_timed_out() {
        NetworkResponse response = mock(NetworkResponse.class);

        try {
            NetworkManager.setSslSocketFromKey();

            // create NetworkManager which timeouts after 0 ms, so timeouts always gets triggered
            NetworkManager manager = new NetworkManager(0);
            response = manager.getData("https://localhost:8080/amIloggedIn");

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.timed_out, response.getStatus());
    }

    @Test
    public void testGetData_no_internet() {
        NetworkResponse response = new NetworkResponse();

        try {
            NetworkManager.setSslSocketFromKey();
            // create a mock NetworkManager which never has an internet connection
            NetworkManager mockManager = mock(NetworkManager.class);
            when(mockManager.getData(anyString())).thenCallRealMethod();
            when(mockManager.getData(anyString(), anyString())).thenCallRealMethod();
            FieldSetter.setField(mockManager, NetworkManager.class.getDeclaredField("timeoutms"), 5000);
            when(mockManager.hasInternet()).thenReturn(false);

            // now trigger IOException, which in part will test the internet connection
            response = mockManager.getData("https://dontexist.localhost");

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.no_internet, response.getStatus());
    }

    @Test
    public void testGetData_bad_statuscode() {
        NetworkResponse response = new NetworkResponse();

        try {
            NetworkManager.setSslSocketFromKey();

            // connect to httpstat.us to retrieve specific status code
            NetworkManager manager = new NetworkManager(5000);
            response = manager.getData("https://localhost:8080/login");

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.bad_statuscode, response.getStatus());
    }

    @Test
    public void testGetData_invalid_url() {
        NetworkResponse response = new NetworkResponse();

        try {

            // try to reach an URL with invalid format
            NetworkManager manager = new NetworkManager(5000);
            response = manager.getData("https//com");

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.invalid_url, response.getStatus());
    }

    @Test
    public void testGetData_invalid_url_2() {
        NetworkResponse response = new NetworkResponse();

        try {

            // try to reach non existing host
            NetworkManager manager = new NetworkManager(5000);
            response = manager.getData("https://localhost:9999999");

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.invalid_url, response.getStatus());
    }

    @Test
    public void testGetJson_invalid_url_2() {
        NetworkResponse response = new NetworkResponse();

        try {

            // try to reach non existing host
            NetworkManager manager = new NetworkManager(5000);
            response = manager.getJson("https://localhost:9999999", "");

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.invalid_url, response.getStatus());
    }

    @Test
    public void testGetImage_invalid_url_2() {
        NetworkResponse response = new NetworkResponse();

        try {
            NetworkManager.setSslSocketFromKey();

            // try to reach non existing host
            NetworkManager manager = new NetworkManager(5000);
            response = manager.getImage("https://localhost:9999999", 0.0, 0.0);

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.invalid_url, response.getStatus());
    }

    @Test
    public void testSendJson_invalid_url_2() {
        NetworkResponse response = new NetworkResponse();

        try {
            // create a simple JSON object
            JSONObject json = new JSONObject();
            json.put("key1", "value2");

            // try to reach own site, which should always accept the data
            NetworkManager manager = new NetworkManager(5000);
            response = manager.sendJson("http://localhost:99999", json);

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.invalid_url, response.getStatus());
    }

    @Test
    public void testSendImage_invalid_url_2() {
        NetworkResponse response = new NetworkResponse();

        try {
            // try to reach own site, which should always accept the data
            NetworkManager manager = new NetworkManager(5000);
            response = manager.sendImage("http://localhost:99999", "any_image");

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.invalid_url, response.getStatus());
    }

    @Test
    public void testGetData_bad_host() {
        NetworkResponse response = new NetworkResponse();

        try {
            NetworkManager.setSslSocketFromKey();

            // try to reach non existing host
            NetworkManager manager = new NetworkManager(5000);
            response = manager.getData("https://localhost:1");

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.bad_host, response.getStatus());
    }

    @Test
    public void testGetData_valid_json() {
        NetworkResponse response = new NetworkResponse();

        try {
            NetworkManager.setSslSocketFromKey();

            // try to get valid JSON
            NetworkManager manager = new NetworkManager(5000);
            response = manager.getJson("https://localhost:8080/amIloggedIn", "");

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.success, response.getStatus());
    }

    @Test
    public void testGetImage_success() {
        NetworkResponse response = new NetworkResponse();

        try {
            NetworkManager.setSslSocketFromKey();

            // try to reach non existing host
            NetworkManager manager = new NetworkManager(5000);
            response = manager.getImage("https://localhost:8080/user/getProfilePic?username=jakob_hand", 0.0, 0.0);

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.success, response.getStatus());
    }

    @Test
    public void testGetImage_bad_host() {
        NetworkResponse response = new NetworkResponse();

        try {
            NetworkManager.setSslSocketFromKey();

            // try to reach non existing host
            NetworkManager manager = new NetworkManager(5000);
            response = manager.getImage("https://localhost:1", 0.0, 0.0);

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.bad_host, response.getStatus());
    }

    @Test
    public void testGetImage_invalid_url() {
        NetworkResponse response = new NetworkResponse();

        try {

            // try to reach non existing host
            NetworkManager manager = new NetworkManager(5000);
            response = manager.getImage("https//com", 0.0, 0.0);

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.invalid_url, response.getStatus());
    }

    @Test
    public void testGetImage_no_url() {
        NetworkResponse response = new NetworkResponse();

        try {

            // try to reach non existing host
            NetworkManager manager = new NetworkManager(5000);
            response = manager.getImage(null, 0.0, 0.0);

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.invalid_url, response.getStatus());
    }

    @Test
    public void testGetImage_no_width() {
        NetworkResponse response = new NetworkResponse();

        try {
            NetworkManager.setSslSocketFromKey();

            // try to reach non existing host
            NetworkManager manager = new NetworkManager(5000);
            response = manager.getImage("https://localhost:8080/user/getProfilePic?username=jakob_hand", null, 0.0);

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.success, response.getStatus());
    }

    @Test
    public void testGetImage_no_height() {
        NetworkResponse response = new NetworkResponse();

        try {
            NetworkManager.setSslSocketFromKey();

            // try to reach non existing host
            NetworkManager manager = new NetworkManager(5000);
            response = manager.getImage("https://localhost:8080/user/getProfilePic?username=jakob_hand", 0.0, null);

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.success, response.getStatus());
    }

    @Test
    public void testGetData_failed_json() {
        NetworkResponse response = new NetworkResponse();

        try {

            // try to get JSON from non existing host
            NetworkManager manager = new NetworkManager(5000);
            response = manager.getJson("http://localhost:1", "");

        } catch(Exception e) {}

        assertNotEquals(NetworkResponse.ResponseStatus.success, response.getStatus());
    }

    @Test
    public void testGetJson_null_url() {
        NetworkResponse response = new NetworkResponse();

        try {

            // try to get JSON from non existing host
            NetworkManager manager = new NetworkManager(5000);
            response = manager.getJson(null, "");

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.invalid_url, response.getStatus());
    }

    @Test
    public void testGetData_null_url() {
        NetworkResponse response = new NetworkResponse();

        try {

            // try to get JSON from non existing host
            NetworkManager manager = new NetworkManager(5000);
            response = manager.getData(null);

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.invalid_url, response.getStatus());
    }

    @Test
    public void testGetData_null_urlCookie() {
        NetworkResponse response = new NetworkResponse();

        try {

            // try to get JSON from non existing host
            NetworkManager manager = new NetworkManager(5000);
            response = manager.getData(null, "");

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.invalid_url, response.getStatus());
    }

    @Test
    public void testSendJson_success() {
        NetworkResponse response = new NetworkResponse();

        try {
            NetworkManager.setSslSocketFromKey();

            // create a simple JSON object
            JSONObject json = new JSONObject();
            json.put("key1", "value2");

            // try to reach own site, which should always accept the data
            NetworkManager manager = new NetworkManager(5000);
            response = manager.sendJson("https://localhost:8080/login", json);

        } catch(Exception e) {}

        assertTrue(response.wasSuccess());
    }

    @Test
    public void testSendImage_success() {
        NetworkResponse response = new NetworkResponse();

        try {
            NetworkManager.setSslSocketFromKey();

            // try to reach own site, which should always accept the data
            NetworkManager manager = new NetworkManager(5000);
            response = manager.sendImage("https://localhost:8080/setProfilePic", "../readme-assets/maven_menu.png");

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.success, response.getStatus());
    }

    @Test
    public void testSendJson_successCookie() {
        NetworkResponse response = new NetworkResponse();

        try {
            NetworkManager.setSslSocketFromKey();

            // create a simple JSON object
            JSONObject json = new JSONObject();
            json.put("key1", "value2");

            // try to reach own site, which should always accept the data
            NetworkManager manager = new NetworkManager(5000);
            response = manager.sendJson("https://localhost:8080/login", json, "");

        } catch(Exception e) {}

        assertTrue(response.wasSuccess());
    }

    @Test
    public void testSendJson_invalid_url() {
        NetworkResponse response = new NetworkResponse();

        try {
            // create a simple JSON object
            JSONObject json = new JSONObject();
            json.put("key1", "value2");

            // try to reach own site, which should always accept the data
            NetworkManager manager = new NetworkManager(5000);
            response = manager.sendJson("https//com", json);

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.invalid_url, response.getStatus());
    }

    @Test
    public void testSendImage_invalid_url() {
        NetworkResponse response = new NetworkResponse();

        try {

            // try to reach own site, which should always accept the data
            NetworkManager manager = new NetworkManager(5000);
            response = manager.sendImage("https://com", "any_image");

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.invalid_url, response.getStatus());
    }

    @Test
    public void testSendJson_bad_host() {
        NetworkResponse response = new NetworkResponse();

        try {
            NetworkManager.setSslSocketFromKey();

            // create a simple JSON object
            JSONObject json = new JSONObject();
            json.put("key1", "value2");

            // try to reach own site, which should always accept the data
            NetworkManager manager = new NetworkManager(5000);
            response = manager.sendJson("https://localhost:1", json);

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.bad_host, response.getStatus());
    }

    @Test
    public void testSendImage_bad_host() {
        NetworkResponse response = new NetworkResponse();

        try {
            NetworkManager.setSslSocketFromKey();

            // try to reach own site, which should always accept the data
            NetworkManager manager = new NetworkManager(5000);
            response = manager.sendImage("https://localhost:1", "any_image");

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.bad_host, response.getStatus());
    }

    @Test
    public void testSendJson_null_url() {
        NetworkResponse response = new NetworkResponse();

        try {
            // create a simple JSON object
            JSONObject json = new JSONObject();
            json.put("key1", "value2");

            // try to reach own site, which should always accept the data
            NetworkManager manager = new NetworkManager(5000);
            response = manager.sendJson(null, json);

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.invalid_url, response.getStatus());
    }

    @Test
    public void testSendImage_null_url() {
        NetworkResponse response = new NetworkResponse();

        try {

            // try to reach own site, which should always accept the data
            NetworkManager manager = new NetworkManager(5000);
            response = manager.sendImage(null, "any_image");

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.invalid_url, response.getStatus());
    }

    @Test
    public void testSendJson_null_data() {
        NetworkResponse response = new NetworkResponse();

        try {
            // create a simple JSON object
            JSONObject json = new JSONObject();
            json.put("key1", "value2");

            // try to reach own site, which should always accept the data
            NetworkManager manager = new NetworkManager(5000);
            response = manager.sendJson("https//magistat.nl", null);

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.no_postdata, response.getStatus());
    }

    @Test
    public void testSendImage_null_data() {
        NetworkResponse response = new NetworkResponse();

        try {

            // try to reach own site, which should always accept the data
            NetworkManager manager = new NetworkManager(5000);
            response = manager.sendImage("http://localhost:1", null);

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.no_postdata, response.getStatus());
    }

    @Test
    public void testSendJson_null_urlCookie() {
        NetworkResponse response = new NetworkResponse();

        try {
            // create a simple JSON object
            JSONObject json = new JSONObject();
            json.put("key1", "value2");

            // try to reach own site, which should always accept the data
            NetworkManager manager = new NetworkManager(5000);
            response = manager.sendJson(null, json, "");

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.invalid_url, response.getStatus());
    }

    @Test
    public void testSendJson_null_dataCookie() {
        NetworkResponse response = new NetworkResponse();

        try {
            // create a simple JSON object
            JSONObject json = new JSONObject();
            json.put("key1", "value2");

            // try to reach own site, which should always accept the data
            NetworkManager manager = new NetworkManager(5000);
            response = manager.sendJson("https//magistat.nl", null, "");

        } catch(Exception e) {}

        assertEquals(NetworkResponse.ResponseStatus.no_postdata, response.getStatus());
    }
}