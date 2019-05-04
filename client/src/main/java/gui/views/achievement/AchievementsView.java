package gui.views.achievement;

import gui.helpers.ObjectRefresher;
import gui.helpers.StyleSheet;
import gui.views.general.AlertView;
import gui.views.general.PopupView;
import gui.views.general.ScrollView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Achievement;

import java.util.Comparator;
import java.util.List;

public class AchievementsView extends ScrollView {

    /**
     * Variable that holds shown activities in list.
     */
    private List<Achievement> achievements;


    private ObjectRefresher<List<Achievement>> refresher;

    /**
     * Stage containing the main application.
     */
    private Stage stage;

    /**
     * View that holds all users.
     */
    private VBox listView;

    /**
     * Define default constructor as private to enforce passing activities.
     */
    private AchievementsView() {}

    /**
     * Constructor of the activity list component.
     * @param refresher list of PageAchievementView objects which will be shown in the window
     */
    public AchievementsView(ObjectRefresher<List<Achievement>> refresher, Stage stage) {
        super("Achievements");

        this.refresher = refresher;
        this.listView = new VBox();
        this.stage = stage;

        listView.setFillWidth(true);
        this.setSubview(listView);

        this.set("background", "transparent");

        refresh();
    }

    /**
     * Set a value for a CSS attribute.
     * @param attribute string of the JavaFX CSS attribute
     * @param value string of the JavaFX CSS value
     */
    @Override
    public void set(StyleSheet.MouseState state, String attribute, String value) {
        super.set(state, attribute, value);
        if (listView != null) {
            setNode(state, listView, attribute, value);
        }
    }

    /**
     * Refreshes the view contents.
     */
    @Override
    public void refresh() {
        this.achievements = refresher.get();

        if (achievements == null) {
            PopupView.init(stage);
            new AlertView(
                    "Could not retrieve your achievements",
                    "Failed to retrieve your achievements, "
                            + "this probably means you don't have an internet connection",
                    "okay :("
            );
            listView.getChildren().clear();
            return;
        }

        this.achievements.sort(Comparator.comparing(Achievement::getAchievementId));

        render();
    }

    /**
     * Adds all subviews and sets constraints.
     */
    private void render() {
        if (achievements == null) {
            return;
        }

        // clear gui.deprecated subviews
        listView.getChildren().clear();

        for (Achievement achievement: achievements) {
            PageAchievementView achievementView = new PageAchievementView(achievement);
            listView.getChildren().add(achievementView);
        }
    }
}
