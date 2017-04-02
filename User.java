package sample;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gusty on 3/23/17.
 */

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userName;
    private String passWord;
    private String email;
    private String aboutMe;
    private List<InstaImage> instaImages;
    private List<User> follows;

    public User(String userName, String passWord, String email, String aboutMe) {
        this.userName = userName;
        this.passWord = passWord;
        this.email = email;
        this.aboutMe = aboutMe;
        this.instaImages = new ArrayList<>();
        this.follows = new ArrayList<>();
        System.out.println("Created " + userName);
    }

    public String getUserName() { return userName; }

    public String getPassWord() { return passWord; }

    public void setPassWord(String password) { this.passWord = password; }

    public String getEmail() { return email; }

    public String getAboutMe() { return aboutMe; }

    public void setAboutMe(String aboutMe) { this.aboutMe = aboutMe; }

    public void addInstaImage(InstaImage im) { instaImages.add(im); }

    public InstaImage[] getInstaImages() {
        //return (InstaImage[])instaImages.toArray();
        InstaImage[] imgs = new InstaImage[instaImages.size()];
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = instaImages.get(i);
        return imgs;
    }

    public InstaImage[] getFeedImages() {
        InstaImage[][] imgs2d = new InstaImage[follows.size()][];

        System.out.println(Arrays.toString(follows.toArray()));
        int totImages = 0;
        for (int i = 0; i < follows.size(); i++ ) {
            imgs2d[i] = follows.get(i).getInstaImages();
            totImages += imgs2d[i].length;
        }
        System.out.println("totImages: " + totImages);
        InstaImage[] imgs = new InstaImage[totImages];
        int k = 0;
        for (int i = 0; i < follows.size(); i++)
            for (int j = 0; j < imgs2d[i].length; j++) {
                imgs[k] = imgs2d[i][j];
                k++;
            }

        return imgs;
    }

    public void clearFollows() { follows = new ArrayList<>(); }

    public int getNumFollows() { return follows != null ? follows.size() : 0; }

    public String[] getFollows() {
        if (follows.size() > 0) {
            String[] f = new String[follows.size()];
            for (int i = 0; i < follows.size(); i++)
                f[i] = follows.get(i).getUserName();
            return f;
        }
        return null;

    }

    public void follow(User u) {
        System.out.print("Adding: " + u.getUserName());
        System.out.println(follows == null);
        if (!follows.contains(u))
            follows.add(u);
        /*
        if (follows == null) {
            follows = new ArrayList<>();
            if (!follows.contains(u))
                follows.add(u);
        }
        else
            if (!follows.contains(u))
                follows.add(u);
        */
    }

    public void unFollow(User u) {
        if (this.follows != null && this.follows.size() > 0 && this.follows.contains(u))
            this.follows.remove(u);
    }

    public String toString() {
        return userName + " " + passWord + email + aboutMe + follows + instaImages;
    }

}
