package gui.views.friends;

import gui.helpers.ObjectRefresher;
import gui.helpers.StyleSheet;
import gui.views.general.ListView;
import gui.views.general.ScrollView;
import gui.views.general.View;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import models.User;

import java.util.List;

public class RequestListView extends ScrollView {

    /**
     * Handle to refresh list of friends.
     */
    private ObjectRefresher<List<User>> refresher;

    /**
     * Variable that holds all FriendsRequests.
     */
    private List<User> requests;

    /**
     * View that holds all FriendsRequests.
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
     * String to indicate which RequestView to show.
     */
    private String type;

    /**
     * Define default constructor as private to enforce passing FriendsRequests.
     */
    private RequestListView() {}

    /**
     * List of RequestView objects.
     * @param refresher Object containing list of FriendRequest the user has.
     */
    public RequestListView(ObjectRefresher<List<User>> refresher, View view,
                           Stage stage, String type) {
        super(null);


        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.view = view;
        this.refresher = refresher;
        this.stage = stage;
        this.type = type;

        this.listView = new ListView();

        this.setSubview(listView);

        refresh();

    }

    /**
     * Setter to update list of activities.
     * @param requests list of RequestView objects which will be shown in the window
     */
    public void setRequests(List<User> requests) {
        this.requests = requests;
        render();
    }

    /**
     * Set a value for a CSS attribute.
     * @param state StyleSheet state.
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
     *  Refreshes the view contents.
     * @throws NullPointerException For when list doesn't contain any requests.
     */
    @Override
    public void refresh() throws NullPointerException {
        try {
            setRequests(refresher.get());
        } catch (NullPointerException e) {
            System.out.println("RequestList is empty");
        }
    }

    /**
     * Adds all subviews and sets constraints.
     */
    private void render() {
        // clear old subviews
        listView.clear();

        if (!hasRequest()) {
            return;
        }

        for (User req: requests) {
            RequestView request = new RequestView(req, view, stage, type);
            listView.append(request);
        }
    }

    /**
     * Checks if there are any requests.
     * @return Boolean, in case if there are no requests return false, else returns true;
     */
    public boolean hasRequest() {
        if (requests == null || requests.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }



}
