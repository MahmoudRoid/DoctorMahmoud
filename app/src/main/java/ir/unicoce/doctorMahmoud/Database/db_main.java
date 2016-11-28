package ir.unicoce.doctorMahmoud.Database;

import com.orm.SugarRecord;

/**
 * Created by soheil syetem on 11/25/2016.
 */

public class db_main extends SugarRecord {
    public int parentID,sId;
    public String title;

    public db_main(){}

    public db_main(int parentID, int sId, String title) {
        this.parentID = parentID;
        this.sId = sId;
        this.title = title;
    }

    public int getParentID() {  return parentID;}
    public int getsId() { return sId;}
    public String getTitle(){return title;}
}
