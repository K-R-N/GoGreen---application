package gui.views.achievement;

import gui.helpers.StyleSheet;
import gui.views.general.CardView;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import models.Achievement;

public class PageAchievementView extends CardView {

    private Achievement achievement;

    private PageAchievementView() {}

    /**
     * View to show an ranking.
     * @param achievement that will be displayed in the ranking
     */
    public PageAchievementView(Achievement achievement) {
        super(132);

        this.achievement = achievement;

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
    }

    /**
     * Applies constraints and styling.
     */
    public void render() {

        // add points icon
        ImageView badgePictureView = new ImageView(achievement.getBigBadgeImage());
        contentView.addSubview(badgePictureView);
        contentView.setInsetsFor(badgePictureView, null, null, null, 18);

        if (!achievement.isAcquired()) {
            ColorAdjust grayoutFilter = new ColorAdjust();
            grayoutFilter.setSaturation(-1);
            grayoutFilter.setBrightness(0.6);
            badgePictureView.setEffect(grayoutFilter);
        }


        double total = 1;
        double progress = achievement.isAcquired() ? 1 : 0;

        ProgressBar pb = new ProgressBar(0);
        pb.setStyle("-fx-accent: lightgreen");
        pb.setProgress(progress / total);
        contentView.addSubview(pb);
        contentView.setInsetsFor(pb, null, 25, null, null);

        Text pbStatus = new Text((int) progress + "/" + (int) total);
        StyleSheet styleSheet = new StyleSheet();
        styleSheet.set("font-family", "'Roboto Regular', sans-serif");
        styleSheet.set("font-size", "14");
        styleSheet.set("fill", "#808080");
        styleSheet.applyOn(pbStatus);
        styleSheet.clear();
        contentView.addSubview(pbStatus);
        contentView.setInsetsFor(pbStatus, 35, 65, null, null);

        Text achievementTitle = new Text(achievement.getTitle());
        styleSheet.set("font-family", "'Roboto Light', sans-serif");
        styleSheet.set("font-size", "25");
        styleSheet.set("fill", "#505050");
        styleSheet.applyOn(achievementTitle);
        styleSheet.clear();
        contentView.addSubview(achievementTitle);
        contentView.setInsetsFor(achievementTitle, 25, null, null, null);

        String desc = null;
        if (achievement.isAcquired()) {
            desc = "gotten by " + achievement.getDesc().replace("complete", "completing");
        } else {
            desc = "to get this yourself, " + achievement.getDesc();
        }

        Text achievementDesc = new Text(desc);
        achievementDesc.setTextAlignment(TextAlignment.CENTER);
        achievementDesc.setWrappingWidth(400.0);
        styleSheet.set("font-family", "'Roboto Light', sans-serif");
        styleSheet.set("font-size", "17");
        styleSheet.set("fill", "#606060");
        styleSheet.applyOn(achievementDesc);
        styleSheet.clear();
        contentView.addSubview(achievementDesc);
        contentView.setInsetsFor(achievementDesc, 60, null, null, null);
    }
}
