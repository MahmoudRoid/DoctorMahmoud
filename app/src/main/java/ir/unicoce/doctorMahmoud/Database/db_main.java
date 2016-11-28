package ir.unicoce.doctorMahmoud.Database;

import com.orm.SugarRecord;

/**
 * Created by soheil syetem on 11/25/2016.
 */

public class db_main extends SugarRecord {
    public int parentid,sid;
    public String title;

    public db_main(){}

    public db_main(int parentid, int sid, String title) {
        this.parentid = parentid;
        this.sid = sid;
        this.title = title;
    }

    public int getparentid() {  return parentid;}
    public int getsid() { return sid;}
    public String getTitle(){return title;}
}
