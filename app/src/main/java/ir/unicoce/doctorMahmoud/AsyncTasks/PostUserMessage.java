package ir.unicoce.doctorMahmoud.AsyncTasks;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.unicoce.doctorMahmoud.Classes.URLS;
import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.Interface.IWebserviceByTag;
import ir.unicoce.doctorMahmoud.Objects.Object_Chat;
import ir.unicoce.doctorMahmoud.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by mohad syetem on 11/29/2016.
 */

public class PostUserMessage extends AsyncTask<Void,Void,String> {

    public Context context;
    private IWebserviceByTag delegate = null;
    private SweetAlertDialog pDialog;
    public String url,username,message,Tag;
    private Object_Chat myOb;

    public PostUserMessage(Context context, IWebserviceByTag delegate,String message,String username,String Tag){
        this.context    = context;
        this.delegate   = delegate;
        this.message = message ;
        this.username = username;
        this.Tag =Tag;

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        this.url= URLS.AddMessage;
    }// end GetData()

    @Override
    protected void onPreExecute() {
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("در حال ارسال پیام");
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        Response response = null;
        String strResponse = "nothing_got";

        for(int i=0;i<=9;i++){
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody body = new FormBody.Builder()
                        .add("Token", Variables.TOKEN)
                        .add("Sender", username)
                        .add("Receiver",Variables.Domain)
                        .add("Message", message)
                        .add("Title","Android")
                        .build();
                Request request = new Request.Builder()
                        .url(this.url)
                        .post(body)
                        .build();

                response = client.newCall(request).execute();
                strResponse= response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(response!=null) break;
        }
        return strResponse;
    }

    @Override
    protected void onPostExecute(String result) {
        pDialog.dismiss();
        Log.i(Variables.Tag,"res: "+result);
        if (result.equals("nothing_got")) {
            try {
                delegate.getError(context.getResources().getString(R.string.error_empty_server),Tag);
            }
            catch (Exception e) {e.printStackTrace();}
        }
        else if(!result.startsWith("{")){

            try {
                delegate.getError(context.getResources().getString(R.string.error_server),Tag);
            }
            catch (Exception e) {e.printStackTrace();}
        }

        else {

            try {
                JSONObject jsonObject=new JSONObject(result);
                int Type=jsonObject.getInt("Status");
                if(Type==1){
                    delegate.getResult(message,Tag);
                }
                else {
                    // server said data is incorrect
                    delegate.getError(context.getResources().getString(R.string.error_invalid),Tag);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    * this method get all user message history with admin :
//
//    URL +/+ getUserChat
//    send:	{Token,userName1(username man),userName2(domain)}
//    get:	{sender,reciver,content,title}
//
//    * this method send user message to admin :
//
//    URL +/+ AddMessage
//    send:	{Token,Sender(username),Receiver(domain),Message,Title(android)}
}
