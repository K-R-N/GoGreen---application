package gui.session;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Session Object class.
 */
@XmlRootElement
public class Session {

    /**
     * String which will be stored into an file.
     */
    private String storedString;

    /**
     * Constructor of the session object.
     */
    public Session()  {}

    /**
     * Gets the stored string from the session object.
     * @return String which is contained in the Session object,
     */
    public String getStoredString() {
        return storedString;
    }

    /**
     * Sets the value of the stored string.
     * @param storedString String which the session object will contain.
     */
    @XmlElement
    public void setStoredString(String storedString) {

        this.storedString = storedString;
    }
}
