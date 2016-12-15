package ir.unicoce.doctorMahmoud.Database;

import com.orm.SugarRecord;

/**
 * Created by soheil syetem on 12/15/2016.
 */

public class db_reserv extends SugarRecord {

    public String reservationid;

    public db_reserv(){}

    public db_reserv(String reservationid){
        this.reservationid = reservationid;
    }
    public String getReservationid(){return reservationid;}
}
