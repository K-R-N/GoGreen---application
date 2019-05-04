package gui.views.general;

import static gui.helpers.StyleSheet.MouseState.HOVERED;
import static gui.helpers.StyleSheet.MouseState.NONE;
import static gui.helpers.StyleSheet.MouseState.PRESSED;

import gui.helpers.TextDimension;
import gui.helpers.Trigger;
import javafx.scene.text.Text;


public class ButtonView extends View {

    private ButtonView() {}

    /**
     * method that akes a button.
     * @param title name
     * @param lightMode if it is ligth or not.
     * @param onClick the trigger when clicked.
     */
    public ButtonView(String title, boolean lightMode, Trigger onClick) {

        // add button to submit the activity
        Text addButtonText = new Text(title.toUpperCase());
        addSubview(addButtonText);

        // style the submit button
        if (lightMode) {
            set(NONE, "background-color", "#87cc95");
            set(NONE, "fill", "#edf7ee");
            set(HOVERED, "background-color", "#66a072");
            set(HOVERED, "fill", "#d3edd5");
            set(PRESSED, "background-color", "#437a4e");
            set(PRESSED, "fill", "#a2bfa4");
        } else {
            set(NONE, "background-color", "#edf7ee");
            set(NONE, "fill", "#87cc95");
            set(HOVERED, "background-color", "#d3edd5");
            set(HOVERED, "fill", "#66a072");
            set(PRESSED, "background-color", "#a2bfa4");
            set(PRESSED, "fill", "#437a4e");
        }
        set("font-family", "'Roboto Regular', sans-serif");
        set("font-size", "19");
        set("background-radius", "7");
        addButtonText.styleProperty().bind(this.styleProperty());
        setMaxHeight(TextDimension.calculateFor(addButtonText).getHeight() + 14);
        setMaxWidth(TextDimension.calculateFor(addButtonText).getWidth() + 20);

        // define action on click
        setOnMouseClicked(e -> onClick.fire());

        setOnMouseEntered(e -> {
            if (lightMode) {
                set(HOVERED, "background-color", "#66a072");
                set(HOVERED, "fill", "#d3edd5");
            } else {
                set(HOVERED, "background-color", "#d3edd5");
                set(HOVERED, "fill", "#66a072");
            }
        });

        setOnMouseReleased(e -> {
            if (lightMode) {
                set(HOVERED, "background-color", "#87cc95");
                set(HOVERED, "fill", "#edf7ee");
            } else {
                set(HOVERED, "background-color", "#d3edd5");
                set(HOVERED, "fill", "#66a072");
            }
        });
    }

    private void render() {

    }
}
