package ir.unicoce.doctorMahmoud.Activity;

import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.Fragment.ListDataFragment;
import ir.unicoce.doctorMahmoud.Interface.OnFragmentInteractionListener;
import ir.unicoce.doctorMahmoud.R;

public class NewsActivity extends AppCompatActivity
        implements
        OnFragmentInteractionListener {

    private Typeface San;
    private Toolbar toolbar;
    private TextView txtToolbar;
    private FragmentManager fragmentManager;
    /*FACTION is type of data which get from server*/
    private String FACTION = Variables.getNews;
    /*isFolder = {
                false :list of data to show
                true  :folder of objects
     }*/
    private Boolean isFolder = false;
    /*onCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
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
        txtToolbar.setText("اخبار");
        fragmentManager = getSupportFragmentManager();
    }// end define()
    /*set fragment*/
    protected void setFragment() {

        ListDataFragment myFragment = new ListDataFragment();
        Bundle bundle = new Bundle();
        bundle.putString("FACTION", FACTION);
        bundle.putBoolean("KIND", isFolder);
        myFragment.setArguments(bundle);

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.frame, myFragment);
        ft.commit();

    }// end setFragment()

    @Override
    public void onFragmentInteraction(int tagNumber) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_empty, menu);
        return true;
    }

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
    }

}// end class
