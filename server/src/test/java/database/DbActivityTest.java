package database;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class DbActivityTest {

    DbAccess db;

    @BeforeEach
    public void setUp() {
        db = new DbAccess(DbRetrieve.retrieveTestDb());
    }
    @Test
    public void testCheckSolarTrue() throws SQLException {
        db.getDataSource().getConnection().prepareStatement("INSERT INTO "
                + "activity_log(username, activity_id) VALUES "
                + "('testsolar', 22);").executeUpdate();
        int before = db.countRows("activity_log");
        boolean result = db.insertUserActivity("testsolar", 22);
        int after = db.countRows("activity_log");
        db.getDataSource().getConnection().prepareStatement("DELETE FROM "
                        + "activity_log WHERE username = 'testsolar';").executeUpdate();
        assertEquals(before, after);
        assertFalse(result);
    }

    @Test
    public void testCheckSolarFalse() throws SQLException {
        db.getDataSource().getConnection().prepareStatement("INSERT INTO "
                + "activity_log(username, activity_id) VALUES "
                + "('testsolar', 22);").executeUpdate();
        int before = db.countRows("activity_log");
        boolean result = db.insertUserActivity("testsolar", 20);
        int after = db.countRows("activity_log");
        db.getDataSource().getConnection().prepareStatement("DELETE FROM "
                + "activity_log WHERE username = 'testsolar';").executeUpdate();
        assertEquals(before, after-1);
        assertTrue(result);
    }

    @Test
    public void testCountSolarTotalQuantity() throws SQLException {
        Connection connection = db.getDataSource().getConnection();
        connection.prepareStatement("INSERT INTO "
                + "activity_log(username, activity_id) VALUES "
                + "('testsolar', 22);").executeUpdate();
        connection.prepareStatement("INSERT INTO "
                + "activity_log(username, activity_id) VALUES "
                + "('testsolar', 20);").executeUpdate();
        int total = DbActivity.countSolarTotalQuantity(db.getDataSource(), "testsolar");
        db.getDataSource().getConnection().prepareStatement("DELETE FROM "
                + "activity_log WHERE username = 'testsolar';").executeUpdate();
        assertEquals(8, total);
    }

    @Test
    public void testRetrieveActivityQuantity (){
        int quantity = DbActivity.retrieveActivityQuantity(db.getDataSource(), 22);
        assertEquals(6, quantity);
    }
}
