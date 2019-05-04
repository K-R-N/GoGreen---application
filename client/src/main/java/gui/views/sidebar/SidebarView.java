package gui.views.sidebar;

import controller.DataAccess;
import gui.App;
import gui.LoginScreen;
import gui.views.general.AlertView;
import gui.views.general.ListView;
import gui.views.general.PopupView;
import gui.views.general.View;
import gui.views.profile.ProfilePageView;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.User;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Sidebar view for the project.
 * @author Jules van der Toorn
 */
public class SidebarView extends View {

    public static double width = 170;

    protected static Stage mainStage;

    /**
     * Used to show profile picture and name in top of sidebar.
     */
    private User user;

    private View viewport;

    private ListView menuItems;

    private List<SidebarItemView> itemViews;

    private SidebarItemView currentItem;

    private SidebarItemView profileHeaderView;

    /**
     * Default constructor set to private to enforce passing ServerUser object.
     */
    private SidebarView() {}

    /**
     * Initializes the sidebar view.
     */
    public SidebarView(View viewport, User user) {
        super();

        this.user = user;
        this.menuItems = new ListView();
        this.viewport = viewport;
        this.itemViews = new ArrayList<>();
        this.currentItem = null;

        render();
    }

    /**
     * Adds item to the sidebar.
     * @param view the view that will be displayed when item gets clicked
     * @param title title of the menu item
     * @param imgUrl url of the menu item image
     */
    public void addItem(View view, String title, String imgUrl) {
        SidebarItemView item = new SidebarItemView(this, viewport, view, title, imgUrl, mainStage);
        menuItems.append(item);
        itemViews.add(itemViews.size() - 1, item);
    }

    /**
     * Switches current page to a given index.
     * @param index the current index
     */
    public void setCurrentIndex(int index) {
        setCurrentItem(itemViews.get(index));
    }

    /**
     * Refreshes current page.
     */
    public void reloadCurrentPage() {

        this.currentItem.refreshView();
    }

    /**
     * reloads a picture.
     */
    public void reloadPicture() {
        if (this.profileHeaderView != null) {
            this.profileHeaderView.refresh();
        }
    }

    /**
     * Switches current page to a given sidebar item view.
     * @param item the current item
     */
    public void setCurrentItem(SidebarItemView item) {
        this.currentItem = item;
        for (SidebarItemView itemView: itemViews) {
            itemView.setEnabled(itemView == item);
        }
        if (item.title.equals("log out")) {
            if (DataAccess.logout()) {
                LoginScreen.startup(mainStage);
            }
            mainStage.close();
            return;
        }
        viewport.getChildren().clear();
        viewport.addSubview(item.contentView);
        viewport.setInsetsFor(item.contentView, 0);
        item.refreshView();
    }

    /**
     * Switches current page to a given sidebar item title.
     * @param item the title of the current item
     */
    public void setCurrentItem(String item) {
        if (item == null || item.isEmpty()) {
            return;
        }
        SidebarItemView correspondingItem = null;
        for (SidebarItemView itemView: itemViews) {
            if (itemView.title != null && itemView.title.equals(item)) {
                correspondingItem = itemView;
                itemView.setEnabled(true);
            } else {
                itemView.setEnabled(false);
            }
        }
        if (correspondingItem == null) {
            System.out.println("Couldn't find sidebar item '" + item + "'!");
            return;
        }
        if (correspondingItem.title.equals("log out")) {
            DataAccess.logout();
            System.out.println("Logged out!");
            mainStage.close();
            return;
        }
        this.currentItem = correspondingItem;
        viewport.getChildren().clear();
        viewport.addSubview(correspondingItem.contentView);
        viewport.setInsetsFor(correspondingItem.contentView, 0);
        correspondingItem.refreshView();
    }

    /**
     * Sets the main stage window, which is used to use the close button.
     * @param stage the main stage
     */
    public static void init(Stage stage) {
        mainStage = stage;
    }

    /**
     * Applies constraints and styling.
     */
    private void render() {

        // add menu items view
        addSubview(menuItems);
        setInsetsFor(menuItems, 0, 0, 0, 0);

        set("background-color", "#87cc95");

        // create simple border around viewport
        set("border-style", "solid");
        //set("border-color", "#81c48e");
        set("border-color", "#7ebc8b");
        set("border-width", "0 3 0 0");

        // CLOSE ICON
        InputStream imageStream = App.class.getResourceAsStream("close-icon.png");
        InputStream hoveredImageStream = App.class.getResourceAsStream("close-icon-black.png");
        if (imageStream == null || hoveredImageStream == null) {
            System.out.println("The resource 'close-icon.png' couldn't be found!");
        } else {

            // add container view, so clicking a little bit outside cross also works
            View imageContainer = new View();
            addSubview(imageContainer);
            setInsetsFor(imageContainer, 0, null, null, 0);
            imageContainer.setMaxHeight(30);
            imageContainer.setMaxWidth(30);

            // create images
            Image defaultImage = new Image(imageStream);

            // create the image view
            ImageView crossPictureView = new ImageView(defaultImage);
            crossPictureView.setFitHeight(13.0);
            crossPictureView.setFitWidth(13.0);

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
            imageContainer.setOnMouseClicked(e -> {
                mainStage.close();
            });
        }

        // add profile header to sidebar
        // ProfileView profileView = new ProfileView(user);

        ProfilePageView profilePageView = new ProfilePageView(
                user, (fromDate, toDate) ->
                        DataAccess.getActivityHistory(false,
                                fromDate, toDate, null), activity ->
                DataAccess.removeActivity(activity.getId()), mainStage
        );

        this.profileHeaderView = new ProfileHeaderView(
                this, viewport, profilePageView, user
        );

        menuItems.append(profileHeaderView);
        itemViews.add(profileHeaderView);

        // add special bottom aligned sidebar item for logout button
        SidebarItemView logoutItem = new SidebarItemView(
                this,
                viewport,
                null,
                "log out",
                "logout.png",
                mainStage
        );
        logoutItem.setOnMouseClicked(e -> {
            try {
                if (DataAccess.logout()) {
                    LoginScreen.startup(mainStage);
                }
            } catch (NullPointerException x) {
                PopupView.init(mainStage);
                new AlertView("Couldn't log out poperly",
                        "Logging out had some problems, "
                       + "you probably lost connection", "Ok :(");
                mainStage.close();
            }
        });

        itemViews.add(logoutItem);
        addSubview(logoutItem);
        setInsetsFor(logoutItem, null, 0, 0, 0);
    }
}