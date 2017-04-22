package sample;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * InstaImage is a class for each image posted in instagramFiddling
 * The comparator and contains are not used at this time.
 * The date posted field was added late in development
 */
public class InstaImage implements Serializable {
    private static final long serialVersionUID = 1L;

    public static Comparator<InstaImage> DATE_COMPARATOR = new Comparator<InstaImage>() {
        @Override
        public int compare(final InstaImage o1, final InstaImage o2) {
            return o1.sortDate.compareTo(o2.sortDate);
        }
    };


    private String fileUrl;
    private String aboutImage;
    private Date datePosted;
    private String sortDate;
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public InstaImage(String fileUrl, Date datePosted, String aboutImage) {
        this.fileUrl = fileUrl;
        if (datePosted == null)
            datePosted = new Date();
        this.datePosted = datePosted;
        this.sortDate = dateFormat.format(datePosted);
        this.aboutImage = aboutImage;
    }

    public String getFileUrl() { return fileUrl; }

    public String getAboutImage() { return this.aboutImage; }

    public void setAboutImage(String aboutImage) { this.aboutImage = aboutImage; }

    public boolean contains(String s) { return aboutImage.contains(s); }
}
