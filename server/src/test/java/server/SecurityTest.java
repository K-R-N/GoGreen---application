package server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecurityTest {

    @Test
    void passwordhashingmatches() {
        String password = "123";

        Security sec = new Security();
        String hashed = sec.passwordhashing(password);
        assertTrue(sec.passwordconfirmation(password,hashed));
    }
    @Test
    public void passwordincorrect(){
        String password1 = "123";
        String password2 = "132";
        Security sec = new Security();
        String hashedpassword1 = sec.passwordhashing(password1);
        assertFalse(sec.passwordconfirmation(password2,hashedpassword1));
    }

    @Test
    void hashedpasswordisdifferent() {
        String password = "21341";
        Security sec = new Security();
        assertNotEquals(password.length(),sec.passwordhashing(password).length());
    }

}