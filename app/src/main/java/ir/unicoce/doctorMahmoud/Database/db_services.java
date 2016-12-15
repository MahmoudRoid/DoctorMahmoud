package ir.unicoce.doctorMahmoud.Database;

import com.orm.SugarRecord;

/**
 * Created by soheil syetem on 12/11/2016.
 */

public class db_services extends SugarRecord {

    public String title , content , price;

    public db_services(){}

    public db_services(String title, String content, String price) {
        this.title = title;
        this.content = content;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getPrice() {
        return price;
    }
}
