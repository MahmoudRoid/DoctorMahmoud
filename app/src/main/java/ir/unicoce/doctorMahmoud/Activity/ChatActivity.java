package ir.unicoce.doctorMahmoud.Activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.unicoce.doctorMahmoud.Adapter.RecycleViewAdapter_chat;
import ir.unicoce.doctorMahmoud.AsyncTasks.GetAllUserChatMessages;
import ir.unicoce.doctorMahmoud.AsyncTasks.PostUserMessage;
import ir.unicoce.doctorMahmoud.Classes.Internet;
import ir.unicoce.doctorMahmoud.Interface.IWebservice;
import ir.unicoce.doctorMahmoud.Objects.Object_Message;
import ir.unicoce.doctorMahmoud.R;

public class ChatActivity extends AppCompatActivity
        implements
        IWebservice
{
    /*class variables*/
    private Typeface San;
    private Toolbar toolbar;
    private TextView txtToolbar;
    private RecyclerView rv;
    private ArrayList<Object_Message> mylist = new ArrayList<>();
    private RecycleViewAdapter_chat mAdapter;
    private SweetAlertDialog pDialog;
    private ImageView ivSend;
    private EditText edtSend;
    private String NationalCode="";
    private SharedPreferences prefs;
    private boolean isSendMessage = false;
    /*onCreate method*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        define();

        AskServer();

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = edtSend.getText().toString();
                if(!input.equals("")){
                    AskServer();
                }
            }
        });

        //TestInit();

    }// end onCreate()

    private void define(){
        San = Typeface.createFromAsset(getAssets(), "fonts/SansLight.ttf");
        toolbar = (Toolbar) findViewById(R.id.toolbar_mail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtToolbar = (TextView) findViewById(R.id.txtToolbar_appbar);
        txtToolbar.setTypeface(San);
        txtToolbar.setText("پرسش و پاسخ");

        rv = (RecyclerView) findViewById(R.id.mail_recycler);
        LinearLayoutManager lm = new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(lm);

        ivSend = (ImageView) findViewById(R.id.ivSend_mail);
        edtSend = (EditText) findViewById(R.id.edtSend_mail);

        edtSend.setTypeface(San);

        prefs = getSharedPreferences("Login", MODE_PRIVATE);
        NationalCode = prefs.getString("national_code","");

        setSweetDialog();

    }// end define()

    private void setSweetDialog(){
        pDialog= new SweetAlertDialog(ChatActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("لطفا صبور باشید!");
        pDialog.setCancelable(false);
    }// end setSweetDialog()

    private void refreshAdapter(){
        mAdapter = new RecycleViewAdapter_chat(mylist,San,ChatActivity.this);
        rv.setAdapter(mAdapter);
    }// end refreshAdapter()

    private void TestInit(){
        mylist.add(new Object_Message("من","سلام دکتر حالتون خوبه؟",true));
        mylist.add(new Object_Message("دکتر","درود, ممنون مشکل شما رفع شد؟",false));
        mylist.add(new Object_Message("من","بله دکتر حالم خیلی بهتره ممنون که انقدر خوبید",true));
        mylist.add(new Object_Message("دکتر","خواهش میکنم وقت کردی حتما آزمایش هات رو انجام بده و قرص هاتو بخور",false));
        mylist.add(new Object_Message("من","چشم دکتر شما هم مراقب خودت باش",true));
        mylist.add(new Object_Message("من","سلام دکتر حالتون خوبه؟",true));
        mylist.add(new Object_Message("دکتر","درود, ممنون مشکل شما رفع شد؟",false));
        mylist.add(new Object_Message("من","بله دکتر حالم خیلی بهتره ممنون که انقدر خوبید",true));
        mylist.add(new Object_Message("دکتر","خواهش میکنم وقت کردی حتما آزمایش هات رو انجام بده و قرص هاتو بخور",false));
        mylist.add(new Object_Message("من","چشم دکتر شما هببله خیلی هم خوبم  دکمتر کی میای بریم شمال کنار ویلا تو شمال بام خموش بگذرونیم و بگیم و بخندیم و باهم بازی کنیم و از دوتستانمون یاد کنیم و از این حرفات که این متن به اندازه کافی طولانی شود و ما بتوانیم تست بگیریمم مراقب خودت باش",true));
        refreshAdapter();
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
        return super.onOptionsItemSelected(item);
    }

    private void AskServer(){
        if(Internet.isNetworkAvailable(this)){
            if(isSendMessage){
                Object_Message ob = new Object_Message(NationalCode,edtSend.getText().toString(),true);
                PostUserMessage async = new PostUserMessage(this,this,ob);
                async.execute();
            }else{
                GetAllUserChatMessages async = new GetAllUserChatMessages(this,this,NationalCode);
                async.execute();
            }
        }else{
            Toast.makeText(ChatActivity.this, getResources().getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
        }
    }// end AskServer()

    @Override
    public void getResult(Object result) throws Exception {
        if(isSendMessage){
            mylist.add(new Object_Message("من",edtSend.getText().toString(),true));
        }
        else{
            mylist.clear();
            mylist = (ArrayList<Object_Message>) result;
            isSendMessage = true;
        }
        refreshAdapter();
    }

    @Override
    public void getError(String ErrorCodeTitle) throws Exception {
        Toast.makeText(this, ErrorCodeTitle, Toast.LENGTH_SHORT).show();
    }


}// end class
