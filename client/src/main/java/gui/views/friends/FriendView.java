package gui.views.friends;

import static gui.helpers.StyleSheet.MouseState.HOVERED;
import static gui.helpers.StyleSheet.MouseState.NONE;
import static gui.helpers.StyleSheet.MouseState.PRESSED;

import controller.DataAccess;
import gui.App;
import gui.helpers.StyleSheet;
import gui.views.general.AlertView;
import gui.views.general.ButtonView;
import gui.views.general.PictureView;
import gui.views.general.PopupView;
import gui.views.general.View;
import gui.views.timeline.TimelineView;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import models.Achievement;
import models.User;

import java.io.InputStream;
import java.util.List;




public class FriendView extends View {

    /**
     * View that holds contents of FriendView.
     */
    private View borderView;

    /**
     * Object User that represents a Friend.
     */
    private User user;


    /**
     * View containing the friendspage.
     */
    private View view;

    /**
     * Main stage of the program.
     */
    private Stage stage;

    /**
     * Define default constructor as private to enforce passing friends.
     */
    private FriendView() {}

    /**
     * View to show an Friends.
     * @param user Friend that will be displayed in Friends.
     * @param view View containing the friend request.
     */
    public FriendView(User user, View view, Stage stage) {
        super();

        this.user = user;
        this.view = view;
        this.stage = stage;

        render();
    }

    /**
     * Set a value for a CSS attribute.
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
     * Applies constraints and styling.
     */
    public void render() {
        this.getChildren().clear();

        borderView = new View();
        this.addSubview(borderView);

        this.setInsetsFor(borderView, 15);

        borderView.setMinHeight(100);

        // add border
        borderView.set("border-color", "white");
        borderView.set("border-style", "solid inside");
        borderView.set("border-width", "1");
        borderView.set("background-color", "white");
        borderView.set("background-radius", "5");
        borderView.set("border-radius", "5");


        // add profile picture
        getProfilePicture(borderView, 60.0, 10, true);

        // add user name
        Text userText = new Text(user.getUsername());
        userText.setFont(Font.font("Helvetica", 15));
        userText.setFill(Color.rgb(0, 0, 0));
        borderView.addSubview(userText);
        borderView.setInsetsFor(userText, 70, null, null, 30);

        // add points icon
        InputStream leafStream = App.class.getResourceAsStream("leaf/leaf-50px.png");
        if (leafStream == null) {
            System.out.println("The resource 'leaf/leaf-50px.png' couldn't be found!");
        } else {
            Image leafPicture = new Image(leafStream);
            ImageView leafPictureView = new ImageView(leafPicture);
            borderView.addSubview(leafPictureView);
            borderView.setInsetsFor(leafPictureView, 25, null, null, 280);

            Text pointsText = new Text(Integer.toString(user.getTotalPoints()));
            styleSheet.set("font-family", "'Roboto Regular', sans-serif");
            styleSheet.set("font-size", "30");
            styleSheet.set("fill", "#7BB242");
            styleSheet.applyOn(pointsText);
            styleSheet.clear();
            borderView.addSubview(pointsText);
            borderView.setInsetsFor(pointsText, 25, null, null, 210);
        }

        // adds badge pictures
        getBadges(user, borderView, 3, 35, 400, 35.0);

        //add Delete friend button
        Button deleteFriend = Friends.getButton("delete");
        borderView.addSubview(deleteFriend);
        borderView.setInsetsFor(deleteFriend, 30, 20, null, null);

        //Calls popup-window from Popup.java
        deleteFriend.setOnAction(e -> {
            PopupView.init(stage);
            AlertView alert = new AlertView(
                         "Deleting " + user.getUsername(),
                            "Are you sure you want to delete your (only) friend?",
                       "Yes",
                            "No"
            );
            alert.setHandler(
                    "Yes",
                () -> {
                    DataAccess.deleteFriend(user);
                    FriendsRequest.refreshPage(view, stage);
                }
            );
        });
    }


    private void openProfile() {
        PopupView.init(stage);
        if (DataAccess.getUserData() == null) {
            refresh();
            return;
        }

        String setting = user.getPrivacy();
        if (setting == null) {
            setting = "public";
        }
        if (setting.equals("private")) {
            new AlertView("Can't accessed",
                    "This account has been set to private and thus cannot be accessed",
                    "Ok");
        } else {
            View contentView = new View();

            //close button
            ButtonView close = new ButtonView("Close",
                    true, () -> FriendsRequest.refreshPage(view, stage) );
            contentView.addSubview(close);
            contentView.setInsetsFor(close, 30, 30, null, null);

            //profilepicture
            getProfilePicture(contentView, 100.0, 20, false);

            Text userText = new Text(user.getUsername());
            styleSheet.set("font-family", "'Roboto Regular', sans-serif");
            styleSheet.set("font-size", "50");
            styleSheet.set("fill", "#505050");
            styleSheet.applyOn(userText);
            contentView.addSubview(userText);
            contentView.setInsetsFor(userText, 20, null, null, 150);


            // add points
            InputStream leafStream = App.class.getResourceAsStream("leaf/leaf-50px.png");
            if (leafStream == null) {
                System.out.println("The resource 'leaf/leaf-50px.png' couldn't be found!");
            } else {
                Image leafPicture = new Image(leafStream);
                ImageView leafPictureView = new ImageView(leafPicture);
                contentView.addSubview(leafPictureView);
                contentView.setInsetsFor(leafPictureView, 80, null, null, 230);
            }
            Text totalPoints = new Text(Integer.toString(user.getTotalPoints()));
            styleSheet.set("font-size", "35");
            styleSheet.set("fill", "#7BB242");
            styleSheet.applyOn(totalPoints);
            contentView.addSubview(totalPoints);
            contentView.setInsetsFor(totalPoints, 80, null, null, 150);



            //badges text
            Text badges = new Text("Badges");
            styleSheet.set("font-size", "33");
            styleSheet.set("fill", "#505050");
            styleSheet.applyOn(badges);
            contentView.addSubview(badges);
            contentView.setInsetsFor(badges, 130, null, null, 25);

            //badges
            getBadges(user, contentView, 12, 180, 20, 50.0);

            //timeline
            TimelineView friendTimeLine = new TimelineView(
                (fromDate, toDate) ->
                        DataAccess.getActivityHistory(false, fromDate, toDate, user.getUsername() ),
                    3,
                    "Timeline",
                    stage
            );
            friendTimeLine.refresh();
            contentView.addSubview(friendTimeLine);
            contentView.setInsetsFor(friendTimeLine, 250, 10, 0, 10);

            view.getChildren().clear();
            view.addSubview(contentView);
        }

    }

    /**
     * Adds a profile picture to the view.
     * @param borderView View containing the friend.
     */
    private void getProfilePicture(View borderView, double imgSize, int top, boolean getOverlay) {

        if (user.getPicturePath() != null) {
            Image pf = DataAccess.getImageForUrl(DataAccess.host + user.getPicturePath(),
                    null, imgSize);
            if (pf.isError()) {
                pf = new Image(App.class.getResourceAsStream("profile.png"));
            }

            PictureView pictureView = new PictureView(pf, imgSize);
            pictureView.setOnMouseClicked(e -> openProfile());
            borderView.addSubview(pictureView);
            borderView.setInsetsFor(pictureView, top, null, null, 25);

            if (getOverlay) {
                // create overlay view
                View overlayView = new View();
                overlayView.setMaxSize(imgSize, imgSize);

                // create dark see through background
                View backgroundView = new View();
                overlayView.addSubview(backgroundView);
                overlayView.setInsetsFor(backgroundView, 0);
                backgroundView.set("background-color", "black");
                backgroundView.setOpacity(0.5);

                // create overlay text
                Text overlayText = new Text("show profile");
                overlayText.setTextAlignment(TextAlignment.CENTER);
                overlayText.setWrappingWidth(40);
                StyleSheet styleSheet = new StyleSheet();
                styleSheet.set("font-size", "13");
                styleSheet.set("font-family", "'Roboto Light', sans-serif");
                styleSheet.set("fill", "white");
                styleSheet.applyOn(overlayText);
                overlayView.addSubview(overlayText);

                // add overlay to picture view
                pictureView.addOverlay(overlayView);

                // set show on hover
                pictureView.setOnMouseExited(e -> pictureView.setOverlayVisible(false));
                pictureView.setOnMouseEntered(e -> pictureView.setOverlayVisible(true));


                // make overlay even darker on click
                backgroundView.setOnMouseReleased(e -> {
                    styleSheet.set("fill", "white");
                    styleSheet.applyOn(overlayText);
                    backgroundView.setOpacity(0.5);
                });
                backgroundView.setOnMousePressed(e -> {
                    styleSheet.set("fill", "#bababa");
                    styleSheet.applyOn(overlayText);
                    backgroundView.setOpacity(0.65);
                });

                overlayText.onMousePressedProperty().bind(backgroundView.onMousePressedProperty());
                overlayText.onMouseReleasedProperty()
                        .bind(backgroundView.onMouseReleasedProperty());
            }
        }
    }

    /**
     * Shows the three badges the friend user has gotten from an achievement.
     * @param user User which is a friend.
     * @param borderView View containing the friend.
     */
    private void getBadges(User user, View borderView,
                           int amount, int top, int left, double imgSize) {
        List<Achievement> achievements =  DataAccess.getFriendsAchievements(user);

        HBox badgeCollection = new HBox();
        badgeCollection.setSpacing(5);

        int stop = amount;
        for (int i = 0; i < achievements.size(); i++) {
            if (stop == 0) {
                break;
            }

            Achievement achievement = achievements.get(i);
            if (achievement.isAcquired()) {
                Image badgePicture = DataAccess.getImageForUrl(DataAccess.host
                        + achievement.getBigBadgePath(), imgSize, imgSize);
                PictureView pictureView;

                if (badgePicture.isError()) {
                    badgePicture = new Image(App.class.getResourceAsStream("randomBADGE.png"));
                    pictureView = new PictureView(badgePicture, imgSize);
                } else {
                    pictureView = new PictureView(badgePicture, imgSize);
                    pictureView.setOnMouseClicked(e -> popupBadge(achievement));
                }

                badgeCollection.getChildren().add(pictureView);
                stop--;
            }
        }
        if (stop != 0) {
            while (stop != 0) {
                Image badgePicture = new Image(App.class.getResourceAsStream("randomBADGE.png"));
                PictureView pictureView = new PictureView(badgePicture, imgSize);
                badgeCollection.getChildren().add(pictureView);
                stop--;
            }
        }
        borderView.addSubview(badgeCollection);
        borderView.setInsetsFor(badgeCollection, top, null, null, left);
    }

    /**
     * Creates a popup for when user clicks on the badge of a Friend.
     * @param achievement The badge which will be displayed in the popup.
     */
    private void popupBadge(Achievement achievement) {
        PopupView.init(stage);
        View contentView = new View();
        double imgSize = 150.0;

        contentView.set("background-color", "white");//"#f5fef2");
        contentView.set("background-insets", "10");
        contentView.set("effect", "dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.5, 0.0, 0.0)");


        Image badgePicture = DataAccess.getImageForUrl(DataAccess.host
                + achievement.getBigBadgePath(), imgSize, imgSize);
        PictureView pictureView = new PictureView(badgePicture, imgSize);
        contentView.addSubview(pictureView);
        contentView.setInsetsFor(pictureView, 20, null, null, 100);

        Text titleText = new Text(achievement.getTitle());
        contentView.addSubview(titleText);
        contentView.setInsetsFor(titleText, 170, null, null, null);
        StyleSheet styleSheet = new StyleSheet();
        styleSheet.set("font-family", "'Roboto Regular', sans-serif");
        styleSheet.set("font-size", "23");
        styleSheet.set("fill", "#87cc95");
        styleSheet.applyOn(titleText);

        Text desc = new Text("To get this badge, " + achievement.getDesc());
        desc.wrappingWidthProperty().setValue(300.0);
        contentView.addSubview(desc);
        contentView.setInsetsFor(desc, null, null, 80, 25);
        styleSheet.set("font-size", "18");
        styleSheet.set("fill", "#505050");
        styleSheet.applyOn(desc);

        Text close = new Text("Close");
        contentView.addSubview(close);
        contentView.setInsetsFor(close, null, null, 30, null);
        styleSheet.set("font-family", "'Roboto Regular', sans-serif");
        styleSheet.set("font-size", "20");
        styleSheet.set(NONE, "fill", "#505050");
        styleSheet.set(HOVERED, "fill", "#303030");
        styleSheet.set(PRESSED, "fill", "#101010");
        styleSheet.applyOn(close);
        styleSheet.clear();

        PopupView pop = new PopupView(contentView, 350, 350);
        close.setOnMouseClicked(e -> pop.close());
    }



}
