package ir.unicoce.doctorMahmoud.Database;

import com.orm.SugarRecord;

/**
 * Created by soheil syetem on 11/26/2016.
 */

public class db_details extends SugarRecord {
    public int sid,parentid;
    public String title,content,imageurl;
    public boolean favorite;

    public db_details(){}

    public db_details(int sid, int parentid, String title, String content, String imageurl, boolean favorite) {
        this.sid = sid;
        this.parentid = parentid;
        this.title = title;
        this.content = content;
        this.imageurl = imageurl;
        this.favorite = favorite;
    }

    public int getsid() {
        return sid;
    }

    public int getparentid() {
        return parentid;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageurl;
    }

    public boolean isFavorite() {
        return favorite;
    }
}
