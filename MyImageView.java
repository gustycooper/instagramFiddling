package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * MyImageView is used to connect InstaImage to an ImageView in the vbox.
 * See code in Controller.displayImages() for how this works.
 * Each image is a clickable item.
 * When clicked a dialog box with comments about the image is displayed.
 * The user can add text to the dialog box and select ok.
 * The added text is placed in the InstaImage
 * The mouse event handler can access the InstaImage text via the following.
 *     imageViewA.getImg().setAboutImage(s);
 * Where imageViewA is a MyImageView object
 */
public class MyImageView extends ImageView {

    private InstaImage img;

    public MyImageView(Image img, InstaImage instaImg) {
        super(img);
        this.img = instaImg;
    }

    public InstaImage getImg() { return img; }

}
