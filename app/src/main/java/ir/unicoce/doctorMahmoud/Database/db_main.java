package ir.unicoce.doctorMahmoud.Database;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by mohad syetem on 11/25/2016.
 */

public class db_main extends SugarRecord {
    public int parentid,sid;
    public String title;
    public String folderimageurl;

    public db_main(){}

    public db_main(int parentid, int sid, String title,String folderimageurl) {
        this.parentid = parentid;
        this.sid = sid;
        this.title = title;
        this.folderimageurl = folderimageurl;
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
    public void setTitle(String title) {
        this.title = title;
    }
    public String getFolderimageurl() {
        return folderimageurl;
    }
    public void setFolderimageurl(String folderimageurl) {
        this.folderimageurl = folderimageurl;
    }

}
