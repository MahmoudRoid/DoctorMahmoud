package ir.unicoce.doctorMahmoud.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
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
import ir.unicoce.doctorMahmoud.Helper.Object_Data;
import ir.unicoce.doctorMahmoud.Interface.OnFragmentInteractionListener;
import ir.unicoce.doctorMahmoud.Interface.onFragmentInteractionListener2;
import ir.unicoce.doctorMahmoud.R;

public class NewsActivity extends AppCompatActivity
        implements
        onFragmentInteractionListener2 {

    private Typeface San;
    private Toolbar toolbar;
    private TextView txtToolbar;
    private FragmentManager fragmentManager;
    private FragmentTransaction ft;
    /*FACTION is type of data which get from server*/
    private String FACTION = Variables.getNews;
    /*isFolder = {
                false :list of data to show
                true  :folder of objects
     }*/
    private Boolean isFolder = true;
    /*DEPTH_OF_FOLDERS number shows how many folders of intricate exist*/
    public static int DEPTH_OF_FOLDERS = 1;
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
        bundle.putString("PARENT_FACTION", FACTION);
        bundle.putString("FACTION", FACTION);
        bundle.putBoolean("KIND", isFolder);
        bundle.putInt("DEPTH_OF_FOLDERS",DEPTH_OF_FOLDERS);
        myFragment.setArguments(bundle);

        ft = fragmentManager.beginTransaction();
        ft.add(R.id.frame, myFragment);
        String backStackName = myFragment.getClass().getName();
        ft.addToBackStack(backStackName);
        ft.commit();

    }// end setFragment()

    @Override
    public void onFragmentInteraction(int tagNumber, boolean isFolder, Object_Data ob) {
        Fragment fragment;
        ListDataFragment myFragment;
        switch (tagNumber){
            case 0:
                Intent intent = new Intent(NewsActivity.this,ShowActivity.class);
                intent.putExtra("sid",ob.getSid());
                intent.putExtra("title",ob.getTitle());
                intent.putExtra("content",ob.getContent());
                intent.putExtra("image_url",ob.getImageUrl());
                intent.putExtra("faction",FACTION);
                startActivity(intent);
                break;
            case 1:
                // remove current fragment
                fragment = fragmentManager.findFragmentById(R.id.frame);
                ft = fragmentManager.beginTransaction();
                ft.remove(fragment);
                ft.commit();
                // start fragment with new data
                myFragment = new ListDataFragment();
                Bundle bundle = new Bundle();
                bundle.putString("PARENT_FACTION", FACTION);
                bundle.putString("FACTION", ob.getSid()+"");
                bundle.putBoolean("KIND", isFolder);
                bundle.putInt("DEPTH_OF_FOLDERS",0);
                myFragment.setArguments(bundle);

                ft = fragmentManager.beginTransaction();
                ft.add(R.id.frame, myFragment);
                String backStackName = myFragment.getClass().getName();
                ft.addToBackStack(backStackName);
                ft.commit();
                break;

            case 2:
                break;

            default:
                break;
        }
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

    @Override
    public void onBackPressed() {
        finish();
    }
}// end class
