package datageneration;

/**
 * This class is used for data generation for the database.
 * It has nothing to do with the application and
 * it has been commented out to exclude it from the coverage report.
 * It can uncommented to generate the data needed
 * for the database and commented out later.
 */

/*import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;*/

public class ContributionGenerator {

    /*private static LocalDate prevDate = LocalDate.parse("2019-03-31");

    private static String[] users = {
        "jakob_hand",
        "secern",
        "darius",
        "pandacrazybr",
        "wout1999",
        "durnity",
        "encted"
    };

    private static int activityCount = 22;
    private static int activityIndex = 1;

    private static int achievementCount = 15;
    private static int achievementIndex = 1;

    *//**
     * Generates list of mock data.
     * @param args command line arguments
     *//*
    public static void main(String[] args) {

        int generateLines = 200;

        List<String> activityList = new ArrayList();
        List<String> achievementList = new ArrayList<>();

        for (int i = 0; i < generateLines; i++) {
            int randInt = ThreadLocalRandom.current().nextInt(1, 5);
            if (randInt == 4) {
                achievementList.add(getRandomAchievementLine());
            } else {
                activityList.add(getRandomActivityLine());
            }
        }

        System.out.println("----------- ACHIEVEMENTS -----------");
        for (String line: achievementList) {
            System.out.println(line);
        }
        System.out.println("-------- ENDOF ACHIEVEMENTS --------");

        System.out.println("\n\n\n");

        System.out.println("----------- ACTIVITIES -----------");
        for (String line: activityList) {
            System.out.println(line);
        }
        System.out.println("-------- ENDOF ACTIVITIES --------");
    }

    *//**
     * Returns SQL string for parameters.
     * @return string containing SQL
     *//*
    public static String getRandomActivityLine() {

        // choose random name
        String randUsername = users[ThreadLocalRandom.current().nextInt(0, users.length)];

        // choose random activity id
        int randActivityId = ThreadLocalRandom.current().nextInt(1, activityCount + 1);

        // add random amount of days to previous date
        prevDate = prevDate.minusDays(ThreadLocalRandom.current().nextInt(1, 4));
        String randDate = prevDate.toString();

        // generate random time
        String randStrTime = ThreadLocalRandom.current().nextInt(0, 24) + ":"
                + ThreadLocalRandom.current().nextInt(0, 60) + ":"
                + ThreadLocalRandom.current().nextInt(0, 60);

        return "INSERT INTO public.activity_log (username, activity_id, date, \"time\") "
                + "VALUES ('" + randUsername + "', " + randActivityId + ", '"
                + randDate + "', '" + randStrTime + "');";
    }

    *//**
     * Returns SQL string for parameters.
     * @return string containing SQL
     *//*
    public static String getRandomAchievementLine() {

        // choose random name
        String randUsername = users[ThreadLocalRandom.current().nextInt(0, users.length)];

        // choose random activity id
        int randAchievementId = ThreadLocalRandom.current().nextInt(1, achievementCount + 1);

        // add random amount of days to previous date
        prevDate = prevDate.minusDays(ThreadLocalRandom.current().nextInt(1, 4));
        String randDate = prevDate.toString();

        // generate random time
        String randStrTime = ThreadLocalRandom.current().nextInt(0, 24) + ":"
                + ThreadLocalRandom.current().nextInt(0, 60) + ":"
                + ThreadLocalRandom.current().nextInt(0, 60);

        return "INSERT INTO public.achievement_log (username, achievement_id, "
                + "date, \"time\") "
                + "VALUES ('" + randUsername
                + "', " + randAchievementId + ", '"
                + randDate + "', '" + randStrTime + "');";
    }*/
}