package gui.views.profile;

import static gui.helpers.StyleSheet.MouseState.HOVERED;
import static gui.helpers.StyleSheet.MouseState.NONE;
import static gui.helpers.StyleSheet.MouseState.PRESSED;

import controller.DataAccess;
import gui.App;
import gui.LoginScreen;
import gui.helpers.StyleSheet;

import gui.session.SessionIO;
import gui.views.general.AlertView;
import gui.views.general.ButtonView;
import gui.views.general.PictureView;
import gui.views.general.PopupView;
import gui.views.general.View;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.User;
import network.NetworkManager;
import network.NetworkResponse;
import org.controlsfx.control.ToggleSwitch;

import java.io.File;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class ProfileSettingsView extends View {

    private static double settingsWidth = 300.0;

    private String selectedImagePath;
    private String externalImagePath;
    private File selectedImage;
    private Stage mainStage;

    private ProfileSettingsView() {}

    /**
     * view of the setting page.
     * @param profilePage the page with which it is connected
     * @param mainStage the main stage
     * @param maskView a mask (masking view)
     */
    public ProfileSettingsView(ProfilePageView profilePage, Stage mainStage, View maskView) {
        this.selectedImagePath = null;
        this.externalImagePath = null;
        this.selectedImage = null;
        this.mainStage = mainStage;

        set("background-color", "#87cc95");

        // add header
        Text titleText = new Text("Settings");
        View titleView = new View();
        titleView.addSubview(titleText);
        titleView.setInsetsFor(titleText, 15, null, null, 15);

        // create simple border around viewport
        set("border-style", "solid");
        set("border-color", "#7ebc8b");
        set("border-width", "0 0 0 3");

        // add stylesheet
        StyleSheet styleSheet = new StyleSheet();

        // apply styling on header
        styleSheet.set("font-family", "'Roboto Regular', sans-serif");
        styleSheet.set("font-size", "33");
        styleSheet.set("fill", "#505050");
        styleSheet.applyOn(titleText);
        styleSheet.clear();

        addSubview(titleView);
        setInsetsFor(titleView, 0, 0, null, 0);
        titleView.setMaxHeight(75);
        titleView.setPrefHeight(75);
        titleView.setMinHeight(75);

        // get profile picture from server
        double imgWidth = 175.0;
        double borderWidth = 3.0;

        User user = DataAccess.getUserData();
        if ( user == null) {
            PopupView.init(mainStage);
            new AlertView(
                    "Connection lost",
                    "Failed connect to the server, "
                            + "this probably means you don't have an internet connection "
                            + "the application now closes automatically",
                    "okay :("
            );
            mainStage.close();
            return;
        }

        NetworkResponse<Image> result = null;
        if (user.getPicture() == null) {
            System.out.println("Main user has no profile picture!");
        } else {

            NetworkManager manager = new NetworkManager(5000);
            result = manager.getImage(DataAccess.host + user.getPicturePath(),
                    null, imgWidth
            );
            if (!result.wasSuccess()) {
                System.out.println("Couldn't fetch profile picture");
                result = null;
            }
        }

        View borderView = new View();
        borderView.setMaxWidth(imgWidth + borderWidth * 2.0);
        borderView.setMaxHeight(imgWidth + borderWidth * 2.0);
        borderView.set("background-color", "#d6edce");
        borderView.set("background-radius", Double.toString(imgWidth + borderWidth * 2));
        addSubview(borderView);
        setInsetsFor(borderView,
                17.0 + (settingsWidth - imgWidth) / 2 - borderWidth,
                null, null,
                (settingsWidth - imgWidth) / 2 - borderWidth
        );


        addPicture(user, result, imgWidth, mainStage, profilePage, maskView);

    }

    /**
     * Initialises the image on settings view.
     * @param user object containing information about the user
     * @param result result
     * @param imgWidth width of the image
     * @param mainStage main stage
     * @param profilePage profile page
     * @param maskView masking view
     */
    public void addPicture(
            User user,
            NetworkResponse<Image> result,
            double imgWidth,
            Stage mainStage,
            ProfilePageView profilePage,
            View maskView
    ) {
        PictureView pictureView;
        if (result.getData().isError()) {
            Image pf = new Image(App.class.getResourceAsStream("profile.png"));
            pictureView = new PictureView(pf, imgWidth);
        } else {
            pictureView = new PictureView(result.getData(), imgWidth);
        }
        addSubview(pictureView);
        setInsetsFor(pictureView,
                17.0 + (settingsWidth - imgWidth) / 2,
                null, null,
                (settingsWidth - imgWidth) / 2
        );
        // create overlay view
        View overlayView = new View();
        overlayView.setMaxWidth(pictureView.getPrefWidth());
        overlayView.setMaxHeight(pictureView.getPrefHeight());

        // create dark see through background
        View backgroundView = new View();
        overlayView.addSubview(backgroundView);
        overlayView.setInsetsFor(backgroundView, 0);
        backgroundView.set("background-color", "black");
        backgroundView.setOpacity(0.5);
        // create overlay text
        Text overlayText = new Text("choose picture");
        overlayText.setWrappingWidth(80);
        overlayText.setTextAlignment(TextAlignment.CENTER);
        styleSheet.set("font-family", "'Roboto Light', sans-serif");
        styleSheet.set("font-size", "23");
        styleSheet.set("fill", "white");
        styleSheet.applyOn(overlayText);
        overlayView.addSubview(overlayText);

        // add overlay to picture view
        pictureView.addOverlay(overlayView);

        // set show on hover
        pictureView.setOnMouseEntered(e -> pictureView.setOverlayVisible(true));
        pictureView.setOnMouseExited(e -> pictureView.setOverlayVisible(false));

        // make overlay even darker on click
        backgroundView.setOnMouseDragged(null);
        setOnMousePressed(null);

        backgroundView.setOnMousePressed(e -> {
            styleSheet.set("fill", "#bababa");
            styleSheet.applyOn(overlayText);
            backgroundView.setOpacity(0.65);
        });
        backgroundView.setOnMouseReleased(e -> {
            styleSheet.set("fill", "white");
            styleSheet.applyOn(overlayText);
            backgroundView.setOpacity(0.5);

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png")
            );
            File selectedFile = fileChooser.showOpenDialog(mainStage);
            if (selectedFile != null) {
                try {
                    this.selectedImagePath = selectedFile.getAbsolutePath();
                    this.externalImagePath = selectedFile.toURI().toURL().toExternalForm();
                    this.selectedImage = selectedFile;

                    Image newImage = new Image(selectedFile.toURI().toURL().toExternalForm());

                    pictureView.setPicture(newImage);
                    pictureView.addOverlay(overlayView);
                } catch (MalformedURLException e2) {
                    System.out.println("File URL was incorrect");
                    new AlertView("An error occurred", "File URL was incorrect, ",
                            "Close");

                }
            }
        });
        overlayText.onMousePressedProperty().bind(backgroundView.onMousePressedProperty());
        overlayText.onMouseReleasedProperty().bind(backgroundView.onMouseReleasedProperty());

        ButtonView settingsButton = new ButtonView("DONE", false, () -> {
            profilePage.getChildren().remove(this);
            App.root.getChildren().remove(maskView);
        });

        addSubview(settingsButton);
        setInsetsFor(settingsButton, 17, 17, null, null);

        continuePfSv(user, profilePage);
    }

    /**
     * method which shortens up the main method.
     * @param user object containing information about the user
     * @param profilePage  the page view
     */
    public void continuePfSv(User user, ProfilePageView profilePage) {

        // Full Name
        final Label nameLabel = new Label("Full name");
        TextField nameField = new TextField();
        nameField.setPromptText("Full name");
        nameField.setText(user.getFirstname());

        //Change/Confirm password
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter new password");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Repeat new password");

        //E-mail
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setText(user.getEmailadress());

        //Public/private account button
        ToggleSwitch toggleSwitch = new ToggleSwitch("Switch to private account");
        styleSheet.set("font-family", "Roboto Light");
        styleSheet.set("font-size","14");
        styleSheet.applyOn(toggleSwitch);
        styleSheet.clear();
        if (user.getPrivacy().equals("private")) {
            toggleSwitch.setSelected(true);
        } else {
            toggleSwitch.setSelected(false);
        }

        //VBox containing all of the user information
        View spacer1 = new View();
        spacer1.setMinHeight(10);
        View spacer2 = new View();
        spacer2.setMinHeight(10);
        View spacer3 = new View();
        spacer3.setMinHeight(10);
        View spacer4 = new View();
        spacer4.setMinHeight(10);
        Label emailLabel = new Label("Email");
        Label confirmPasswordLabel = new Label("Confirm password");
        final Label passwordLabel = new Label("Change password");
        VBox info = new VBox();
        info.getChildren().addAll(nameLabel, nameField, spacer1, passwordLabel,
                passwordField, spacer2, confirmPasswordLabel, confirmPasswordField,
                spacer3, emailLabel, emailField,spacer4, toggleSwitch);
        info.setSpacing(4);
        addSubview(info);
        setInsetsFor(info,280,15,null,15);
        //Label stylesheet
        StyleSheet styleSheetLabel = new StyleSheet();
        styleSheetLabel.set("font-family", "'Roboto Light', sans-serif");
        styleSheetLabel.set("font-size", "18");
        styleSheetLabel.set("fill", "#344c39");
        styleSheetLabel.applyOn(nameLabel);
        styleSheetLabel.applyOn(passwordLabel);
        styleSheetLabel.applyOn(confirmPasswordLabel);
        styleSheetLabel.applyOn(emailLabel);

        ButtonView submitButton = new ButtonView("SAVE", false, () -> {
            List<String> updatedValues = new ArrayList();

            checkPasswordField(passwordField, confirmPasswordField,
                    updatedValues);

            selectImage(updatedValues, profilePage, user);
            checkFields(emailField, nameField, toggleSwitch, updatedValues, profilePage);

            String output = "Successfully updated ";
            if (updatedValues.size() == 0) {
                new AlertView("No new settings",
                        "You didn't change anything, so nothing got updated!", "okay");
                return;
            } else if (updatedValues.size() == 1) {
                output += updatedValues.get(0);
            } else if (updatedValues.size() == 2) {
                output += updatedValues.get(0) + " and " + updatedValues.get(1);
            } else {
                for (int i = 0; i < updatedValues.size() - 1; i++) {
                    if (i == 0) {
                        output += updatedValues.get(i);
                    } else {
                        output += ", " + updatedValues.get(i);
                    }
                }
                output += " and " + updatedValues.get(updatedValues.size() - 1);
            }
            output += "!";

            new AlertView("Success", output, "great!");
        });
        addSubview(submitButton);
        setInsetsFor(submitButton, null, null, 20, 30);

        Button delete = deleteAccount();
        addSubview(delete);
        setInsetsFor(delete, null, 20, 25, null);
    }


    /**
     * check if password fields are empty.
     * @param passwordField main password field to be checked
     * @param confirmPasswordField to confirm your password
     * @param updatedValues the values that are updated
     */
    public  void checkPasswordField(PasswordField passwordField,
                                    PasswordField confirmPasswordField,
                                    List<String> updatedValues
    ) {

        if (!passwordField.getText().isEmpty()) {
            if (passwordField.getText().equals(confirmPasswordField.getText())) {
                if (passwordField.getText().length() > 6) {
                    updatedValues.add("password");
                    System.out.println("New password: " + passwordField.getText());
                    DataAccess.changePassword(passwordField.getText());
                } else {
                    new AlertView("Incorrect password",
                            "Password must be longer than 6 characters!", "got it!");
                    return;
                }
            } else {
                new AlertView("Different passwords", "Passwords didn't match!", "got it!");
                return;
            }
        }

    }

    /** method that gets an image if it exists and if it's size meets current standards.
     * @param updatedValues values that are updated
     * @param profilePage page view
     * @param user object containing information about the user
     */
    public void selectImage(List<String> updatedValues,
                            ProfilePageView profilePage,
                            User user) {

        if (this.selectedImagePath != null) {
            updatedValues.add("profile picture");

            if (this.selectedImage.length() > 1040000) {
                new AlertView("Image too large", "Image is too large to upload, "
                        + "please select a smaller one!", "got it!");
                return;
            }

            DataAccess.cachedImages.put(DataAccess.host
                    + user.getPicturePath(), new Image(externalImagePath));
            profilePage.refresh();
            App.sidebar.reloadPicture();
            DataAccess.sendImage(this.selectedImagePath);
        }

    }

    /** This method checks if there has been anything  changed in name/email field or toggle switch.
     * @param emailField email field
     * @param nameField name field
     * @param toggleSwitch toggleSwitch
     * @param updatedValues values that are updated
     * @param profilePage  the page view
     */
    public void checkFields(TextField emailField,
                            TextField nameField,
                            ToggleSwitch toggleSwitch,
                            List<String> updatedValues,
                            ProfilePageView profilePage) {
        User user = DataAccess.getUserData();

        if (!nameField.getText().equals(user.getFirstname()) && !nameField.getText().isBlank()) {
            updatedValues.add("name");
            System.out.println("New name: " + nameField.getText());
            profilePage.refresh();
            DataAccess.changeFirstname(nameField.getText());
        }
        if (!emailField.getText().equals(user.getEmailadress())
                && !emailField.getText().isBlank()) {
            updatedValues.add("email address");
            System.out.println("New email: " + emailField.getText());
            DataAccess.changeEmaiAddress(emailField.getText());
        }

        String toggle = "public";
        if (toggleSwitch.isSelected()) {
            toggle = "private";
        }
        if (!toggle.equals(user.getPrivacy())) {
            updatedValues.add("privacy");
            DataAccess.setPrivacySetting();
            user = DataAccess.getUserData();
            System.out.println("Privacy set to: " + user.getPrivacy());
        }

    }

    private Button deleteAccount() {
        Button delete = new Button("Delete Account");
        styleSheet.set("font-family", "'Roboto Regular', sans-serif");
        styleSheet.set("text-fill", "white");
        styleSheet.set("font-size", "13");
        styleSheet.set("background-radius", "10");
        styleSheet.set("border-radius", "10");
        styleSheet.set(NONE, "background-color", "#ED5E68");
        styleSheet.set(HOVERED, "opacity", "0.8");
        styleSheet.applyOn(delete);
        styleSheet.clear();



        delete.setOnAction(e -> {
            AlertView confirm = new AlertView("Confirmation",
                    "Are you sure you want to delete your account", "yes", "no");
            confirm.setHandler("yes", () -> {


                TextField user = new TextField();
                user.setPromptText("Username");
                user.setMaxWidth(100);
                TextField password = new TextField();
                password.setPromptText("Password");
                password.setMaxWidth(100);


                styleSheet.set("font-family", "'Roboto Regular', sans-serif");
                styleSheet.set("font-size", "15");
                Text userfield = new Text("Username:");
                styleSheet.applyOn(userfield);
                Text passwordField = new Text("Password:");
                styleSheet.applyOn(passwordField);
                Text confirmationText = new Text("Please confirm your account before deleting it");
                confirmationText.setWrappingWidth(200);
                styleSheet.applyOn(confirmationText);

                View content = new View();
                content.addSubview(confirmationText);
                content.setInsetsFor(confirmationText, 10, null, null, null);
                content.addSubview(userfield);
                content.setInsetsFor(userfield, 60, null ,null, null);
                content.addSubview(user);
                content.setInsetsFor(user, 80, null, null, null);
                content.addSubview(passwordField);
                content.setInsetsFor(passwordField, 120, null,null,null);
                content.addSubview(password);
                content.setInsetsFor(password, 140, null, null,null);
                Text fieldConfirm = new Text("Confirm");
                content.addSubview(fieldConfirm);
                content.setInsetsFor(fieldConfirm, null, null, 20, 50);

                Text close = new Text("Close");
                content.addSubview(close);
                content.setInsetsFor(close, null, 50, 20, null);
                styleSheet.set("font-family", "'Roboto Regular', sans-serif");
                styleSheet.set("font-size", "20");
                styleSheet.set(NONE, "fill", "#505050");
                styleSheet.set(HOVERED, "fill", "#303030");
                styleSheet.set(PRESSED, "fill", "#101010");
                styleSheet.applyOn(close);
                styleSheet.applyOn(fieldConfirm);
                styleSheet.clear();

                PopupView control = new PopupView(content, 250, 250);
                fieldConfirm.setOnMouseClicked(x -> {
                            if (DataAccess.login(
                                    user.getText().toLowerCase(),
                                    password.getText())) {
                                control.close();
                                DataAccess.deleteAccount();
                                SessionIO.write(null);
                                LoginScreen.startup(mainStage);
                                new AlertView("Succesfull",
                                        "Account has been succesfully deleted. \n goodbye :(",
                                        "Goodbye!");
                            } else {
                                new AlertView("Incorrect login",
                                        "Incorrect information has been inputted",
                                        "Retry");
                            }
                        }
                );

                close.setOnMouseClicked(o -> control.close());
                }
            );

        });
        return delete;

    }
}
