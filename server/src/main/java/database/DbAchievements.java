package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.sql.DataSource;

/**
 * This class is used to check if the requirements are met(or maintained) for the user achievements.
 */
public class DbAchievements {

    /**
     * A method used to check if a user had acquired a new achievement in the food category.
     * @param source the DataSource of the Database.
     * @param username the username of the user
     */
    public static void checkInsertFood(DataSource source, String username) {
        int total = countActivities(source, username, 1);
        if (total == 25) {
            if (!achievementAcquired(source, username, 1)) {
                insertAchievement(source, username, 1);
            }
        }
        if (total == 50) {
            if (!achievementAcquired(source, username, 2)) {
                insertAchievement(source, username, 2);
            }
        }
        if (total == 100) {
            if (!achievementAcquired(source, username, 3)) {
                insertAchievement(source, username, 3);
            }
        }
    }

    /**
     * A method used to check if a user had acquired a new achievement in the transport category.
     * @param source the DataSource of the Database.
     * @param username the username of the user.
     */
    public static void checkInsertTransport(DataSource source, String username) {
        int total = countActivities(source, username, 2);
        if (total == 25) {
            if (!achievementAcquired(source, username, 4)) {
                insertAchievement(source, username, 4);
            }
        }
        if (total == 50) {
            if (!achievementAcquired(source, username, 5)) {
                insertAchievement(source, username, 5);
            }
        }
        if (total == 100) {
            if (!achievementAcquired(source, username, 6)) {
                insertAchievement(source, username, 6);
            }
        }
    }

    /**
     * A method used to check if a user had acquired a new achievement in the energy category.
     * @param source the DataSource of the Database.
     * @param username the username of the user
     */
    public static void checkInsertEnergy(DataSource source, String username) {
        int total = countActivities(source, username, 3);
        if (total == 25) {
            if (!achievementAcquired(source, username, 7)) {
                insertAchievement(source, username, 7);
            }
        }
        if (total == 50) {
            if (!achievementAcquired(source, username, 8)) {
                insertAchievement(source, username, 8);
            }
        }
        if (total == 100) {
            if (!achievementAcquired(source, username, 9)) {
                insertAchievement(source, username, 9);
            }
        }
    }

    /**
     * A method used to check if a user had acquired a new achievement in the energy category.
     * @param source the DataSource of the Database.
     * @param username the username of the user
     */
    public static void checkInsertTemperature(DataSource source, String username) {
        int total = countActivities(source, username, 4);
        if (total == 25) {
            if (!achievementAcquired(source, username, 10)) {
                insertAchievement(source, username, 10);
            }
        }
        if (total == 50) {
            if (!achievementAcquired(source, username, 11)) {
                insertAchievement(source, username, 11);
            }
        }
        if (total == 100) {
            if (!achievementAcquired(source, username, 12)) {
                insertAchievement(source, username, 12);
            }
        }
    }

    /**
     * A method used to check if a user had acquired a new achievement in the other category.
     * @param source the DataSource of the Database.
     * @param username the username of the user
     */
    public static void checkInsertOther(DataSource source, String username) {
        int total = countActivities(source, username, 5);
        if (total == 25) {
            if (!achievementAcquired(source, username, 13)) {
                insertAchievement(source, username, 13);
            }
        }
        if (total == 50) {
            if (!achievementAcquired(source, username, 14)) {
                insertAchievement(source, username, 14);
            }
        }
        if (total == 100) {
            if (!achievementAcquired(source, username, 15)) {
                insertAchievement(source, username, 15);
            }
        }
    }

    /**
     * A method used to check if a user had an achievement removed in the other category.
     * @param source the DataSource of the Database.
     * @param username the username of the user
     */
    public static void checkDeleteFood(DataSource source, String username) {
        int total = countActivities(source, username, 1);
        if (total < 25) {
            deleteAchievement(source, username, 1);
        } else if (total < 50) {
            deleteAchievement(source, username, 2);
        } else if (total < 100) {
            deleteAchievement(source, username, 3);
        }
    }

    /**
     * A method used to check if a user had an achievement removed in the other category.
     * @param source the DataSource of the Database.
     * @param username the username of the user
     */
    public static void checkDeleteTransport(DataSource source, String username) {
        int total = countActivities(source, username, 2);
        if (total < 25) {
            deleteAchievement(source, username, 4);
        } else if (total < 50) {
            deleteAchievement(source, username, 5);
        } else if (total < 100) {
            deleteAchievement(source, username, 6);
        }
    }

    /**
     * A method used to check if a user had an achievement removed in the other category.
     * @param source the DataSource of the Database.
     * @param username the username of the user
     */
    public static void checkDeleteEnergy(DataSource source, String username) {
        int total = countActivities(source, username, 3);
        if (total < 25) {
            deleteAchievement(source, username, 7);
        } else if (total < 50) {
            deleteAchievement(source, username, 8);
        } else if (total < 100) {
            deleteAchievement(source, username, 9);
        }
    }

    /**
     * A method used to check if a user had an achievement removed in the other category.
     * @param source the DataSource of the Database.
     * @param username the username of the user
     */
    public static void checkDeleteTemperature(DataSource source, String username) {
        int total = countActivities(source, username, 4);
        if (total < 25) {
            deleteAchievement(source, username, 10);
        } else if (total < 50) {
            deleteAchievement(source, username, 11);
        } else if (total < 100) {
            deleteAchievement(source, username, 12);
        }
    }

    /**
     * A method used to check if a user had an achievement removed in the other category.
     * @param source the DataSource of the Database.
     * @param username the username of the user
     */
    public static void checkDeleteOther(DataSource source, String username) {
        int total = countActivities(source, username, 5);
        if (total < 25) {
            deleteAchievement(source, username, 13);
        } else if (total < 50) {
            deleteAchievement(source, username, 14);
        } else if (total < 100) {
            deleteAchievement(source, username, 15);
        }
    }


    /**
     * A method used to check if an achievement had been previously acquired by a user.
     * @param source the DataSource of the Database.
     * @param username The username of the user.
     * @param achievementId the id of the achievement.
     * @return returns true if the achievement had been previously acquired.
     */
    private static boolean achievementAcquired(DataSource source,
                                              String username, int achievementId) {
        boolean found = false;
        try {
            Connection connection = source.getConnection();
            PreparedStatement pst = connection.prepareStatement("SELECT EXISTS(SELECT * "
                    + "FROM achievement_log "
                    + "WHERE username = ? AND achievement_id = ?);");
            pst.setString(1, username);
            pst.setInt(2, achievementId);
            ResultSet result = pst.executeQuery();
            result.next();
            found = result.getBoolean(1);
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't look up the achievement");
            return false;
        }
        return found;
    }

    /**
     * A method used to count the activities of user of a certain category.
     * @param source the DataSource of the Database.
     * @param username the username of the user.
     * @param categoryId the id of the category.
     * @return returns the total number of activities done by a user in a category.
     */
    private static int countActivities(DataSource source, String username, int categoryId) {
        int count = 0;
        try {
            Connection connection = source.getConnection();
            PreparedStatement pst = connection.prepareStatement("SELECT COUNT(*) FROM activity_log "
                    + "LEFT JOIN activity ON activity.activity_id = activity_log.activity_id "
                    + "WHERE activity_log.username = ? AND activity.category_id = ?;");
            pst.setString(1, username);
            pst.setInt(2, categoryId);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                count = result.getInt(1);
            }
            result.close();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't count the activities");
            return 0;
        }
        return count;
    }

    /**
     * A method used to insert a new achievement.
     * @param source the DataSource of the Database.
     * @param username the username of the user.
     * @param achievementId the id of the achievement.
     * @return returns false if SQLException was thrown.
     */
    private static boolean insertAchievement(DataSource source,
                                             String username, int achievementId) {
        try {
            Connection connection = source.getConnection();
            PreparedStatement pst = connection.prepareStatement("INSERT INTO achievement_log "
                    + "(username, achievement_id, date, time) VALUES (?, ?, ?, ?)");

            // set variables
            pst.setString(1, username);
            pst.setInt(2, achievementId);
            ZoneId zone1 = ZoneId.of("UTC");
            LocalDate localDate = LocalDate.now(zone1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = localDate.format(formatter);
            String timeColonPattern = "HH:mm:ss";
            DateTimeFormatter timeColonFormatter = DateTimeFormatter.ofPattern(timeColonPattern);
            LocalTime localTime = LocalTime.now(zone1).truncatedTo(ChronoUnit.SECONDS);
            String formattedTime = localTime.format(timeColonFormatter);
            pst.setDate(3, java.sql.Date.valueOf(formattedDate));
            pst.setTime(4, java.sql.Time.valueOf(formattedTime));
            pst.executeUpdate();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't insert an achievement.");
            return false;
        }
        return true;
    }

    /**
     * The method deletes an achievement if the user no longer qualifies for it.
     * @param source the DataSource of the Database.
     * @param username the username of the user.
     * @param achId the id of the achievement.
     * @return true if achievement is deleted else false.
     */
    public static boolean deleteAchievement(DataSource source, String username, int achId) {
        try {
            Connection connection = source.getConnection();
            PreparedStatement pst = connection.prepareStatement("DELETE  FROM achievement_log "
                    + "WHERE username = ? AND achievement_id = ?;");

            // set variables
            pst.setString(1, username);
            pst.setInt(2, achId);
            pst.executeUpdate();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't delete an achievement.");
            return false;
        }
        return true;
    }

    /**
     * A method used to retrieve the activity id from the log id.
     * @param source the DataSource of the Database
     * @param logId the log id of the activity.
     * @return the activity id.
     */
    public static int getActivityTypeId(DataSource source, int logId) {
        int id = 0;
        try {
            Connection connection = source.getConnection();
            PreparedStatement pst = connection.prepareStatement(
                    "SELECT activity_id FROM activity_log WHERE log_id = ?;");
            pst.setInt(1, logId);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                id = result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't get the activity id.");
            return 0;
        }
        return id;
    }
}
