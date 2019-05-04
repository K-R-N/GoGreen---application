package server;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;

public class ServerApplicationTest {

    @Test
    public void contextLoads() {
        String[] params = {};
        ServerApplication.main(params);

        // assert that program doesn't crash or enters an endless loop

        assertTrue(true);
    }
}
