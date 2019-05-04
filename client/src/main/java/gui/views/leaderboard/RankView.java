package gui.views.leaderboard;

import static gui.helpers.StyleSheet.MouseState.HOVERED;
import static gui.helpers.StyleSheet.MouseState.NONE;
import static gui.helpers.StyleSheet.MouseState.PRESSED;

import controller.DataAccess;
import gui.App;
import gui.helpers.StyleSheet;
import gui.helpers.TextDimension;
import gui.views.general.CardView;
import gui.views.general.PictureView;
import gui.views.general.PopupView;
import gui.views.general.View;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Achievement;
import models.User;

import java.io.InputStream;
import java.util.List;



public class RankView extends CardView {

    private User user;

    private Stage stage;

    private int rankNum;

    private RankView() {
    }

    /**
     * View to show an ranking.
     *
     * @param user that will be displayed in the ranking
     */
    public RankView(User user, Stage stage, int rankNum) {
        super(110);

        this.user = user;
        this.stage = stage;
        this.rankNum = rankNum;

        render();
    }

    /**
     * Set a value for a CSS attribute.
     *
     * @param attribute string of the JavaFX CSS attribute
     * @param value     string of the JavaFX CSS value
     */
    @Override
    public void set(StyleSheet.MouseState state, String attribute, String value) {
        super.set(state, attribute, value);
    }

    /**
     * Applies constraints and styling.
     */
    public void render() {

        // get profile picture from server
        double imgSize = 65.0;
        StyleSheet styleSheet = new StyleSheet();
        if (rankNum == 1 || rankNum == 2 || rankNum == 3) {
            addRankPicture(contentView, rankNum);
        } else {
            Text rankNumber = new Text(Integer.toString(rankNum));
            styleSheet.set("font-family", "'Roboto Regular', sans-serif");
            styleSheet.set("font-size", "30");
            styleSheet.set("fill", "#505050");
            styleSheet.applyOn(rankNumber);
            contentView.addSubview(rankNumber);
            contentView.setInsetsFor(rankNumber, 30, null, null, 30);
        }

        if (user.getPicturePath() != null) {
            Image pf = DataAccess.getImageForUrl(DataAccess.host
                    + user.getPicturePath(), imgSize, imgSize);

            if (pf.isError()) {
                pf = new Image(App.class.getResourceAsStream("profile.png"));
            }

            PictureView pictureView = new PictureView(pf, imgSize);


            contentView.addSubview(pictureView);
            contentView.setInsetsFor(pictureView, 13, null, null, 70);
        }



        // add user name
        Text userText = new Text(user.isMainUser() ? "you" : user.getUsername());
        styleSheet.set("font-family", "'Roboto Regular', sans-serif");
        styleSheet.set("font-size", "17");
        styleSheet.set("fill", "#505050");
        styleSheet.applyOn(userText);
        styleSheet.clear();
        contentView.addSubview(userText);
        double textWidth = TextDimension.calculateFor(userText).getWidth();
        contentView.setInsetsFor(userText, null, null, 8.0, 103.5 - textWidth / 2.0);


        // add points icon
        InputStream leafStream = App.class.getResourceAsStream("leaf/leaf-50px.png");
        if (leafStream == null) {
            System.out.println("The resource 'leaf/leaf-50px.png' couldn't be found!");
        } else {
            Image leafPicture = new Image(leafStream);
            ImageView leafPictureView = new ImageView(leafPicture);
            contentView.addSubview(leafPictureView);
            contentView.setInsetsFor(leafPictureView, null, 30, null, null);

            getBadges(user, contentView);

            Text pointsText = new Text(Integer.toString(user.getTotalPoints()));
            styleSheet.set("font-family", "'Roboto Regular', sans-serif");
            styleSheet.set("font-size", "35");
            styleSheet.set("fill", "#7BB242");
            styleSheet.applyOn(pointsText);
            styleSheet.clear();
            contentView.addSubview(pointsText);
            contentView.setInsetsFor(pointsText, null, 88.0, null, null);
        }
    }


    /**
     * Shows the three badges the friend user has gotten from an achievement.
     * @param user User which is a friend.
     * @param borderView View containing the friend.
     */
    private void getBadges(User user, View borderView) {
        List<Achievement> achievements;
        if (user.isMainUser()) {
            achievements = DataAccess.getOwnAchievements();
        } else {
            achievements = DataAccess.getFriendsAchievements(user);
        }

        if (achievements == null) {
            return;
        }
        double imgSize = 50.0;
        HBox badgeCollection = new HBox();
        badgeCollection.setSpacing(5);

        int stop = 6;
        for (int i = 0; i < achievements.size(); i++) {
            if (stop == 0) {
                break;
            }

            Achievement achievement = achievements.get(i);
            if (achievement.isAcquired()) {
                Image badgePicture = DataAccess.getImageForUrl(DataAccess.host
                        + achievement.getBigBadgePath(), imgSize, imgSize);
                PictureView pictureView;

                try {
                    if (!badgePicture.isError()) {
                        pictureView = new PictureView(badgePicture, imgSize);
                        pictureView.setOnMouseClicked(e -> popupBadge(achievement));
                        badgeCollection.getChildren().add(pictureView);
                        stop--;
                    }
                } catch (NullPointerException e) {
                    return;
                }
            }
        }

        borderView.addSubview(badgeCollection);
        borderView.setInsetsFor(badgeCollection, 30, null, null, 250);
    }

    /**
     * Creates a popup for when user clicks on the badge of a Friend.
     *
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

    private static void addRankPicture(View view, int rankNum) {
        try {
            String rankPlace;
            if (rankNum == 1) {
                rankPlace = "first.png";
            } else if (rankNum == 2) {
                rankPlace = "second.png";
            } else {
                rankPlace = "third.png";
            }
            Image rankPicture = new Image(App.class.getResourceAsStream(rankPlace));
            ImageView rank = new ImageView(rankPicture);
            rank.setFitHeight(50);
            rank.setFitWidth(50);
            view.addSubview(rank);
            view.setInsetsFor(rank, 30, null, null, 10);

        } catch (NullPointerException e) {

            StyleSheet styleSheet = new StyleSheet();
            Text rankNumber = new Text(Integer.toString(rankNum));
            styleSheet.set("font-family", "'Roboto Regular', sans-serif");
            styleSheet.set("font-size", "30");
            styleSheet.set("fill", "#505050");
            styleSheet.applyOn(rankNumber);
            view.addSubview(rankNumber);
            view.setInsetsFor(rankNumber, 30, null, null, 30);
        }

    }
}
