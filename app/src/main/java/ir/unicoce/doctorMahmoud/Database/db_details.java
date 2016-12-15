package ir.unicoce.doctorMahmoud.Database;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by mohad syetem on 11/26/2016.
 */

public class db_details extends SugarRecord {
    public int sid;
    public int parentid;
    public int seennumber;
    public String title,content,imageurl,datecreated,datemodified;
    public String files;
    public boolean favorite;

    public db_details(){}

    public db_details(int sid, int parentid, String title, String content, String imageurl,
                      int seennumber,String datecreated,String datemodified,String files,boolean favorite) {
        this.sid = sid;
        this.parentid = parentid;
        this.title = title;
        this.content = content;
        this.imageurl = imageurl;
        this.seennumber=seennumber;
        this.datecreated=datecreated;
        this.datemodified=datemodified;
        this.files=files;
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

    public int getSeennumber() {return seennumber;}

    public String getDatecreated() {  return datecreated;}

    public String getDatemodified() {  return datemodified;}

    public String getFiles() {return files;}

    public boolean isFavorite() {
        return favorite;
    }
}
