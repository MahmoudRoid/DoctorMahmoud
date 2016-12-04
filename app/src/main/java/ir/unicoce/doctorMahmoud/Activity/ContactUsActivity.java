package ir.unicoce.doctorMahmoud.Activity;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ir.unicoce.doctorMahmoud.Fragment.MapsFragment;
import ir.unicoce.doctorMahmoud.Interface.OnFragmentInteractionListener;
import ir.unicoce.doctorMahmoud.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ContactUsActivity extends AppCompatActivity
        implements
        OnFragmentInteractionListener
{

    private Typeface San;
    private Toolbar toolbar;
    private TextView txtToolbar;
    private ImageView ivMap_Tehran,ivMap_Karaj;
    private FragmentManager fragmentManager;
    private FragmentTransaction ft;
    // end onCreate()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        define();
        setFragment();
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
        fragmentManager = getSupportFragmentManager();
    }// end define()
    /*set map fragment to activity*/
    protected void setFragment() {

        /*FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, new MapsFragment());
        fragmentTransaction.commit();
*/

        // old
        MapsFragment myFragment = new MapsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Lat", "41.0760475");
        bundle.putString("Lng", "29.0275024");
        bundle.putString("Title", "مطب تهران");
        myFragment.setArguments(bundle);

        ft = fragmentManager.beginTransaction();
        ft.add(R.id.frame, myFragment);
        ft.commit();

    }// end setFragment()
    /*on fragment views click handler*/// Click to Show the branch map
    private void onBranchMapShowClick() {

        ivMap_Tehran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MapsFragment myFragment = new MapsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Lat", "41.0760475");
                bundle.putString("Lng", "29.0275024");
                bundle.putString("Title", "مطب تهران");
                myFragment.setArguments(bundle);

                ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                ft.add(R.id.frame, myFragment);
                String backStackName = myFragment.getClass().getName();
                ft.addToBackStack(backStackName);
                ft.commit();

            }
        });

        ivMap_Karaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = fragmentManager.findFragmentById(R.id.frame);
                ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                ft.remove(fragment);
                ft.commit();

                MapsFragment myFragment = new MapsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Lat", "40.0760475");
                bundle.putString("Lng", "25.0275024");
                bundle.putString("Title", "مطب کرج");
                myFragment.setArguments(bundle);

                ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                ft.add(R.id.frame, myFragment);
                ft.commit();
            }
        });

    }// end onBranchMapShowClick()
    /*transactions happen here which calls from fragments inside this activity*/
    @Override
    public void onFragmentInteraction(int tagNumber) {

    }// end onFragmentInteraction()
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}// end class
