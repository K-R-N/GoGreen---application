package gui;

import static gui.App.borderRadius;

import controller.DataAccess;
import gui.helpers.DraggableNode;
import gui.helpers.Pdf;
import gui.helpers.StyleSheet;
import gui.session.SessionIO;
import gui.views.general.AlertView;
import gui.views.general.PictureView;
import gui.views.general.PopupView;
import gui.views.general.View;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import network.NetworkManager;
import org.json.JSONException;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Objects;

public class LoginScreen extends Application {

    private static Stage stage;

    private static boolean noAuth = false;

    /**
     * Calls the start method provided with the window stage.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--noauth")) {
            noAuth = true;
        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        getIconApp(primaryStage);
        startup(primaryStage);
    }

    /**
     * Actual startup program of the application.
     * @param primaryStage Window where the login screen and register screen are on.
     */
    public static void startup(Stage primaryStage) {
        NetworkManager.setSslSocketFromKey();

        if (noAuth) {
            System.out.println("Bypassing authentication (--noauth flag detected)");

            // login as user jakob_hand
            DataAccess.login("jakob_hand", "ea8phe1Zaz");

            App.start();
            return;
        }

        String sessionID = SessionIO.read();
        if (sessionID == null) {
            System.out.println("No stored session available");
        } else {
            System.out.println("Autologin started");
            try {
                DataAccess.sessionId = sessionID;
                App.start();
                return;
            } catch (JSONException e) {
                System.out.println("Can't login automatically");
            }
        }

        stage = primaryStage;
        stage.setScene(getLogin());
        try {
            stage.initStyle(StageStyle.TRANSPARENT);
        } catch (IllegalStateException e) {
            System.out.println("app restarted");
        }
        PopupView.init(stage);
        stage.show();

        // center it on the screen
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

    }

    /**
     * Creates a Scene for the Login screen.
     * @return Scene which contains the login screen.
     */
    private static Scene getLogin() {
        View window = new View();
        PopupView.init(stage);
        window.set("background-radius", Integer.toString(borderRadius));
        window.set("background-color", "#acd2b4     ");
        window.set("background-insets", "10");
        window.set("effect", "dropshadow(gaussian, rgba(0, 0, 0, 0.4), 10, 0.5, 0.0, 0.0)");

        InputStream inputStream = App.class.getResourceAsStream("logoFinal2.png");
        ImageView logo = null;
        if (inputStream == null) {
            System.out.println("Login image couldn't be loaded");
        } else {
            Image image = new Image(inputStream);
            logo = new ImageView(image);
            logo.setFitHeight(130);
            logo.setFitWidth(140);
            logo.setPreserveRatio(true);
        }

        InputStream inputStream1 = App.class.getResourceAsStream("Lprofile.png");
        ImageView profileIcon = null;
        if (inputStream1 == null) {
            System.out.println("Login image couldn't be loaded");
        } else {
            Image profileImage = new Image(inputStream1);
            profileIcon = new ImageView(profileImage);
            profileIcon.setFitWidth(35);
            profileIcon.setFitHeight(35);
            profileIcon.setPreserveRatio(true);
        }
        InputStream inputStream2 = App.class.getResourceAsStream("lock.png");
        ImageView lockIcon = null;
        if (inputStream2 == null) {
            System.out.println("Login image couldn't be loaded");
        } else {
            Image lockImage = new Image(inputStream2);
            lockIcon = new ImageView(lockImage);
            lockIcon.setFitHeight(35);
            lockIcon.setFitWidth(35);
            lockIcon.setPreserveRatio(true);
        }

        //Creating the grid (basic layout)
        GridPane grid =  new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(12);
        grid.setHgap(2);

        Label welcomeMessage = new Label("WELCOME!");
        welcomeMessage.setFont(Font.font("Impact", FontWeight.LIGHT, 35));
        grid.add(welcomeMessage, 0,0);

        TextField nameInput = new TextField();
        nameInput.setPromptText("Username");
        nameInput.getStyleClass().add("label");
        nameInput.setPrefWidth(200);

        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Password");
        passwordInput.getStyleClass().add("label");
        passwordInput.setPrefWidth(200);

        //Creating HBox for username
        HBox usernameBox = new HBox();
        usernameBox.getChildren().addAll(profileIcon, nameInput);
        usernameBox.setPadding(new Insets(5,10,5,2));
        grid.add(usernameBox,0,1);
        HBox passwordBox = new HBox();
        passwordBox.getChildren().addAll(lockIcon, passwordInput);
        grid.add(passwordBox,0,2);

        //BUTTONS
        Button loginButton = new Button("Log In");
        loginButton.setOnAction(e ->  {
            if (nameInput.getText().isBlank() || passwordInput.getText().isBlank()) {
                new AlertView("Can't login", "Input of the fields can't be empty","Accept");
            } else {
                if (DataAccess.login(nameInput.getText().toLowerCase(), passwordInput.getText())) {
                    App.start();
                    stage.close();
                } else {
                    new AlertView("Can't login", "Wrong username/password!","retry");
                }
            }
        });

        Button cancelButton =  new Button("Exit");
        cancelButton.setOnAction(e -> stage.close());

        HBox buttons = new HBox();
        buttons.getChildren().addAll(loginButton, cancelButton);
        buttons.setSpacing(32);
        buttons.setPadding(new Insets(20,10,50,50));
        grid.add(buttons, 0,3);

        if (logo != null) {
            VBox topPart = new VBox();
            topPart.getChildren().add(logo);
            window.setInsetsFor(topPart, 15, null, null, 175);
            window.addSubview(topPart);
        }

        //SIGN UP
        Label text = new Label("New to GoGreen?");
        text.setId("text");

        Button signUpButton = new Button("Sign up now!");
        signUpButton.setId("signUpButton");
        signUpButton.setOnAction(e -> stage.setScene(getRegister()));

        HBox signUp = new HBox();
        signUp.getChildren().addAll(text,signUpButton);
        signUp.setSpacing(5);
        grid.add(signUp,0,4);
        grid.setVgap(5);
        grid.setAlignment(Pos.CENTER);

        Hyperlink openfile = openfile("free software license");
        TextFlow license = new TextFlow(new Text("This software is distributed under"), openfile);
        grid.add(license, 0, 5);

        // WINDOW CREATION
        grid.getStylesheets().add(
                LoginScreen.class.getResource("LoginScreen.css").toExternalForm());
        window.setInsetsFor(grid, 90, null, null, 25);
        window.addSubview(grid);

        DraggableNode.enableDrag(stage, window);
        Scene scene = new Scene(window, 500, 500);
        scene.setFill(Color.TRANSPARENT);


        return scene;
    }

    private static Scene getRegister() {
        View window = new View();
        window.set("background-radius", Integer.toString(borderRadius));
        window.set("background-insets", "10");
        window.set("effect", "dropshadow(gaussian, rgba(0, 0, 0, 0.4), 10, 0.5, 0.0, 0.0)");
        DraggableNode.enableDrag(stage, window);
        //Generating a stylesheet
        StyleSheet styleSheet = new StyleSheet();

        //Header
        Label signUpLabel = new Label("Make an impact today!");
        window.addSubview(signUpLabel);
        window.setInsetsFor(signUpLabel,20,null,null,null);
        //Styling the header
        styleSheet.set("font-size", "25");
        styleSheet.set("font-family", "Roboto Regular");
        styleSheet.set("font-weight", "bold");
        styleSheet.applyOn(signUpLabel);
        styleSheet.clear();

        //Profile picture
        Image profileImage = new Image(App.class.getResourceAsStream("profile.png"));
        PictureView pictureView = new PictureView(profileImage, 130);
        window.addSubview(pictureView);
        window.setInsetsFor(pictureView, 80, null, null, 350);
        addPicture(pictureView);

        Text pictureText = new Text("Upload picture");
        styleSheet.set("font-size", "15");
        styleSheet.set("font-family", "Roboto Regular");
        styleSheet.applyOn(pictureText);
        window.addSubview(pictureText);
        window.setInsetsFor(pictureText, 220, null, null, 370);

        //LABELS
        Label fullNameLabel = new Label("Full name");
        Label usernameLabel = new Label("Username");
        Label passwordLabel = new Label("Password");
        Label confirmPasswordLabel = new Label("Confirm password");
        Label emailLabel = new Label("Email");

        //Label photoLabel = new Label("Profile picture");
        //Stylesheet
        styleSheet.set("font-size", "15");
        styleSheet.set("font-family", "Roboto Regular");
        styleSheet.applyOn(fullNameLabel);
        styleSheet.applyOn(usernameLabel);
        styleSheet.applyOn(passwordLabel);
        styleSheet.applyOn(confirmPasswordLabel);
        styleSheet.applyOn(emailLabel);
        styleSheet.clear();

        //styleSheet.applyOn(photoLabel);
        styleSheet.clear();

        //INPUT FIELDS
        TextField fullNameField = new TextField();
        fullNameField.setPromptText("Enter your full name");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter a username");

        //Add label, that you cannot change it anymore (unique) + only lowercase
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Repeat your password"); //Add an additional label
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");

        //Styling the fields
        styleSheet.set("max-width", "250");
        styleSheet.set("min-height", "32");
        styleSheet.set("font-size","15");
        styleSheet.applyOn(fullNameField);
        styleSheet.applyOn(usernameField);
        styleSheet.applyOn(passwordField);
        styleSheet.applyOn(confirmPasswordField);
        styleSheet.applyOn(emailField);
        styleSheet.clear();

        //FIELD RULES
        Label usernameRule1 = new Label("\t *Use lowercase letters only! \n\t "
                + "(Username will be automatically set to lowercase)");
        Label usernameRule2 = new Label("\t *Username is unique and cannot be changed!");
        Label confirmPasswordRule = new Label("\t *Please make sure both "
                + "password fields match.\n\n");
        //Styling the rules
        styleSheet.set("font-size", "12");
        styleSheet.set("font-family", "Roboto Regular");
        styleSheet.set("text-fill", "black");
        styleSheet.applyOn(usernameRule1);
        styleSheet.applyOn(usernameRule2);
        styleSheet.applyOn(confirmPasswordRule);
        styleSheet.clear();

        //VBOXES
        VBox names = new VBox();
        names.getChildren().addAll(fullNameLabel,fullNameField,usernameLabel,usernameField,
                usernameRule1,usernameRule2);
        names.setSpacing(3);
        names.setMaxWidth(300);
        window.addSubview(names);
        window.setInsetsFor(names,60,null,null,30);

        VBox otherInfo = new VBox();
        otherInfo.getChildren().addAll(passwordLabel,passwordField,confirmPasswordLabel,
                confirmPasswordField,confirmPasswordRule,emailLabel,emailField);
        otherInfo.setSpacing(5);
        window.addSubview(otherInfo);
        window.setInsetsFor(otherInfo,240,null,null,30);

        return getRegisterChecking(window, fullNameField, usernameField, emailField,
                passwordField, confirmPasswordField, pictureView);
    }

    /**
     * Creates overlay on top of the pictureView and also gives
     * the functionality to add a new Picture.
     * @param pictureView Picture for the profile.
     */
    private static void addPicture(PictureView pictureView) {
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
        StyleSheet styleSheet = new StyleSheet();
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
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                try {
                    String externalImagePath = selectedFile.toURI().toURL().toExternalForm();
                    Image newImage = new Image(externalImagePath);
                    pictureView.setPicture(newImage);
                    pictureView.addOverlay(overlayView);
                    new AlertView("About the profile picture",
                            "Uploading profile picture at register isn't supported,"
                                    +   " instead after creating an account you can change "
                                    +  "you profile picture in the profile settings",
                            "I understand");
                } catch (MalformedURLException e2) {
                    System.out.println("File URL was incorrect");
                    new AlertView("An error occurred", "File URL was incorrect, ",
                            "Close");

                }
            }
        });
        overlayText.onMousePressedProperty().bind(backgroundView.onMousePressedProperty());
        overlayText.onMouseReleasedProperty().bind(backgroundView.onMouseReleasedProperty());

    }

    private static Scene getRegisterChecking(
            View window,
            TextField fullNameField,
            TextField usernameField,
            TextField emailField,
            TextField passwordField,
            TextField confirmPasswordField,
            PictureView pictureView
    ) {

        //CHECBOX

        CheckBox agreement = new CheckBox();
        window.addSubview(agreement);
        window.setInsetsFor(agreement,500,null,null,40);

        // Creating a stylesheet
        Hyperlink openfile = openfile("Terms and conditions");
        TextFlow terms = new TextFlow(new Text("I have read and I also agree to the "), openfile);
        terms.setMaxWidth(180);


        window.addSubview(terms);
        window.setInsetsFor(terms, 500, null, null, 70);

        //BUTTONS
        Button createAccountButton = new Button("Create Account");
        createAccountButton.setOnAction(e -> {

            String fullname = fullNameField.getText();
            String username = usernameField.getText().toLowerCase();
            String email = emailField.getText();
            String confirmPassword = confirmPasswordField.getText();
            String password = passwordField.getText();
            Image image = pictureView.getPicture();
            String path = image.getUrl();
            if (!fullname.isBlank()
                    && !username.isBlank()
                    && !email.isBlank()
                    && !password.isBlank()
                    && !confirmPassword.isBlank()
            ) {
                if (!isValidEmailAddress(email)) {
                    new AlertView("Not valid email",
                            "Inputted email address is not a valid one",
                            "got it!");
                    return;
                }
                if (!passwordsMatch(password, confirmPassword)) {
                    new AlertView("Different passwords", "Passwords didn't match!", "got it!");
                    return;
                } else if (!agreement.isSelected()) {
                    new AlertView(
                            "Empty agreement",
                            "Please accept the agreement to complete your account!",
                            "got it!"
                    );
                    return;
                } else {
                    System.out.println("Account created:");
                    System.out.println("username: " + username);
                    System.out.println("password: " + password);
                    System.out.println("name: " + fullname);
                    System.out.println("email: " + email);
                    DataAccess.createAccount(username, password, fullname, email);
                    //DataAccess.login(username, password);
                    //User user = DataAccess.getUserData();
                    //DataAccess.cachedImages.put(DataAccess.host
                    //+ user.getPicturePath(), new Image(path));
                    //DataAccess.sendImage(path);
                    //DataAccess.logout();
                    stage.setScene(getLogin());
                    new AlertView(
                            "Success",
                            "Created your account successfully, check it out!",
                            "okay!"
                    );
                }
            } else {
                new AlertView(

                        "Empty fields",
                        "You missed some fields, please fill everything in!",
                        "got it!"
                );
                return;
            }
        });

        //Styling the createAccount button
        StyleSheet styleSheet = new StyleSheet();
        styleSheet.set("font-size", "15");
        styleSheet.set("font-family", "Roboto Regular");
        styleSheet.set("text-fill", "white");
        styleSheet.set("font-weight", "bold");
        styleSheet.set("border-radius", "15");
        styleSheet.set("background-color", "#28B463");
        styleSheet.set(StyleSheet.MouseState.HOVERED,"background-color", "#43d680");
        styleSheet.set(StyleSheet.MouseState.PRESSED,"background-color", "#abedc7");
        styleSheet.set(StyleSheet.MouseState.HOVERED,"text-fill", "white");
        styleSheet.set(StyleSheet.MouseState.PRESSED,"text-fill", "black");
        styleSheet.applyOn(createAccountButton);
        window.addSubview(createAccountButton);
        window.setInsetsFor(createAccountButton, 500, 150, null, null);

        //Cancel button
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> stage.setScene(getLogin()));
        styleSheet.set("border-radius", "15");
        styleSheet.set("background-color", "#ee4466");
        styleSheet.set(StyleSheet.MouseState.HOVERED,"background-color", "#f48aa0");
        styleSheet.set(StyleSheet.MouseState.PRESSED,"background-color", "#f8b9c6");
        styleSheet.set(StyleSheet.MouseState.HOVERED,"text-fill", "white");
        styleSheet.set(StyleSheet.MouseState.PRESSED,"text-fill", "black");
        styleSheet.applyOn(cancelButton);
        styleSheet.clear();
        window.addSubview(cancelButton);
        window.setInsetsFor(cancelButton, 500, 50, null, null);


        //Adjusting the scene
        Scene scene = new Scene(window,550, 600);
        scene.setFill(Color.TRANSPARENT);
        return scene;
    }

    /**
     * Creates an icon on the task bar for the application.
     * @param stage Stage on which this icon replaces the default one.
     */
    public static void getIconApp(Stage stage) {
        InputStream inputStream = App.class.getResourceAsStream("leaf/leaf-50px.png");
        ImageView logo = null;
        if (inputStream == null) {
            System.out.println("Application icon couldn't be found");
        } else {
            Image image = new Image(inputStream);
            stage.getIcons().add(image);
        }
    }

    /**
     * the method checks whether the password and confirmpassword match.
     * @param password the password user insert first time
     * @param confirmPassword the password user insert second time
     * @return true if they match, false if they doesn't match
     */
    public static boolean passwordsMatch(String password, String confirmPassword) {
        if (Objects.equals(password, confirmPassword)) {
            return true;
        }
        return false;
    }

    /**
     *  Checks if the input email is a valid email address.
     * @param email Input email that will be checked if it is valid.
     * @return true if the entered email address is valid.
     */
    public static boolean isValidEmailAddress(String email) {
        String mailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\"
                + ".[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(mailPattern);
        java.util.regex.Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static Hyperlink openfile(String text) {
        Hyperlink openfile = new Hyperlink(text);
        openfile.getStyleClass().add("Link");
        openfile.getStylesheets().add(App.class.getResource("HomeScreenLight.css").toString());
        if (text.contains("Terms")) {
            openfile.setOnAction(e -> Pdf.openFile("term_conditions.pdf"));
        } else {
            openfile.setOnAction(e -> Pdf.openFile("app_license.pdf"));
        }
        return openfile;
    }
}

