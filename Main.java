package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Instagram Fiddling");
        primaryStage.setScene(new Scene(root, 500, 600));
        primaryStage.show();
    }

    @Override
    public void stop() throws IOException {
        System.out.println("Stage is closing");
        System.out.println(Arrays.toString(udb.getUsers()));
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

    public static UserDB udb;
    private static boolean haveDBFile = true;
    private static boolean clearFollows = false;

    public static void main(String[] args) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println("Date is: " + dateFormat.format(date));

        if (haveDBFile) {
            System.out.println("Using file DB.");
            ObjectInputStream objectinputstream = null;
            FileInputStream streamIn = null;

            try {
                streamIn = new FileInputStream("instaFiddleDB.ser");
                objectinputstream = new ObjectInputStream(streamIn);
                udb = (UserDB) objectinputstream.readObject();

                if (clearFollows)
                    for (User u : udb.getUserObjects())
                        u.clearFollows();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (objectinputstream != null) {
                    objectinputstream.close();
                }
            }
            System.out.println(Arrays.toString(udb.getUsers()));
        }
        else {
            System.out.println("Using internal DB.");
            udb = new UserDB();
            udb.initializeUserDB();
        }
        launch(args);
    }
}
