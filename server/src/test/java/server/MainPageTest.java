package server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MainPageTest {

    @Test
    public void StringResponseAreEqual() {
    MainPage page = new MainPage();
    assertEquals("Welcome to the main page, currently this page doesn't have "
            + "much information available at the moment but stay tuned for future updates",page.mainpage());
    }
    @Test
    public void StrinResponseIsNotEqual(){
        MainPage page = new MainPage();
        assertFalse("Why did the user try to reach the main page?".equals(page.mainpage()));
    }
}