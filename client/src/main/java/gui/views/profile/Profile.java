package gui.views.profile;

//import controller.DataAccess;
import gui.App;
import gui.helpers.StyleSheet;
import gui.views.general.View;
import gui.views.timeline.TimelineView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Profile {

    public static TimelineView activityList;

    /*
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.png")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            System.out.println(selectedFile.getAbsolutePath());
        }
     */

    /**
     * Returns the profile view.
     * @return view containing profile information
     */
    public static View getProfile() {
        //Main view
        final View root = new View();
        //root.set("background-color", "white");  // #7FFFD4 <- We can use this color later

        //Profile photo
        Image profilePicture = new Image(App.class.getResourceAsStream("profile.png"));
        ImageView profilePictureView = new ImageView(profilePicture);
        profilePictureView.setFitHeight(100.0);
        profilePictureView.setFitWidth(100.0);
        profilePictureView.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("Profile picture clicked!"); // change functionality
        });
        root.addSubview(profilePictureView);
        root.setInsetsFor(profilePictureView, 10, null, null, 10);

        //ServerUser information (text)
        Text usernameText = new Text("@username");
        usernameText.setFont(Font.font("Helvetica", 35));
        usernameText.setFill(Color.rgb(0, 0, 0));

        //Status
        Label statusLabel = new Label("Status:");
        statusLabel.setFont(Font.font("Helvetica", 30));
        TextField statusField = new TextField();
        statusField.setPromptText("How are you feeling ...");
        statusField.setPrefWidth(250);
        statusField.setPrefHeight(40);
        HBox status = new HBox();
        status.getChildren().addAll(statusLabel, statusField);
        status.setSpacing(20);

        //ServerUser Info - username, status, totalpoints
        VBox userInfo = new VBox();
        userInfo.getChildren().addAll(usernameText, status);
        userInfo.setSpacing(20);

        //Generating a couple of random badges
        Image image = new Image(App.class.getResourceAsStream("randomBADGE.png"));
        ImageView badge1 = new ImageView(image);
        badge1.setFitHeight(50);
        badge1.setFitWidth(50);
        //badge2
        ImageView badge2 = new ImageView(image);
        badge2.setFitHeight(50);
        badge2.setFitWidth(50);
        //badge3
        ImageView badge3 = new ImageView(image);
        badge3.setFitHeight(50);
        badge3.setFitWidth(50);
        //bage4
        ImageView badge4 = new ImageView(image);
        badge4.setFitHeight(50);
        badge4.setFitWidth(50);
        //badge5
        ImageView badge5 = new ImageView(image);
        badge5.setFitHeight(50);
        badge5.setFitWidth(50);
        //badge6
        ImageView badge6 = new ImageView(image);
        badge6.setFitHeight(50);
        badge6.setFitWidth(50);

        //Badge Collection
        HBox badgeCollection = new HBox();
        badgeCollection.setSpacing(5);
        badgeCollection.getChildren().addAll(badge1, badge2, badge3, badge4, badge5, badge6);

        //Adding user info to root
        root.addSubview(userInfo);
        root.setInsetsFor(userInfo, 10, null, null, 120);

        //Total Points
        Text totalpointsText = new Text("Total Points: 435 GP");
        totalpointsText.setFont(Font.font("Helvetica", 30));
        totalpointsText.setFill(Color.FORESTGREEN);

        //AchievementsPage(badges) label
        Label badgesLabel = new Label("Your AchievementsPage: ");
        badgesLabel.setFont(Font.font("Helvetica", 30));

        //Stats
        VBox stats = new VBox();
        stats.getChildren().addAll(totalpointsText,badgesLabel, badgeCollection);

        //Adding badge collection
        root.addSubview(stats);
        root.setInsetsFor(stats, 130, null, null, 20);

        //Adding ActivityView
        // create activity list inside root
        activityList = null;

        // Adjusting the Activity View
        //        root.addSubview(activityList);
        root.setInsetsFor(activityList, 0);
        root.setInsetsFor(activityList, 300, null, 50, 20);

        //Creating "Edit Profile" button
        Button editProfileButton = new Button("Edit Profile");
        editProfileButton.setOnAction(e -> {
            root.getChildren().clear();
            root.addSubview(editProfile());
        });

        //StyleSheet for editProfileButton
        StyleSheet styleSheet = new StyleSheet();
        styleSheet.set("font-size", "15");
        styleSheet.set("border-radius", "15");
        styleSheet.set("background-radius","15");
        styleSheet.set("border-color", "black");
        styleSheet.set("background-color", "lightgreen");
        styleSheet.set(StyleSheet.MouseState.HOVERED, "background-color", "#99ff99");
        styleSheet.set(StyleSheet.MouseState.PRESSED, "background-color", "white");
        styleSheet.applyOn(editProfileButton);
        styleSheet.clear();

        root.addSubview(editProfileButton);
        root.setInsetsFor(editProfileButton,20,20,null,0);

        return root;
    }

    /**
     * Returns the Settings Page.
     */
    public static View editProfile() {
        View settingsView = new View();

        //Header
        Label header = new Label("Edit Profile");
        header.setFont(new Font("Roboto", 30));
        header.setStyle("-fx-font-weight: bold");
        settingsView.addSubview(header);
        settingsView.setInsetsFor(header,10,300,0,200);

        //Profile photo Label
        Label profilePhotoLabel = new Label("Change photo");
        profilePhotoLabel.setFont(new Font("Roboto",18));
        profilePhotoLabel.setStyle("-fx-font-weight: bold");
        settingsView.addSubview(profilePhotoLabel);
        settingsView.setInsetsFor(profilePhotoLabel,85,null,null,40);

        // Full Name
        final Label nameLabel = new Label("Full name");
        TextField nameField = new TextField();
        nameField.setPromptText("Full name");
        nameField.setPrefHeight(20);
        nameField.setPrefWidth(30);

        //Change/Confirm password
        final Label passwordLabel = new Label("Change password");
        TextField passwordField = new TextField();
        passwordField.setPromptText("Enter new password");
        Label confirmPasswordLabel = new Label("Confirm password");
        TextField confirmPasswordField = new TextField();
        confirmPasswordField.setPromptText("Repeat new password");

        //E-mail
        Label emailLabel = new Label("Email");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        //VBox containing all of the user information
        VBox info = new VBox();
        info.getChildren().addAll(nameLabel, nameField, passwordLabel, passwordField,
                confirmPasswordLabel, confirmPasswordField, emailLabel, emailField);
        info.setSpacing(10);
        settingsView.addSubview(info);
        settingsView.setInsetsFor(info,80,10,25,300);

        // Field stylesheet
        StyleSheet styleSheetTextField = new StyleSheet();
        styleSheetTextField.set("max-width", "300");
        styleSheetTextField.set("font-size", "20");
        styleSheetTextField.applyOn(nameField);
        styleSheetTextField.applyOn(passwordField);
        styleSheetTextField.applyOn(confirmPasswordField);
        styleSheetTextField.applyOn(emailField);

        //Label stylesheet
        StyleSheet styleSheetLabel = new StyleSheet();
        styleSheetLabel.set("font-size", "25px");
        styleSheetLabel.set("font-family", "Roboto");
        styleSheetLabel.applyOn(nameLabel);
        styleSheetLabel.applyOn(passwordLabel);
        styleSheetLabel.applyOn(confirmPasswordLabel);
        styleSheetLabel.applyOn(emailLabel);

        //Profile image
        Image profilePicture = new Image(App.class.getResourceAsStream("profile.png"));
        ImageView profilePictureView = new ImageView(profilePicture);
        profilePictureView.setFitHeight(170.0);
        profilePictureView.setFitWidth(170.0);
        profilePictureView.setOnMouseClicked((MouseEvent e) -> {
            changeProfilePicture(); // change functionality
        });
        settingsView.setInsetsFor(profilePictureView, 60, 520, 230, 20);
        settingsView.addSubview(profilePictureView);

        //"Save changes" Button
        Button saveChangesButton = new Button("Save Changes");
        saveChangesButton.setOnAction(e -> saveChanges());  // change functionality

        //StyleSheet for "Save Changes" buttons
        StyleSheet buttonsStyleSheet = new StyleSheet();
        buttonsStyleSheet.set("font-size", "17");
        buttonsStyleSheet.set("text-fill", "white");
        buttonsStyleSheet.set("font-weight", "bold");
        buttonsStyleSheet.set("border-radius", "15");
        buttonsStyleSheet.set("background-color", "#28B463");
        buttonsStyleSheet.set(StyleSheet.MouseState.HOVERED,"background-color", "#43d680");
        buttonsStyleSheet.set(StyleSheet.MouseState.HOVERED,"text-fill", "white");
        buttonsStyleSheet.set(StyleSheet.MouseState.PRESSED,"background-color", "#abedc7");
        buttonsStyleSheet.set(StyleSheet.MouseState.PRESSED,"text-fill", "black");
        buttonsStyleSheet.applyOn(saveChangesButton);
        buttonsStyleSheet.clear();

        //"Cancel" Button
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            settingsView.getChildren().clear();
            settingsView.getChildren().add(getProfile());
        });

        //StyleSheet for "Cancel" button
        buttonsStyleSheet.set("font-size", "17");
        buttonsStyleSheet.set("border-radius", "50px");
        buttonsStyleSheet.set("background-color", "#ee4466");
        buttonsStyleSheet.set("text-fill", "white");
        buttonsStyleSheet.set("font-weight", "bold");
        buttonsStyleSheet.set(StyleSheet.MouseState.HOVERED,"background-color", "#f48aa0");
        buttonsStyleSheet.set(StyleSheet.MouseState.HOVERED,"text-fill", "white");
        buttonsStyleSheet.set(StyleSheet.MouseState.PRESSED,"background-color", "#f8b9c6");
        buttonsStyleSheet.set(StyleSheet.MouseState.PRESSED,"text-fill", "black");
        buttonsStyleSheet.applyOn(cancelButton);

        //HBox containing the buttons
        HBox buttons = new HBox();
        buttons.getChildren().addAll(saveChangesButton,cancelButton);
        buttons.setSpacing(20);
        settingsView.addSubview(buttons);
        settingsView.setInsetsFor(buttons, 510,null,10,300);

        //Change picture on hover
        StyleSheet profileStyleSheet = new StyleSheet();
        //profileStyleSheet.set(StyleSheet.MouseState.HOVERED,"background-image", "url('https://ibb.co/GRC4WYr')");
        profileStyleSheet.applyOn(profilePictureView);
        
        return  settingsView;
    }

    /**
     * Changes the Current profile picture with a new one.
     */
    public static void changeProfilePicture() {
        System.out.println("Changing profile photo ...");
    }

    /**
     * Saves all of the made changes from the user and returns the Main Profile Page.
     */
    public static void saveChanges() {
        System.out.println("Changes have been saved!");
    }


}
