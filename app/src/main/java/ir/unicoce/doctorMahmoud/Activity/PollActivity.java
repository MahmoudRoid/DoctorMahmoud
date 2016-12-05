package ir.unicoce.doctorMahmoud.Activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.unicoce.doctorMahmoud.AsyncTasks.GetPoll;
import ir.unicoce.doctorMahmoud.AsyncTasks.GetResult;
import ir.unicoce.doctorMahmoud.Classes.Internet;
import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.Interface.IWebservice;
import ir.unicoce.doctorMahmoud.Interface.IWebservice2;
import ir.unicoce.doctorMahmoud.Objects.Object_Vote;
import ir.unicoce.doctorMahmoud.R;

public class PollActivity extends AppCompatActivity
        implements
        IWebservice2,
        IWebservice
{
    /*class variables*/
    private Toolbar toolbar;
    private TextView txtToolbar, txtQuestion;
    private Typeface San;
    private Button btnSendVote;
    private SweetAlertDialog pDialog;
    private ArrayList<Object_Vote> myAnswerList = new ArrayList<>();
    private Object_Vote myQuestion;
    private RadioGroup rGroup;
    private boolean isSendAnswer = false;
    /*onCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);
        define();
        ArcLoader();
        askServer();
        btnSendVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askServer();
            }
        });

    }// end onCreate()
    /*set typeface findViewByIds set toolbar text and navigation*/
    private void define() {
        San = Typeface.createFromAsset(getAssets(), "fonts/SansLight.ttf");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtToolbar = (TextView) findViewById(R.id.txtToolbar_appbar);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        btnSendVote = (Button) findViewById(R.id.btnConfirm);
        btnSendVote.setTypeface(San);
        txtToolbar.setTypeface(San);
        txtQuestion.setTypeface(San);
        txtToolbar.setText("نظرسنجی");
        rGroup = (RadioGroup) findViewById(R.id.rg_reservation);
    }// end define()
    /*prepare ProgressBar dialog*/
    private void ArcLoader(){
        String tmp = "";
        pDialog= new SweetAlertDialog(PollActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(tmp);
        pDialog.setCancelable(true);
    }// end ArcLoader()
    /*ask server to get data of poll*/
    private void askServer(){
        if(Internet.isNetworkAvailable(this)){
            // network available than ask server for data
            if(isSendAnswer){
                //Object_Vote ob;
                if(rGroup.getCheckedRadioButtonId() != -1){
                    int id= rGroup.getCheckedRadioButtonId();
                    View radioButton = rGroup.findViewById(id);
                    int radioId = rGroup.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) rGroup.getChildAt(radioId);
                    int tagId = (int) btn.getTag();
                    Object_Vote ob = myAnswerList.get(tagId);
                    GetResult async = new GetResult(PollActivity.this,PollActivity.this,ob);
                    async.execute();
                }else{
                    Toast.makeText(this, "لطفا یکی از گزینه ها را انتخاب کنید.", Toast.LENGTH_SHORT).show();
                }
            }else{
                GetPoll async = new GetPoll(PollActivity.this,PollActivity.this);
                async.execute();
            }
        }else{
            // network isn't available than toast user a message
            Toast.makeText(PollActivity.this, R.string.error_nework, Toast.LENGTH_SHORT).show();
        }// end check Internet
    }// end askServer()
    /*make dynamic RadioButton*/
    private void cRadioButton(int tagNum){
        String text = myAnswerList.get(tagNum).getQueAns();
        Log.i(Variables.Tag,"radioButton text: "+text);
        RadioButton rb = new RadioButton(PollActivity.this);
        rb.setTypeface(San);
        rb.setTag(tagNum);
        rb.setTextColor(PollActivity.this.getResources().getColor(R.color.PrimaryText));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 10, 0, 10);
        lp.gravity= Gravity.RIGHT | Gravity.CENTER_VERTICAL;
//        rb.setPadding(10, 10, 10, 10);
        rb.setLineSpacing(30,1);
        rb.setTypeface(San);
        rb.setText(text);
        rGroup.addView(rb,lp);
        Log.i(Variables.Tag,"rb made id: "+rb.getId());
    }// end cRadioButton()

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
    public void getResult(Object result, Object result2) throws Exception {
        myQuestion = (Object_Vote) result2;
        txtQuestion.setText(myQuestion.getQueAns());

        myAnswerList = (ArrayList<Object_Vote>) result;
        for (int i=0;i<myAnswerList.size();i++){
            cRadioButton(i);
        }
        isSendAnswer = true;
    }// end getResult()

    @Override
    public void getResult(Object result) throws Exception {
        Toast.makeText(this, "نظر شما با موفقیت ثبت شد.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void getError(String ErrorCodeTitle) throws Exception {
        Toast.makeText(this, ErrorCodeTitle, Toast.LENGTH_SHORT).show();
    }

}// end class
