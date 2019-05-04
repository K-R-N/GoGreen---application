package server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Class that manages the homepage of the web server.
 */
@Controller
public class MainPage {

    /**
     * The homepage of the web server.
     * @return string with information about the page
     */
    @ResponseBody
    @RequestMapping("/")
    public String mainpage() {

        return "Welcome to the main page, currently this page doesn't have "
                + "much information available at the moment but stay tuned for future updates";
    }
}
