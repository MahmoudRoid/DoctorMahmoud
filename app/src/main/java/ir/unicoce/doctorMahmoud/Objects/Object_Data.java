package ir.unicoce.doctorMahmoud.Objects;

import java.util.List;

import static android.R.id.list;

/**
 * Created by soheil syetem on 11/26/2016.
 */

public class Object_Data {

    public int sid,parentId,seenNumber;
    public String title,content,imageUrl,dateCreated,dateModified;
    public String files;
    public boolean favoirite ;

    public Object_Data(int sid, int parentId, String title, String content, String imageUrl
            ,int seenNumber,String dateCreated,String dateModified,String files, boolean favoirite) {
        this.sid = sid;
        this.parentId = parentId;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.seenNumber=seenNumber;
        this.dateCreated=dateCreated;
        this.dateModified=dateModified;
        this.files=files;
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

    public int getSeenNumber() {  return seenNumber;}

    public String getDateCreated() {  return dateCreated;}

    public String getDateModified() {    return dateModified;}

    public String getFiles() { return files;}

    public boolean isFavoirite() {
        return favoirite;
    }
}
