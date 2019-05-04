package network;

/**
 * Object containing response of request, in case the request failed, the object contains an error.
 * @author Jules van der Toorn
 */
public class NetworkResponse<T> {

    private T data;
    private ResponseStatus status;
    private int responseCode = -1;
    private  String cookie;

    /**
     * Indicates whether request was success.
     * @return boolean indicating if successful
     */
    public boolean wasSuccess() {
        return this.status == ResponseStatus.success;
    }

    /**
     * An enum containing all the possible request states.
     */
    public enum ResponseStatus {
        success("The request completed successfully!"),
        timed_out("The request took too long to complete."),
        no_internet("There appears to be no internet connection."),
        bad_statuscode("The response code was different than 200."),
        bad_host("The server couldn't be reached, it might be offline."),
        invalid_url("The provided url is not in a valid format."),
        invalid_json("The returned data is not in JSON format."),
        no_json("The response doesn't contain any JSON."),
        no_data("The response doesn't contain any data."),
        no_postdata("The request doesn't contain any data.");

        private final String description;

        ResponseStatus(String desc) {
            description = desc;
        }

        public String toString() {
            return this.description;
        }
    }

    /**
     * Setter for the response code.
     * @param responseCode the value you want to set
     */
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * Getter for the response code.
     * @return the response code
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * Setter for the ResponseStatus object.
     * @param status the status you want to set
     */
    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    /**
     * Setter for the cookie.
     * @param cookie the string of what the server sends to the client
     */
    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    /**
     * Getter for the ResponseStatus object.
     * @return the ResponseStatus object
     */
    public ResponseStatus getStatus() {
        return status;
    }

    /**
     * Setter for the response data.
     * @param data the data you want to set
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Getter for the response data.
     * @return the response data
     */
    public T getData() {
        return data;
    }

    /**
     * Getter for the cookie.
     * @return the value of the cookie
     */
    public String getCookie() {
        return cookie;
    }
}
