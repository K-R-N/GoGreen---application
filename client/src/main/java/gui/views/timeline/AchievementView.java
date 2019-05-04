package gui.views.timeline;

import gui.helpers.StyleSheet;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import models.Achievement;

/**
 * View to show an activity.
 * @author Jules van der Toorn
 */
public class AchievementView extends ContributionView {

    private Achievement achievement;

    /**
     * Define default constructor as private to enforce passing activity.
     */
    private AchievementView() {}

    /**
     * Renders the activity view.
     * @param achievement the achievement that will be displayed
     */
    public AchievementView(Achievement achievement) {
        super(achievement.getUser(), achievement.getDate(), achievement.getTime(), 132);

        this.achievement = achievement;

        render();
    }

    /**
     * Applies constraints and styling.
     */
    private void render() {

        StyleSheet styleSheet = new StyleSheet();
        // add activity title
        Text activityText = new Text("Got the badge '" + achievement.getTitle() + "'");
        styleSheet.set("font-family", "'Roboto Light', sans-serif");
        styleSheet.set("font-size", "20");
        styleSheet.set("fill", "#505050");
        styleSheet.applyOn(activityText);
        styleSheet.clear();
        contentView.addSubview(activityText);
        contentView.setInsetsFor(activityText, 72, null, null, 11);

        // add activity title
        Text descText = new Text("To get this yourself, " + achievement.getDesc());
        styleSheet.set("font-family", "'Roboto Light', sans-serif");
        styleSheet.set("font-size", "14");
        styleSheet.set("fill", "#505050");
        styleSheet.applyOn(descText);
        styleSheet.clear();
        contentView.addSubview(descText);
        contentView.setInsetsFor(descText, 102, null, null, 11);

        // add badge icon
        ImageView badgePictureView = new ImageView(achievement.getBigBadgeImage());
        contentView.addSubview(badgePictureView);
        contentView.setInsetsFor(badgePictureView, null, 18, null, null);
    }
}