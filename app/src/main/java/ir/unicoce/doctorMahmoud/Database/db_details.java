package ir.unicoce.doctorMahmoud.Database;

import com.orm.SugarRecord;

/**
 * Created by soheil syetem on 11/26/2016.
 */

public class db_details extends SugarRecord {
    public int sId,parentId;
    public String title,content,imageUrl;
    public boolean favorite;

    public db_details(int sId, int parentId, String title, String content, String imageUrl, boolean favorite) {
        this.sId = sId;
        this.parentId = parentId;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.favorite = favorite;
    }

    public int getsId() {
        return sId;
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

    public boolean isFavorite() {
        return favorite;
    }
}
