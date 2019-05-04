package network;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DemoTest {

    @Test
    public void testMain() {
        String[] input = {"https://www.magistat.nl"};
        Demo.main(input);

        // assert that program doesn't crash or enters an endless loop

        assertTrue(true);
    }

    @Test
    public void testMain_no_url() {
        String[] input = {};
        Demo.main(input);

        // assert that program doesn't crash or enters an endless loop

        assertTrue(true);
    }

    @Test
    public void testMain_fail() {
        String[] input = {"invalid url"};
        Demo.main(input);

        // assert that program doesn't crash or enters an endless loop

        assertTrue(true);
    }

    @Test
    public void testMainJson() {
        String[] input = {"http://httpbin.org/post"};

        Demo.main(input);

        // assert that program doesn't crash or enters an endless loop

        assertTrue(true);
    }
}
