package server;

import org.junit.Test;

import static org.junit.Assert.*;

public class ServerUserRegistrationDataTest {

    @Test
    public void MatchingFirstname() {
        UserRegistrationData user = new UserRegistrationData();
        user.setFirstname("John");
        assertEquals("John",user.getFirstname());
    }

    @Test
    public void NotMatchingFirstname() {
        UserRegistrationData user = new UserRegistrationData();
        user.setFirstname("John");
        assertNotEquals("Bob",user.getFirstname());
    }

    @Test
    public void MatchingLastname() {
        UserRegistrationData user = new UserRegistrationData();
        user.setLastname("Smith");
       assertEquals("Smith", user.getLastname());
    }

    @Test
    public void NotMatchingLastname() {
        UserRegistrationData user = new UserRegistrationData();
        user.setLastname("Smith");
        assertNotEquals("Maximus", user.getLastname());
    }

    @Test
    public void MatchingPassword() {
        UserRegistrationData user = new UserRegistrationData();
        user.setPassword("ilikeoop");
        assertEquals("ilikeoop", user.getPassword());
    }

    @Test
    public void NotMatchingPassword() {
        UserRegistrationData user = new UserRegistrationData();
        user.setPassword("ilikeoop");
        assertNotEquals("idontlikeoop", user.getPassword());
    }

    @Test
    public void MatchingEmail() {
        UserRegistrationData user = new UserRegistrationData();
        user.setEmail("john@gmail.com");
        assertEquals("john@gmail.com",user.getEmail());
    }

    @Test
    public void NotMatchingEmail() {
        UserRegistrationData user = new UserRegistrationData();
        user.setEmail("josh@gmail.com");
        assertNotEquals("john@gmail.com",user.getEmail());
    }

    @Test
    public void MatchingCountry() {
        UserRegistrationData user = new UserRegistrationData();
        user.setCountry("Netherlands");
        assertEquals("Netherlands",user.getCountry());
    }

    @Test
    public void NotMatchingCountry() {
        UserRegistrationData user = new UserRegistrationData();
        user.setCountry("Netherlands");
        assertNotEquals("Spain", user.getCountry());
    }
}