package ir.unicoce.doctorMahmoud.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import ir.unicoce.doctorMahmoud.Database.db_details;
import ir.unicoce.doctorMahmoud.Database.db_main;
import ir.unicoce.doctorMahmoud.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashActivity extends AppCompatActivity {

    public static Runnable runnable = null;
    public Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initDB();
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
//                overridePendingTransition(R.anim.modal_in,R.anim.fade_out);
                finish();
            }
        };

        handler.postDelayed(runnable, 1000);


    }

    private void initDB() {
        // TODO : add kardan b jadvale db_main
        // ba tavajoh b server init shavad
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
