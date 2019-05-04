package gui.views.general;

import static gui.helpers.StyleSheet.MouseState.HOVERED;
import static gui.helpers.StyleSheet.MouseState.NONE;
import static gui.helpers.StyleSheet.MouseState.PRESSED;

import gui.helpers.StyleSheet;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Simple adapter class for ScrollPane to create a scollable View.
 * @author Jules van der Toorn
 */
public class ScrollView extends View {

    static int count = 0;

    /**
     * Actual pane to provide scrolling.
     */
    protected ScrollPane scrollPane;

    /**
     * Event that's used to get notified if scrollview fully rendered.
     */
    protected EventType<Event> renderEvent;

    /**
     * Optional view that can be used to show content on top of the scrollview.
     */
    private View headerView;
    private double headerHeight;
    private boolean headerShown;

    /**
     * Encapsulate default constructor to enforce passing parameters.
     */
    protected ScrollView() {
        System.out.println("Default constructor triggered in ScrollView");
        System.out.println("This is BAD, fix this!");
    }

    /**
     * Initialises the scroll view.
     */
    public ScrollView(String title) {
        super();

        renderEvent = new EventType<>("DID_RENDER " + count++);

        // add scroll pane to wrapper view
        this.scrollPane = new CustomScrollPane();
        this.addSubview(scrollPane);
        this.setInsetsFor(scrollPane, -1);
        this.headerHeight = 0.0;
        this.headerShown = false;

        // add event handler to get access to scrollbar later
        scrollPane.addEventHandler(renderEvent, event -> {
            for (Node node: scrollPane.getChildrenUnmodifiable()) {
                if (node instanceof ScrollBar) {
                    ScrollBar scrollBar = (ScrollBar) node;
                    if (scrollBar.getOrientation() == Orientation.HORIZONTAL) {
                        layoutScrollBarX(new CScrollBar(scrollBar));
                    } else {
                        layoutScrollBarY(new CScrollBar(scrollBar));
                    }
                }
            }
        });

        scrollPane.setFitToWidth(true);

        if (title != null) {
            // make background of scrollview see-through
            set("background-color", "transparent");
            set("background", "transparent");

            // add stylesheet
            StyleSheet styleSheet = new StyleSheet();

            // add header
            Text titleText = new Text(title);
            View titleView = new View();
            titleView.addSubview(titleText);
            titleView.setInsetsFor(titleText, 15, null, null, 15);

            // apply styling on header
            styleSheet.set("font-family", "'Roboto Regular', sans-serif");
            styleSheet.set("font-size", "33");
            styleSheet.set("fill", "#505050");
            styleSheet.applyOn(titleText);
            styleSheet.clear();

            setHeaderView(titleView, 75);
        }
    }

    /**
     * Sets the optional header view.
     * @param header header view
     * @param height height that the view should have
     */
    public void setHeaderView(View header, double height) {
        this.headerView = header;
        this.headerHeight = height;

        this.addSubview(headerView);
        this.setInsetsFor(headerView, 0, 0, null, 0);
        headerView.setMinHeight(height);
        headerView.setPrefHeight(height);
        headerView.setMaxHeight(height);
        headerShown = true;

        scrollPane.vvalueProperty().addListener(
            (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                double contentHeight = ((VBox) scrollPane.getContent()).getHeight();
                double pxOffset = scrollPane.getVvalue() * contentHeight;
                double titleOpacity = 1.0 - (pxOffset / height > 1.0 ? 1.0 : pxOffset / 75.0);
                headerView.setOpacity(titleOpacity);
                if (headerShown && titleOpacity == 0.0) {
                    this.getChildren().remove(headerView);
                    headerShown = false;
                } else if (!headerShown && titleOpacity != 0.0) {
                    this.addSubview(headerView);
                    this.setInsetsFor(headerView, 0, 0, null, 0);
                    headerView.setMinHeight(height);
                    headerView.setPrefHeight(height);
                    headerView.setMaxHeight(height);
                    headerShown = true;
                }
            }
        );
    }

    public View getHeaderView() {
        return this.headerView;
    }

    /**
     * Methods that sets the pane which should be displayed inside the scroll view.
     * @param content pane that will be shown inside scroll view
     */
    public void setSubview(Pane content) {

        if (this.headerHeight > 0.0) {
            // add spacer for header
            VBox vbox = new VBox();
            View spacer = new View();
            spacer.setPrefHeight(headerHeight - 5);
            vbox.getChildren().addAll(spacer, content);

            // set content
            scrollPane.setContent(vbox);
        } else {
            scrollPane.setContent(content);
        }

        // update the content pane on a resize
        scrollPane.widthProperty().addListener((val1, val2, val3) -> {
            render();
        });
    }

    /**
     * Set a value for a CSS attribute.
     * @param attribute string of the JavaFX CSS attribute
     * @param value string of the JavaFX CSS value
     */
    @Override
    public void set(StyleSheet.MouseState state, String attribute, String value) {
        super.set(state, attribute, value);
        if (scrollPane.getContent() != null) {
            setNode(state, scrollPane.getContent(), attribute, value);
        } else {
            setNode(state, scrollPane, attribute, value);
        }
    }

    /**
     * Resizes content to fit to outer scroll pane.
     */
    private void render() {
        // nothing to do if scrollview doesn't have any contents
        if (scrollPane.getContent() == null) {
            return;
        }

        double newWidth = scrollPane.viewportBoundsProperty().get().getWidth() - 2.0;
        ((Pane) scrollPane.getContent()).setMinWidth(newWidth);
    }

    /**
     * Method where styling of horizontal scrollbar can be placed.
     * Override this method to use a custom styled scrollbar
     * @param scrollBar a horizontally orientated and rendered scrollbar
     */
    protected void layoutScrollBarX(CScrollBar scrollBar) {}

    /**
     * Method where styling of vertical scrollbar can be placed.
     * Override this method to use a custom styled scrollbar
     * @param scrollBar a vertically orientated and rendered scrollbar
     */
    protected void layoutScrollBarY(CScrollBar scrollBar) {
        StyleSheet styleSheet = new StyleSheet();

        // set style for background of scrollbar
        scrollBar.set("background-color", "transparent");

        // define colors
        String defaultColor = "#878787";
        String hoveredColor = "#707070";
        String pressedColor = "#606060";

        // set style for thumb of scrollbar
        styleSheet.set("background-radius", "15");
        styleSheet.set("background-insets", "0 4 0 4");
        styleSheet.set(NONE, "background-color", defaultColor);
        styleSheet.set(HOVERED, "background-color", hoveredColor);
        styleSheet.set(PRESSED, "background-color", pressedColor);
        styleSheet.applyOn(scrollBar.getThumb());
        styleSheet.clear();

        // make background of buttons transparent
        styleSheet.set("background-color", "transparent");
        styleSheet.applyOn(scrollBar.getIncrementButton());
        styleSheet.applyOn(scrollBar.getDecrementButton());
        styleSheet.clear();

        styleSheet.set("background-insets", "0 0.5 1 0.5");
        styleSheet.set(NONE, "background-color", defaultColor);
        styleSheet.set(HOVERED, "background-color", hoveredColor);
        styleSheet.set(PRESSED, "background-color", pressedColor);
        styleSheet.applyOn(scrollBar.getIncrementArrow());
        styleSheet.clear();

        styleSheet.set("background-insets", "1 0.5 0 0.5");
        styleSheet.set(NONE, "background-color", defaultColor);
        styleSheet.set(HOVERED, "background-color", hoveredColor);
        styleSheet.set(PRESSED, "background-color", pressedColor);
        styleSheet.applyOn(scrollBar.getDecrementArrow());
        styleSheet.clear();
    }

    /**
     * Custom superclass of ScrollPane to get hook on render event.
     */
    private class CustomScrollPane extends ScrollPane {

        private boolean isRendered = false;

        /**
         * Method that gets called when the ScrollPane is rendered.
         */
        @Override
        protected void layoutChildren() {
            super.layoutChildren();

            if (!isRendered) {
                Event event = new Event(renderEvent);
                scrollPane.fireEvent(event);
            }

            isRendered = true;
        }
    }

    /**
     * Custom adapter class of ScrollBar to add extended functionality.
     */
    public static class CScrollBar {

        public ScrollBar node;

        private StyleSheet styleSheet;

        /**
         * Encapsulate default constructor to enforce passing ScrollBar.
         */
        private CScrollBar() {}

        /**
         * Initialises the custom scroll bar.
         * @param scrollBar the adapted scrollbar
         */
        public CScrollBar(ScrollBar scrollBar) {
            this.node = scrollBar;
            this.styleSheet = new StyleSheet();
        }

        /**
         * Finds the scrollbar thumb in children nodes.
         * @return the scrollbar thumb node
         */
        public Node getThumb() {
            for (Node node: node.getChildrenUnmodifiable()) {
                if (node.getStyleClass().contains("thumb")) {
                    return node;
                }
            }
            return null;
        }

        /**
         * Finds the scrollbar increment button in children nodes.
         * @return the scrollbar increment button
         */
        public Node getIncrementButton() {
            for (Node node: node.getChildrenUnmodifiable()) {
                if (node.getStyleClass().contains("increment-button")) {
                    return node;
                }
            }
            return null;
        }

        /**
         * Finds the scrollbar decrement button in children nodes.
         * @return the scrollbar decrement button
         */
        public Node getDecrementButton() {
            for (Node node: node.getChildrenUnmodifiable()) {
                if (node.getStyleClass().contains("decrement-button")) {
                    return node;
                }
            }
            return null;
        }

        /**
         * Finds the scrollbar increment button arrow in children nodes.
         * @return the scrollbar increment button arrow
         */
        public Node getIncrementArrow() {
            if (!(getIncrementButton() instanceof Parent)) {
                System.out.println("Increment button doesn't contain arrow!");
                return null;
            }
            Parent incButton = (Parent) getIncrementButton();
            for (Node node: incButton.getChildrenUnmodifiable()) {
                if (node.getStyleClass().contains("increment-arrow")) {
                    return node;
                }
            }
            return null;
        }

        /**
         * Finds the scrollbar decrement button arrow in children nodes.
         * @return the scrollbar decrement button arrow
         */
        public Node getDecrementArrow() {
            if (!(getDecrementButton() instanceof Parent)) {
                System.out.println("Decrement button doesn't contain arrow!");
                return null;
            }
            Parent decButton = (Parent) getDecrementButton();
            for (Node node: decButton.getChildrenUnmodifiable()) {
                if (node.getStyleClass().contains("decrement-arrow")) {
                    return node;
                }
            }
            return null;
        }

        /**
         * Set a value for a CSS attribute.
         * @param attribute string of the JavaFX attribute
         * @param value value of the JavaFX attribute
         */
        public void set(StyleSheet.MouseState state, String attribute, String value) {
            styleSheet.set(state, attribute, value);
            styleSheet.applyOn(node);
        }

        /**
         * Set a value for a CSS attribute.
         * @param attribute string of the JavaFX attribute
         * @param value value of the JavaFX attribute
         */
        public void set(String attribute, String value) {
            set(NONE, attribute, value);
        }
    }
}
