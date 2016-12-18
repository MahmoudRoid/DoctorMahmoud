package ir.unicoce.doctorMahmoud.AsyncTasks;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.orm.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.unicoce.doctorMahmoud.Classes.URLS;
import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.Database.db_chat;
import ir.unicoce.doctorMahmoud.Interface.IWebservice;
import ir.unicoce.doctorMahmoud.Interface.IWebserviceByTag;
import ir.unicoce.doctorMahmoud.Objects.Object_Chat;
import ir.unicoce.doctorMahmoud.Objects.Object_Message;
import ir.unicoce.doctorMahmoud.Objects.Object_Vote;
import ir.unicoce.doctorMahmoud.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by mohad syetem on 11/29/2016.
 */

public class GetAllUserChatMessages extends AsyncTask<Void,Void,String> {

    public ArrayList<Object_Chat> myList = new ArrayList<>();
    public Context context;
    private IWebserviceByTag delegate = null;
    private SweetAlertDialog pDialog;
    public String url;
    private String UserName,Tag;

    public GetAllUserChatMessages(Context context, IWebserviceByTag delegate, String UserName,String Tag){
        this.context    = context;
        this.delegate   = delegate;
        this.UserName   = UserName;
        this.Tag = Tag ;

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        this.url= URLS.GetUserChat;
    }// end GetData()

    @Override
    protected void onPreExecute() {
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("در حال دریافت اطلاعات");
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
                        .add("userName1",UserName)
                        .add("userName2",Variables.Domain)
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

            // delete  chat tabale
            try {
                List<db_chat> list = Select
                        .from(db_chat.class)
                        .list();

              if(list.size()>0){
                  db_chat.deleteAll(db_chat.class);
              }
            } // end try
            catch (Exception e){e.printStackTrace();}

            try {
                JSONObject jsonObject=new JSONObject(result);
                int Type=jsonObject.getInt("Status");
                if(Type==1){
                    JSONArray jsonArray = jsonObject.getJSONArray("Data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                        String Sender   = jsonObject2.getString("Sender");
                        String Content  = jsonObject2.getString("Content");
                        boolean isMe = false;

                        if(Sender.equals(UserName)){
                            // chate man ast
                            isMe = true ;
                        }
                        else if(!Sender.equals(UserName)){
                            // chate doctor ast
                            isMe = false ;
                        }

                        Object_Chat objectChat = new Object_Chat(Content,isMe);
                        myList.add(objectChat);

                        // save to database
                        db_chat dbChat = new db_chat(Content,isMe);
                        dbChat.save();
                    }

                    delegate.getResult(myList,Tag);
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
