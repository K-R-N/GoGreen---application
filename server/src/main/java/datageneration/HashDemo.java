package datageneration;

/**
 * This class is used for generating the hashed password for the database.
 * It has nothing to do with the application and
 * it has been commented out to exclude it from the coverage report.
 * It can uncommented to generate the hashed password needed
 * for the database and commented out later.
 */

/*import server.Security;

import java.util.Scanner;*/

public class HashDemo {

    /**
     * Simple demo that calculates hash for a plaintext password.
     * @param args command line arguments
     *//*
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter plaintext password: ");
        while (sc.hasNextLine()) {
            String plaintext = sc.nextLine().trim();

            Security sec = new Security();
            String hash = sec.passwordhashing(plaintext);
            System.out.println("Hashed password: " + hash);

            System.out.print("\nEnter plaintext password: ");
        }
    }*/
}
