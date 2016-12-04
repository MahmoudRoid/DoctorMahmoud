package ir.unicoce.doctorMahmoud.Activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class SupportActivity extends AppCompatActivity {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // show map
        setMap("35.8057634","50.9815057","گروه نرم افزاری یونیکد");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : reform phone number blow
                Intent callIntent = new Intent(Intent.ACTION_VIEW);
                callIntent.setData(Uri.parse("tel:02632752049"));
                startActivity(callIntent);
            }
        });
    }

    public  void setMap(String Lat, String Lng, String titld){
        try {
            mMap.clear();
            LatLng mLatLan = new LatLng(Float.parseFloat(Lat), Float.parseFloat(Lng));
            CameraUpdate cam = CameraUpdateFactory.newLatLngZoom(mLatLan,17);
            mMap.animateCamera(cam);
            mMap.addMarker(new MarkerOptions().position(mLatLan)
                    .title(titld)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        } // end try
        catch (Exception e) { e.printStackTrace(); }
    }

    @OnClick({R.id.support_website, R.id.support_telegram, R.id.support_instagram, R.id.support_googleplus, R.id.support_email, R.id.support_apple})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.support_website:
                openWebSite();
                break;
            case R.id.support_telegram:
                openTelegramChannel();
                break;
            case R.id.support_instagram:
                openInstagramPage();
                break;
            case R.id.support_googleplus:
                openGooglePlus();
                break;
            case R.id.support_email:
                sendEmail();
                break;
            case R.id.support_apple:
                // TODO : nemidoonam chi kar konam  :-D   lol  .    khodet bzan mohad injaro ;-)
                break;
        }
    }

    private void openWebSite() {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Variables.WebSite_Link));
            startActivity(browserIntent);
        } catch (Exception e) {
            Toast.makeText(this, "مشکلی پیش آمده است", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGooglePlus() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Variables.GooglePlus)));
        } catch (Exception e) {
            Toast.makeText(this, "مشکلی پیش آمده است", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmail() {
        try {
            final Intent emailIntent = new Intent(Intent.ACTION_SEND);

/* Fill it with Data */
            emailIntent.setType("plain/text");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{Variables.Gmail});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "From app");


/* Send it off to the Activity-Chooser */
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (Exception e) {
            Toast.makeText(this, "مشکلی پیش آمده است", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isAppAvailable(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void openTelegramChannel() {
        final String appName = "org.telegram.messenger";
        final boolean isAppInstalled = isAppAvailable(getApplicationContext(), appName);
        if (isAppInstalled) {
            Intent telegram = new Intent(Intent.ACTION_VIEW);
            telegram.setData(Uri.parse(Variables.Telegram_Channel_Id));
            telegram.setPackage("org.telegram.messenger");
            startActivity(Intent.createChooser(telegram, ""));
        } else {
            Toast.makeText(this, "اپلیکیشن تلگرام یافت نشد", Toast.LENGTH_SHORT).show();
        }
    }

    private void openInstagramPage() {

        Uri uri = Uri.parse(Variables.Instagram_Id_From_App);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(Variables.Instagram_Id)));

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
