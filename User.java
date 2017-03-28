package sample;

import java.io.Serializable;
import java.util.ArrayList;
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

    public User(String userName, String passWord, String email, String aboutMe) {
        this.userName = userName;
        this.passWord = passWord;
        this.email = email;
        this.aboutMe = aboutMe;
        this.instaImages = new ArrayList<>();
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

    public String toString() {
        return userName + " " + passWord;
    }

}
