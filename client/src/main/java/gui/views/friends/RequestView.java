package gui.views.friends;

import controller.DataAccess;
import gui.App;
import gui.helpers.StyleSheet;
import gui.views.general.AlertView;
import gui.views.general.PictureView;
import gui.views.general.PopupView;
import gui.views.general.View;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.User;

import java.io.InputStream;

public class RequestView extends View {

    /**
     * View that holds contents of RequestView.
     */
    private View borderView;

    /**
     * Object User that represents a Friend.
     */
    private User request;

    /**
     * View containing the friendspage.
     */
    private View view;

    /**
     * Main stage of the program.
     */
    private Stage stage;

    /**
     * Define default constructor as private to enforce passing requests.
     */
    private RequestView() {}

    /**
     * View to show friend requests.
     * @param request Friend requests from a potential friend.
     * @param view View containing the friend request.
     * @param stage Main stage of the program.
     * @param type String to indicate which RequestView to show.
     */
    public RequestView(User request, View view, Stage stage, String type) {
        this.request = request;
        this.view = view;
        this.stage = stage;

        if (type.equals("incoming")) {
            renderIncoming();
        } else {
            renderOutgoing();
        }
    }


    /**
     * Set a value for a CSS attribute.
     * @param state mouse state for styling
     * @param attribute string of the JavaFX attribute
     * @param value value of the JavaFX attribute
     */
    @Override
    public void set(StyleSheet.MouseState state, String attribute, String value) {
        super.set(attribute, value);
        if (borderView != null) {
            setNode(state, borderView, attribute, value);
        }
    }

    /**
     * Applies constraints and styling for incoming friend requests.
     */
    public void renderIncoming() {
        this.getChildren().clear();

        borderView = new View();
        this.addSubview(borderView);

        this.setInsetsFor(borderView, 5, 15,5,15);

        borderView.setMinHeight(40);

        // add border
        borderView.set("border-style", "solid inside");
        borderView.set("background-color", "white");
        borderView.set("border-width", "1");
        borderView.set("border-color", "white");
        borderView.set("border-radius", "5");
        borderView.set("background-radius", "5");

        getProfilePicture(request, borderView);

        // add user name
        Text userText = new Text(request.getUsername());
        userText.setFont(Font.font("Helvetica", 15));
        userText.setFill(Color.rgb(0, 0, 0));
        borderView.addSubview(userText);
        borderView.setInsetsFor(userText, 10, null, null, 65);

        //add Text
        Text requestText = new Text(" wants to become your friend!");
        requestText.setFont(Font.font("Helvetica", 15));
        requestText.setFill(Color.rgb(0, 0, 0));
        borderView.addSubview(requestText);
        borderView.setInsetsFor(requestText, 10, null, null, 250);


        //add Accept button
        InputStream acceptImg = App.class.getResourceAsStream("Check.png");
        InputStream acceptImg2 = App.class.getResourceAsStream("Check2.png");
        Image accept = new Image(acceptImg);
        Image accept2 = new Image(acceptImg2);
        ImageView acceptView = responseButton(accept);
        borderView.addSubview(acceptView);
        borderView.setInsetsFor(acceptView, 5, 80, null, null);

        //Creates hover effect for accept button
        acceptView.setOnMouseEntered(e -> {
            acceptView.setImage(accept2);
        });
        acceptView.setOnMouseExited(e -> {
            acceptView.setImage(accept);
        });

        acceptView.setOnMouseClicked(e -> {
            PopupView.init(stage);
            AlertView alert = new AlertView("Accepting friend request",
                    "Are you sure you want to accept " + request.getUsername()
                            + "'s friend request?",
                    "Yes",
                    "No");
            alert.setHandler(
                    "Yes",
                () -> {
                    DataAccess.acceptFriend(request);
                    FriendsRequest.refreshPage(view, stage);
                });
        });

        //add Decline button
        InputStream declineImg = App.class.getResourceAsStream("Decline.png");
        InputStream declineImg2 = App.class.getResourceAsStream("Decline2.png");
        Image decline = new Image(declineImg);
        Image decline2 = new Image(declineImg2);
        ImageView declineView = responseButton(decline);
        borderView.addSubview(declineView);
        borderView.setInsetsFor(declineView, 5, 30, null, null);

        //Creates hover effect for decline button
        declineView.setOnMouseEntered(e -> {
            declineView.setImage(decline2);
        });
        declineView.setOnMouseExited(e -> {
            declineView.setImage(decline);
        });

        //Calls popup window for declining a friends request.
        declineView.setOnMouseClicked(e -> {
            PopupView.init(stage);
            AlertView alert = new AlertView("Declining friend request",
                    "Are you sure you want to decline " + request.getUsername()
                            + "'s friend request?",
                    "Yes",
                    "No");
            alert.setHandler(
                    "Yes",
                () -> {
                    DataAccess.declineFriend(request);
                    FriendsRequest.refreshPage(view, stage);
                });
        });

        //Add event for when view is clicked
        userText.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            openProfile(request);
            event.consume();
        });

        requestText.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            openProfile(request);
            event.consume();
        });
    }

    /**
     * Applies constraints and styling for outgoing friend requests.
     */
    public void renderOutgoing() {
        this.getChildren().clear();

        borderView = new View();
        this.addSubview(borderView);

        this.setInsetsFor(borderView, 5, 15,5,15);

        borderView.setMinHeight(40);

        // add border
        borderView.set("border-style", "solid inside");
        borderView.set("background-color", "white");
        borderView.set("border-width", "1");
        borderView.set("border-color", "white");
        borderView.set("border-radius", "5");
        borderView.set("background-radius", "5");

        getProfilePicture(request, borderView);

        // add user name
        Text userText = new Text(request.getUsername());
        userText.setFont(Font.font("Helvetica", 15));
        userText.setFill(Color.rgb(0, 0, 0));
        borderView.addSubview(userText);
        borderView.setInsetsFor(userText, 10, null, null, 65);

        //add Text
        Text requestText = new Text("Friend request sent to " + request.getUsername()
                + " is still pending");
        requestText.setFont(Font.font("Helvetica", 15));
        requestText.setFill(Color.rgb(0, 0, 0));
        borderView.addSubview(requestText);
        borderView.setInsetsFor(requestText, 10, null, null, 250);

        //add revoke friend request button
        Button revokeFriend = Friends.getButton("revoke");
        revokeFriend.setStyle("-fx-font-size: 15");
        borderView.addSubview(revokeFriend);
        borderView.setInsetsFor(revokeFriend, 4, 40, null, null);

        //Calls popup-window from Popup.java
        revokeFriend.setOnAction(e -> {
            PopupView.init(stage);
            AlertView alert = new AlertView(
                    "Revoking " + request.getUsername() + "'s friend-request",
                    "Are you sure you want to revoke this friend-request?",
                    "Yes",
                    "No"
            );
            alert.setHandler(
                "Yes",
                () -> {
                    DataAccess.revokeFriendRequest(request.getUsername());
                    System.out.println("Friend request "
                            + request.getUsername() + " has been revoked");
                    FriendsRequest.refreshPage(view, stage);
                }
            );

        });

    }

    /**
     * Creates ImageView for the accept and decline buttons.
     * @param image Image used for accept/decline.
     * @return ImageView containing the image with set height and width.
     */
    private static ImageView responseButton(Image image) {
        ImageView img = new ImageView(image);
        img.setFitHeight(25);
        img.setFitWidth(35);
        return img;
    }

    /**
     * Opens profile of the possible friend from the request.
     * @param request Friend from which you can open his profile.
     */
    private static void openProfile(User request) {
        System.out.println(request.getUsername() + " has been clicked!");
    }

    /**
     * Adds a profile picture to the view.
     * @param request Friend from the friend request.
     * @param borderView View containing the friend request.
     */
    private static void getProfilePicture(User request, View borderView) {
        double imgSize = 30.0;
        double borderWidth = 2.0;


        if (request.getPicturePath() != null) {

            View pbView = new View();
            pbView.setMaxWidth(imgSize + borderWidth * 2.0);
            pbView.setMaxHeight(imgSize + borderWidth * 2.0);
            pbView.set("background-color", "white");
            pbView.set("background-radius", Double.toString(imgSize + borderWidth * 2));
            borderView.addSubview(pbView);
            borderView.setInsetsFor(pbView,
                    13.0 - borderWidth,
                    null, null,
                    20.0 - borderWidth
            );

            Image pf = DataAccess.getImageForUrl(DataAccess.host
                    + request.getPicturePath(), null, imgSize);

            if (pf.isError()) {
                pf = new Image(App.class.getResourceAsStream("profile.png"));
            }

            PictureView pictureView = new PictureView(pf, imgSize);


            borderView.addSubview(pictureView);
            borderView.setInsetsFor(pictureView, 5, null, null, 20);
        }


    }



}
