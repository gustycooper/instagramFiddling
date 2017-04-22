package sample;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDB implements the database of users for instagramFiddling
 * The method initializeUserDB is used to bootstrap the DB.  See Main.haveDBfile for how this is done
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

    /**
     * Verifies user and passWord are in the DB
     * @param user
     * @param passWord
     * @return User or null
     */
    public User verifyUser(String user, String passWord) {
        System.out.println("Verifying User:" + user + " " + passWord);
        for (User u : users) {
            System.out.println("User: " + u.getUserName() + " " + u.getPassWord());

            if (u.getUserName().equals(user) && u.getPassWord().equals(passWord)) {
                currentUser = u;
                return u;
            }
        }
        return null;
    }

    public String getCurrentUser() { return currentUserName; }

    public void setCurrentUser(String currentUserName) { this.currentUserName = currentUserName; }

    /**
     * Create a String[] of current users
     * @return
     */
    public String[] getUsers() {
        String[] retArray = new String[users.size()];
        for (int i = 0; i < users.size(); i++)
            retArray[i] = users.get(i).getUserName();
        return retArray;
    }

    /**
     * Create User[] of current users
     * @return
     */
    public User[] getUserObjects() {
        User[] retArray =  new User[users.size()];
        for (int i = 0; i < users.size(); i++)
            retArray[i] =  users.get(i);
        return retArray;
    }

    /**
     * See if userName is in DB
     * @param userName String of user name
     * @return User or null
     */
    private User findUser(String userName) {
        for (User u : users)
            if (u.getUserName().equals(userName))
                return u;
        return null;
    }

    /**
     * Establish User u to follow the User for userName
     * @param u
     * @param userName
     */
    public void follow(User u, String userName) {
        User follower = findUser(userName);
        System.out.println("In UserDB.follow");
        if (follower != null && follower != u) {
            System.out.println("UserDB.follow calls u.follow");
            u.follow(follower);
        }
    }

    public void unFollow(User u, String userName) {
        User follower = findUser(userName);
        if (follower != null)
            u.unFollow(follower);
    }

    /**
     * Place a new user in the DB.  Assumes all parameters have good data.
     * @param userName
     * @param passWord
     * @param email
     * @param aboutMe
     * @return
     */
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

    /**
     * Search all images for the hashValue
     * @param hashValue This is really a string that may contain a # char - like #hello.  # is not important
     * @return array of InstaImage, where each InstaImage has the string in its aboutImage field
     */
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
