package gui.views.timeline;

import controller.DataAccess;
import gui.App;
import gui.helpers.StyleSheet;
import gui.views.general.AlertView;
import gui.views.general.View;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import models.Activity;

import java.io.InputStream;

/**
 * View to show an activity.
 * @author Jules van der Toorn
 */
public class ActivityView extends ContributionView {

    private Activity activity;

    /**
     * Define default constructor as private to enforce passing activity.
     */
    private ActivityView() {}

    /**
     * Renders the activity view.
     * @param activity the activity that will be displayed
     */
    public ActivityView(Activity activity) {
        super(activity.getUser(), activity.getDate(), activity.getTime(), 110);

        this.activity = activity;

        render();
    }

    /**
     * Applies constraints and styling.
     */
    private void render() {

        StyleSheet styleSheet = new StyleSheet();

        // add activity title
        Text activityText = new Text(activity.getType().getDescription());
        styleSheet.set("font-family", "'Roboto Light', sans-serif");
        styleSheet.set("font-size", "20");
        styleSheet.set("fill", "#505050");
        styleSheet.applyOn(activityText);
        styleSheet.clear();
        contentView.addSubview(activityText);
        contentView.setInsetsFor(activityText, 72, null, null, 11);

        // add points icon
        InputStream leafStream = App.class.getResourceAsStream("leaf/leaf-50px.png");
        if (leafStream == null) {
            System.out.println("The resource 'leaf/leaf-50px.png' couldn't be found!");
        } else {
            Image leafPicture = new Image(leafStream);
            ImageView leafPictureView = new ImageView(leafPicture);
            contentView.addSubview(leafPictureView);
            contentView.setInsetsFor(leafPictureView, null, 30, null, null);

            Text pointsText = new Text(Integer.toString(activity.getType().getPoints()));
            styleSheet.set("font-family", "'Roboto Regular', sans-serif");
            styleSheet.set("font-size", "35");
            styleSheet.set("fill", "#7BB242");
            styleSheet.applyOn(pointsText);
            styleSheet.clear();
            contentView.addSubview(pointsText);
            contentView.setInsetsFor(pointsText, null, 88.0, null, null);
        }

        if (activity.getUser().isMainUser()) {

            // CLOSE ICON
            InputStream imageStream = App.class.getResourceAsStream("delete-icon.png");
            InputStream hoveredImageStream = App.class.getResourceAsStream(
                    "delete-icon-pressed.png"
            );
            if (imageStream == null || hoveredImageStream == null) {
                System.out.println("The resource 'close-icon.png' couldn't be found!");
            } else {

                // add container view, so clicking a little bit outside cross also works
                View imageContainer = new View();
                contentView.addSubview(imageContainer);
                setInsetsFor(imageContainer, 0, 0, null, null);
                imageContainer.setMaxHeight(25);
                imageContainer.setMaxWidth(25);

                // create images
                Image defaultImage = new Image(imageStream);

                // create the image view
                ImageView crossPictureView = new ImageView(defaultImage);
                crossPictureView.setFitHeight(10.0);
                crossPictureView.setFitWidth(10.0);

                // add image view in center of container view
                imageContainer.addSubview(crossPictureView);
                imageContainer.setAlignment(crossPictureView, Pos.CENTER);

                Image hoveredImage = new Image(hoveredImageStream);

                imageContainer.setOnMouseEntered(e -> {
                    crossPictureView.setImage(hoveredImage);
                });
                imageContainer.setOnMouseExited(e -> {
                    crossPictureView.setImage(defaultImage);
                });
                imageContainer.setOnMouseReleased(e -> {
                    crossPictureView.setImage(defaultImage);
                });
                imageContainer.setOnMouseClicked(e -> {
                    AlertView alert = new AlertView("Remove activity",
                        "Are you sure you want to remove the activity '"
                        + activity.getType().getDescription()
                        + "' from your profile?", "cancel", "yes");
                    alert.setHandler("yes", () -> {
                        boolean success = DataAccess.removeActivity(activity.getId());
                        DataAccess.getUserData();
                        if (success) {
                            App.sidebar.reloadCurrentPage();
                            App.sidebar.reloadPicture();
                            new AlertView("Success", "Removed activity successfully!", "nice!");
                        } else {
                            new AlertView(
                                    "Fail",
                                    "Something went wrong, couldn't remove activity",
                                    "okay :("
                            );
                        }
                    });
                });
            }
        }
    }
}
