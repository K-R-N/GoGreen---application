package gui.views.friends;

import gui.helpers.ObjectRefresher;
import gui.helpers.StyleSheet;
import gui.views.general.AlertView;
import gui.views.general.ListView;
import gui.views.general.ScrollView;
import gui.views.general.View;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import models.User;

import java.util.List;

public class FriendListView extends ScrollView {

    /**
     * Handle to refresh list of friends.
     */
    private ObjectRefresher<List<User>> refresher;

    /**
     * Variable that holds shown activities in list.
     */
    private List<User> users;

    /**
     * View that holds all users.
     */
    private ListView listView;

    /**
     * View containing the friendspage.
     */
    private View view;

    /**
     * Main stage of the program.
     */
    private Stage stage;

    /**
     * Define default constructor as private to enforce passing activities.
     */
    private FriendListView() {}

    /**
     * Constructor of the activity list component.
     * @param refresher list of FriendListView objects which will be shown in the window
     * @param view View containing the friends page.
     * @param stage Main stage of the program.
     */
    public FriendListView(ObjectRefresher<List<User>> refresher, View view, Stage stage) {
        super(null);


        // disable horizontal scrollbar
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        this.stage = stage;
        this.view = view;
        this.refresher = refresher;
        this.listView = new ListView();

        this.setSubview(listView);


        refresh();
    }

    /**
     * Setter to update list of friends.
     * @param users List of friends which will be shown in the window.
     */
    public void setUsers(List<User> users) {
        this.users = users;
        render();
    }

    /**
     * Set a value for a CSS attribute.
     * @param attribute string of the JavaFX attribute
     * @param value value of the JavaFX attribute
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
     * @throws NullPointerException For when list doesn't contain any friends.
     */
    public void refresh() throws NullPointerException {
        setUsers(refresher.get());

        if (users == null) {
            new AlertView(
                    "Could not retrieve your friends",
                    "Failed to retrieve your friends, "
                            + "this probably means you don't have an internet connection",
                    "okay :("
            );
            return;
        }
    }


    /**
     * Adds all subviews and sets constraints.
     */
    private void render() {
        // clear old subviews
        listView.clear();


        if (!hasFriends()) {
            return;
        }

        for (User user: users) {
            FriendView rank = new FriendView(user, view, stage);
            listView.append(rank);
        }
    }

    /**
     * Checks if there are any friends.
     * @return Boolean, in case if there are no requests return false, else returns true;
     */
    public boolean hasFriends() {
        if (users == null || users.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

}
