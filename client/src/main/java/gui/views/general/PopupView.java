package gui.views.general;

import gui.App;
import gui.helpers.DraggableNode;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PopupView extends View {

    protected static Stage mainStage;

    protected int width;
    protected int height;

    protected Stage stage;

    protected View contentView;

    /**
     * Encapsulate default constructor to enforce passing attributes.
     */
    protected PopupView() {}

    /**
     * Creates a popup view.
     * @param contentView the view that will be displayed inside the popup
     * @param width width of the popup
     * @param height height of the popup
     */
    public PopupView(View contentView, int width, int height) {
        super();

        if (contentView == null) {
            System.out.println("No content view passed to PopupView!");
            return;
        }

        // store passed attributes
        this.width = width;
        this.height = height;
        this.contentView = contentView;
        this.stage = new Stage();

        contentView.set("background-radius", Integer.toString(App.borderRadius - 3));
        DraggableNode.enableDrag(stage, contentView);

        // create scene from contentview
        Scene scene = new Scene(contentView, width, height);

        // load custom font from Google
        scene.getStylesheets().add("http://fonts.googleapis.com/css?family=Roboto:300,400");

        // make window background transparent
        scene.setFill(Color.TRANSPARENT);

        // hide top bar
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setScene(scene);

        stage.setOpacity(0.95);

        // render contents
        render();

        // show the popup
        show();
    }

    /**
     * Sets the main stage window, which is used to center the popup.
     * @param stage the main stage
     */
    public static void init(Stage stage) {
        mainStage = stage;
    }

    /**
     * Lays out subviews, meant to be overriden.
     */
    protected void render() {

        // center it on the screen
        stage.setX(mainStage.getX() + (mainStage.getWidth() - width) / 2);
        stage.setY(mainStage.getY() + (mainStage.getHeight() - height) / 2);
    }

    /**
     * Shows the popup.
     */
    public void show() {
        stage.show();
    }

    /**
     * Closes the popup.
     */
    public void close() {
        stage.close();
    }
}
