package gui.views.activityform;

import gui.helpers.StyleSheet;
import gui.helpers.TextDimension;
import gui.views.general.ListView;
import gui.views.general.View;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import models.Activity;
import models.ActivityCategory;

import java.util.ArrayList;
import java.util.List;

public class ActivityGroupView extends ListView {

    public int totalHeight;

    private String groupTitle;
    private List<Activity> groupActivities;
    private ToggleGroup toggleGroup;
    private ActivityCategory category;

    /**
     * Creates an activity group view for a certain group id.
     * @param groupId the group id you want to create the view of
     * @param activities list of all available activities
     */
    public ActivityGroupView(int groupId, List<Activity> activities, ToggleGroup toggleGroup) {

        this.groupActivities = new ArrayList<>();
        this.toggleGroup = toggleGroup;
        this.totalHeight = 0;

        // filter all activities that are in this group
        if (activities != null) {
            for (Activity activity : activities) {
                if (activity.getType().getCategory().getId() == groupId) {
                    groupActivities.add(activity);
                }
            }
        }
        this.category = groupActivities.get(0).getType().getCategory();

        groupTitle = category.getName();

        // capitalize first letter
        if (groupTitle != null && groupTitle.length() > 1) {
            groupTitle = groupTitle.substring(0, 1).toUpperCase() + groupTitle.substring(1);
        }

        render();
    }

    /**
     * Applies constraints and styling.
     */
    public void render() {

        // add header
        View titleView = new View();

        Text titleText = new Text(groupTitle);
        titleView.addSubview(titleText);
        titleView.setInsetsFor(titleText, 0, null, 8, 15);

        // apply styling on header
        styleSheet.set("font-family", "'Roboto Regular', sans-serif");
        styleSheet.set("font-size", "20");
        styleSheet.set("fill", "#505050");
        styleSheet.applyOn(titleText);
        styleSheet.clear();

        if (category.getImage() == null) {
            System.out.println("Category '" + category.getDesc() + "' didn't contain any icon!");
        } else {

            // custom icon
            ImageView iconView = new ImageView(category.getImage());
            iconView.setFitHeight(40.0);
            iconView.setFitWidth(40.0);
            titleView.addSubview(iconView);
            titleView.setInsetsFor(iconView,
                    null, null, 0.0,
                    TextDimension.calculateFor(titleText).getWidth() + 15.0 + 7.0
            );
        }

        append(titleView);

        totalHeight += TextDimension.calculateFor(titleText).height + 25;

        for (Activity activity: groupActivities) {
            append(getActivityView(activity, this.toggleGroup));
            totalHeight += 40;
        }

        // add spacer on bottom
        View spacer = new View();
        spacer.setMinHeight(25.0);
        append(spacer);
    }

    /**
     * Creates radio button for a given activity.
     * @param activity the activity object containing its name
     * @param toggleGroup the group that the radio button should be part of
     * @return a view representing the activity
     */
    public View getActivityView(Activity activity, ToggleGroup toggleGroup) {

        // create the radio button
        RadioButton buttonView = new RadioButton(activity.getType().getDescription());
        buttonView.setToggleGroup(toggleGroup);

        // store activity object in the radio button
        buttonView.setUserData(activity);

        // apply styling on the radio button text
        StyleSheet styleSheet = new StyleSheet();
        styleSheet.set("font-family", "'Roboto Light', sans-serif");
        styleSheet.set("font-size", "15");
        styleSheet.set("text-fill", "#505050");
        styleSheet.applyOn(buttonView);
        styleSheet.clear();

        // set insets for the radio button
        View activityView = new View();
        activityView.addSubview(buttonView);
        activityView.setInsetsFor(buttonView, 10, null, null, 15);

        int height = 40;
        activityView.setPrefHeight(height);
        activityView.setMinHeight(height);
        activityView.setMaxHeight(height);

        return activityView;
    }
}
