package ir.unicoce.doctorMahmoud.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import ir.unicoce.doctorMahmoud.BroadcastReceiver.AlarmNotification;

public class ReservationService extends Service {
    AlarmNotification alarm = new AlarmNotification();
    public ReservationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarm.SetAlarm(this);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        alarm.CancelAlarm(this);
        super.onDestroy();
    }

    @Override
    public boolean stopService(Intent name) {
        try {
            alarm.CancelAlarm(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.stopService(name);

    }
}
