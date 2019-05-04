package database;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DbQueries {

    /**
     * A method used to retrieve a query from xml file.
     * @param queryId the id of the query.
     * @return the query as a String.
     */
    public static String retrieve(String queryId) {
        String result = "";
        try {
            File input = new File("db_queries.xml");
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = null;
            docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(input);
            doc.getDocumentElement().normalize();
            Element element = (Element) doc.getElementsByTagName(queryId).item(0);
            result = element.getTextContent();
            result = result.replaceAll("\n","");
            result = result.replaceAll("\t","");
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
