package server;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


public class Security {
    /**Hashing a password before putting it on the database.
     *
     * @param rawpassword = the raw password provided by the user during registration.
     * @return = returns the encoded password.
     */
    public String passwordhashing(String rawpassword) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(rawpassword);
    }

    /** Password gets confirmed if it matches the hashed password.
     *
     * @param userpassword = the password provided by user during login.
     * @param hashedpassword = the password fetched from the database and compared.
     * @return = true if password matches the hashed password, else false.
     */
    public Boolean passwordconfirmation(String userpassword, String hashedpassword) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(userpassword,hashedpassword);
    }

}
