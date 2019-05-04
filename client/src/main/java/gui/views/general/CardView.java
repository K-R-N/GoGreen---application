package gui.views.general;

import gui.helpers.StyleSheet;
import javafx.scene.Node;

/**
 * View to show an activity.
 * @author Jules van der Toorn
 */
public class CardView extends View {

    protected View contentView;
    
    protected double height;

    /**
     * Define default constructor as private to enforce passing activity.
     */
    protected CardView() {}

    /**
     * Renders the activity view.
     * @param height height of the card
     */
    public CardView(double height) {
        super();
        
        this.height = height;
        this.contentView = new View();

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
        if (contentView != null) {
            setNode(state, contentView, attribute, value);
        }
    }

    /**
     * Sets subview for the card.
     * @param node node that will become subview
     */
    public void setSubview(Node node) {
        contentView.addSubview(node);
        contentView.setInsetsFor(node, 0);
    }

    /**
     * Applies constraints and styling.
     */
    private void render() {

        // clear subviews
        this.getChildren().clear();

        // add border view
        this.addSubview(contentView);

        // set insets
        this.setInsetsFor(contentView, 0, 15, 25, 15);

        // add border
        contentView.setMinHeight(height);
        contentView.set("border-radius", "7");
        contentView.set("background-radius", "7");

        // set content background
        contentView.set("background-color", "white");
    }
}
