package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by gusty on 3/28/17.
 */
public class MyImageView extends ImageView {

    private InstaImage img;

    public MyImageView(Image img, InstaImage instaImg) {
        super(img);
        this.img = instaImg;
    }

    public InstaImage getImg() { return img; }

}
