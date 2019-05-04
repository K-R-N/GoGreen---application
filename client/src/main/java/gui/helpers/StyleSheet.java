package gui.helpers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;

import java.util.HashMap;
import java.util.Map;

public class StyleSheet {

    private Map<String, String> styleSheet;
    private Map<String, String> hoveredStyleSheet;
    private Map<String, String> pressedStyleSheet;

    /**
     * Initialises the stylesheet map.
     */
    public StyleSheet() {
        styleSheet = new HashMap<>();
        hoveredStyleSheet = new HashMap<>();
        pressedStyleSheet = new HashMap<>();
    }

    /**
     * Sets JavaFX CSS value to an attribute.
     * @param attribute string of the JavaFX CSS attribute
     * @param value string of the JavaFX CSS value
     */
    public void set(String attribute, String value) {
        set(MouseState.NONE, attribute, value);
    }

    /**
     * Sets JavaFX CSS value to an attribute.
     * @param attribute string of the JavaFX CSS attribute
     * @param value string of the JavaFX CSS value
     */
    public void set(MouseState state, String attribute, String value) {
        switch (state) {
            case NONE:
                styleSheet.put(attribute, value);
                break;
            case HOVERED:
                hoveredStyleSheet.put(attribute, value);
                break;
            case PRESSED:
                pressedStyleSheet.put(attribute, value);
                break;
            default:
                break;
        }
    }

    /**
     * Removes all entries from the stylesheet.
     */
    public void clear() {
        styleSheet.clear();
        hoveredStyleSheet.clear();
        pressedStyleSheet.clear();
    }

    /**
     * Applies the style sheets to the node.
     */
    public void applyOn(Node node) {
        // check for hover or click attributes
        if (!hoveredStyleSheet.isEmpty() || !pressedStyleSheet.isEmpty()) {
            // bind hovered and pressed stylesheet to node
            StringBinding binding = Bindings.when(node.pressedProperty())
                    .then(new SimpleStringProperty(toString(getFullMap(pressedStyleSheet))))
                    .otherwise(Bindings.when(node.hoverProperty())
                            .then(new SimpleStringProperty(toString(getFullMap(hoveredStyleSheet))))
                            .otherwise(new SimpleStringProperty(toString(styleSheet))));
            node.styleProperty().bind(binding);
        } else {
            node.setStyle(toString(styleSheet));
        }
    }

    /**
     * Converts the map to a JavaFX stylesheet.
     * @return the stylesheet
     */
    public String toString(Map<String, String> map) {
        String styleString = "";
        for (String key: map.keySet()) {
            styleString += "-fx-" + key + ": "
                    + map.get(key) + ";\n";
        }
        return styleString;
    }

    private Map<String, String> getFullMap(Map<String, String> map) {
        Map<String, String> fullMap = new HashMap<>();
        fullMap.putAll(styleSheet);
        fullMap.putAll(map);
        return fullMap;
    }

    /**
     * An enum containing all the possible mouse states.
     */
    public enum MouseState {
        NONE,
        HOVERED,
        PRESSED
    }
}
