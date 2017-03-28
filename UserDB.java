package sample;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gusty on 3/23/17.
 */
public class UserDB implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<User> users;
    private String currentUserName;
    private User currentUser;

    public UserDB() {
        users = new ArrayList<>();
        currentUserName = "Guest";
    }

    public void initializeUserDB() {
        System.out.println("\n\nHERE I AM\n\n");
        User[] usersForNow = {
                new User("gusty", "gusty", "gusty@x.com", "hello"),
                new User("emily", "emily", "emily@y.com", "goodbye")
        };
        String[] gustyFileUrls = {
                "file:/Users/gusty/Google Drive/00UMW/11CPSC240/Karen/code/instagramFiddling/img/giraffeOnBike.png",
                "file:/Users/gusty/Google Drive/00UMW/11CPSC240/Karen/code/instagramFiddling/img/7LayerCake.jpeg",
                "file:/Users/gusty/Google Drive/00UMW/11CPSC240/Karen/code/instagramFiddling/img/GustyZacDrivingUSSTexas.jpg",
                "file:/Users/gusty/Google Drive/00UMW/11CPSC240/Karen/code/instagramFiddling/img/GustyGiraffe.jpg"
        };
        String[] gustyTextInfo = {"Giraffe on a bike", "seven layer cake", "USS Texas driving", "gusty giraffe"};
        String[] emilyFileUrls = {
                "file:/Users/gusty/Google Drive/00UMW/11CPSC240/Karen/code/instagramFiddling/img/jeremy2.jpg",
                "file:/Users/gusty/Google Drive/00UMW/11CPSC240/Karen/code/instagramFiddling/img/augustDeMorgan.png",
                "file:/Users/gusty/Google Drive/00UMW/11CPSC240/Karen/code/instagramFiddling/img/colettasBirthday.JPG",
                "file:/Users/gusty/Google Drive/00UMW/11CPSC240/Karen/code/instagramFiddling/img/emilyGusty.JPG"
        };
        String[] emilyTextInfo = {"Jeremy photo", "augustus demorgan", "colletta's birthday", "emily and gusty photo"};

        for (User u : usersForNow) {
            users.add(u);
            String[] urls = null;
            String[] txt = null;
            if (u.getUserName().equals("gusty")) {
                urls = gustyFileUrls;
                txt = gustyTextInfo;
            } else {
                urls = emilyFileUrls;
                txt = emilyTextInfo;
            }
            for (int i = 0; i < 4; i++) {
                u.addInstaImage(new InstaImage(urls[i], null, txt[i]));
            }
        }
    }

    public User verifyUser(String user, String passWord) {
        System.out.println("HELLO\n.");
        for (User u : users) {
            System.out.println("HERE" + u);
            if (u.getUserName().equals(user) && u.getPassWord().equals(passWord)) {
                currentUser = u;
                return u;
            }
        }
        return null;
    }

    public String getCurrentUser() { return currentUserName; }

    public void setCurrentUser(String currentUserName) { this.currentUserName = currentUserName; }

    public String[] getUsers() {
        String[] retArray = new String[users.size()];
        for (int i = 0; i < users.size(); i++)
            retArray[i] = users.get(i).getUserName();
        return retArray;
    }

    public boolean registerUser(String userName, String passWord, String email, String aboutMe) {
        users.add(new User(userName, passWord, email, aboutMe));
        return true;
    }

    public InstaImage[] getCurrentUserImages() { return currentUser.getInstaImages(); }

    public InstaImage randomImage() {
        int r1 = (int)(users.size() * Math.random());
        InstaImage[] imgs = users.get(r1).getInstaImages();
        int r2 = (int)(imgs.length * Math.random());
        return imgs[r2];
    }

    public InstaImage[] getHashInstaImages(String hashValue) {
        List<InstaImage> l = new ArrayList<>();
        for (User u : users)
            for (InstaImage i : u.getInstaImages()) {
                if (i.getAboutImage().contains(hashValue))
                    l.add(i);
            }
        InstaImage[] retArray = new InstaImage[l.size()];
        for (int i = 0; i < l.size(); i++)
            retArray[i] = l.get(i);
        return retArray;
    }

}
