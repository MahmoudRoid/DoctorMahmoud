package ir.unicoce.doctorMahmoud.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.unicoce.doctorMahmoud.Adapter.RecycleViewAdapter_chat;
import ir.unicoce.doctorMahmoud.AsyncTasks.GetAllUserChatMessages;
import ir.unicoce.doctorMahmoud.AsyncTasks.PostUserMessage;
import ir.unicoce.doctorMahmoud.Classes.Internet;
import ir.unicoce.doctorMahmoud.Database.db_chat;
import ir.unicoce.doctorMahmoud.Interface.IWebserviceByTag;
import ir.unicoce.doctorMahmoud.Objects.Object_Chat;
import ir.unicoce.doctorMahmoud.R;

public class ChatsActivity extends AppCompatActivity implements IWebserviceByTag {

    @BindView(R.id.chat_recycler)
    RecyclerView rv;
    @BindView(R.id.edtSend_chat)
    EditText edtSend;

    private Typeface San;
    private Toolbar toolbar;
    private TextView txtToolbar;
    public SharedPreferences prefs;
    private RecycleViewAdapter_chat mAdapter;
    String  myMeesage;
    ArrayList<Object_Chat> objectChatArrayList;

    public static String Get_Tag="Get_Tag";
    public static String Post_Tag="Post_Tag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        ButterKnife.bind(this);

        define();
        init();

    }

    private void init() {

        if(Internet.isNetworkAvailable(this)){
            // load from server
            GetAllUserChatMessages get = new GetAllUserChatMessages(this,this,getUsername(),Get_Tag);
            get.execute();
        }
        else {
            Toast.makeText(this, R.string.error_internet, Toast.LENGTH_SHORT).show();
            // load everything in database
            List<db_chat> list = Select.from(db_chat.class).list();
            if(list.size()>0){
                // set adapter with db items
                objectChatArrayList.clear();
                for(db_chat ob : list){
                    Object_Chat objectChat = new Object_Chat(ob.getMessage(),ob.isme());
                    objectChatArrayList.add(objectChat);
                }
                showChats(objectChatArrayList);
            }
        }



    }

    private void define() {

        San = Typeface.createFromAsset(getAssets(), "fonts/SansLight.ttf");
        toolbar = (Toolbar) findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtToolbar = (TextView) findViewById(R.id.txtToolbar_appbar);
        txtToolbar.setTypeface(San);
        txtToolbar.setText("پرسش و پاسخ");
        edtSend.setTypeface(San);
        objectChatArrayList = new ArrayList<>();

        LinearLayoutManager lm = new LinearLayoutManager(ChatsActivity.this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(lm);

    }// end define()

    @OnClick(R.id.ivSend_chat)
    public void onClick() {
        myMeesage = edtSend.getText().toString();
        if(!myMeesage.equals("")){
            // matni nvshte shode va ghabele ersal ast
            if(Internet.isNetworkAvailable(this)){
                // post data to server
                PostUserMessage post = new PostUserMessage(this,this,myMeesage,getUsername(),Post_Tag);
                post.execute();
            }
            else {
                Toast.makeText(this,R.string.error_internet, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getUsername(){
        prefs = getSharedPreferences("Login", MODE_PRIVATE);
        return  prefs.getString("username","");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch(id){
            case android.R.id.home:
                back();
                break;
            case R.id.action_refresh:
                GetAllUserChatMessages get = new GetAllUserChatMessages(this,this,getUsername(),Get_Tag);
                get.execute();
                break;

            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getResult(Object result, String Tag) throws Exception {
        switch (Tag){
            case "Get_Tag":
            // pak kardane list va sepas add kardane result
                objectChatArrayList.clear();
                objectChatArrayList = (ArrayList<Object_Chat>) result;
                showChats(objectChatArrayList);
                break;
            case "Post_Tag":

                mAdapter.add(new Object_Chat(String.valueOf(result),true));
                scrollToBottom();
                // add message to db
                db_chat db = new db_chat(String.valueOf(result),true);
                db.save();
                // empty edittex
                edtSend.setText("");
                break;
        }
    }

    private void scrollToBottom() {
        rv.setAdapter(mAdapter);
        rv.scrollToPosition(objectChatArrayList.size()-1);
    }

    @Override
    public void getError(String ErrorCodeTitle, String Tag) throws Exception {
        switch (Tag){
            case "Get_Tag":
                Toast.makeText(this, R.string.error_server, Toast.LENGTH_SHORT).show();
                break;
            case "Post_Tag":
                Toast.makeText(this, R.string.sent_failed, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void showChats(ArrayList<Object_Chat> chatArrayList){
        mAdapter = new RecycleViewAdapter_chat(chatArrayList,San,this);
        rv.setAdapter(mAdapter);
        rv.scrollToPosition(chatArrayList.size()-1);
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        startActivity(new Intent(this,MainActivity.class));
    }


}
