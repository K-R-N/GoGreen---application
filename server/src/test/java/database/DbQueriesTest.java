package database;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DbQueriesTest {

    @Test
    void retrieve() {
        String sqlQuery = "Select user_details.username, user_details.profile_picture, "
                + "user_details.first_name, COALESCE(Sum(activity.green_points), 0), "
                + "user_details.email_address, user_details.privacy_settings "
                + "From user_details Left Join activity_log "
                + "On user_details.username = activity_log.username "
                + "Left Join activity On activity.activity_id "
                + "= activity_log.activity_id "
                + "Where user_details.username = ? Group by user_details.username;";
        assertEquals(sqlQuery, DbQueries.retrieve("get_user"));
    }
}