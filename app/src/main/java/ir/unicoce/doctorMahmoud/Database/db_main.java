package ir.unicoce.doctorMahmoud.Database;

import com.orm.SugarRecord;

/**
 * Created by soheil syetem on 11/25/2016.
 */

public class db_main extends SugarRecord {
    public int parentid,sid;
    public String title;
    public String folderImageUrl;

    public db_main(){}

    public db_main(int parentid, int sid, String title,String folderImageUrl) {
        this.parentid = parentid;
        this.sid = sid;
        this.title = title;
        this.folderImageUrl = folderImageUrl;
    }

    public int getparentid() {  return parentid;}
    public int getsid() { return sid;}
    public String getTitle(){return title;}
    public int getParentid() {
        return parentid;
    }
    public void setParentid(int parentid) {
        this.parentid = parentid;
    }
    public String getFolderImageUrl() {
        return folderImageUrl;
    }
    public void setFolderImageUrl(String folderImageUrl) {
        this.folderImageUrl = folderImageUrl;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
