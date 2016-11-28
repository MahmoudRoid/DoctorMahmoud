package ir.unicoce.doctorMahmoud.Helper;

/**
 * Created by soheil syetem on 11/26/2016.
 */

public class AboutUs {

    public int sid,parentId;
    public String title,content,imageUrl;
    public boolean favoirite ;

    public AboutUs(int sid, int parentId, String title, String content, String imageUrl, boolean favoirite) {
        this.sid = sid;
        this.parentId = parentId;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.favoirite = favoirite;
    }

    public int getSid() {
        return sid;
    }

    public int getParentId() {
        return parentId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isFavoirite() {
        return favoirite;
    }
}
