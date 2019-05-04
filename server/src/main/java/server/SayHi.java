package server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Class that manages the endpoint that returns a simple greeting message.
 */
@Controller
public class SayHi {

    /**
     * Returns a simple greeting message.
     */
    @ResponseBody
    @RequestMapping("/sayhi")
    public static String sayhi() {
        return "Hello there!";
    }
}
