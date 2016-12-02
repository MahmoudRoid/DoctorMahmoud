package ir.unicoce.doctorMahmoud.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import ir.unicoce.doctorMahmoud.Adapter.ItemClickSupport;
import ir.unicoce.doctorMahmoud.Adapter.RecycleViewAdapter_ObjectData;
import ir.unicoce.doctorMahmoud.Database.db_details;
import ir.unicoce.doctorMahmoud.Objects.Object_Data;
import ir.unicoce.doctorMahmoud.R;

public class FavoriteActivity extends AppCompatActivity
{
    /*variables and views inside this activity :*/
    Typeface San;
    Toolbar toolbar;
    TextView txtToolbar;
    RecyclerView rv;
    RecycleViewAdapter_ObjectData mAdapter;
    ArrayList<Object_Data> myList = new ArrayList<>();
    /*onCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        define();
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
        txtToolbar.setText("علاقمندی ها");
        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager lm = new LinearLayoutManager(FavoriteActivity.this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(lm);

        /*on RecycleView item click support*/
        ItemClickSupport.addTo(rv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Object_Data ob = myList.get(position);
                /*Open ShowActivity*/
                Intent intent = new Intent(FavoriteActivity.this,ShowActivity.class);
                intent.putExtra("sid",ob.getSid());
                intent.putExtra("title",ob.getTitle());
                intent.putExtra("content",ob.getContent());
                intent.putExtra("image_url",ob.getImageUrl());
                intent.putExtra("faction",ob.getParentId());
                startActivity(intent);
            }// end onItemClicked()
        });

    }// end define()
    /*load all favorite objects from database*/
    private void init(){
        myList.clear();
        List<db_details> list = Select
                .from(db_details.class)
                .where(Condition.prop("favorite").eq(1))
                .list();
        for (int i=0;i<list.size();i++){
            myList.add(new Object_Data(
                    list.get(i).getsid(),
                    list.get(i).getparentid(),
                    list.get(i).getTitle(),
                    list.get(i).getContent(),
                    list.get(i).getImageUrl(),
                    list.get(i).isFavorite())
            );
        }
        refreshRecycleView();
    }// end init()
    /*refresh and set RecycleView*/
    private void refreshRecycleView(){
        mAdapter = new RecycleViewAdapter_ObjectData(myList,San,FavoriteActivity.this);
        rv.setAdapter(mAdapter);
    }// end refreshRecycleView()
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
    /*call onResume for the time which back from showActivity and object remover from favorites*/
    @Override
    protected void onResume() {
        super.onResume();
        init();
    }// end onResume()

}// end class
