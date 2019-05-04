package gui;

import controller.DataAccess;
import gui.helpers.DraggableNode;
import gui.views.achievement.AchievementsView;
import gui.views.activityform.ActivityFormView;
import gui.views.friends.Friends;
import gui.views.general.AlertView;
import gui.views.general.PopupView;
import gui.views.general.View;
import gui.views.leaderboard.RankListView;
import gui.views.sidebar.SidebarView;

import gui.views.timeline.TimelineView;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Entrypoint of the client application.
 */
public class App {

    /**
     * Border radius of the window.
     */
    public static int borderRadius = 10;

    /**
     * Window dimensions.
     */
    public static int width = 1000;
    public static int height = 700;

    public static SidebarView sidebar;

    public static View root;

    /**
     * Creates the scene for the provided stage.
     */

    public static void start() {
        Stage stage = new Stage();
        PopupView.init(stage);
        SidebarView.init(stage);
        LoginScreen.getIconApp(stage);

        View window = new View();
        window.set("background-radius", Integer.toString(borderRadius));
        window.set("background-color", "#DCEADD");
        window.set("background-insets", "5");
        window.set("effect", "dropshadow(gaussian, rgba(0, 0, 0, 0.4), 10, 0.3, 0.0, 0.0)");

        // ROOT
        // create a simple root view
        root = new View();
        window.addSubview(root);
        window.setInsetsFor(root, 5);
        root.set("background-radius", Integer.toString(borderRadius));

        // VIEWPORT
        // add viewport view
        View viewport = new View();
        root.addSubview(viewport);
        root.setInsetsFor(viewport, 0.0, 0.0, 0.0, SidebarView.width);

        // SIDE BAR
        // add sidebar view
        sidebar = new SidebarView(viewport, DataAccess.getUserData());
        root.addSubview(sidebar);
        root.setInsetsFor(sidebar, 0, null, 0, 0);
        sidebar.setMaxWidth(SidebarView.width);

        // adds custom entries to side bar
        initSideBar(sidebar, stage);

        // WINDOW CREATION
        // hide default top bar
        stage.initStyle(StageStyle.TRANSPARENT);
        DraggableNode.enableDrag(stage, root);

        // create a window with the root view
        Scene scene = new Scene(window, width, height);

        // load custom font from Google
        scene.getStylesheets().add("http://fonts.googleapis.com/css?family=Roboto:300");

        // show the window
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();

        // center it on the screen
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

        // start the program on the first non-profile page
        sidebar.setCurrentItem("friends");

        new AlertView(
            "Welcome to #GoGreen",
            "Get started with saving the environment!",
            "okay!"
        );
    }

    /**
     * Places all the menu bar items on the menu bar.
     * @param sidebar sidebar object
     */
    public static void initSideBar(SidebarView sidebar, Stage stage) {

        //Retrieving userdata failure
        var result3 = DataAccess.getUserData();
        if (result3 == null) {
            new AlertView(
                    "Could not retrieve your profile info",
                    "Failed to retrieve your profile info, "
                            + "this probably means you don't have an internet connection",
                    "okay :("
            );
        }

        // create activity form that's connected to the DataAccess class
        ActivityFormView activityForm = new ActivityFormView(
            () -> DataAccess.fetchActivityTypes(),
            activity -> {
                boolean success = DataAccess.addActivity(activity);
                DataAccess.getUserData();
                if (success) {
                    new AlertView(
                        "Activity added",
                        "Added the activity successfully to your profile!",
                        "nice!"
                    );
                } else {
                    new AlertView(
                        "Activity not added",
                        "Failed to add the activity to your profile, "
                                + "this probably means you don't have an internet connection",
                        "okay :("
                    );
                }
            },
                stage
        );

        // add full timeline to sidebar
        TimelineView activityHistory = new TimelineView(
            (fromDate, toDate) -> DataAccess.getActivityHistory(true, fromDate, toDate, null),
            3,
            "Timeline",
                stage
        );


        RankListView rankList = new RankListView(() -> DataAccess.getLeaderboard(), stage);
        View friendsPage = Friends.getFriendPage(stage);
        AchievementsView achievementsView =
                new AchievementsView(() -> DataAccess.getOwnAchievements(), stage);

        // create menu bar items with the views
        sidebar.addItem(activityForm, "add activity", "add-activity.png");
        sidebar.addItem(activityHistory, "timeline", "timeline.png");
        sidebar.addItem(friendsPage,"friends", "friends.png");
        sidebar.addItem(rankList, "leaderboard", "leaderboard.png");
        sidebar.addItem(achievementsView, "achievements", "achievements.png");
    }
}