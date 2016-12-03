package ir.unicoce.doctorMahmoud.Activity;

import android.content.Context;
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
import ir.unicoce.doctorMahmoud.Objects.Object_Data;
import ir.unicoce.doctorMahmoud.Interface.onFragmentInteractionListener2;
import ir.unicoce.doctorMahmoud.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PartsActivity extends AppCompatActivity
        implements
        onFragmentInteractionListener2
{
    /*variables and views inside this activity : */
    Typeface San;
    Toolbar toolbar;
    TextView txtToolbar;
    private FragmentManager fragmentManager;
    private FragmentTransaction ft;
    /*FACTION is type of data which get from server*/
    private String FACTION = Variables.getParts;
    /*isFolder = {
                false :list of objects to show
                true  :list of folders to show
     }*/
    private Boolean isFolder = true;
    /*Determine depth of folders inside each other*/
    private static final int DEPTH_OF_FOLDERS = 1;
    /*onCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parts);
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
        txtToolbar.setText("بخش های تخصصی");
        fragmentManager = getSupportFragmentManager();
    }// end define()
    /*set fragment*/
    protected void setFragment() {

        ListDataFragment myFragment = new ListDataFragment();
        Bundle bundle = new Bundle();
        bundle.putString("PARENT_FACTION", FACTION);
        bundle.putString("FACTION", FACTION);
        bundle.putBoolean("KIND", isFolder);
        if(isFolder){
            bundle.putInt("DEPTH_OF_FOLDERS",DEPTH_OF_FOLDERS);
        }else{
            bundle.putInt("DEPTH_OF_FOLDERS",0);
        }
        myFragment.setArguments(bundle);

        ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.add(R.id.frame, myFragment);
        ft.commit();

    }// end setFragment()
    /*transactions happen here which calls from fragments inside this activity*/
    @Override
    public void onFragmentInteraction(int folderDepth, Object_Data ob) {

        if(folderDepth == 0){
            /*Open ShowActivity*/
            Intent intent = new Intent(PartsActivity.this,ShowActivity.class);
            intent.putExtra("sid",ob.getSid());
            intent.putExtra("title",ob.getTitle());
            intent.putExtra("content",ob.getContent());
            intent.putExtra("image_url",ob.getImageUrl());
            intent.putExtra("faction",FACTION);
            startActivity(intent);
        }else{
            // hide current fragment
            Fragment fragment = fragmentManager.findFragmentById(R.id.frame);
            ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            ft.hide(fragment);
            ft.commit();
            // start fragment with new data
            ListDataFragment myFragment = new ListDataFragment();
            Bundle bundle = new Bundle();
            bundle.putString("PARENT_FACTION", FACTION);
            bundle.putString("FACTION", ob.getSid()+"");
            if(folderDepth==1){
                bundle.putBoolean("KIND", false);
            }else{
                bundle.putBoolean("KIND", true);
            }
            bundle.putInt("DEPTH_OF_FOLDERS",--folderDepth);
            myFragment.setArguments(bundle);

            ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            ft.add(R.id.frame, myFragment);
            String backStackName = myFragment.getClass().getName();
            ft.addToBackStack(backStackName);
            ft.commit();

        }

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
                onBackPressed();
                break;

            default:
                break;

        }
        return false;
    }// end onOptionsItemSelected()
    /*on back button click*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frame);
        ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.show(fragment);
        ft.commit();
    }// end onBackPressed()
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}// end class
