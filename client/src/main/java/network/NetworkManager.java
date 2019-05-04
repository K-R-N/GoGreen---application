package network;

// maven import
import controller.DataAccess;
import javafx.scene.image.Image;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.security.Key;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Class that handles URL requests.
 * @author Jules van der Toorn
 */
public class NetworkManager {
    private static SSLSocketFactory sslSocket;

    private int timeoutms;

    private final String boundary =  "*****";
    private final String crlf = "\r\n";
    private final String twoHyphens = "--";

    /**
     * Constructor of a NetworkManager object.
     * @param timeoutms specifies in ms how long a request should take before timing out
     */
    public NetworkManager(int timeoutms) {
        this.timeoutms = (timeoutms > 0) ? timeoutms : 0;
    }

    /**
     * Get the contents of a webpage in string format.
     * @param url the webpage that gets connected to
     * @return a NetworkResponse object with String data, containing information about the request
     */
    public NetworkResponse<String> getData(String url) {
        return getData(url, "");
    }

    /**
     * Get the contents of a webpage in string format.
     * @param url the webpage that gets connected to
     * @param cookie the String of the session cookie
     * @return a NetworkResponse object with String data, containing information about the request
     */
    public NetworkResponse<String> getData(String url, String cookie) {
        NetworkResponse response = new NetworkResponse<String>();

        if (url == null) {
            response.setStatus(NetworkResponse.ResponseStatus.invalid_url);
            return response;
        }

        HttpsURLConnection connection;
        try {
            // create the connection object
            URL urlObj = new URL(url);
            connection = (HttpsURLConnection) urlObj.openConnection();
            connection.setSSLSocketFactory(getSslSocketFromKey());
            connection.setRequestMethod("GET");

            // adds cookie to the request
            connection.addRequestProperty("Cookie", cookie);

            // gets the cookie from the response
            cookie = connection.getHeaderField("Set-Cookie");

            // handle the connection
            response = evaluateConnection(connection, false, null);
            // sets the cookie to the response object
            response.setCookie(cookie);
        } catch (ConnectException e) { // handle the case where the provided host is not reachable
            response.setStatus(NetworkResponse.ResponseStatus.bad_host);
        } catch (IOException | IllegalArgumentException e) {
            // handle the case where the provided url is invalid
            response.setStatus(NetworkResponse.ResponseStatus.invalid_url);
        }

        return response;
    }

    /**
     * Get the JSON contents of a webpage as a parsed object.
     * @param url the webpage that gets connected to
     * @return a NetworkResponse object with JSON data, containing information about the request
     */
    public NetworkResponse<JSONObject> getJson(String url, String cookie) {
        // call generic web request method
        NetworkResponse response = getData(url, cookie);

        if (url == null) {
            response.setStatus(NetworkResponse.ResponseStatus.invalid_url);
            return response;
        }

        // clone it into a NetworkResponse with <JSONObject> generics
        NetworkResponse jsonResponse = new NetworkResponse<JSONObject>();
        jsonResponse.setResponseCode(response.getResponseCode());
        jsonResponse.setStatus(response.getStatus());
        jsonResponse.setCookie(response.getCookie());

        if (response.wasSuccess()) {
            String responseStr = (String) response.getData();
            if (responseStr.equals("null")) {
                jsonResponse.setData("");
                jsonResponse.setStatus(NetworkResponse.ResponseStatus.no_json);
                return jsonResponse;
            }

            // try to parse the data string into a JSON object, otherwise set 'invalid json' status
            try {
                jsonResponse.setData(new JSONObject((String) response.getData()));
            } catch (JSONException e) {
                jsonResponse.setStatus(NetworkResponse.ResponseStatus.invalid_json);
            }
        }

        return jsonResponse;
    }

    /**
     * Send a JSON object to a webpage via POST.
     * @param url the webpage where the data gets sent to
     * @param data the JSON data
     * @return a NetworkResponse object with JSON data, containing information about the request
     */
    public NetworkResponse<JSONObject> sendJson(String url, JSONObject data) {
        return sendJson(url, data, "");
    }

    /**
     * Send a JSON object to a webpage via POST.
     * @param url the webpage where the data gets sent to
     * @param data the JSON data
     * @param cookie the String of the session cookie
     * @return a NetworkResponse object with JSON data, containing information about the request
     */
    @SuppressWarnings("checkstyle:IllegalCatch")
    public NetworkResponse<JSONObject> sendJson(String url, JSONObject data, String cookie) {
        NetworkResponse response = new NetworkResponse<String>();

        if (url == null) {
            response.setStatus(NetworkResponse.ResponseStatus.invalid_url);
            return response;
        }
        if (data == null) {
            response.setStatus(NetworkResponse.ResponseStatus.no_postdata);
            return response;
        }

        HttpsURLConnection connection;
        try {

            // create the connection object
            URL urlObj = new URL(url);
            connection = (HttpsURLConnection) urlObj.openConnection();
            connection.setSSLSocketFactory(getSslSocketFromKey());

            // set POST attributes for connection
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestMethod("POST");

            // adds cookie to the request
            connection.addRequestProperty("Cookie", cookie);

            // write JSON object to the POST data
            OutputStream dataBuffer = connection.getOutputStream();
            dataBuffer.write(data.toString().getBytes("UTF-8"));
            dataBuffer.close();

            // gets the cookie from the response
            cookie = connection.getHeaderField("Set-Cookie");

            // handle the connection
            response = evaluateConnection(connection, false, null);
            // sets the cookie to the response object
            response.setCookie(cookie);
        } catch (ConnectException e) { // handle the case where the provided host is not reachable
            response.setStatus(NetworkResponse.ResponseStatus.bad_host);
        } catch (IOException | RuntimeException e) {
            // handle the case where the provided url is invalid
            response.setStatus(NetworkResponse.ResponseStatus.invalid_url);
        }

        return response;
    }

    /**
     * Sends an image to a webpage via POST.
     * @param url webpage url
     * @param imagePath absolute or relative path of the image
     * @return the body of the webpage response
     */
    @SuppressWarnings("checkstyle:IllegalCatch")
    public NetworkResponse<String> sendImage(String url, String imagePath) {
        // create response object
        NetworkResponse response = new NetworkResponse<String>();

        if (url == null) {
            response.setStatus(NetworkResponse.ResponseStatus.invalid_url);
            return response;
        }
        if (imagePath == null) {
            response.setStatus(NetworkResponse.ResponseStatus.no_postdata);
            return response;
        }

        try {
            // create the connection object
            HttpsURLConnection connection = (HttpsURLConnection) (new URL(url)).openConnection();
            connection.setSSLSocketFactory(getSslSocketFromKey());

            // set POST attributes for connection
            connection.setUseCaches(false);
            connection.setDoOutput(true); // indicates POST method
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty(
                "Content-Type", "multipart/form-data;boundary=" + this.boundary
            );
            connection.addRequestProperty("Cookie", DataAccess.sessionId);

            // get output stream of connection to write file to
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

            // put file into output stream
            File file = new File(imagePath);
            String fileName = file.getName();
            outputStream.writeBytes(this.twoHyphens + this.boundary + this.crlf);
            outputStream.writeBytes(
                "Content-Disposition: form-data; name=\""
                    + "image"
                    + "\";filename=\""
                    + fileName
                    + "\"" + this.crlf
            );
            outputStream.writeBytes(this.crlf);
            byte[] bytes = Files.readAllBytes(file.toPath());
            outputStream.write(bytes);
            outputStream.writeBytes(this.crlf);
            outputStream.writeBytes(this.twoHyphens + this.boundary + this.twoHyphens + this.crlf);
            outputStream.flush();
            outputStream.close();

            // evaluate connection
            response = evaluateConnection(connection, false, null);

        } catch (NoSuchFileException e) { // handle the case where provided file doesn't exist
            System.out.println("Provided file doesn't exist!");
            response.setStatus(NetworkResponse.ResponseStatus.no_postdata);
        } catch (ConnectException e) { // handle the case where the provided host is not reachable
            response.setStatus(NetworkResponse.ResponseStatus.bad_host);
        } catch (IOException | RuntimeException e) {
            // handle the case where the provided url is invalid
            response.setStatus(NetworkResponse.ResponseStatus.invalid_url);
        }

        return response;
    }

    /**
     * Gets an JavaFX image from a webpage.
     * @param url the webpage where the image gets downloaded from
     * @return a NetworkResponse object with a JavaFX image
     */
    public NetworkResponse<Image> getImage(String url, Double width, Double height) {
        NetworkResponse response = new NetworkResponse();

        if (url == null) {
            response.setStatus(NetworkResponse.ResponseStatus.invalid_url);
            return response;
        }

        HttpsURLConnection connection;
        try {
            // create the connection object
            URL urlObj = new URL(url);
            connection = (HttpsURLConnection) urlObj.openConnection();
            connection.setSSLSocketFactory(getSslSocketFromKey());
            connection.setRequestMethod("GET");

            // handle the connection
            Dimension dimension = new Dimension();
            dimension.setSize(width == null ? 800 : width, height == null ? 600 : height);

            response = evaluateConnection(connection, true, dimension);
        } catch (IOException e) { // handle the case where the provided url is invalid
            response.setStatus(NetworkResponse.ResponseStatus.invalid_url);
        }

        return response;
    }


    /**
     * Private core method that gets adapted to for networking.
     * @param connection the connection that gets evaluated
     * @return a NetworkResponse object with any data, containing information about the request
     */
    @SuppressWarnings("checkstyle:IllegalCatch")
    private NetworkResponse<Object> evaluateConnection(HttpURLConnection connection,
            boolean imageResponse, Dimension size) {
        NetworkResponse response = new NetworkResponse<Object>();

        try {
            // set the request timeout
            if (timeoutms == 0) {
                throw new java.net.SocketTimeoutException();
            }
            connection.setConnectTimeout(timeoutms);

            // set default attributes
            connection.setInstanceFollowRedirects(false);

            // connect to the host and get the response code
            response.setResponseCode(connection.getResponseCode());

            if (response.getResponseCode() != HttpURLConnection.HTTP_OK) {
                // handle all response codes besides 200 (HTTP_OK)
                response.setStatus(NetworkResponse.ResponseStatus.bad_statuscode);

            } else { // request was successful

                if (imageResponse) {
                    // get raw input stream
                    InputStream input = connection.getInputStream();

                    if (input == null) {
                        response.setStatus(NetworkResponse.ResponseStatus.no_data);
                    } else {
                        response.setStatus(NetworkResponse.ResponseStatus.success);

                        // create JavaFX image from input stream
                        response.setData(
                                new Image(input, size.getWidth(), size.getHeight(),true, true)
                        );
                    }
                } else {
                    response.setStatus(NetworkResponse.ResponseStatus.success);

                    // create a string builder to build the body contents with
                    StringBuilder body = new StringBuilder();

                    // create a reader from the input stream so we can loop through all the lines
                    InputStreamReader streamReader = new InputStreamReader(
                            connection.getInputStream()
                    );
                    BufferedReader bufferedReader = new BufferedReader(streamReader);

                    // build a string containing the full body
                    String line;
                    body.append(bufferedReader.readLine());
                    while ((line = bufferedReader.readLine()) != null) {
                        body.append("\n" + line);
                    }
                    bufferedReader.close();

                    // write the body to the response object
                    response.setData(body.toString());
                }
            }
        } catch (java.net.SocketTimeoutException e) { // handle request timeout
            response.setStatus(NetworkResponse.ResponseStatus.timed_out);
        } catch (IOException e) {
            // handle the case where the connection couldn't be completed

            if (!hasInternet()) { // handle the case where there is no internet
                response.setStatus(NetworkResponse.ResponseStatus.no_internet);
            } else { // handle the case where the host couldn't be reached (e.g. it doesn't exist)
                response.setStatus(NetworkResponse.ResponseStatus.bad_host);
            }
        } catch (RuntimeException e) { // handle the case where the url has invalid arguments
            response.setStatus(NetworkResponse.ResponseStatus.invalid_url);
        } finally {
            connection.disconnect();
        }

        return response;
    }

    /**
     * Method that checks if internet is available by trying to reach Google.
     * @return boolean indicating whether internet is available
     */
    public boolean hasInternet() {
        try {
            // open the connection
            URL urlObj = new URL("https://www.google.com");
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

            // set the request timeout
            connection.setConnectTimeout(timeoutms);

            // try to connect to the host
            connection.connect();

        } catch (IOException e) { // handle failed request (no internet)
            return false;
        }

        return true;
    }

    /**
     * gets the certificate from the file and creates a ssl socket.
     */
    public static void setSslSocketFromKey() {

        try {

            KeyStore ks = KeyStore.getInstance("JKS");
            InputStream readStream = new FileInputStream("localhost.jks");
            ks.load(readStream, "localhost".toCharArray());
            Key key = ks.getKey("localhost", "localhost".toCharArray());
            readStream.close();

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(ks);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            sslSocket = sslSocketFactory;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * returns the sslkey that has been retrieved with setSslSockerFromKey.
     * @return sslsocket.
     */
    private SSLSocketFactory getSslSocketFromKey() {
        return sslSocket;
    }
}
