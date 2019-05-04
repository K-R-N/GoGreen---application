package gui.views.sidebar;

import static gui.helpers.StyleSheet.MouseState.HOVERED;
import static gui.helpers.StyleSheet.MouseState.NONE;
import static gui.helpers.StyleSheet.MouseState.PRESSED;

import gui.App;
import gui.helpers.TextDimension;
import gui.views.friends.Friends;
import gui.views.general.View;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.InputStream;

/**
 * Sidebar view for the project.
 * @author Jules van der Toorn
 */
public class SidebarItemView extends View {

    // default colors
    static String defaultColor = "#344c39";
    static String hoveredColor = "#1a261c";
    static String pressedColor = "#0f1610";

    // enabled item colors
    static String enDefaultColor = "#0f1610";
    static String enHoveredColor = "#0f1610";
    static String enPressedColor = "#0f1610";

    /**
     * Item attributes.
     */
    public View contentView;
    protected String title;
    protected String imgPath;

    /**
     * Used as handles.
     */
    protected View viewport;
    protected SidebarView sidebar;

    private Stage stage;

    /**
     * Default constructor set to protected to enforce passing ServerUser object.
     */
    protected SidebarItemView() {
        super();
    }

    /**
     * Initializes the sidebar view.
     */
    protected SidebarItemView(SidebarView sb,
                              View viewport,
                              View vw,
                              String title,
                              String imgPath,
                            Stage stage) {
        super();

        this.title = title;
        this.contentView = vw;
        this.imgPath = imgPath;
        this.stage = stage;
        this.viewport = viewport;
        this.sidebar = sb;

        this.setOnMouseClicked(e -> {
            sidebar.setCurrentItem(this);
        });

        set("font-family", "'Roboto Light', sans-serif");
        set("font-size", "18");

        setEnabled(false);

        render();
    }

    /**
     * Switches colours based on if item is enabled.
     * @param enabled boolean indicating state
     */
    public void setEnabled(boolean enabled) {
        if (enabled) {
            set(NONE, "fill", enDefaultColor);
            set(HOVERED, "fill", enHoveredColor);
            set(PRESSED, "fill", enPressedColor);
        } else {
            set(NONE, "fill", defaultColor);
            set(HOVERED, "fill", hoveredColor);
            set(PRESSED, "fill", pressedColor);
        }
    }

    /**
     * Refreshes contens of the sidebar view.
     */
    public void refreshView() {
        if (title.equals("friends")) {
            contentView = Friends.getFriendPage(stage);
            return;
        }
        if (contentView != null) {
            contentView.refresh();
        }

    }

    /**
     * Applies constraints and styling.
     */
    protected void render() {

        // set height of item
        setPrefHeight(50);
        setMaxHeight(50);

        // custom icon
        InputStream imageStream = App.class.getResourceAsStream(imgPath);
        if (imageStream == null) {
            System.out.println("The resource '" + imgPath + "' couldn't be found!");
        } else {

            // add container view, so clicking a little bit outside cross also works
            View imageContainer = new View();
            //addSubview(imageContainer);
            setInsetsFor(imageContainer, 0);
            imageContainer.setMaxHeight(20);
            imageContainer.setMaxWidth(20);

            // create the image view
            // create images
            Image defaultImage = new Image(imageStream, 20, 20, true, true);
            ImageView iconPictureView = new ImageView(defaultImage);

            // add image view in center of container view
            imageContainer.addSubview(iconPictureView);

            // add points text
            Text titleText = new Text(title);

            View fullTextContainer = new View();
            fullTextContainer.addSubview(imageContainer);
            fullTextContainer.addSubview(titleText);
            fullTextContainer.setInsetsFor(imageContainer, null, null, null, 0);
            fullTextContainer.setInsetsFor(titleText, null, null, null, 30);
            addSubview(fullTextContainer);
            centerChild(fullTextContainer);
            fullTextContainer.setMaxWidth(30.0 + TextDimension.calculateFor(titleText).getWidth());

            // pass style attributes to inner text view
            titleText.styleProperty().bind(this.styleProperty());
        }
    }
}