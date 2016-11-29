package ir.unicoce.doctorMahmoud.Activity;

import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import ir.unicoce.doctorMahmoud.Fragment.MapsFragment;
import ir.unicoce.doctorMahmoud.Interface.OnFragmentInteractionListener;
import ir.unicoce.doctorMahmoud.R;

public class ContactUs extends AppCompatActivity
        implements
        OnFragmentInteractionListener
{

    private Typeface San;
    private Toolbar toolbar;
    private TextView txtToolbar;
    private FragmentManager fragmentManager;
    // end onCreate()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        define();
        setFragment();
    }// end onCreate()
    /*set typeface findViewByIds set toolbar text and navigation*/
    private void define() {
        San = Typeface.createFromAsset(getAssets(), "fonts/SansLight.ttf");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtToolbar = (TextView) findViewById(R.id.txtToolbar_appbar);
        txtToolbar.setTypeface(San);
        txtToolbar.setText("تماس با ما");
        fragmentManager = getSupportFragmentManager();
    }// end define()
    /*set map fragment to activity*/
    protected void setFragment() {

        MapsFragment myFragment = new MapsFragment();
        /*Bundle bundle = new Bundle();
        bundle.putString("", "");
        bundle.putString("", "");
        myFragment.setArguments(bundle);*/

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.frame, myFragment);
        ft.commit();

    }// end setFragment()
    /*on fragment views click handler*/
    @Override
    public void onFragmentInteraction(int tagNumber) {

    }// end onFragmentInteraction()

}// end class
