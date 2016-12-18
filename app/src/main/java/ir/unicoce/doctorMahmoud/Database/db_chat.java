package ir.unicoce.doctorMahmoud.Database;

import com.orm.SugarRecord;

/**
 * Created by soheil syetem on 12/18/2016.
 */

public class db_chat extends SugarRecord {
    String message ;
    boolean isme;

    public db_chat(){}

    public db_chat(String message, boolean isme) {
        this.message = message;
        this.isme = isme;
    }

    public String getMessage() {
        return message;
    }

    public boolean isme() {
        return isme;
    }
}
