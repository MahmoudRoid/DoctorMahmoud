package ir.unicoce.doctorMahmoud.BroadcastReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.orm.query.Select;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.unicoce.doctorMahmoud.AsyncTasks.CheckReservationStatusPost;
import ir.unicoce.doctorMahmoud.Classes.Internet;
import ir.unicoce.doctorMahmoud.Database.db_reserv;
import ir.unicoce.doctorMahmoud.Interface.ITest;
import ir.unicoce.doctorMahmoud.Interface.IWebservice;
import ir.unicoce.doctorMahmoud.Interface.IWebservice2;
import ir.unicoce.doctorMahmoud.Service.ReservationService;

import static android.content.Context.MODE_PRIVATE;

public class AlarmNotification extends BroadcastReceiver implements ITest{

    public Context context;
    MediaPlayer mediaPlayer;
    AlarmManager am;
    PendingIntent pi;
    public SharedPreferences prefs;



    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        // Put here YOUR code.
        checkForReservation();

        wl.release();
    }

    private void checkForReservation() {
        if(Internet.isNetworkAvailable(context)){
            CheckReservationStatusPost post = new CheckReservationStatusPost(context,getServiceId());
            post.execute();
        }
    }

    private String getServiceId() {
         // get data from db
        List<db_reserv> db = Select.from(db_reserv.class).list();
        String serviceId = db.get(db.size()-1).getReservationid();

        return serviceId;
    }

    public void SetAlarm(Context context)
    {
        am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context,AlarmNotification.class);
        pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),30000, pi); // 900000 = 15 min

    }

    public void CancelAlarm(Context context)
    {
        try {
            pi.cancel();
            am.cancel(pi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void cancelAlarm() {
        try {
            pi.cancel();
            am.cancel(pi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
