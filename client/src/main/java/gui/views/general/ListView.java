package gui.views.general;

import gui.helpers.StyleSheet;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple adapter class for ScrollPane to create a scollable View.
 * @author Jules van der Toorn
 */
public class ListView extends View {

    /**
     * Actual list to provide vertical stacking of nodes.
     */
    public VBox vbox;

    /**
     * List of Views inside the ListView.
     */
    private List<View> viewList;

    public ListView() {
        this(new ArrayList<View>());
    }

    /**
     * Initialises the list view.
     */
    public ListView(List<View> viewList) {
        super();

        // add scroll pane to wrapper view
        vbox = new VBox();
        this.addSubview(vbox);
        vbox.setFillWidth(true);

        this.viewList = viewList;

        // let the scroll view and its contents fill available width
        vbox.prefWidthProperty().bind(this.widthProperty());
        vbox.prefHeightProperty().bind(this.heightProperty());

        render();
    }

    /**
     * Appends a View at the bottom of the list.
     * @param view the View that will be appended
     */
    public void append(Node view) {
        vbox.getChildren().add(view);

    }

    /**
     * Updates the full content of the ViewList.
     * @param viewList the list of Views that will be the new content
     */
    public void update(List<View> viewList) {
        this.viewList = viewList;
        render();
    }

    /**
     * Removes all Views from the list.
     */
    public void clear() {
        viewList.clear();
        render();
    }

    /**
     * Set a value for a CSS attribute.
     * @param attribute string of the JavaFX CSS attribute
     * @param value string of the JavaFX CSS value
     */
    @Override
    public void set(StyleSheet.MouseState state, String attribute, String value) {
        super.set(state, attribute, value);
        setNode(state, vbox, attribute, value);
    }

    /**
     * Iterates to all views in the list and adds them to the parent view.
     */
    private void render() {
        vbox.getChildren().clear();


        for (View view: viewList) {
            vbox.getChildren().add(view);
        }
    }
}
