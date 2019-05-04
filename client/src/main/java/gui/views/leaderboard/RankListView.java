package gui.views.leaderboard;

import gui.helpers.ObjectRefresher;
import gui.helpers.StyleSheet;
import gui.views.general.AlertView;
import gui.views.general.PopupView;
import gui.views.general.ScrollView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.User;

import java.util.List;

public class RankListView extends ScrollView {

    /**
     * Variable that holds shown activities in list.
     */
    private List<User> users;


    /**
     * Object containing the list of users from highest scoring to lowest.
     */
    private ObjectRefresher<List<User>> refresher;

    /**
     * Main stage of the program.
     */
    private Stage stage;

    /**
     * View that holds all users.
     */
    private VBox listView;

    /**
     * Define default constructor as private to enforce passing activities.
     */
    private RankListView() {}

    /**
     * Constructor of the activity list component.
     * @param refresher list of RankListView objects which will be shown in the window
     */
    public RankListView(ObjectRefresher<List<User>> refresher, Stage stage) {
        super("Leaderboard");

        this.refresher = refresher;
        this.listView = new VBox();
        this.stage = stage;

        listView.setFillWidth(true);
        this.setSubview(listView);

        this.set("background", "transparent");

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
        if (listView != null) {
            setNode(state, listView, attribute, value);
        }
    }

    /**
     * Refreshes the view contents.
     */
    @Override
    public void refresh() {
        this.users = refresher.get();

        if (users == null) {
            PopupView.init(stage);
            new AlertView(
                    "Could not retrieve leaderboard",
                    "Failed to retrieve your leaderboard, "
                            + "this probably means you don't have an internet connection",
                    "okay :(");
        }
        render();
    }

    /**
     * Adds all subviews and sets constraints.
     */
    private void render() {
        if (users == null) {
            return;
        }

        // clear gui.deprecated subviews
        listView.getChildren().clear();
        int rankNum = 0;
        for (User user: users) {
            rankNum++;
            RankView rank = new RankView(user, stage, rankNum);
            listView.getChildren().add(rank);
        }
    }
}
