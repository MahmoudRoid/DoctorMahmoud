package ir.unicoce.doctorMahmoud.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.unicoce.doctorMahmoud.AsyncTasks.GetData;
import ir.unicoce.doctorMahmoud.Classes.Internet;
import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.Database.db_details;
import ir.unicoce.doctorMahmoud.Objects.Object_Data;
import ir.unicoce.doctorMahmoud.Interface.IWebservice;
import ir.unicoce.doctorMahmoud.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AboutUsActivity extends AppCompatActivity implements IWebservice {

    @BindView(R.id.ivHeader_show)
    ImageView ivHeaderShow;
    @BindView(R.id.layMatn_show)
    LinearLayout lay;
    private ArrayList<Object_Data> objectDataArrayList;


    public static int w = 0, h = 0;
    private Typeface San;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);

        define();
        init();

    }

    private void define() {
        San = Typeface.createFromAsset(getAssets(), "fonts/SansLight.ttf");
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        h = displaymetrics.heightPixels;
        w = displaymetrics.widthPixels;
    }

    private void init() {

        try {
            List<db_details> db = Select
                    .from(db_details.class)
                    .where(Condition.prop("parentid").eq(Variables.getAboutUs))
                    .list();

            if (db.size()==0 || db.get(0).getContent().equals("")) {
                // get data from service
                if (Internet.isNetworkAvailable(this)) {
                    // call service
                    GetData getdata = new GetData(this, this, Variables.getAboutUs,false);
                    getdata.execute();
                }
            } else {
                showDetails(db.get(0).getTitle(),db.get(0).getContent(), db.get(0).getImageUrl());
            }
        }
        catch (Exception e) {e.printStackTrace();}

    }

    public void showDetails(String title, String content, String imageUrl) {
        this.setTitle(title);
        Glide.with(this)
                .load(imageUrl)
                .override(200, 200)
                .placeholder(R.mipmap.ic_launcher)
                .into(ivHeaderShow);

        setContent(content + "<\"\">");
    }

    private void setContent(String text) {
        int c1 = 0, c2 = 0, c3 = 0;

        for (int i = 0; i < text.length(); i++) {

            if (text.charAt(i) == '<') {
                c2 = i;
                if (!text.substring(c1, c2).equals("")) {
                    ctext(text.substring(c1, c2));
                }

            }
            if (text.charAt(i) == '>') {
                c3 = i;
                cimg(text.substring(c2 + 2, c3 - 1));
                c1 = i + 1;
            }
        }
    }// end setContent()

    private void ctext(String text) {
        TextView tv = new TextView(AboutUsActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 10, 0, 10);
        lp.gravity = Gravity.TOP;
        tv.setPadding(10, 10, 10, 10);
        tv.setLineSpacing(30, 1);
        tv.setTypeface(San);
        tv.setText(text);
        lay.addView(tv, lp);

    }// end ctext()

    private void cimg(String image_url) {

        if (!image_url.equals("")) {
            ImageView img = new ImageView(AboutUsActivity.this);
            double ch = w / 2;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(w, (int) ch);
            lp.gravity = Gravity.CENTER;

            Glide.with(this)
                    .load(image_url)
                    .override(200, 200)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(img);

            lay.addView(img, lp);
        }
    }// end cimg()

    @Override
    public void getResult(Object result) throws Exception {
        objectDataArrayList = (ArrayList<Object_Data>) result;
        showDetails(objectDataArrayList.get(0).getTitle(),
                objectDataArrayList.get(0).getContent(),
                objectDataArrayList.get(0).getImageUrl());
    }

    @Override
    public void getError(String ErrorCodeTitle) throws Exception {
        Toast.makeText(this, ErrorCodeTitle, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AboutUsActivity.this, MainActivity.class));
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
