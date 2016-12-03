package ir.unicoce.doctorMahmoud.Activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.unicoce.doctorMahmoud.Classes.Internet;
import ir.unicoce.doctorMahmoud.R;

public class SendIdeasActivity extends AppCompatActivity
        //implements
        // Async_SendMessage.SendMessage
{

    private Toolbar toolbar;
    private Typeface San;
    private TextView txtToolbar;
    private SweetAlertDialog pDialog;
    private EditText edtNameFamily, edtPhone, edtEmail, edtTitle, edtMessage;
    private TextInputLayout til1,til2, til3, til4, til5;
    private Button btnSend;
    private CoordinatorLayout coordinatorLayout;
    private String namefamily, email, phone1, title, content;
    //private String URL= URLS.WEB_SERVICE_URL,TOKEN="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_ideas);
        define();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptMessage();
                hideSoftKeyboard(SendIdeasActivity.this, view);
            }
        });
    }// end onCreate()
    private void define(){
        San = Typeface.createFromAsset(getAssets(), "fonts/SansLight.ttf");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout_form);
        til1 = (TextInputLayout) findViewById(R.id.til1_form);
        til2 = (TextInputLayout) findViewById(R.id.til2_form);
        til3 = (TextInputLayout) findViewById(R.id.til3_form);
        til4 = (TextInputLayout) findViewById(R.id.til4_form);
        til5 = (TextInputLayout) findViewById(R.id.til5_form);
        btnSend = (Button) findViewById(R.id.btnConfirm_from);
        txtToolbar = (TextView) findViewById(R.id.txtToolbar_appbar);
        edtMessage = (EditText) findViewById(R.id.edtMessage_form);
        edtTitle = (EditText) findViewById(R.id.edtTitle_form);
        edtEmail = (EditText) findViewById(R.id.edtEmail_form);
        edtPhone = (EditText) findViewById(R.id.edtPhone_form);
        edtNameFamily = (EditText) findViewById(R.id.edtName_form);

        txtToolbar.setTypeface(San);
        btnSend.setTypeface(San);
        til1.setTypeface(San);
        til2.setTypeface(San);
        til3.setTypeface(San);
        til4.setTypeface(San);
        til5.setTypeface(San);
        edtMessage.setTypeface(San);
        edtNameFamily.setTypeface(San);
        edtTitle.setTypeface(San);
        edtEmail.setTypeface(San);
        edtPhone.setTypeface(San);

        txtToolbar.setText("انتقادات و پیشنهادات");
        ArcLoader();

    }// end define()

    private void ArcLoader(){
        pDialog= new SweetAlertDialog(SendIdeasActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("لطفا صبور باشید!");
        pDialog.setCancelable(false);
    }// end ArcLoader()

    private void Snackbar_show(String message){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message,
                Snackbar.LENGTH_INDEFINITE)
                .setAction("×", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
        snackbar.show();
        snackbar.setActionTextColor(getResources().getColor(R.color.RedDark));
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(getResources().getColor(R.color.PrimaryColor));
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(San);
        sbView.setBackgroundColor(ContextCompat.getColor(SendIdeasActivity.this, R.color.AccentColor));
        snackbar.show();
    }// end Snackbar_show()

    public static void hideSoftKeyboard(Activity activity, View view){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }// end hideSoftKeyboard()

    private boolean isEmailValid(String email){
        return email.contains("@");
    }// end isEmailValid()

    private boolean isInputValid(String data){
        if(data.length() > 5 && data.length() < 50){
            return true;
        }else{
            return false;
        }
    }// end isInputValid()

    private void attemptMessage() {
        edtNameFamily.setError(null);
        edtPhone.setError(null);
        edtEmail.setError(null);
        edtTitle.setError(null);
        edtMessage.setError(null);

        namefamily = edtNameFamily.getText().toString();
        phone1 = edtPhone.getText().toString();
        title = edtTitle.getText().toString();
        content = edtMessage.getText().toString();
        email = edtEmail.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid Name and Family.
        if (TextUtils.isEmpty(namefamily)) {
            edtNameFamily.setError(getString(R.string.error_empty));
            focusView = edtNameFamily;
            cancel = true;
        } else if (!isInputValid(namefamily)) {
            edtNameFamily.setError(getString(R.string.error_length));
            focusView = edtNameFamily;
            cancel = true;
        }

        // Check for a valid mobile phone.
        if (TextUtils.isEmpty(phone1)) {
            edtPhone.setError(getString(R.string.error_empty));
            focusView = edtPhone;
            cancel = true;
        }
        else if(phone1.length()!=11){
            edtPhone.setError("تعداد ارقام وارد شده صحیح نیست");
            focusView = edtPhone;
            cancel = true;
        }
        else if (!isInputValid(phone1)) {
            edtPhone.setError(getString(R.string.error_invalid));
            focusView = edtPhone;
            cancel = true;
        }

        // Check for a valid email.
        if (TextUtils.isEmpty(email)) {

        } else if (!isEmailValid(email)) {
            edtEmail.setError(getString(R.string.error_invalid));
            focusView = edtEmail;
            cancel = true;
        }

        // check content
        if (TextUtils.isEmpty(content)) {
            edtMessage.setError(getString(R.string.error_empty));
            focusView = edtMessage;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            SendTOServer();

        }
    }// end attemptLogin()

    private void SendTOServer(){
        if (Internet.isNetworkAvailable(this)){
            // TODO : Send data to server (implement async class)
        }else{
            Snackbar_show(getResources().getString(R.string.error_internet));
        }
    }// end SendTOServer()

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
