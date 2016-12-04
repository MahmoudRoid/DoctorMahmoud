package ir.unicoce.doctorMahmoud.Activity;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ir.unicoce.doctorMahmoud.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ContactUsActivity extends AppCompatActivity
{
    /*all class variables*/
    private GoogleMap mMap;
    private Typeface San;
    private Toolbar toolbar;
    private TextView txtToolbar;
    private ImageView ivMap_Tehran,ivMap_Karaj;
    /*onCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        define();
        setMap("41.0760475","39.0275024","نمونه");
        onBranchMapShowClick();
    }// end onCreate()
    /*set typeface findViewByIds set toolbar text and navigation*/
    private void define() {
        San = Typeface.createFromAsset(getAssets(), "fonts/SansLight.ttf");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtToolbar = (TextView) findViewById(R.id.txtToolbar_appbar);
        ivMap_Tehran = (ImageView) findViewById(R.id.ivMaps_Tehran);
        ivMap_Karaj = (ImageView) findViewById(R.id.ivMaps_Karaj);
        txtToolbar.setTypeface(San);
        txtToolbar.setText("تماس با ما");

        try {
            mMap = ((MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map))
                    .getMap();
        }
        catch (Exception e) {e.printStackTrace();}

    }// end define()
    /*set map*/
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
    /*on fragment views click handler*/// Click to Show the branch map
    private void onBranchMapShowClick() {

        ivMap_Tehran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMap("41.0760475","29.0275024","مطب اول");
            }
        });

        ivMap_Karaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMap("40.0760475","28.0275024","مطب دوم");
            }
        });

    }// end onBranchMapShowClick()
    /*create toolbar menu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_empty, menu);
        return true;
    }// end onCreateOptionsMenu()
    /*on toolbar menu item click support*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch(id){
            case android.R.id.home:
                finish();
                break;

            default:
                break;

        }
        return false;
    }// end onOptionsItemSelected()
    /*set fonts of xml*/
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}// end class
