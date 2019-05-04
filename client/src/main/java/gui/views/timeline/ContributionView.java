package gui.views.timeline;

import controller.DataAccess;
import gui.App;
import gui.helpers.StyleSheet;
import gui.helpers.TextDimension;
import gui.views.general.CardView;
import gui.views.general.PictureView;
import gui.views.general.View;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import models.User;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * View to show an activity.
 * @author Jules van der Toorn
 */
public class ContributionView extends CardView {

    protected User user;
    protected LocalDate date;
    protected LocalTime time;

    /**
     * Define default constructor as private to enforce passing activity.
     */
    protected ContributionView() {}

    /**
     * Renders the activity view.
     * @param user owner of the contribution
     */
    public ContributionView(User user, LocalDate date, LocalTime time, double height) {
        super(height);

        this.date = date;
        this.time = time;
        this.user = user;
        if (user == null) {
            this.user = DataAccess.currentUser;
        }

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
    private void render() {

        // get profile picture from server
        double imgWidth = 50.0;
        double borderWidth = 2.0;


        if (user.getPicturePath() != null) {

            View pbView = new View();
            pbView.setMaxWidth(imgWidth + borderWidth * 2.0);
            pbView.setMaxHeight(imgWidth + borderWidth * 2.0);
            pbView.set("background-color", "white");
            pbView.set("background-radius", Double.toString(imgWidth + borderWidth * 2));
            contentView.addSubview(pbView);
            contentView.setInsetsFor(pbView,
                    10.0 - borderWidth,
                    null, null,
                    10.0 - borderWidth
            );

            PictureView pictureView = new PictureView(
                    DataAccess.getImageForUrl(DataAccess.host + user.getPicturePath(),
                            null, imgWidth),
                    50.0
            );
            contentView.addSubview(pictureView);
            contentView.setInsetsFor(pictureView, 10, null, null, 10);
        }

        // add user name
        Text userText = new Text(user.isMainUser() ? "you" : user.getUsername());
        // apply styling on header
        StyleSheet styleSheet = new StyleSheet();
        styleSheet.set("font-family", "'Roboto Regular', sans-serif");
        styleSheet.set("font-size", "19");
        styleSheet.set("fill", "#505050");
        styleSheet.applyOn(userText);
        styleSheet.clear();
        contentView.addSubview(userText);
        contentView.setInsetsFor(userText, 14, null, null, 70);

        // add points icon
        InputStream smallLeafStream = App.class.getResourceAsStream("leaf/leaf-20px.png");
        if (smallLeafStream == null) {
            System.out.println("The resource 'leaf/leaf-20px.png' couldn't be found!");
        } else {
            Image leafPicture = new Image(smallLeafStream);
            ImageView leafPictureView = new ImageView(leafPicture);
            contentView.addSubview(leafPictureView);
            contentView.setInsetsFor(leafPictureView,
                    17.0,
                    null, null,
                    70.0 + TextDimension.calculateFor(userText).getWidth() + 7.0);

            Text pointsText = new Text(Integer.toString(user.getTotalPoints()));
            styleSheet.set("font-family", "'Roboto Light', sans-serif");
            styleSheet.set("font-size", "15");
            styleSheet.set("fill", "#7BB242");
            styleSheet.applyOn(pointsText);
            styleSheet.clear();
            contentView.addSubview(pointsText);
            contentView.setInsetsFor(pointsText,
                    17.0,
                    null, null,
                    70.0 + TextDimension.calculateFor(userText).getWidth() + 7.0 + 20.0 + 1.0);
        }

        // add date
        Text dateText = new Text(beautifyTimestamp(date, time));
        styleSheet.set("font-family", "'Roboto Regular', sans-serif");
        styleSheet.set("font-size", "13");
        styleSheet.set("fill", "#808080");
        styleSheet.applyOn(dateText);
        styleSheet.clear();
        contentView.addSubview(dateText);
        contentView.setInsetsFor(dateText, 37, null, null, 70);
    }

    /**
     * make timestap beautifull.
     * @param date the date
     * @param time the time
     * @return String of the beautified time and datestamp
     */
    public static String beautifyTimestamp(LocalDate date, LocalTime time) {
        if (date == null || time == null) {
            System.out.println("ERROR: incomplete timestamp passed for beautifier!");
            return "sometime";
        }
        String result;

        long dayDiff = LocalDate.now().toEpochDay() - date.toEpochDay();

        if (dayDiff == 0) {
            int hourDiff = LocalTime.now().getHour() - time.getHour();
            if (hourDiff == 0) {
                int minuteDiff = LocalTime.now().getMinute() - time.getMinute();
                if (minuteDiff < 4) {
                    result = "just now";
                } else {
                    result = minuteDiff + " minutes ago";
                }
            } else if (hourDiff == 1) {
                result = "1 hour ago";
            } else {
                result = hourDiff + " hours ago";
            }
        } else if (dayDiff == 1) {
            result = "yesterday at " + time.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else if (dayDiff < 7) {
            result = date.format(DateTimeFormatter.ofPattern("EE")) + " at "
                    + time.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else if (dayDiff < 365) {
            result = date.format(DateTimeFormatter.ofPattern("d MMMM")) + " at "
                    + time.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else {
            result = date.format(DateTimeFormatter.ofPattern("d MMMM u")) + " at "
                    + time.format(DateTimeFormatter.ofPattern("HH:mm"));
        }

        return result;
    }
}
