package gui.views.sidebar;

import controller.DataAccess;
import gui.App;
import gui.helpers.StyleSheet;
import gui.views.general.PictureView;
import gui.views.general.View;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import models.User;
import network.NetworkManager;
import network.NetworkResponse;

import java.io.InputStream;

public class ProfileHeaderView extends SidebarItemView {

    protected User user;
    private PictureView pictureView;
    private View borderView;
    private View overlayView;

    private ProfileHeaderView() {    }

    protected ProfileHeaderView(SidebarView sb, View viewport, View vw, User user) {
        super(sb, viewport, vw, "profile", null, null);

        this.user = user;

        render();
    }

    /**
     * Override render method to use custom subview layout.
     */
    @Override
    protected void render() {

        // set height of item
        setPrefHeight(SidebarView.width - 20.0);

        if (user == null) {
            // for initial overridden render
            return;
        }

        // get profile picture from server
        double imgWidth = 100.0;
        double borderWidth = 2.0;

        if (user.getPicture() == null) {
            System.out.println("Main user has no profile picture!");
            Image pf = new Image(App.class.getResourceAsStream("profile.png"));
            this.pictureView = new PictureView(pf, imgWidth);
        } else {
            NetworkManager manager = new NetworkManager(5000);
            NetworkResponse<Image> result = manager.getImage(
                    DataAccess.host + user.getPicturePath(),
                    null, 100.0
            );

            if (!result.wasSuccess() || result.getData().isError()) {
                System.out.println("Couldn't fetch profile picture");
                Image pf = new Image(App.class.getResourceAsStream("profile.png"));
                this.pictureView = new PictureView(pf, imgWidth);
            } else {
                this.pictureView = new PictureView(result.getData(), imgWidth);
            }
        }
        borderView = new View();
        borderView.setMaxWidth(imgWidth + borderWidth * 2.0);
        borderView.setMaxHeight(imgWidth + borderWidth * 2.0);
        borderView.set("background-color", "#d6edce");
        borderView.set("background-radius", Double.toString(imgWidth + borderWidth * 2));
        addSubview(borderView);
        setInsetsFor(borderView,
                (SidebarView.width - imgWidth) / 2 - borderWidth,
                null, null,
                (SidebarView.width - imgWidth) / 2 - borderWidth
        );

        addSubview(pictureView);
        setInsetsFor(pictureView,
                (SidebarView.width - imgWidth) / 2,
                null, null,
                (SidebarView.width - imgWidth) / 2
        );

        // create overlay view
        this.overlayView = new View();
        overlayView.setMaxWidth(pictureView.getPrefWidth());
        overlayView.setMaxHeight(pictureView.getPrefHeight());

        // create dark see through background
        View backgroundView = new View();
        overlayView.addSubview(backgroundView);
        overlayView.setInsetsFor(backgroundView, 0);
        backgroundView.set("background-color", "black");
        backgroundView.setOpacity(0.5);

        // create overlay text
        Text overlayText = new Text("show profile");
        overlayText.setWrappingWidth(80);
        overlayText.setTextAlignment(TextAlignment.CENTER);
        StyleSheet styleSheet = new StyleSheet();
        styleSheet.set("font-family", "'Roboto Light', sans-serif");
        styleSheet.set("font-size", "18");
        styleSheet.set("fill", "white");
        styleSheet.applyOn(overlayText);
        overlayView.addSubview(overlayText);

        // add overlay to picture view
        pictureView.addOverlay(overlayView);

        // set show on hover
        pictureView.setOnMouseEntered(e -> pictureView.setOverlayVisible(true));
        pictureView.setOnMouseExited(e -> pictureView.setOverlayVisible(false));

        // make overlay even darker on click
        backgroundView.setOnMousePressed(e -> {
            styleSheet.set("fill", "#bababa");
            styleSheet.applyOn(overlayText);
            backgroundView.setOpacity(0.65);
        });
        backgroundView.setOnMouseReleased(e -> {
            styleSheet.set("fill", "white");
            styleSheet.applyOn(overlayText);
            backgroundView.setOpacity(0.5);
        });
        overlayText.onMousePressedProperty().bind(backgroundView.onMousePressedProperty());
        overlayText.onMouseReleasedProperty().bind(backgroundView.onMouseReleasedProperty());

        displayTotalPoints();
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (borderView != null) {
            borderView.set("background-color", enabled ? "#0f1610" : "#d6edce");
        }
    }

    @Override
    public void refresh() {
        this.getChildren().clear();
        user = DataAccess.getUserData();
        render();
        if (this.pictureView != null) {
            pictureView.setPicture(
                    DataAccess.getImageForUrl(DataAccess.host + user.getPicturePath(), null, 100.0)
            );

            if (this.overlayView != null) {
                pictureView.addOverlay(overlayView);
            }
        }
    }

    /**
     * Displays the total green points of the user.
     */
    public void displayTotalPoints() {
        // add points icon
        InputStream leafStream = App.class.getResourceAsStream("leaf/leaf-50px.png");
        if (leafStream == null) {
            System.out.println("The resource 'leaf/leaf-50px.png' couldn't be found!");
        } else {
            Image leafPicture = new Image(leafStream);
            ImageView leafPictureView = new ImageView(leafPicture);
            leafPictureView.setFitHeight(25);
            leafPictureView.setFitWidth(25);
            addSubview(leafPictureView);
            setInsetsFor(leafPictureView, 150, 50, 2, null);

            Text pointsText = new Text(Integer.toString(user.getTotalPoints()));
            styleSheet.set("font-family", "'Roboto Regular', sans-serif");
            styleSheet.set("font-size", "22");
            styleSheet.set("fill", "#666666");
            styleSheet.applyOn(pointsText);
            styleSheet.clear();
            addSubview(pointsText);
            setInsetsFor(pointsText, null, 78.0, 2.0, null);
        }
    }
}
