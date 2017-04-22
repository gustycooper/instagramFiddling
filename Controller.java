package sample;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Contains code to handle the GUI events
 */
public class Controller {

    private User currentUser = null; // User that is currently logged in

    /*
     * variables and method used to flop the first picture in the feed
     * If the variable Main.clearView is true, the first picture (and others) are cleared from view each time the
     * view changes, i.e., new login or view menue selected
     */
    private int flipFlop = 0;
    private Image gustyGiraffe = new Image("file:/Users/gusty/Google%20Drive/00UMW/11CPSC240/Karen/code/instagramFiddling/img/GustyGiraffe.jpg");
    private Image zacNavy = new Image("file:/Users/gusty/Google%20Drive/00UMW/11CPSC240/Karen/code/instagramFiddling/img/zacNavy.jpg");

    private void flopPicture() {
        // model: retrieve a random image for display
        InstaImage img = Main.udb.randomImage();
        Image imageB = new Image(img.getFileUrl());
        switch (++flipFlop % 3) {
            case 0:
                label1.setText("Gusty and his wooden giraffe");
                image1.setImage(gustyGiraffe);
                break;
            case 1:
                label1.setText("Zac in his Navy uniform.");
                image1.setImage(zacNavy);
                break;
            case 2:
                label1.setText("Random Image from Instagram");
                image1.setImage(imageB);
        }
    }

    /**
     * The various FXML variables origniate from SceneBuildier
     */
    @FXML
    private Label label1;  // label on the first image

    @FXML
    private ImageView image1; // first image

    @FXML
    private ScrollPane mainScrollPane;

    @FXML
    private VBox vbox; // vbox in the mainScrollPane

    @FXML
    private Label whoAmI; // label that displays name of current user (or Guest)

    /**
     * displayTime is used to display time of day
     * displayTime has a setter and getter (setter not used)
     */
    @FXML
    private Label displayTime;

    public String getDisplayTime() { return displayTime.getText(); }
    public void setDisplayTime(String s) { displayTime.setText(s); }

    @FXML
    private Menu postInfo;

    @FXML
    private MenuItem loginMenuItem;

    @FXML
    private MenuItem logoutMenuItem;

    @FXML
    private MenuItem registerMenuItem;

    @FXML
    private RadioMenuItem feedMenuItem;

    @FXML
    private CustomMenuItem customMenuItem;

    @FXML
    private CheckBox customCheckBox;

    @FXML
    private TextField customTextField;

    @FXML
    private RadioMenuItem mystuffMenuItem;

    @FXML
    private Menu followMenu;

    private List<RadioMenuItem> followItems;

    // Method that handles mouse clicks in the first picture in the feed
    @FXML
    void handlePicture1(MouseEvent event) {
        System.out.println("Picture 1 clicked!");
        flopPicture();
    }

    // Method that handles the Post Info > Choose menu item
    @FXML
    void handleChooseFile(ActionEvent event) {
        System.out.println("Handle Choose File Selected");
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(mainScrollPane.getScene().getWindow());
        String filename = selectedFile.getName();
        String path = selectedFile.getAbsolutePath();
        path = "file:" + path;
        System.out.println("Filename: " + filename);
        System.out.println("Filepath: " + path);

        // model: Add image to the current user
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date imageAddedDate = new Date();
        InstaImage img = new InstaImage(path, imageAddedDate, "Date Image Added: " + dateFormat.format(imageAddedDate));
        currentUser.addInstaImage(img);

        Label label = new Label(filename);
        vbox.setPrefHeight(vbox.getPrefHeight() + label.getPrefHeight());
        vbox.getChildren().add(label);
        Image imageA = new Image(path);
        //ImageView imageViewA = new ImageView(imageA);
        MyImageView imageViewA = new MyImageView(imageA, img);
        imageViewA.setFitHeight(155);
        imageViewA.setFitWidth(125);
        // New stuff
        imageViewA.setOnMouseClicked(
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                        Stage primaryStage = (Stage) label.getScene().getWindow();
                        final Stage dialog = new Stage();
                        dialog.initModality(Modality.APPLICATION_MODAL);
                        dialog.initOwner(primaryStage);
                        VBox dialogVbox = new VBox(20);
                        Scene dialogScene = new Scene(dialogVbox, 300, 200);

                        GridPane gridpane = new GridPane();
                        gridpane.setPadding(new Insets(5));
                        gridpane.setHgap(5);
                        gridpane.setVgap(5);

                        Label infoLbl = new Label("Image Info: ");
                        infoLbl.setMinHeight(30);
                        infoLbl.setMinWidth(80);
                        gridpane.add(infoLbl, 0, 1);
                        TextArea textAreaFld = new TextArea(img.getAboutImage());
                        textAreaFld.setEditable(true);
                        textAreaFld.setWrapText(true);
                        gridpane.add(textAreaFld, 1, 1);

                        Button addInfo = new Button("Add Info");
                        addInfo.setOnAction(new EventHandler<ActionEvent>() {
                            public void handle(ActionEvent event) {
                                System.out.println("Handle Add Info Button");
                                String s = textAreaFld.getText();
                                img.setAboutImage(s);
                                System.out.println("Add Info Text: " + s);
                                dialog.close();
                            }
                        });
                        gridpane.add(addInfo, 1, 3);
                        GridPane.setHalignment(addInfo, HPos.RIGHT);
                        dialogVbox.getChildren().add(gridpane);
                        dialog.setScene(dialogScene);
                        dialog.show();
                    }
                }
            });
        // New stuff
        vbox.getChildren().add(imageViewA);
        //vbox.getChildren();
        mainScrollPane.setContent(vbox);
    }

    // Method that processes logins, Account > Login menu item
    @FXML
    void handleLogin(ActionEvent event) {
        Stage primaryStage = (Stage) whoAmI.getScene().getWindow();
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text("Login Dialog"));
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        // New code
        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(5));
        gridpane.setHgap(5);
        gridpane.setVgap(5);

        Label userNameLbl = new Label("User Name: ");
        gridpane.add(userNameLbl, 0, 1);

        Label passwordLbl = new Label("Password: ");
        gridpane.add(passwordLbl, 0, 2);
        final TextField userNameFld = new TextField("");
        gridpane.add(userNameFld, 1, 1);

        final PasswordField passwordFld = new PasswordField();
        passwordFld.setText("");
        gridpane.add(passwordFld, 1, 2);

        Button login = new Button("Login");
        login.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String userName = userNameFld.getText();
                String passWord = passwordFld.getText();
                System.out.println("Login:" + userName + " " + passWord);

                // model: Verify the userName and passWord
                currentUser = Main.udb.verifyUser(userName, passWord);
                if (currentUser != null) {
                    System.out.println("Good User: " + currentUser.getUserName() + " " + currentUser.getPassWord());
                    postInfo.setDisable(false);
                    feedMenuItem.setDisable(false);
                    mystuffMenuItem.setDisable(false);
                    loginMenuItem.setDisable(true);
                    logoutMenuItem.setDisable(false);
                    registerMenuItem.setDisable(true);
                    followMenu.setDisable(false);
                    whoAmI.setText(userName);
                    // Add users and who is being followed
                    followMenu.getItems().clear();
                    // model: Get the users from the DB and display them in the follow menu
                    // Check those that are being followed
                    String[] follows = currentUser.getFollows(); // null indicates does not follow anyone
                    String[] users = Main.udb.getUsers();
                    final RadioMenuItem[] items = new RadioMenuItem[users.length];
                    followItems = new ArrayList<RadioMenuItem>();
                    for (int i = 0; i < users.length; i++) {
                        items[i] = new RadioMenuItem(users[i]);
                        followItems.add(items[i]);
                        followMenu.getItems().addAll(items[i]);
                        if (follows != null)  // make sure the user follows someone
                            for (String s : follows)
                                if (s.equals(users[i]))
                                    items[i].setSelected(true);
                    }

                    // model: Display current user's images to the feed
                    InstaImage[] imgs = currentUser.getInstaImages();
                    displayImages(imgs, "My Stuff");
                }
                else
                    System.out.println("Bad User");
                dialog.close();
            }
        });
        gridpane.add(login, 1, 3);
        GridPane.setHalignment(login, HPos.RIGHT);
        dialogVbox.getChildren().add(gridpane);
        // New code end
        dialog.setScene(dialogScene);
        dialog.show();
    }

    // Method the processes logouts, Account > Logout
    @FXML
    void handleLogout(ActionEvent event) {
        postInfo.setDisable(true);
        feedMenuItem.setDisable(true);
        mystuffMenuItem.setDisable(true);
        loginMenuItem.setDisable(false);
        logoutMenuItem.setDisable(true);
        registerMenuItem.setDisable(false);
        followMenu.setDisable(true);

        whoAmI.setText("Guest");
    }

    // Method that processes the Account > Exit menu item
    @FXML
    void handleExit(ActionEvent event) {
        Platform.exit();
        //System.exit(0); // if you call System.exit, stop() is not called
    }

    // Method that processes account registrations, Account > Register
    @FXML
    void handleRegister(ActionEvent event) {
        System.out.println("Handle Register.");
        Stage primaryStage = (Stage) whoAmI.getScene().getWindow();
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox(20);
        Scene dialogScene = new Scene(dialogVbox, 500, 400);
        // New code
        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(5));
        gridpane.setHgap(5);
        gridpane.setVgap(5);

        // User Name
        Label userNameLbl = new Label("User Name: ");
        userNameLbl.setMinHeight(30);
        userNameLbl.setMinWidth(80);
        gridpane.add(userNameLbl, 0, 1);

        final TextField userNameFld = new TextField("");
        gridpane.add(userNameFld, 1, 1);

        // Password
        Label passwordLbl = new Label("Password: ");
        passwordLbl.setMinHeight(30);
        passwordLbl.setMinWidth(80);
        gridpane.add(passwordLbl, 0, 2);

        final PasswordField passwordFld = new PasswordField();
        passwordFld.setText("");
        gridpane.add(passwordFld, 1, 2);

        // Email
        Label emailLbl = new Label("Email: ");
        emailLbl.setMinHeight(30);
        emailLbl.setMinWidth(80);
        gridpane.add(emailLbl, 0, 3);

        final TextField emailFld = new TextField("");
        gridpane.add(emailFld, 1, 3);

        // About You
        Label textAreaLbl = new Label("About You: ");
        textAreaLbl.setMinHeight(30);
        textAreaLbl.setMinWidth(80);
        gridpane.add(textAreaLbl, 0, 4);
        TextArea textAreaFld = new TextArea("");
        textAreaFld.setEditable(true);
        textAreaFld.setWrapText(true);
        gridpane.add(textAreaFld, 1, 4);

        Button register = new Button("Register");
        register.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String userName = userNameFld.getText();
                String passWord = passwordFld.getText();
                String email = emailFld.getText();
                String personalDescription = textAreaFld.getText();
                System.out.println("Register:" + userName + " " + passWord + personalDescription);
                if (!userName.equals("") && !passWord.equals("")) {
                    System.out.println("Good Registration");

                    // model: register the new user
                    Main.udb.registerUser(userName, passWord, email, personalDescription);
                }
                else
                    System.out.println("Bad Registration");
                dialog.close();
            }
        });
        gridpane.add(register, 1, 5);
        GridPane.setHalignment(register, HPos.RIGHT);
        dialogVbox.getChildren().add(gridpane);
        // New code end
        dialog.setScene(dialogScene);
        dialog.show();

    }

    String currentFeedSelection = "none";

    @FXML
    void handleFeed(ActionEvent event) {
        System.out.println("Handle Feed");
        mystuffMenuItem.setSelected(false);
        customCheckBox.setSelected(false);
        if (feedMenuItem.isSelected()) {
            System.out.println("MenuItem: " + feedMenuItem.getText() + (feedMenuItem.selectedProperty().getValue() ? " checked" : " unchecked"));
            // model: Display current user's images to the feed
            InstaImage[] imgs = currentUser.getFeedImages();
            displayImages(imgs, "My Feed");
        }
        else {
            System.out.println("MenuItem: " + feedMenuItem.getText() + (feedMenuItem.selectedProperty().getValue() ? " checked" : " unchecked"));
            feedMenuItem.setSelected(true);
        }
        System.out.println(currentUser.getUserName() + " follows:");
        String[] ss = currentUser.getFollows();
        if (ss != null)
            for (String s : ss)
                System.out.println(s);
        else
            System.out.println("No one");
    }

    @FXML
    void handleMyStuff(ActionEvent event) {
        System.out.println("Handle MyStuff");
        feedMenuItem.setSelected(false);
        customCheckBox.setSelected(false);
        if (mystuffMenuItem.isSelected()) {
            System.out.println("MenuItem: " + mystuffMenuItem.getText() + (mystuffMenuItem.selectedProperty().getValue() ? " checked" : " unchecked"));
            InstaImage[] imgs = currentUser.getInstaImages();
            displayImages(imgs, "My Stuff");
        }
        else {
            System.out.println("MenuItem: " + mystuffMenuItem.getText() + (mystuffMenuItem.selectedProperty().getValue() ? " checked" : " unchecked"));
            mystuffMenuItem.setSelected(true);
        }
    }

    private void displayImages(InstaImage[] imgs, String type) {
        System.out.println("displayImages() Information");
        System.out.println("InstaImage[]: " + Arrays.toString(imgs));
        String[] fileUrls = new String[imgs.length+1];
        String[] aboutImages = new String[imgs.length+1];
        fileUrls[0] = "file:/Users/gusty/Google%20Drive/00UMW/11CPSC240/Karen/code/instagramFiddling/img/changeView.png";
        aboutImages[0] = "User: " + currentUser.getUserName() + " Image View: " + type;
        for (int i = 0; i < imgs.length; i++) {
            fileUrls[i+1] = imgs[i].getFileUrl();
            aboutImages[i+1] = imgs[i].getAboutImage();
            // Todo - figure how to put aboutImages into the popup text box
        }
        if (Main.clearView) {
            vbox.getChildren().clear();
            /*
             * Next line attempts to keep the flip-flopping first image
             * The approach only works for the first login.
             */
            //vbox.getChildren().remove(1,vbox.getChildren().size()-1);
        }
        Label labelView = new Label(aboutImages[0]);
        vbox.setPrefHeight(vbox.getPrefHeight() + labelView.getPrefHeight());
        vbox.getChildren().add(labelView);
        Image imageView = new Image(fileUrls[0]);
        ImageView imageViewView = new ImageView(imageView);
        imageViewView.setFitHeight(200);
        imageViewView.setFitWidth(275);
        vbox.getChildren().add(imageViewView);
        mainScrollPane.setContent(vbox);
        for (InstaImage img : imgs) {
        //for (String f : fileUrls) {
            System.out.println(img.getFileUrl());
            Label label = new Label(img.getFileUrl());
            vbox.setPrefHeight(vbox.getPrefHeight() + label.getPrefHeight());
            vbox.getChildren().add(label);
            Image imageA = new Image(img.getFileUrl());
            //ImageView imageViewA = new ImageView(imageA);
            MyImageView imageViewA = new MyImageView(imageA, img);
            imageViewA.setFitHeight(155);
            imageViewA.setFitWidth(125);
            imageViewA.setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                            System.out.println(imageViewA.getImg().getFileUrl());
                            System.out.println("Image mouseEvent: " + mouseEvent.toString());
                            System.out.println("Image mouseEvent source: " + mouseEvent.getSource().toString());
                            System.out.println("Image mouseEvent sorce hashcode: " + mouseEvent.getSource().hashCode());
                            Stage primaryStage = (Stage) label.getScene().getWindow();
                            final Stage dialog = new Stage();
                            dialog.initModality(Modality.APPLICATION_MODAL);
                            dialog.initOwner(primaryStage);
                            VBox dialogVbox = new VBox(20);
                            Scene dialogScene = new Scene(dialogVbox, 300, 200);

                            GridPane gridpane = new GridPane();
                            gridpane.setPadding(new Insets(5));
                            gridpane.setHgap(5);
                            gridpane.setVgap(5);

                            Label infoLbl = new Label("Image Info: ");
                            infoLbl.setMinHeight(30);
                            infoLbl.setMinWidth(80);
                            gridpane.add(infoLbl, 0, 1);
                            TextArea textAreaFld = new TextArea(imageViewA.getImg().getAboutImage());
                            textAreaFld.setEditable(true);
                            textAreaFld.setWrapText(true);
                            gridpane.add(textAreaFld, 1, 1);

                            Button addInfo = new Button("Add Info");
                            addInfo.setOnAction(new EventHandler<ActionEvent>() {
                                public void handle(ActionEvent event) {
                                    System.out.println("Add Info Button Selected");
                                    String s = textAreaFld.getText();
                                    System.out.println("Text in Add Info: " + s);
                                    imageViewA.getImg().setAboutImage(s);
                                    dialog.close();
                                }
                            });
                            gridpane.add(addInfo, 1, 3);
                            GridPane.setHalignment(addInfo, HPos.RIGHT);
                            dialogVbox.getChildren().add(gridpane);
                            dialog.setScene(dialogScene);
                            dialog.show();
                        }
                    }
                });
            vbox.getChildren().add(imageViewA);
            mainScrollPane.setContent(vbox);
        }
    }

    @FXML
    void handleSearchHashTag(ActionEvent event) {
        System.out.println("Handle Search HashTag");
        feedMenuItem.setSelected(false);
        mystuffMenuItem.setSelected(false);
        String text = customTextField.getText();
        System.out.println("Text in Search Field: " + text);
        if (customCheckBox.isSelected()) {
            System.out.println("MenuItem: " + customCheckBox.getText() + (customCheckBox.selectedProperty().getValue() ? " checked" : " unchecked"));
            // model: Display images that match the hashtag
            InstaImage[] imgs = Main.udb.getHashInstaImages(text);
            displayImages(imgs, "Search Results");
        }
        else {
            System.out.println("MenuItem: " + customCheckBox.getText() + (customCheckBox.selectedProperty().getValue() ? " checked" : " unchecked"));
            customCheckBox.setSelected(true);
        }
        //customCheckBox.setSelected(true);
    }

    @FXML
    void handleFollowMenu(ActionEvent event) {
        System.out.println("Handle Follow Menu.");
        for (RadioMenuItem rm : followItems) {
            if (rm.isSelected()) {
                System.out.println("MenuItem: " + rm.getText() + (rm.selectedProperty().getValue() ? " checked" : " unchecked"));
                Main.udb.follow(currentUser, rm.getText());
            }
            else {
                System.out.println("MenuItem: " + rm.getText() + (rm.selectedProperty().getValue() ? " checked" : " unchecked"));
                Main.udb.unFollow(currentUser, rm.getText());
            }
        }
    }

    @FXML
    void handleAbout(ActionEvent event) {
        System.out.println("Handle Register.");
        Stage primaryStage = (Stage) whoAmI.getScene().getWindow();
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox(20);
        TextArea textAreaFld = new TextArea("");
        dialogVbox.getChildren().add(new Text("Instagram Fiddling\nCPSC 240 Final Project."));
        Scene dialogScene = new Scene(dialogVbox, 200, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

}
/*
 * todo - fix image file references to use: images[i] = new Image(getClass().getResourceAsStream(imageNames[i]));
 */