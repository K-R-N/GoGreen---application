package network;

import controller.DataAccess;

/**
 * Demo class to showcase some functionality.
 * @author Jules van der Toorn
 */
public class Demo {

    /**
     * Main method of the demo.
     * @param args = arguments provided via terminal
     */
    public static void main(String[] args) {

        // set the timeout to 5 seconds
        NetworkManager manager = new NetworkManager(5000);

        DataAccess.login("jakob_hand", "ea8phe1Zau");

        // send image to webpage
        NetworkResponse<String> response = manager.sendImage("http://localhost:8080/setProfilePic", "../project/readme-assets/maven_menu.png");

        // print the result
        System.out.println(response.getData());
    }
}
