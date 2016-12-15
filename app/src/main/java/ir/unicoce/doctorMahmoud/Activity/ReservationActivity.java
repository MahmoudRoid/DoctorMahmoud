package ir.unicoce.doctorMahmoud.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.unicoce.doctorMahmoud.Adapter.ServiceAdapter;
import ir.unicoce.doctorMahmoud.AsyncTasks.GetService;
import ir.unicoce.doctorMahmoud.AsyncTasks.ReservationPost;
import ir.unicoce.doctorMahmoud.Classes.Internet;
import ir.unicoce.doctorMahmoud.Database.db_reserv;
import ir.unicoce.doctorMahmoud.Interface.IService;
import ir.unicoce.doctorMahmoud.Interface.IWebserviceByTag;
import ir.unicoce.doctorMahmoud.Objects.Object_Service;
import ir.unicoce.doctorMahmoud.R;
import ir.unicoce.doctorMahmoud.Service.ReservationService;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

public class ReservationActivity extends AppCompatActivity implements IWebserviceByTag,IService {

    @BindView(R.id.reservation_recycler)
    RecyclerView rv;
    @BindView(R.id.reservation_price)
    TextView reservationPrice;

    private Typeface San;
    private Toolbar toolbar;
    private TextView txtToolbar;
    public SharedPreferences prefs;

    String GET_DATA_TAG = "GET_DATA_TAG";
    String POST_DATA_TAG = "POST_DATA_TAG";

    public static ArrayList<Object_Service> objectServiceArrayList;
    public ServiceAdapter serviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
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
        txtToolbar.setText("نوبت دهی");
    }

    private void init() {
        if (Internet.isNetworkAvailable(this)) {
            GetService get = new GetService(this, this, GET_DATA_TAG);
            get.execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_empty, menu);
        return true;
    }

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
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        startActivity(new Intent(this, MainActivity.class));
    }


    @OnClick(R.id.reservation_button)
    public void onClick() {
        int totalPrice = 0 ;
        for(Object_Service os : objectServiceArrayList){
            if(os.ischeked()){
                totalPrice += os.getPrice();
            }
        }
        reservationPrice.setVisibility(View.VISIBLE);
        reservationPrice.setText(String.valueOf(totalPrice));
    }

    @Override
    public void iShowDialog(final int serviceId) {
        final Dialog d = new Dialog(this);
        d.setCancelable(true);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog_send_reservation);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = d.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        final TextInputLayout til = (TextInputLayout) d.findViewById(R.id.til_dialogReservation);
        final EditText edtReservation = (EditText) d.findViewById(R.id.edtDescription_dialogReservation);
        final Button btnSendReservation = (Button) d.findViewById(R.id.btnLogin_dialogReservation);

        til.setTypeface(San);
        edtReservation.setTypeface(San);
        btnSendReservation.setTypeface(San);

        btnSendReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = edtReservation.getText().toString();
                if(description.equals(null)) description = "";

                if(Internet.isNetworkAvailable(ReservationActivity.this)){
                    ReservationPost post = new ReservationPost(ReservationActivity.this,ReservationActivity.this
                    ,String.valueOf(serviceId),getUserId(),description,POST_DATA_TAG);

                    post.execute();
                    d.dismiss();
                }
                else {
                    Toast.makeText(ReservationActivity.this,R.string.error_internet, Toast.LENGTH_SHORT).show();
                }
            }
        });

        d.show();

    }

    @Override
    public void starNewActivity(String title,String content,String imageUrl) {
        // show service Activity content in new activity
        Intent intent = new Intent(this,ShowServiceActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("content",content);
        intent.putExtra("image_url",imageUrl);
        startActivity(intent);
    }

    private String getUserId() {
        prefs = getSharedPreferences("Login", MODE_PRIVATE);
        return prefs.getString("username","") ;
    }

    @Override
    public void getResult(Object result, String Tag) throws Exception {
        switch (Tag) {
            case "GET_DATA_TAG":
                // show list
                showList((ArrayList<Object_Service>)result);
                break;

            case "POST_DATA_TAG":
                // save int DB
                db_reserv db = new db_reserv(String.valueOf(result));
                db.save();

                Toast.makeText(this, "منتظر جواب بمانید", Toast.LENGTH_SHORT).show();
                startService(new Intent(ReservationActivity.this, ReservationService.class));

                break;
        }
    }

    @Override
    public void getError(String ErrorCodeTitle, String Tag) throws Exception {
        switch (Tag) {
            case "GET_DATA_TAG":
                Toast.makeText(this,R.string.error_server, Toast.LENGTH_SHORT).show();
                break;

            case "POST_DATA_TAG":
                Toast.makeText(this,R.string.error_server, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void showList(ArrayList<Object_Service> arrayList) {
        objectServiceArrayList = arrayList;
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(lm);
        serviceAdapter = new ServiceAdapter(objectServiceArrayList,San,this,this);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(serviceAdapter);
        alphaAdapter.setDuration(1000);
        rv.setAdapter(alphaAdapter);
    }
}
