package gui.views.general;

import static gui.helpers.StyleSheet.MouseState.NONE;

import gui.helpers.StyleSheet;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * Wrapper class for StackPane to add extended functionality for placing subpanes.
 * @author Jules van der Toorn
 */
public class View extends StackPane {

    protected Rectangle rect;

    protected StyleSheet styleSheet;

    private Insets insets;

    /**
     * Constructor that adds listeners for a resize so layout can be adjusted.
     */
    public View() {
        insets = new Insets(0, 0, 0, 0);
        rect = new Rectangle();
        rect.widthProperty().bind(this.widthProperty());
        rect.heightProperty().bind(this.heightProperty());
        this.setClip(rect);

        styleSheet = new StyleSheet();
    }

    /**
     * Set a value for a CSS attribute.
     * Overridden by superviews to redirect styling to specific view.
     * @param attribute string of the JavaFX attribute
     * @param value value of the JavaFX attribute
     */
    public void set(String attribute, String value) {
        set(NONE, attribute, value);
    }

    /**
     * Set a value for a CSS attribute.
     * Overridden by superviews to redirect styling to specific view.
     * @param state mouse state for styling
     * @param attribute string of the JavaFX attribute
     * @param value value of the JavaFX attribute
     */
    public void set(StyleSheet.MouseState state, String attribute, String value) {
        setNode(state,this, attribute, value);
    }

    /**
     * Internal method that handles actual style applying.
     * @param node node that will get the style
     * @param attribute string of the JavaFX CSS attribute
     * @param value string of the JavaFX CSS value
     */
    protected void setNode(StyleSheet.MouseState state, Node node, String attribute, String value) {
        styleSheet.set(state, attribute, value);

        // updates view rect if view gets corner radius
        if (attribute.contains("radius") && node instanceof View) {
            View nodeView = (View) node;
            double radius = 0.0;
            try {
                String[] numbers = value.split("\\s+");
                for (String strnumb: numbers) {
                    if (radius < Double.parseDouble(strnumb)) {
                        radius = Double.parseDouble(strnumb);
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Couldn't parse corner radius!");
            }
            nodeView.rect.setArcHeight(radius);
            nodeView.rect.setArcWidth(radius);
        }

        styleSheet.applyOn(node);
    }

    /**
     * Method during development to show outline of a view if you're uncertain about its size.
     * @param showOutline set to true to show the outline
     */
    public void showOutline(boolean showOutline) {
        set("border-color", showOutline ? "black" : "");
        set("border-style", showOutline ? "solid" : "");
        set("border-width", showOutline ? "2" : "");
    }

    /**
     * Checks if the parent of an object is a member of the Pane class.
     * @param obj the child with an unknown parent
     * @return the parent object or null if unknown parent
     */
    protected Pane validatePane(Object obj) {
        Parent parentObj = this.getParent();
        if (parentObj instanceof Pane) {
            return (Pane) parentObj;
        }
        // fail
        System.out.println("Fail: parent node is not a Pane");
        return null;
    }

    /**
     * Helper function because CheckStyle complained about cyclomatic complexity...
     * @param child the child view
     * @param top top inset
     * @param right right inset
     * @param bottom bottom inset
     * @param left left inset
     */
    private void set2Align(Node child, Number top, Number right, Number bottom, Number left) {
        if (top != null && right != null) {
            StackPane.setAlignment(child, Pos.TOP_RIGHT);
        } else if (right != null && bottom != null) {
            StackPane.setAlignment(child, Pos.BOTTOM_RIGHT);
        } else if (bottom != null && left != null) {
            StackPane.setAlignment(child, Pos.BOTTOM_LEFT);
        } else if (left != null && top != null) {
            StackPane.setAlignment(child, Pos.TOP_LEFT);
        } else {
            set1Align(child, top, right, bottom, left);
        }
    }

    /**
     * Helper function because CheckStyle complained about cyclomatic complexity...
     * @param child the child view
     * @param top top inset
     * @param right right inset
     * @param bottom bottom inset
     * @param left left inset
     */
    private void set1Align(Node child, Number top, Number right, Number bottom, Number left) {
        if (top != null) {
            StackPane.setAlignment(child, Pos.TOP_CENTER);
        } else if (right != null) {
            StackPane.setAlignment(child, Pos.CENTER_RIGHT);
        } else if (bottom != null) {
            StackPane.setAlignment(child, Pos.BOTTOM_CENTER);
        } else if (left != null) {
            StackPane.setAlignment(child, Pos.CENTER_LEFT);
        } else {
            StackPane.setAlignment(child, Pos.CENTER);
        }
    }

    /**
     * Centers a child node.
     * @param child child node
     */
    public void centerChild(Node child) {
        StackPane.setAlignment(child, Pos.CENTER);
    }

    /**
     * Sets amount of bottom inset of this view in relation to its parent.
     * @param inset amount of inset in pixels
     */
    public void setInsetsFor(Node child, double inset) {
        setInsetsFor(child, inset, inset, inset, inset);
    }

    /**
     * Configures the insets of a chld view.
     * @param child the child view
     * @param top top inset
     * @param right right inset
     * @param bottom bottom inset
     * @param left left inset
     */
    public void setInsetsFor(Node child, Double top, Double right, Double bottom, Double left) {
        set2Align(child, top, right, bottom, left);
        double dtop = top == null ? 0.0 : top;
        double dright = right == null ? 0.0 : right;
        double dbottom = bottom == null ? 0.0 : bottom;
        double dleft = left == null ? 0.0 : left;
        setInsetsFor(child, dtop, dright, dbottom, dleft);
    }


    /**
     * Configures the insets of a chld view.
     * @param child the child view
     * @param top top inset
     * @param right right inset
     * @param bottom bottom inset
     * @param left left inset
     */
    public void setInsetsFor(Node child, Integer top, Integer right, Integer bottom, Integer left) {
        set2Align(child, top, right, bottom, left);
        double dtop = top == null ? 0.0 : top.doubleValue();
        double dright = right == null ? 0.0 : right.doubleValue();
        double dbottom = bottom == null ? 0.0 : bottom.doubleValue();
        double dleft = left == null ? 0.0 : left.doubleValue();
        setInsetsFor(child, dtop, dright, dbottom, dleft);
    }

    /**
     * Configures the insets of a child view.
     * @param child the child view
     * @param top top inset
     * @param right right inset
     * @param bottom bottom inset
     * @param left left inset
     */
    private void setInsetsFor(Node child, double top, double right, double bottom, double left) {
        insets = new Insets(top, right, bottom, left);
        StackPane.setMargin(child, insets);
    }

    /**
     * Method that can be overriden to provjde refresh capabilities.
     */
    public void refresh() {}

    /**
     * Simple wrapper method to add a subview.
     * @param child node that you want to put inside this view
     */
    public void addSubview(Node child) {
        this.getChildren().add(child);
        this.centerChild(child);
    }
}
