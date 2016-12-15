package ir.unicoce.doctorMahmoud.Activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orm.query.Condition;
import com.orm.query.Select;


import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ir.unicoce.doctorMahmoud.Adapter.RecycleViewAdapter_Images;
import ir.unicoce.doctorMahmoud.AsyncTasks.GetData;
import ir.unicoce.doctorMahmoud.Classes.Internet;
import ir.unicoce.doctorMahmoud.Classes.RecyclerItemClickListener;
import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.Database.db_details;
import ir.unicoce.doctorMahmoud.Interface.IWebservice;
import ir.unicoce.doctorMahmoud.Objects.Object_Data;
import ir.unicoce.doctorMahmoud.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ImageGalleryActivity extends AppCompatActivity {
    /*variables and views inside this activity : */
    private ImageView imagesDetailImage;
    private RecyclerView mRecyclerView;
    private RecycleViewAdapter_Images mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Typeface San;
    private Toolbar toolbar;
    private TextView txtToolbar;
    private View snack_view;
    private ArrayList<Object_Data> myList = new ArrayList<>();
    private String FACTION = "";
    private String Title;

    /*onCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        define();
        init();
    }// end onCreate()
    /*set typeface findViewByIds set toolbar text and navigation*/
    private void define() {
        San = Typeface.createFromAsset(getAssets(), "fonts/SansLight.ttf");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imagesDetailImage = (ImageView) findViewById(R.id.images_detail_main_image);
        txtToolbar = (TextView) findViewById(R.id.txtToolbar_appbar);
        txtToolbar.setTypeface(San);

        Title       = getIntent().getStringExtra("title");
        txtToolbar.setText(Title);
    }// end define()
    /*choose get recycleView data from database or server*/
    public void init() {
        List<String> list = new ArrayList<>();
        String[] tmp = getIntent().getStringExtra("files").split(",");
        int tmp_size =  Arrays.asList(tmp).size();

       for(int i=0;i<tmp_size;i+=2){
           list.add(tmp[i]);
       }
        showImages(list);
    }// end init()

    /*set data to recycleView*/
//    public void showList(ArrayList<Object_Data> arrayList) {
//        myList = arrayList;
//        mRecyclerView = (RecyclerView) findViewById(R.id.images_detail_recycler);
//        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mAdapter = new RecycleViewAdapter_Images(myList,San,ImageGalleryActivity.this);
//        mRecyclerView.setAdapter(mAdapter);
//
//        Glide.with(this).load(arrayList.get(0).getImageUrl())
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .placeholder(R.mipmap.ic_launcher)
//                .into(imagesDetailImage);
//
//        mRecyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(
//                        ImageGalleryActivity.this,
//                        new RecyclerItemClickListener.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, int position) {
//
//                                Glide.with(ImageGalleryActivity.this)
//                                        .load(myList.get(position).getImageUrl())
//                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                                        .placeholder(R.mipmap.ic_launcher).into(imagesDetailImage);
//
//                            }
//                        }));
//    }

    public void showImages(final List<String> images){

        mRecyclerView = (RecyclerView) findViewById(R.id.images_detail_recycler);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecycleViewAdapter_Images(images,San,ImageGalleryActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        Glide.with(this).load(images.get(0))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.ic_launcher)
                .into(imagesDetailImage);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        ImageGalleryActivity.this,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                Glide.with(ImageGalleryActivity.this)
                                        .load(images.get(position))
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .placeholder(R.mipmap.ic_launcher).into(imagesDetailImage);

                            }
                        }));

    }
    /*create toolbar menu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_empty, menu);
        return true;
    }
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
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}// end class