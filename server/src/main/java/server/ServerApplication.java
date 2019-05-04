package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Class that starts up the server.
 */
@SpringBootApplication
public class ServerApplication {

    /**
     * Entry point of web server.
     * @param args SpringBoot arguments
     */
    public static void main(String[] args) {
        // SpringApplication from the SpringBoot library is called to start the server.
        SpringApplication.run(ServerApplication.class, args);
    }
}