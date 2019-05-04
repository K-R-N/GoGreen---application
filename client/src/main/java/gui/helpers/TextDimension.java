package gui.helpers;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;

import java.awt.Dimension;

/**
 * Class that's used to calculate the dimensions of a text view.
 */
public class TextDimension {

    /**
     * Calculates dimensions of text.
     * @param text text node
     * @return dimensions
     */
    public static Dimension calculateFor(Text text) {
        // clone text object to isolate it
        Text cloneText = new Text(text.getText());
        cloneText.setStyle(text.getStyle());
        cloneText.setWrappingWidth(text.getWrappingWidth());

        // make temporary scene to apply CSS
        new Scene(new Group(cloneText));
        cloneText.applyCss();
        Dimension dimension = new Dimension();

        // get dimensions
        dimension.setSize(
                cloneText.getLayoutBounds().getWidth(),
                cloneText.getLayoutBounds().getHeight()
        );
        return dimension;
    }
}
