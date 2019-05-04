package gui.views.activityform;

import static gui.helpers.StyleSheet.MouseState.HOVERED;
import static gui.helpers.StyleSheet.MouseState.NONE;
import static gui.helpers.StyleSheet.MouseState.PRESSED;

import gui.App;
import gui.helpers.ObjectPasser;
import gui.helpers.ObjectRefresher;
import gui.helpers.TextDimension;
import gui.views.general.AlertView;
import gui.views.general.ListView;
import gui.views.general.PopupView;
import gui.views.general.ScrollView;
import gui.views.general.View;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Activity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ActivityFormView extends View {

    // default colors
    static String defaultColor = "#878787";
    static String hoveredColor = "#66a072";
    static String pressedColor = "#505050";

    protected List<Activity> activities;

    /**
     * Lambda objects used to pass to and retrieve activities from MVC.
     */
    private ObjectRefresher<List<Activity>> refresher;
    private ObjectPasser<Activity> passer;

    private Stage stage;

    private ActivityFormView() {}

    /**
     * Creates the activity from view.
     * @param refresher object with get() method which returns most recent available activities
     */
    public ActivityFormView(
            ObjectRefresher<List<Activity>> refresher,
            ObjectPasser<Activity> passer,
            Stage stage) {
        this.refresher = refresher;
        this.passer = passer;
        this.stage = stage;

        refresh();
    }

    /**
     * Refreshes the view contents.
     */
    @Override
    public void refresh() {
        this.getChildren().clear();

        this.activities = refresher.get();
        if (activities == null) {
            PopupView.init(stage);
            new AlertView(
                    "Could not retrieve the activities",
                    "Failed to retrieve all activities, "
                            + "this probably means you don't have an internet connection",
                    "okay :("
            );
        }
        render();
    }

    /**
     * Applies constraints and styling.
     */
    public void render() {
        if (activities == null) {
            return;
        }

        ScrollView scrollView = new ScrollView("Add activity");
        addSubview(scrollView);
        setInsetsFor(scrollView, 0, 0, 0, 0);

        // add hbox to space columns horizontally
        HBox columns = new HBox();
        columns.setFillHeight(true);
        scrollView.setSubview(columns);

        // add columns to the hbox
        ListView leftColumn = new ListView();
        leftColumn.prefWidthProperty().bind(widthProperty().divide(2));
        ListView rightColumn = new ListView();
        rightColumn.prefWidthProperty().bind(widthProperty().divide(2));
        columns.getChildren().addAll(leftColumn, rightColumn);

        // create radio button toggle group
        ToggleGroup toggleGroup = new ToggleGroup();

        // iterate through groups to calculate total height
        int totalHeight = 0;
        Set<Integer> addedGroups = new HashSet<>();
        for (Activity activity: activities) {
            if (activity.getType().getCategory() != null) {
                if (!addedGroups.contains(activity.getType().getCategory().getId())) {
                    addedGroups.add(activity.getType().getCategory().getId());

                    totalHeight += new ActivityGroupView(
                        activity.getType().getCategory().getId(),
                        activities,
                        toggleGroup
                    ).totalHeight;
                }
            }
        }
        addedGroups.clear();

        // add activity groups to the form
        int leftColumnHeight = 0;
        for (Activity activity: activities) {
            if (activity.getType().getCategory() != null) {
                if (!addedGroups.contains(activity.getType().getCategory().getId())) {
                    addedGroups.add(activity.getType().getCategory().getId());

                    ActivityGroupView groupView = new ActivityGroupView(
                            activity.getType().getCategory().getId(),
                            activities,
                            toggleGroup
                    );

                    // seperate groups evenly across the 2 columns
                    if (leftColumnHeight < totalHeight / 2) {
                        leftColumn.append(groupView);
                        leftColumnHeight += groupView.totalHeight;
                    } else {
                        rightColumn.append(groupView);
                    }
                }
            }
        }

        // add button to submit the activity
        View addButtonView = new View();
        addSubview(addButtonView);
        setInsetsFor(addButtonView, null, null, 10, null);
        Text addButtonText = new Text("SUBMIT");
        addButtonView.addSubview(addButtonText);

        // style the submit button
        addButtonView.set(NONE, "background-color", "#87cc95");
        addButtonView.set(NONE, "fill", "#edf7ee");
        addButtonView.set(HOVERED, "background-color", "#66a072");
        addButtonView.set(HOVERED, "fill", "#d3edd5");
        addButtonView.set(PRESSED, "background-color", "#437a4e");
        addButtonView.set(PRESSED, "fill", "#a2bfa4");
        addButtonView.set("font-family", "'Roboto Regular', sans-serif");
        addButtonView.set("font-size", "19");
        addButtonView.set("background-radius", "7");
        addButtonText.styleProperty().bind(addButtonView.styleProperty());
        addButtonView.setMaxHeight(TextDimension.calculateFor(addButtonText).getHeight() + 14);
        addButtonView.setMaxWidth(TextDimension.calculateFor(addButtonText).getWidth() + 20);

        // define action on click
        addButtonView.setOnMouseClicked(e -> {

            // send activity to MVC using a lambda expression
            Toggle selectedToggle = toggleGroup.getSelectedToggle();
            if (selectedToggle == null) {
                AlertView alert = new AlertView(
                    "No activity selected",
                    "Please select an activity before clicking on submit!",
                    "got it!"
                );
            } else {
                Activity selectedActivity = (Activity) selectedToggle.getUserData();
                this.passer.pass(selectedActivity);
                App.sidebar.reloadPicture();
            }
        });

        addButtonView.setOnMouseEntered(e -> {
            addButtonView.set(HOVERED, "background-color", "#66a072");
            addButtonView.set(HOVERED, "fill", "#d3edd5");
        });

        addButtonView.setOnMouseReleased(e -> {
            addButtonView.set(HOVERED, "background-color", "#87cc95");
            addButtonView.set(HOVERED, "fill", "#edf7ee");
        });
    }
}
