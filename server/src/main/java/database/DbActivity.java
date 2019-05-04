package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * This class is used that the limit for solar panels hasn't been acceded.
 * A restriction for installing insulation(can be only done once) is implemented
 * as an SQL trigger in the database.
 */
public class DbActivity {

    /**
     * A method used to check if a user exceeds the limit for solar panels.
     * @param source the DataSource of the Database.
     * @param username the username of the user.
     * @return true if the limit will be exceeded.
     */
    public static boolean solarCheck(DataSource source, String username, int typeId) {
        if (DbActivity.countSolarTotalQuantity(source, username)
                + DbActivity.retrieveActivityQuantity(source, typeId) > 10) {
            return true;
        }
        return false;
    }

    /**
     * A method used to retrieve the total amount of
     * solar panels installed by a user.
     * @param source the DataSource of the Database.
     * @param username the username of the user.
     * @return the total amount of solar panels installed by a user.
     */
    public static int countSolarTotalQuantity(DataSource source, String username) {
        int quantity = 0;
        try {
            Connection connection = source.getConnection();
            PreparedStatement pst = connection.prepareStatement(
                    "SELECT COALESCE(SUM(activity.quantity),0) AS total "
                    + "FROM activity_log JOIN activity "
                    + "ON activity_log.activity_id = activity.activity_id "
                    + "WHERE activity_log.username = ? AND (activity_log.activity_id = 20 "
                    + "OR activity_log.activity_id = 21 OR activity_log.activity_id = 22) "
                    + "GROUP BY activity_log.username;");
            pst.setString(1, username);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                quantity = result.getInt(1);
            }
            result.close();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't total quantity for the user");
            return 0;
        }
        return quantity;
    }

    /**
     * A methood used to return the quantity of an activity type.
     * @param source the DataSource of the Database.
     * @param typeId the activity type ID.
     * @return the quantity of the activity.
     */
    public static int retrieveActivityQuantity(DataSource source, int typeId) {
        int quantity = 0;
        try {
            Connection connection = source.getConnection();
            PreparedStatement pst = connection.prepareStatement(
                    "SELECT COALESCE(quantity,0) AS quantity "
                            + "FROM activity WHERE activity_id = ?;");
            pst.setInt(1, typeId);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                quantity = result.getInt("quantity");
            }
            result.close();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL: Couldn't get the quantity of the activity");
            return 0;
        }
        return quantity;
    }

}
