package network;

// import jUnit

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NetworkResponseTest {

    @Test
    public void testResponseStatus() {
        NetworkResponse.ResponseStatus status = NetworkResponse.ResponseStatus.success;

        assertEquals("The request completed successfully!", status.toString());
    }

    @Test
    public void testWasSuccess() {
        NetworkResponse response = new NetworkResponse<String>();
        response.setStatus(NetworkResponse.ResponseStatus.success);
        assertTrue(response.wasSuccess());
    }

    @Test
    public void testValueOf() {
        assertEquals(NetworkResponse.ResponseStatus.success, NetworkResponse.ResponseStatus.valueOf("success"));
    }

    @Test
    public void testEnumValues() {
        NetworkResponse.ResponseStatus[] values = {
                NetworkResponse.ResponseStatus.success,
                NetworkResponse.ResponseStatus.timed_out,
                NetworkResponse.ResponseStatus.no_internet,
                NetworkResponse.ResponseStatus.bad_statuscode,
                NetworkResponse.ResponseStatus.bad_host,
                NetworkResponse.ResponseStatus.invalid_url,
                NetworkResponse.ResponseStatus.invalid_json,
                NetworkResponse.ResponseStatus.no_json,
                NetworkResponse.ResponseStatus.no_data,
                NetworkResponse.ResponseStatus.no_postdata
        };

        assertArrayEquals(values, NetworkResponse.ResponseStatus.values());
    }

    @Test
    public void testSetCooke() {
        NetworkResponse response = new NetworkResponse<String>();
        response.setCookie("cookie");
        assertEquals("cookie", response.getCookie());

    }
}