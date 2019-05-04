package gui.views.friends;

import static gui.helpers.StyleSheet.MouseState.HOVERED;
import static gui.helpers.StyleSheet.MouseState.NONE;
import static gui.helpers.StyleSheet.MouseState.PRESSED;

import controller.DataAccess;
import gui.App;
import gui.helpers.ObjectRefresher;
import gui.helpers.StyleSheet;
import gui.views.general.View;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.User;

import java.io.InputStream;
import java.util.List;



public class FriendsRequest {

    // default colors
    public static String optionDefaultColor = "#505050";
    public static String optionHoveredColor = "#303030";
    public static String optionPressedColor = "#101010";

    /**
     * Creates a View containing the list of Friends Requests and a title.
     * @return View containing the friends requests.
     */
    public static View getRequestsIncoming(View view, Stage stage, String type) {
        View request = new View();
        Pane pane;

        ObjectRefresher<List<User>> list = null;
        if (type.equals("incoming")) {
            list = () -> DataAccess.getFriendRequests(false);
        }  else {
            list = () -> DataAccess.getFriendRequests(true);
        }

        RequestListView requestList = new RequestListView(
                list,
                view,
                stage,
                type
        );
        requestList.set("background-color", "#DCEADD");

        if (requestList.hasRequest()) {
            pane = new StackPane();
            StackPane.setMargin(requestList, new Insets(35, 0, 440, 0));
            pane.getChildren().addAll(requestTitle(view, stage, request), requestList);
        } else {
            pane = new VBox();
            HBox empty = emptyRequests();
            VBox.setMargin(empty, new Insets(20));
            pane.getChildren().addAll(requestTitle(view, stage, request), empty);

        }
        request.addSubview(pane);
        return request;
    }

    /**
     * Creates HBox for the title of the Friends Requests list.
     * @param view View containing the friends page.
     * @param stage Main stage of the program.
     * @param request View containing the friends requests.
     * @return HBox containing the title.
     */
    private static HBox requestTitle(View view, Stage stage, View request) {
        View income = new View();
        Text incoming = new Text("Incoming Friends Requests:");
        ImageView inImg = getImg("incoming.png");
        if (inImg != null) {
            income.addSubview(inImg);
            income.setInsetsFor(inImg, null, null, null, 0);
            income.setInsetsFor(incoming, null, null, null, 40);
        }
        income.addSubview(incoming);
        income.setOnMouseClicked(e -> {
            request.getChildren().clear();
            request.addSubview(getRequestsIncoming(view, stage, "incoming"));
        });

        View outgo = new View();
        Text outgoing = new Text("Outgoing Friends Requests:");
        ImageView outImg = getImg("outgoing.png");
        if (outImg != null) {
            outgo.addSubview(outImg);
            outgo.setInsetsFor(outImg, null, null, null, 0);
            outgo.setInsetsFor(outgoing, null, null, null, 40);
        }
        outgo.addSubview(outgoing);
        outgo.setOnMouseClicked(e -> {
            request.getChildren().clear();
            request.addSubview(getRequestsIncoming(view, stage, "outgoing"));
        });

        StyleSheet styleSheet = new StyleSheet();
        styleSheet.set("font-family", "'Roboto Regular', sans-serif");
        styleSheet.set("font-size", "17");
        styleSheet.set(NONE, "fill", optionDefaultColor);
        styleSheet.set(PRESSED, "fill", optionPressedColor);
        styleSheet.set(HOVERED, "fill", optionHoveredColor);
        styleSheet.applyOn(income);
        styleSheet.applyOn(outgo);
        incoming.styleProperty().bind(income.styleProperty());
        outgoing.styleProperty().bind(outgo.styleProperty());
        styleSheet.clear();


        InputStream imageStream = App.class.getResourceAsStream("Refresh.png");
        InputStream imageStream2 = App.class.getResourceAsStream("Refresh2.png");
        Image refreshImg = new Image(imageStream);
        Image refreshImg2 = new Image(imageStream2);
        ImageView refresh = new ImageView(refreshImg);
        refresh.setFitHeight(25.0);
        refresh.setFitWidth(25.0);


        refresh.setOnMouseEntered(e ->
            refresh.setImage(refreshImg2)
        );
        refresh.setOnMouseExited(e ->
            refresh.setImage(refreshImg)
        );
        refresh.setOnMouseClicked(e ->
            refreshPage(view, stage)
        );

        HBox bar = new HBox();
        bar.setMargin(refresh, new Insets(0, 80, 0, 80));
        bar.getChildren().addAll(income, refresh, outgo);
        bar.setAlignment(Pos.BASELINE_CENTER);

        return bar;
    }

    /**
     * Creates label with message in case there are no friends requests.
     * @return HBox containing a label.
     */
    private static HBox emptyRequests() {
        HBox empty = new HBox();
        Label title = new Label("You don't have any incoming requests "
                + "(Click on refresh to check for new requests)");
        title.getStyleClass().add("empty-request");
        title.getStylesheets().add(App.class.getResource("HomeScreenLight.css").toExternalForm());
        empty.getChildren().add(title);
        empty.setAlignment(Pos.BASELINE_CENTER);

        return empty;

    }

    /**
     * Refreshes the FriendsPage whenever the refresh button is clicked.
     * @param view of the FriendsPage.
     */
    public static void refreshPage(View view, Stage stage) {
        view.getChildren().clear();
        view.addSubview(Friends.getFriendPage(stage));

    }

    /**
     * Gets image from path.
     * @param imgPath Path to the location of the image.
     * @return ImageView containing the image.
     */
    private static ImageView getImg(String imgPath) {
        InputStream stream = App.class.getResourceAsStream(imgPath);
        if (stream == null) {
            return null;
        }
        Image img = new Image(stream, 30, 30, true, true);
        ImageView imgV = new ImageView(img);

        return imgV;
    }




}
