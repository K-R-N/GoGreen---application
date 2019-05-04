package database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DbAchievementsTest {
    private DbAccess db;

    @BeforeEach
    public void setUp() {
        db = new DbAccess(DbRetrieve.retrieveTestDb());
    }

    @Test
    public void testCheckInsertFood() throws SQLException {
        Connection con = db.getDataSource().getConnection();
        for(int i = 0; i < 25; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachfood', 1);").executeUpdate();
        }
        int before25 = db.countRows("achievement_log");
        DbAchievements.checkInsertFood(db.getDataSource(), "testachfood");
        int after25 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachfood';").executeUpdate();
        assertEquals(before25, after25-1);
        for(int i = 0; i < 50; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachfood', 1);").executeUpdate();
        }
        int before50 = db.countRows("achievement_log");
        DbAchievements.checkInsertFood(db.getDataSource(), "testachfood");
        int after50 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachfood';").executeUpdate();
        assertEquals(before50, after50-1);
        for(int i = 0; i < 100; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachfood', 1);").executeUpdate();
        }
        int before100 = db.countRows("achievement_log");
        DbAchievements.checkInsertFood(db.getDataSource(), "testachfood");
        int after100 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachfood';").executeUpdate();
        con.prepareStatement("DELETE FROM achievement_log WHERE username = 'testachfood';").executeUpdate();
        assertEquals(before100, after100-1);
    }

    @Test
    public void testCheckInsertTransport() throws SQLException {
        Connection con = db.getDataSource().getConnection();
        for(int i = 0; i < 25; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachtransport', 3);").executeUpdate();
        }
        int before25 = db.countRows("achievement_log");
        DbAchievements.checkInsertTransport(db.getDataSource(), "testachtransport");
        int after25 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachtransport';").executeUpdate();
        assertEquals(before25, after25-1);
        for(int i = 0; i < 50; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachtransport', 3);").executeUpdate();
        }
        int before50 = db.countRows("achievement_log");
        DbAchievements.checkInsertTransport(db.getDataSource(), "testachtransport");
        int after50 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachtransport';").executeUpdate();
        assertEquals(before50, after50-1);
        for(int i = 0; i < 100; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachtransport', 3);").executeUpdate();
        }
        int before100 = db.countRows("achievement_log");
        DbAchievements.checkInsertTransport(db.getDataSource(), "testachtransport");
        int after100 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachtransport';").executeUpdate();
        con.prepareStatement("DELETE FROM achievement_log WHERE username = 'testachtransport';").executeUpdate();
        assertEquals(before100, after100-1);
    }

    @Test
    public void testCheckInsertEnergy() throws SQLException {
        Connection con = db.getDataSource().getConnection();
        for(int i = 0; i < 25; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachenergy', 18);").executeUpdate();
        }
        int before25 = db.countRows("achievement_log");
        DbAchievements.checkInsertEnergy(db.getDataSource(), "testachenergy");
        int after25 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachenergy';").executeUpdate();
        assertEquals(before25, after25-1);
        for(int i = 0; i < 50; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachenergy', 18);").executeUpdate();
        }
        int before50 = db.countRows("achievement_log");
        DbAchievements.checkInsertEnergy(db.getDataSource(), "testachenergy");
        int after50 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachenergy';").executeUpdate();
        assertEquals(before50, after50-1);
        for(int i = 0; i < 100; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachenergy', 18);").executeUpdate();
        }
        int before100 = db.countRows("achievement_log");
        DbAchievements.checkInsertEnergy(db.getDataSource(), "testachenergy");
        int after100 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachenergy';").executeUpdate();
        con.prepareStatement("DELETE FROM achievement_log WHERE username = 'testachenergy';").executeUpdate();
        assertEquals(before100, after100-1);
    }

    @Test
    public void testCheckInsertTemperature() throws SQLException {
        Connection con = db.getDataSource().getConnection();
        for(int i = 0; i < 25; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachtemp', 15);").executeUpdate();
        }
        int before25 = db.countRows("achievement_log");
        DbAchievements.checkInsertTemperature(db.getDataSource(), "testachtemp");
        int after25 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachtemp';").executeUpdate();
        assertEquals(before25, after25-1);
        for(int i = 0; i < 50; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachtemp', 15);").executeUpdate();
        }
        int before50 = db.countRows("achievement_log");
        DbAchievements.checkInsertTemperature(db.getDataSource(), "testachtemp");
        int after50 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachtemp';").executeUpdate();
        assertEquals(before50, after50-1);
        for(int i = 0; i < 100; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachtemp', 15);").executeUpdate();
        }
        int before100 = db.countRows("achievement_log");
        DbAchievements.checkInsertTemperature(db.getDataSource(), "testachtemp");
        int after100 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachtemp';").executeUpdate();
        con.prepareStatement("DELETE FROM achievement_log WHERE username = 'testachtemp';").executeUpdate();
        assertEquals(before100, after100-1);
    }

    @Test
    public void testCheckInsertOther() throws SQLException {
        Connection con = db.getDataSource().getConnection();
        for(int i = 0; i < 25; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachother', 23);").executeUpdate();
        }
        int before25 = db.countRows("achievement_log");
        DbAchievements.checkInsertOther(db.getDataSource(), "testachother");
        int after25 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachother';").executeUpdate();
        assertEquals(before25, after25-1);
        for(int i = 0; i < 50; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachother', 23);").executeUpdate();
        }
        int before50 = db.countRows("achievement_log");
        DbAchievements.checkInsertOther(db.getDataSource(), "testachother");
        int after50 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachother';").executeUpdate();
        assertEquals(before50, after50-1);
        for(int i = 0; i < 100; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachother', 23);").executeUpdate();
        }
        int before100 = db.countRows("achievement_log");
        DbAchievements.checkInsertOther(db.getDataSource(), "testachother");
        int after100 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachother';").executeUpdate();
        con.prepareStatement("DELETE FROM achievement_log WHERE username = 'testachother';").executeUpdate();
        assertEquals(before100, after100-1);
    }

    @Test
    public void testAcquiredCheckInsertFood() throws SQLException {
        Connection con = db.getDataSource().getConnection();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachfood', 1);").executeUpdate();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachfood', 2);").executeUpdate();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachfood', 3);").executeUpdate();
        for(int i = 0; i < 25; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachfood', 1);").executeUpdate();
        }
        int before25 = db.countRows("achievement_log");
        DbAchievements.checkInsertFood(db.getDataSource(), "testachfood");
        int after25 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachfood';").executeUpdate();
        assertEquals(before25, after25);
        for(int i = 0; i < 50; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachfood', 1);").executeUpdate();
        }
        int before50 = db.countRows("achievement_log");
        DbAchievements.checkInsertFood(db.getDataSource(), "testachfood");
        int after50 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachfood';").executeUpdate();
        assertEquals(before50, after50);
        for(int i = 0; i < 100; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachfood', 1);").executeUpdate();
        }
        int before100 = db.countRows("achievement_log");
        DbAchievements.checkInsertFood(db.getDataSource(), "testachfood");
        int after100 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachfood';").executeUpdate();
        con.prepareStatement("DELETE FROM achievement_log WHERE username = 'testachfood';").executeUpdate();
        con.prepareStatement("DELETE FROM achievement_log where username = 'testachfood';").executeUpdate();
        assertEquals(before100, after100);
    }

    @Test
    public void testAcquiredCheckInsertTransport() throws SQLException {
        Connection con = db.getDataSource().getConnection();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachtransport', 4);").executeUpdate();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachtransport', 5);").executeUpdate();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachtransport', 6);").executeUpdate();
        for(int i = 0; i < 25; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachtransport', 3);").executeUpdate();
        }
        int before25 = db.countRows("achievement_log");
        DbAchievements.checkInsertTransport(db.getDataSource(), "testachtransport");
        int after25 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachtransport';").executeUpdate();
        assertEquals(before25, after25);
        for(int i = 0; i < 50; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachtransport', 3);").executeUpdate();
        }
        int before50 = db.countRows("achievement_log");
        DbAchievements.checkInsertTransport(db.getDataSource(), "testachtransport");
        int after50 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachtransport';").executeUpdate();
        assertEquals(before50, after50);
        for(int i = 0; i < 100; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachtransport', 3);").executeUpdate();
        }
        int before100 = db.countRows("achievement_log");
        DbAchievements.checkInsertTransport(db.getDataSource(), "testachtransport");
        int after100 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachtransport';").executeUpdate();
        con.prepareStatement("DELETE FROM achievement_log WHERE username = 'testachtransport';").executeUpdate();
        con.prepareStatement("DELETE FROM achievement_log where username = 'testachtransport';").executeUpdate();
        assertEquals(before100, after100);
    }

    @Test
    public void testAcquiredCheckInsertEnergy() throws SQLException {
        Connection con = db.getDataSource().getConnection();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachenergy', 7);").executeUpdate();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachenergy', 8);").executeUpdate();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachenergy', 9);").executeUpdate();
        for(int i = 0; i < 25; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachenergy', 18);").executeUpdate();
        }
        int before25 = db.countRows("achievement_log");
        DbAchievements.checkInsertEnergy(db.getDataSource(), "testachenergy");
        int after25 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachenergy';").executeUpdate();
        assertEquals(before25, after25);
        for(int i = 0; i < 50; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachenergy', 18);").executeUpdate();
        }
        int before50 = db.countRows("achievement_log");
        DbAchievements.checkInsertEnergy(db.getDataSource(), "testachenergy");
        int after50 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachenergy';").executeUpdate();
        assertEquals(before50, after50);
        for(int i = 0; i < 100; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachenergy', 18);").executeUpdate();
        }
        int before100 = db.countRows("achievement_log");
        DbAchievements.checkInsertEnergy(db.getDataSource(), "testachenergy");
        int after100 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachenergy';").executeUpdate();
        con.prepareStatement("DELETE FROM achievement_log WHERE username = 'testachenergy';").executeUpdate();
        con.prepareStatement("DELETE FROM achievement_log where username = 'testachenergy';").executeUpdate();
        assertEquals(before100, after100);
    }

    @Test
    public void testAcquiredCheckInsertTemperature() throws SQLException{
        Connection con = db.getDataSource().getConnection();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachtemp', 10);").executeUpdate();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachtemp', 11);").executeUpdate();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachtemp', 12);").executeUpdate();
        for(int i = 0; i < 25; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachtemp', 15);").executeUpdate();
        }
        int before25 = db.countRows("achievement_log");
        DbAchievements.checkInsertTemperature(db.getDataSource(), "testachtemp");
        int after25 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachtemp';").executeUpdate();
        assertEquals(before25, after25);
        for(int i = 0; i < 50; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachtemp', 15);").executeUpdate();
        }
        int before50 = db.countRows("achievement_log");
        DbAchievements.checkInsertTemperature(db.getDataSource(), "testachtemp");
        int after50 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachtemp';").executeUpdate();
        assertEquals(before50, after50);
        for(int i = 0; i < 100; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachtemp', 15);").executeUpdate();
        }
        int before100 = db.countRows("achievement_log");
        DbAchievements.checkInsertTemperature(db.getDataSource(), "testachtemp");
        int after100 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachtemp';").executeUpdate();
        con.prepareStatement("DELETE FROM achievement_log WHERE username = 'testachtemp';").executeUpdate();
        assertEquals(before100, after100);
    }

    @Test
    public void testAcquiredCheckInsertOther() throws SQLException {
        Connection con = db.getDataSource().getConnection();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachother', 13);").executeUpdate();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachother', 14);").executeUpdate();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachother', 15);").executeUpdate();
        for(int i = 0; i < 25; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachother', 23);").executeUpdate();
        }
        int before25 = db.countRows("achievement_log");
        DbAchievements.checkInsertOther(db.getDataSource(), "testachother");
        int after25 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachother';").executeUpdate();
        assertEquals(before25, after25);
        for(int i = 0; i < 50; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachother', 23);").executeUpdate();
        }
        int before50 = db.countRows("achievement_log");
        DbAchievements.checkInsertOther(db.getDataSource(), "testachother");
        int after50 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachother';").executeUpdate();
        assertEquals(before50, after50);
        for(int i = 0; i < 100; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachother', 23);").executeUpdate();
        }
        int before100 = db.countRows("achievement_log");
        DbAchievements.checkInsertOther(db.getDataSource(), "testachother");
        int after100 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachother';").executeUpdate();
        con.prepareStatement("DELETE FROM achievement_log WHERE username = 'testachother';").executeUpdate();
        assertEquals(before100, after100);
    }

    @Test
    public void testCheckDeleteFood() throws SQLException {
        Connection con = db.getDataSource().getConnection();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachfood', 1);").executeUpdate();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachfood', 2);").executeUpdate();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachfood', 3);").executeUpdate();
        for(int i = 0; i < 5; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachfood', 1);").executeUpdate();
        }
        int before25 = db.countRows("achievement_log");
        DbAchievements.checkDeleteFood(db.getDataSource(), "testachfood");
        int after25 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachfood';").executeUpdate();
        assertEquals(before25, after25+1);
        for(int i = 0; i < 25; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachfood', 1);").executeUpdate();
        }
        int before50 = db.countRows("achievement_log");
        DbAchievements.checkDeleteFood(db.getDataSource(), "testachfood");
        int after50 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachfood';").executeUpdate();
        assertEquals(before50, after50+1);
        for(int i = 0; i < 75; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachfood', 1);").executeUpdate();
        }
        int before100 = db.countRows("achievement_log");
        DbAchievements.checkDeleteFood(db.getDataSource(), "testachfood");
        int after100 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachfood';").executeUpdate();
        assertEquals(before100, after100+1);
        for(int i = 0; i < 120; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachfood', 1);").executeUpdate();
        }
        int before101 = db.countRows("achievement_log");
        DbAchievements.checkDeleteFood(db.getDataSource(), "testachfood");
        int after101 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachfood';").executeUpdate();
        con.prepareStatement("DELETE FROM achievement_log WHERE username = 'testachfood';").executeUpdate();
        assertEquals(before101, after101);
    }

    @Test
    public void testCheckDeleteTransport() throws SQLException {
        Connection con = db.getDataSource().getConnection();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachtransport', 4);").executeUpdate();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachtransport', 5);").executeUpdate();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachtransport', 6);").executeUpdate();
        for(int i = 0; i < 5; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachtransport', 3);").executeUpdate();
        }
        int before25 = db.countRows("achievement_log");
        DbAchievements.checkDeleteTransport(db.getDataSource(), "testachtransport");
        int after25 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachtransport';").executeUpdate();
        assertEquals(before25, after25+1);
        for(int i = 0; i < 30; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachtransport', 3);").executeUpdate();
        }
        int before50 = db.countRows("achievement_log");
        DbAchievements.checkDeleteTransport(db.getDataSource(), "testachtransport");
        int after50 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachtransport';").executeUpdate();
        assertEquals(before50, after50+1);
        for(int i = 0; i < 75; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachtransport', 3);").executeUpdate();
        }
        int before100 = db.countRows("achievement_log");
        DbAchievements.checkDeleteTransport(db.getDataSource(), "testachtransport");
        int after100 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachtransport';").executeUpdate();
        assertEquals(before100, after100+1);
        for(int i = 0; i < 120; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachtransport', 3);").executeUpdate();
        }
        int before101 = db.countRows("achievement_log");
        DbAchievements.checkDeleteTransport(db.getDataSource(), "testachtransport");
        int after101 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachtransport';").executeUpdate();
        con.prepareStatement("DELETE FROM achievement_log WHERE username = 'testachtransport';").executeUpdate();
        assertEquals(before101, after101);
    }

    @Test
    public void testCheckDeleteEnergy() throws SQLException {
        Connection con = db.getDataSource().getConnection();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachenergy', 7);").executeUpdate();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachenergy', 8);").executeUpdate();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachenergy', 9);").executeUpdate();
        for(int i = 0; i < 5; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachenergy', 18);").executeUpdate();
        }
        int before25 = db.countRows("achievement_log");
        DbAchievements.checkDeleteEnergy(db.getDataSource(), "testachenergy");
        int after25 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachenergy';").executeUpdate();
        assertEquals(before25, after25+1);
        for(int i = 0; i < 30; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachenergy', 18);").executeUpdate();
        }
        int before50 = db.countRows("achievement_log");
        DbAchievements.checkDeleteEnergy(db.getDataSource(), "testachenergy");
        int after50 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachenergy';").executeUpdate();
        assertEquals(before50, after50+1);
        for(int i = 0; i < 75; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachenergy', 18);").executeUpdate();
        }
        int before100 = db.countRows("achievement_log");
        DbAchievements.checkDeleteEnergy(db.getDataSource(), "testachenergy");
        int after100 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachenergy';").executeUpdate();
        assertEquals(before100, after100+1);
        for(int i = 0; i < 120; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachenergy', 18);").executeUpdate();
        }
        int before101 = db.countRows("achievement_log");
        DbAchievements.checkDeleteEnergy(db.getDataSource(), "testachenergy");
        int after101 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachenergy';").executeUpdate();
        con.prepareStatement("DELETE FROM achievement_log WHERE username = 'testachenergy';").executeUpdate();
        assertEquals(before101, after101);
    }

    @Test
    public void testCheckDeleteTemperature() throws SQLException{
        Connection con = db.getDataSource().getConnection();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachtemp', 10);").executeUpdate();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachtemp', 11);").executeUpdate();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachtemp', 12);").executeUpdate();
        for(int i = 0; i < 5; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachtemp', 15);").executeUpdate();
        }
        int before25 = db.countRows("achievement_log");
        DbAchievements.checkDeleteTemperature(db.getDataSource(), "testachtemp");
        int after25 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachtemp';").executeUpdate();
        assertEquals(before25, after25+1);
        for(int i = 0; i < 30; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachtemp', 15);").executeUpdate();
        }
        int before50 = db.countRows("achievement_log");
        DbAchievements.checkDeleteTemperature(db.getDataSource(), "testachtemp");
        int after50 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachtemp';").executeUpdate();
        assertEquals(before50, after50+1);
        for(int i = 0; i < 75; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachtemp', 15);").executeUpdate();
        }
        int before100 = db.countRows("achievement_log");
        DbAchievements.checkDeleteTemperature(db.getDataSource(), "testachtemp");
        int after100 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachtemp';").executeUpdate();
        assertEquals(before100, after100+1);
        for(int i = 0; i < 120; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachtemp', 15);").executeUpdate();
        }
        int before101 = db.countRows("achievement_log");
        DbAchievements.checkDeleteTemperature(db.getDataSource(), "testachtemp");
        int after101 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachtemp';").executeUpdate();
        con.prepareStatement("DELETE FROM achievement_log WHERE username = 'testachtemp';").executeUpdate();
        assertEquals(before101, after101);
    }

    @Test
    public void testCheckDeleteOther() throws SQLException {
        Connection con = db.getDataSource().getConnection();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachother', 13);").executeUpdate();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachother', 14);").executeUpdate();
        con.prepareStatement("INSERT INTO achievement_log (username, achievement_id) VALUES ('testachother', 15);").executeUpdate();
        for(int i = 0; i < 5; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachother', 23);").executeUpdate();
        }
        int before25 = db.countRows("achievement_log");
        DbAchievements.checkDeleteOther(db.getDataSource(), "testachother");
        int after25 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachother';").executeUpdate();
        assertEquals(before25, after25+1);
        for(int i = 0; i < 30; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachother', 23);").executeUpdate();
        }
        int before50 = db.countRows("achievement_log");
        DbAchievements.checkDeleteOther(db.getDataSource(), "testachother");
        int after50 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachother';").executeUpdate();
        assertEquals(before50, after50+1);
        for(int i = 0; i < 75; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachother', 23);").executeUpdate();
        }
        int before100 = db.countRows("achievement_log");
        DbAchievements.checkDeleteOther(db.getDataSource(), "testachother");
        int after100 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachother';").executeUpdate();
        assertEquals(before100, after100+1);
        for(int i = 0; i < 120; i++){
            con.prepareStatement("INSERT INTO activity_log(username, activity_id) VALUES ('testachother', 23);").executeUpdate();
        }
        int before101 = db.countRows("achievement_log");
        DbAchievements.checkDeleteOther(db.getDataSource(), "testachother");
        int after101 = db.countRows("achievement_log");
        con.prepareStatement("DELETE FROM activity_log WHERE username = 'testachother';").executeUpdate();
        con.prepareStatement("DELETE FROM achievement_log WHERE username = 'testachother';").executeUpdate();
        assertEquals(before101, after101);
    }
    
    @Test
    public void testGetActivityTypeId() throws SQLException {
        DbRetrieve.retrieveTestDb().getConnection().prepareStatement("INSERT INTO activity_log (log_id, username, activity_id, date, time)"
                + "VALUES (42, 'test', 1, '2019-01-21', '12:00:00');").executeUpdate();
        int id = DbAchievements.getActivityTypeId(db.getDataSource(), 42);
        db.deleteActivity("test", 42);
        assertEquals(1, id);
    }
}