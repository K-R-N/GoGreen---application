package gui.views.general;

import static gui.helpers.StyleSheet.MouseState.HOVERED;
import static gui.helpers.StyleSheet.MouseState.NONE;
import static gui.helpers.StyleSheet.MouseState.PRESSED;

import gui.helpers.StyleSheet;
import gui.helpers.TextDimension;
import gui.helpers.Trigger;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

/**
 * View to show the user an alert.
 */
public class AlertView {

    // default colors
    public static String optionDefaultColor = "#505050";
    public static String optionHoveredColor = "#303030";
    public static String optionPressedColor = "#101010";

    private PopupView popup;

    private Map<String, Trigger> handlers;

    private String title;
    private String msg;
    private String option1;
    private String option2;

    private AlertView() {
        super();
    }

    /**
     * Creates an alert view with 1 option.
     * @param title title of the alert
     * @param msg message of the alert
     * @param option1 title of the option
     */
    public AlertView(String title, String msg, String option1) {
        this.handlers = new HashMap<>();
        this.title = title;
        this.msg = msg;
        this.option1 = option1;

        render();
    }

    /**
     * Creates an alert view with 2 options.
     * @param title title of the alert
     * @param msg message of the alert
     * @param option1 title of left option
     * @param option2 title of right option
     */
    public AlertView(String title, String msg, String option1, String option2) {
        this.handlers = new HashMap<>();
        this.title = title;
        this.msg = msg;
        this.option1 = option1;
        this.option2 = option2;

        render();
    }

    /**
     * Lays out views.
     */
    public void render() {
        View alertView = new View();

        // alertView.set("background-radius", Integer.toString(borderRadius));
        alertView.set("background-color", "white");//"#f5fef2");
        alertView.set("background-insets", "10");
        alertView.set("effect", "dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.5, 0.0, 0.0)");

        //alertView.set("background-color", "#d1ffef");

        Text titleText = new Text(title);
        alertView.addSubview(titleText);
        alertView.setInsetsFor(titleText, 20, null, null, null);
        StyleSheet styleSheet = new StyleSheet();
        styleSheet.set("font-family", "'Roboto Regular', sans-serif");
        styleSheet.set("font-size", "23");
        styleSheet.set("fill", "#87cc95");
        styleSheet.applyOn(titleText);
        styleSheet.clear();

        int totalHeight = 0;
        int totalWidth = 0;
        Dimension titleDimension = TextDimension.calculateFor(titleText);
        totalHeight += 20 + titleDimension.height;
        if (titleDimension.width > totalWidth) {
            totalWidth = titleDimension.width;
        }

        Text msgText = new Text(msg);
        alertView.addSubview(msgText);
        alertView.setInsetsFor(msgText, totalHeight + 15, null, null, null);
        styleSheet.set("font-family", "'Roboto Light', sans-serif");
        styleSheet.set("font-size", "18");
        styleSheet.set("fill", "#505050");
        styleSheet.applyOn(msgText);
        styleSheet.clear();
        msgText.setTextAlignment(TextAlignment.CENTER);
        msgText.setWrappingWidth(350.0);
        Dimension msgDimension = TextDimension.calculateFor(msgText);
        totalHeight += 15 + msgDimension.height;
        if (msgDimension.width > totalWidth) {
            totalWidth = msgDimension.width;
        }

        // add option1 views
        View option1View = new View();
        alertView.addSubview(option1View);
        alertView.setInsetsFor(option1View, totalHeight + 10, null, null, null);
        Text option1Text = new Text(option1);

        option1View.addSubview(option1Text);
        option1View.setOnMouseClicked(e -> {
            popup.close();
            if (handlers.containsKey(option1)) {
                handlers.get(option1).fire();
            }
        });

        // style option1
        styleSheet.set("font-family", "'Roboto Regular', sans-serif");
        styleSheet.set("font-size", "17.5");
        styleSheet.set(NONE, "fill", optionDefaultColor);
        styleSheet.set(HOVERED, "fill", optionHoveredColor);
        styleSheet.set(PRESSED, "fill", optionPressedColor);
        styleSheet.applyOn(option1View);
        option1Text.styleProperty().bind(option1View.styleProperty());
        styleSheet.clear();

        int finalWidth = (totalWidth + 30) > 200 ? (totalWidth + 30) : 200;

        // handle dimensions of option1
        Dimension opt1TextDim = TextDimension.calculateFor(option1Text);
        option1View.setMaxWidth(opt1TextDim.width + 10);
        option1View.setMaxHeight(opt1TextDim.height + 10);

        int optionsWidth = opt1TextDim.width + 10;

        if (option2 != null) {
            // add option2 views
            View option2View = new View();
            alertView.addSubview(option2View);
            Text option2Text = new Text(option2);
            option2View.addSubview(option2Text);

            option2View.setOnMouseClicked(e -> {
                popup.close();
                if (handlers.containsKey(option2)) {
                    handlers.get(option2).fire();
                }
            });

            // style option2
            styleSheet.set("font-family", "'Roboto Regular', sans-serif");
            styleSheet.set("font-size", "17.5");
            styleSheet.set(NONE, "fill", optionDefaultColor);
            styleSheet.set(HOVERED, "fill", optionHoveredColor);
            styleSheet.set(PRESSED, "fill", optionPressedColor);
            styleSheet.applyOn(option2View);
            option2Text.styleProperty().bind(option2View.styleProperty());
            styleSheet.clear();

            // handle dimensions of option2
            Dimension opt2TextDim = TextDimension.calculateFor(option2Text);
            option2View.setMaxWidth(opt2TextDim.width + 10);
            option2View.setMaxHeight(opt2TextDim.height + 10);

            optionsWidth += opt2TextDim.width + 10;

            alertView.setInsetsFor(option2View,
                    totalHeight + 10,
                    null, null,
                    (finalWidth - optionsWidth - 10) / 2 + opt1TextDim.width + 20
            );
            alertView.setInsetsFor(option1View,
                    totalHeight + 10,
                    null, null,
                    (finalWidth - optionsWidth - 10) / 2
            );
        } else {
            alertView.setInsetsFor(option1View,
                    totalHeight + 10,
                    null, null,
                    (finalWidth - optionsWidth) / 2
            );
        }

        totalHeight += 10 + (opt1TextDim.height + 10);

        this.popup = new PopupView(alertView, finalWidth + 20, totalHeight + 15);
    }

    /**
     * Shows the popup.
     */
    public void show() {
        popup.show();
    }

    /**
     * Closes the popup.
     */
    public void close() {
        popup.close();
    }

    /**
     * Sets handler for an alert button.
     * @param option the title of the button
     * @param handler an lambda function that will be ran on button press
     */
    public void setHandler(String option, Trigger handler) {
        if (!handlers.containsKey(option)) {
            handlers.put(option, handler);
        }
    }
}
