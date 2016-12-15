package ir.unicoce.doctorMahmoud.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.unicoce.doctorMahmoud.Adapter.ItemClickSupport;
import ir.unicoce.doctorMahmoud.Adapter.RecycleViewAdapter_ObjectData;
import ir.unicoce.doctorMahmoud.Adapter.VideoItemAdapter;
import ir.unicoce.doctorMahmoud.Classes.RecyclerItemClickListener;
import ir.unicoce.doctorMahmoud.Objects.Object_Data;
import ir.unicoce.doctorMahmoud.R;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

public class VideoesGalleryActivity extends AppCompatActivity {
    @BindView(R.id.rv)
    RecyclerView rv;
    private VideoItemAdapter oAdapter;

    Typeface San;
    Toolbar toolbar;
    TextView txtToolbar;

    String title, files;
    List<String> titleList;
    List<String> urlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoes_gallery);
        ButterKnife.bind(this);
        define();
        init();

    }

    private void define() {
        San = Typeface.createFromAsset(getAssets(), "fonts/SansLight.ttf");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtToolbar = (TextView) findViewById(R.id.txtToolbar_appbar);
        txtToolbar.setTypeface(San);
        txtToolbar.setText(getIntent().getStringExtra("title"));

        files = getIntent().getStringExtra("files");

        titleList = new ArrayList<>();
        urlList = new ArrayList<>();
    }

    private void init() {
        // parse files and set recycler view
        String[] tmp = files.split(",");
        int tmp_size = Arrays.asList(tmp).size();
        // seperate urls
        for (int i = 0; i < tmp_size; i = i + 2) {
            urlList.add(tmp[i]);
        }
        // seperate titles
        for (int i = 1; i < tmp_size; i = i + 2) {
            titleList.add(tmp[i]);
        }

        showList(urlList, titleList);
    }

    private void showList(final List<String> urlList, final List<String> titleList) {

        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(lm);
        oAdapter = new VideoItemAdapter(titleList,San,this);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(oAdapter);
        alphaAdapter.setDuration(1000);
        rv.setAdapter(alphaAdapter);

        rv.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(VideoesGalleryActivity.this,ShowVideoActivity.class);
                intent.putExtra("title",titleList.get(position));
                intent.putExtra("image_url",urlList.get(position));
                startActivity(intent);
            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_empty, menu);
        return true;
    }// end onCreateOptionsMenu()

    /*on toolbar menu item click support*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                back();
                break;

            default:
                break;

        }
        return false;
    }// end onOptionsItemSelected()

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        finish();
    }
}
