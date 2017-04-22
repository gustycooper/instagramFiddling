package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Main extends Application {
    /**
     * Method starts the JavaFX Scene
     * Gets the controller object so timer can upate time on window.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml")); // Original call
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Controller c = loader.getController();
        Timeline oneSecondWonder = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String time = timeFormat.format(new Date());
                //System.out.println("Time is: " + time);
                c.setDisplayTime(time);
            }
        }));
        oneSecondWonder.setCycleCount(Timeline.INDEFINITE);
        oneSecondWonder.play();
        primaryStage.setTitle("Instagram Fiddling");
        primaryStage.setScene(new Scene(root, 500, 600));
        primaryStage.show();
    }

    /**
     * Method called with JavaFX Scene is closed
     * Writes the DB to a file
     * @throws IOException
     */
    @Override
    public void stop() throws IOException {
        System.out.println("Stage is closing");
        System.out.println("User Names in DB: " + Arrays.toString(udb.getUsers()));
        // Save file - add code here
        ObjectOutputStream oos = null;
        FileOutputStream fout = null;
        try{
            fout = new FileOutputStream("instaFiddleDB.ser", false);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(udb);
            System.out.println("instatFiddleDB.ser written");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(oos != null){
                oos.close();
            }
        }

    }

    /*
     * udb - The user data base - user names, passwords, images, comments
     */
    public static UserDB udb;
    /*
     * clearview - controls clearing the view when users login or change view
     */
    public static boolean clearView = false;
    /*
     * haveDBFile - indicates we have a DB file.
     * Set to false resets the DB to two users - gusty and emily - see UserDB.initializeUserDB()
     */
    private static boolean haveDBFile = true;
    /*
     * clearFollows - Used to reset follows to an empty array list.
     * Needed to fix serialized file during development - may come in handy
     */
    private static boolean clearFollows = false;

    /*
     * DateFormat objects used to format a Date object
     */
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * main method that starts everything.
     * Reads the DB or uses UserDB.initializeDB
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Date date = new Date();
        System.out.println("Date is: " + dateFormat.format(date));  // practice using Data nd DateFormat
        System.out.println("Time is: " + timeFormat.format(date));

        if (haveDBFile) {
            System.out.println("Using file DB.");
            ObjectInputStream objectinputstream = null;
            FileInputStream streamIn = null;

            try {
                streamIn = new FileInputStream("instaFiddleDB.ser");
                objectinputstream = new ObjectInputStream(streamIn);
                udb = (UserDB) objectinputstream.readObject();

                if (clearFollows)
                    for (User u : udb.getUserObjects()) {
                        u.clearFollows();
                    }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (objectinputstream != null) {
                    objectinputstream.close();
                }
            }
            System.out.println("User Names in DB: " + Arrays.toString(udb.getUsers()));
        }
        else {
            System.out.println("Using internal DB.");
            udb = new UserDB();
            udb.initializeUserDB();
        }
        launch(args);
    }
}
