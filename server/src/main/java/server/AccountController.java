package server;

import database.DbAccess;
import database.DbRetrieve;
import models.JsonBoolean;
import models.User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 * Class that manipulates the endpoint for account actions.
 */
@RestController
public class AccountController {

    private static DataSource source;

    /**
     * setup the default database.
     */
    @PostConstruct
    public void setSourcePG() {

        this.source = DbRetrieve.retrievePgDb();
    }

    /**
     * setup SqlLite database for CI.
     */
    public static void setSourceSql( DataSource sourcesql) {
        source = sourcesql;
    }

    /**
     * Endpoint for post methods to get authenticated and logged in.
     * @param session the session of the request to identify the serverUser
     * @return boolean if login was a success
     */
    @PostMapping(value = "login")
    public JsonBoolean loginUser(@RequestBody Map<String, String> credentials,
                              HttpSession session) {
        Security sec = new Security();
        DbAccess db = new DbAccess(source);
        String passwordHash = db.getPasswordHash(credentials.get("username"));
        if (passwordHash == null) {
            System.out.println("FAIL: Password hash was null!");
            return new JsonBoolean(false);
        }
        if (sec.passwordconfirmation(credentials.get("password"), passwordHash)) {
            session.setAttribute("username", credentials.get("username"));
            return new JsonBoolean(true);
        }
        System.out.println("FAIL: Incorrect password!");
        return new JsonBoolean(false);
    }

    /**
     * Endpoint to remove authentication of a session.
     * @param session the session of the request to identify the user
     */
    @RequestMapping(value = "logout")
    public JsonBoolean logoutUser(HttpSession session) {
        if (session.getAttribute("username") == null) {
            return new JsonBoolean(false);
        }

        session.invalidate();

        return new JsonBoolean(true);
    }


    /**
     * Method that creates an account.
     * @param user the user object with everything needed for an account
     * @return true if account creating was succesfull, false otherwise.
     *      (username is allready taken)
     */
    @PostMapping(value = "createAccount")
    public JsonBoolean createAccount(@RequestBody User user) {
        /**
         * implement the method of connecting DbAccess and create an user account into database.
         */
        Security sec = new Security();
        DbAccess db = new DbAccess(source);
        user.setPassword(sec.passwordhashing(user.getPassword()));
        boolean res = db.createUser(user.getUsername().toLowerCase(),user.getPassword(),
                user.getFirstname(),user.getEmailadress());
        return new JsonBoolean(res);
    }

    /**
     * method that deleted the account and everything related to it of the user in the session.
     * @param session to get the username of which account to delete.
     * @return true if succesfull, false otherwise(not logged in).
     */
    @RequestMapping(value = "deleteAccount")
    public JsonBoolean deleteAccount(HttpSession session) {

        if (session.getAttribute("username") == null) {
            return new JsonBoolean(false);
        }

        DbAccess db = new DbAccess(source);
        db.deleteUser((String)session.getAttribute("username"));
        session.invalidate();
        return new JsonBoolean(true);
    }

    /**
     * method that changes the pasword of the logged in user.
     * @param password the new password,
     *                 this needs to be hashed and then replace the old password in the database.
     * @param session to indentify the user.
     * @return true if succesfull, false otherwise(not logged in).
     */
    @PostMapping(value = "changePassword")
    public JsonBoolean changePassword(@RequestBody User password, HttpSession session) {

        if (session.getAttribute("username") == null) {
            return new JsonBoolean(false);
        }

        DbAccess db = new DbAccess(source);
        String username = (String)session.getAttribute("username");
        String oldpassword = db.getPasswordHash(username);
        Security sec = new Security();
        if (sec.passwordconfirmation(password.getPassword(),oldpassword)) {
            return new JsonBoolean(false);
        }
        String newpassword = sec.passwordhashing(password.getPassword());

        db.changePassword((String)session.getAttribute("username"),newpassword);

        return new JsonBoolean(true);
    }

    /**
     * method that changes the pasword of the logged in user.
     * @param email the new emailaddress, this replaces the old emailaddress in the database.
     * @param session to indentify the user.
     * @return true if succesfull, false otherwise(not logged in).
     */
    @PostMapping(value = "changeEmail")
    public JsonBoolean changeEmail(@RequestBody User email, HttpSession session) {

        if (session.getAttribute("username") == null) {
            return new JsonBoolean(false);
        }

        DbAccess db = new DbAccess(source);
        String username = (String)session.getAttribute("username");
        String oldemail = db.getUser(username,true).getEmailadress();

        if (email.getEmailadress().equals(oldemail)) {
            System.out.println("Please change to a new emailaddress!");
            return new JsonBoolean(false);
        }
        db.changeEmail((String)session.getAttribute("username"),email.getEmailadress());

        return new JsonBoolean(true);
    }

    /**
     * method that changes the pasword of the logged in user.
     * @param name the new password, this replaces the old firstname in the database.
     * @param session to indentify the user.
     * @return true if succesfull, false otherwise(not logged in).
     */
    @PostMapping(value = "changeFirstname")
    public JsonBoolean changeFirstname(@RequestBody User name, HttpSession session) {

        if (session.getAttribute("username") == null) {
            return new JsonBoolean(false);
        }

        DbAccess db = new DbAccess(source);
        String username = (String)session.getAttribute("username");
        String oldfirstname = db.getUser(username,true).getFirstname();

        if (oldfirstname.equals(name.getFirstname())) {
            System.out.println("Please change to a new firstname!");
            return new JsonBoolean(false);
        }
        db.changeFirstName((String)session.getAttribute("username"),name.getFirstname());

        return new JsonBoolean(true);

    }

    /**
     * method that sets the profile picture of the user.
     * @param image the image to be used as an profile picture.
     * @param session to identify the user.
     * @return json boolean to let the userknow if setting the image was succesfull.
     * @throws IOException if something goes wrong.
     */
    @PostMapping(value = "/setProfilePic")
    public JsonBoolean setProfilePic(@RequestParam("image")MultipartFile image,
                                     HttpSession session) throws IOException {
        if (session.getAttribute("username") == null) {
            return new JsonBoolean(false);
        }

        String username = (String) session.getAttribute("username");

        byte[] bits = image.getBytes();
        DbAccess db = new DbAccess(source);

        return new JsonBoolean(db.setProfilePicture(username, bits));
    }

    /**
     * Mehod to let the user change their profile settings from public to private.
     * @param session the session of the user that the privacy changes will affect.
     * @return returns a JsonBoolean object to confirm if the change has been successful or not.
     */
    @GetMapping(value = "/changePrivacy")
    public JsonBoolean changeProfilePrivacySettings(HttpSession session) {
        if (session.getAttribute("username") == null) {
            return new JsonBoolean(false);
        }
        String username = (String) session.getAttribute("username");
        DbAccess db = new DbAccess(source);
        User user = db.getUser(username, true);

        JsonBoolean output = new JsonBoolean();
        if (user.getPrivacy().contains("public")) {
            output = new JsonBoolean(db.changePrivacySettings(username,"private"));
        } else {
            output = new JsonBoolean(db.changePrivacySettings(username,"public"));
        }
        return output;
    }

    /**
     * method to check if the session is logged in.
     * @param session to check if there is an attribute or not.
     * @return an object of a boolean.
     */
    @GetMapping(value = "/amIloggedIn")
    public JsonBoolean amIloggedIn(HttpSession session) {

        return new JsonBoolean( session.getAttribute("username") != null);

    }

}
