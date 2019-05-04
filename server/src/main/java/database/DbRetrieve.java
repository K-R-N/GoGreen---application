package database;

import org.postgresql.ds.PGSimpleDataSource;
import org.sqlite.SQLiteDataSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.sql.DataSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DbRetrieve {

    /**
     * A method used to read the db_config xml file.
     * @return the node list from the xml file.
     */
    public static NodeList readFile() {
        NodeList nl = null;
        try {
            File input = new File("db_config.xml");
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = null;
            docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(input);
            doc.getDocumentElement().normalize();
            nl = doc.getElementsByTagName("DataSource");

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return nl;
    }

    /**
     * A method used to retrieve a DataSource of postgres database.
     * @return The DataSource of the postgres database
     */
    public static DataSource retrievePgDb() {
        PGSimpleDataSource source = new PGSimpleDataSource();
        Node node = DbRetrieve.readFile().item(0);
        Element element = (Element) node;
        source.setServerName(element.getElementsByTagName("url")
                .item(0).getTextContent());
        source.setPortNumber(Integer.parseInt(element.getElementsByTagName("port_number")
                .item(0).getTextContent()));
        source.setDatabaseName(element.getElementsByTagName("db_name")
                .item(0).getTextContent());
        source.setUser(element.getElementsByTagName("username")
                .item(0).getTextContent());
        source.setPassword(element.getElementsByTagName("password")
                .item(0).getTextContent());
        source.setSocketTimeout(Integer.parseInt(element.getElementsByTagName("timeout")
                .item(0).getTextContent()));
        return source;
    }

    /**
     * A method used to retrieve a DataSource of SQLite database.
     * @return The DataSource of the SQLite database
     */
    public static DataSource retrieveTestDb() {
        SQLiteDataSource source = new SQLiteDataSource();
        Node node = DbRetrieve.readFile().item(1);
        Element element = (Element) node;
        source.setUrl(element.getElementsByTagName("url").item(0).getTextContent());
        return source;
    }
}
