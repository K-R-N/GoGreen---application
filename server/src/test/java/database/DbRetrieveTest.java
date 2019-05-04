package database;

import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.sqlite.SQLiteDataSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class DbRetrieveTest {

    @Test
    public void retrievePG() {
        assertTrue(DbRetrieve.retrievePgDb() instanceof PGSimpleDataSource);
    }

    @Test
    public void retrieveSQLite()  {
        assertTrue(DbRetrieve.retrieveTestDb() instanceof SQLiteDataSource);
    }

}