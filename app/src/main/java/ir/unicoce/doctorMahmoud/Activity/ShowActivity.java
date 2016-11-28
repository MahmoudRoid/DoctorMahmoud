package ir.unicoce.doctorMahmoud.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.R;

public class ShowActivity extends AppCompatActivity {

    public static int w = 0,h = 0;
    private Toolbar toolbar;
    private Typeface San;
    private ImageView ivHeader;
    private TextView txtToolbar;
    private String Faction="",ImageUrl="",Title="",Content="";
    private int Sid;
    private boolean isFav=false;
    private FloatingActionButton fab,fabShare;
    private LinearLayout lay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        define();
        getWhat();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : on favorite icon click
                /*if(isFav){
                    db.open();
                    db.update("Favorite","0","Faction",Faction,"Sid",Sid);
                    db.close();
                    fab.setImageResource(R.drawable.favorite_outline_red);
                    Fav = "0";
                    isFav = false;
                }else{
                    db.open();
                    db.update("Favorite","1","Faction",Faction,"Sid",Sid);
                    db.close();
                    fab.setImageResource(R.drawable.favorite_red);
                    Fav = "1";
                    isFav = true;
                }*/
            }
        });

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT,"دکتر");
                    intent.putExtra(Intent.EXTRA_TEXT, Content);
                    startActivity(Intent.createChooser(intent,"اشتراک گزاری از طریق:"));
                }catch (Exception e){ e.printStackTrace(); }
            }
        });


    }// end onCreate()

    private void define(){
        San = Typeface.createFromAsset(getAssets(), "fonts/SansLight.ttf");
        toolbar = (Toolbar) findViewById(R.id.toolbar_show);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivHeader = (ImageView) findViewById(R.id.ivHeader_show);
        txtToolbar = (TextView) findViewById(R.id.toolbar_invisible_title);
        fabShare = (FloatingActionButton) findViewById(R.id.fabShare_show);
        fab = (FloatingActionButton) findViewById(R.id.fab_show);
        lay = (LinearLayout) findViewById(R.id.layMatn_show);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        h = displaymetrics.heightPixels;
        w = displaymetrics.widthPixels;

        txtToolbar.setTypeface(San);
        txtToolbar.setText("توضیحات");

    }// end define()

    private void getWhat(){
        Sid         = getIntent().getIntExtra("sid",0);
        Title       = getIntent().getStringExtra("title");
        Content     = getIntent().getStringExtra("content");
        ImageUrl    = getIntent().getStringExtra("image_url");

        // TODO : favorite first initialize
        /*db.open();
        Fav = db.DisplayOne(6,"Sid",Sid,"Faction",Faction);
        db.close();

        if(Fav.equals("1")){
            isFav = true;
            fab.setImageResource(R.drawable.favorite_red);
        }else{
            fab.setImageResource(R.drawable.favorite_outline_red);
        }*/

        Glide.with(this)
                .load(ImageUrl)
                .override(200,200)
                .placeholder(R.mipmap.ic_launcher)
                .into(ivHeader);


        txtToolbar.setText(Title);

        if(Faction.equals(Variables.getServices) || Faction.equals(Variables.getInsurance)) {
            fab.setVisibility(View.INVISIBLE);
        }

        setContent(Content+"<\"\">");

    }// end getWhat()

    private void setContent(String text){
        int c1=0,c2=0,c3=0;

        for(int i=0;i<text.length();i++){

            if(text.charAt(i)=='<'){
                c2=i;
                if(!text.substring(c1,c2).equals("")){
                    cText(text.substring(c1,c2));
                }

            }
            if(text.charAt(i)=='>'){
                c3=i;
                cImage(text.substring(c2+2,c3-1));
                c1=i+1;
            }
        }
    }// end setContent()

    private void cText(String text){
        // text = Html.fromHtml(text).toString();
        Log.i(Variables.Tag,"text: "+text);
        TextView tv = new TextView(ShowActivity.this);
        tv.setTypeface(San);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 10, 0, 10);
        lp.gravity= Gravity.TOP;
        tv.setPadding(10, 10, 10, 10);
        tv.setLineSpacing(30,1);
        tv.setTypeface(San);
        tv.setText(text);
        lay.addView(tv,lp);

    }// end cText()

    private void cImage(String image_url){
        Log.i(Variables.Tag,"imageurl: "+image_url);

        if(!image_url.equals("")){
            ImageView img=new ImageView(ShowActivity.this);
            double ch=w/2;
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(w,(int)ch);
            lp.gravity=Gravity.CENTER;

            Glide.with(this)
                    .load(image_url)
                    .override(200,200)
                    .placeholder(R.drawable.folder)
                    .into(img);

            lay.addView(img, lp);
        }
    }// end cImage()

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
