package gui.views.friends;

import controller.DataAccess;
import gui.App;
import gui.helpers.AutoCompleteTextField;
import gui.helpers.ObjectRefresher;
import gui.views.general.AlertView;
import gui.views.general.PopupView;
import gui.views.general.View;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.User;

import java.util.ArrayList;
import java.util.List;

public class Friends {

    protected static HBox friendLabel() {
        HBox labels = new HBox();
        Label name = new Label("Friends:");
        name.setPadding(new Insets(0, 0, 0,30));
        Label points = new Label("Total Points:");
        Label badges = new Label("Badges:");

        labels.getChildren().addAll(name, points, badges);
        labels.setSpacing(120);
        labels.getStyleClass().add("rank-label");
        labels.getStylesheets().add(App.class.getResource("HomeScreenLight.css").toExternalForm());
        return labels;

    }

    /**
     * Bottom Pane containing a search tool for finding new friends.
     * @return HBox with search tool.
     */
    private static HBox addNewFriend(View view, Stage stage) {

        Label search = new Label("Search:");
        search.getStyleClass().add("Search");
        search.setPadding(new Insets(0,0,0,220));

        //Created custom text field which gives suggestions for names
        AutoCompleteTextField field = new AutoCompleteTextField();

        field.textProperty().addListener((observable, oldValue, newValue) -> {
            field.getEntries().clear();
            List<String> suggestions = getSuggestions(
                () -> DataAccess.getUserSuggestions(newValue)
            );
            field.getEntries().addAll(suggestions);
        });


        field.setPromptText("e.g. JohnCena");
        Button sent = new Button("Add");
        sent.setOnAction(e -> addedFriend(field.getText(), view, stage));
        sent.getStyleClass().add("Add-Friend");
        HBox bar = new HBox();
        bar.getChildren().addAll(search, field, sent);
        bar.setSpacing(20);
        bar.getStylesheets().add(App.class.getResource("HomeScreenLight.css").toExternalForm());
        return bar;
    }

    /**
     * Checks if search field is empty.
     * If not, calls popup window.
     * @param name Username the user searches for.
     */
    private static void addedFriend(String name, View view, Stage stage) {
        PopupView.init(stage);
        if (name.isBlank()) {
            new AlertView("Empty input", "Input can't be empty", "Accept");
        } else {

            User user = new User(0, name, null);

            AlertView alert = new AlertView("Adding " + name,
                     "Are you sure you want to add " + name + "?",
                              "Yes",
                             "No");
            alert.setHandler(
                    "Yes",
                () -> {
                    if (!DataAccess.sendFriendRequest(user)) {
                        new AlertView("Can't find " + user.getUsername(),
                                "User " + user.getUsername() + " doesn't exist",
                                "Ok" );
                    } else {
                        FriendsRequest.refreshPage(view, stage);
                    }
                }
            );
        }
    }

    /**
     * Creates Stackpane with top Pane and bottom Pane and a FriendListView in the middle.
     * In case User has FriendRequests, it will show at the top,
     * otherwise it won't show up.
     * @param list FriendListView containing the friends of the user.
     * @return StackPane containing the friendspage
     */
    private static View friendPage(FriendListView list, View view, Stage stage) {
        View friends = new View();
        HBox label = Friends.friendLabel();
        label.setStyle("-fx-background-color: #DCEADD");
        HBox search = Friends.addNewFriend(view, stage);
        friends.setInsetsFor(label, 120, 0,0,0);
        friends.setInsetsFor(search,625, 0, 0, 0);

        if (list.hasFriends()) {
            View reqList = FriendsRequest.getRequestsIncoming(view, stage, "incoming");
            friends.setInsetsFor(reqList,0, 0, 120, 0);
            friends.setInsetsFor(list,150,0,60,0);
            friends.getChildren().addAll(reqList,label, list, search);
        } else {
            Label text = new Label("You don't have any friends yet! "
                   + "You can add new friends with the search tool at the bottom.");
            text.getStyleClass().add("empty-request");
            text.getStylesheets().add(
                    App.class.getResource("HomeScreenLight.css").toExternalForm());
            View reqList = FriendsRequest.getRequestsIncoming(view, stage, "incoming");
            friends.getChildren().addAll(reqList,label, text, search);
        }

        return friends;
    }

    /**
     *  Creates a View out of a StackPane containing the friendspage.
     * @return View containing the friends page
     */
    public static View getFriendPage(Stage stage) {
        View friend = new View();
        FriendListView friendsList = new FriendListView(
            () -> DataAccess.getFriends(),
                friend,
                stage
        );
        friendsList.set("background-color", "#DCEADD");
        StackPane pane = friendPage(friendsList, friend, stage);
        friend.addSubview(pane);
        friend.setInsetsFor(pane, 15, 0, 0, 0);
        return friend;
    }

    /**
     * Creates deletebutton for FriendView.
     * @return Button to delete friends.
     */
    public static Button getButton(String text) {
        Button deleteFriend = new Button();
        deleteFriend.setText(text);
        deleteFriend.getStyleClass().add("Delete-Friend");
        deleteFriend.getStylesheets().add(
                App.class.getResource("HomeScreenLight.css").toExternalForm());
        return deleteFriend;
    }

    private static List<String> getSuggestions(ObjectRefresher<List<User>> refresher) {
        List<String> suggestions = new ArrayList<>();

        for (User u: refresher.get()) {
            suggestions.add(u.getUsername());
        }

        return suggestions;
    }

}
