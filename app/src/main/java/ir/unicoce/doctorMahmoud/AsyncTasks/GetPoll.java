package ir.unicoce.doctorMahmoud.AsyncTasks;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.unicoce.doctorMahmoud.Classes.URLS;
import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.Interface.IWebservice2;
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

public class GetPoll extends AsyncTask<Void,Void,String> {

    public ArrayList<Object_Vote> myAnswerList = new ArrayList<>();
    private Object_Vote myQuestion;
    public Context context;
    private IWebservice2 delegate = null;
    SweetAlertDialog pDialog;
    public String url;

    public GetPoll(Context context, IWebservice2 delegate){
        this.context    = context;
        this.delegate   = delegate;

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        this.url= URLS.GetVoting;
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
                delegate.getError(context.getResources().getString(R.string.error_empty_server));
            }
            catch (Exception e) {e.printStackTrace();}
        }
        else if(!result.startsWith("{")){

            try {
                delegate.getError(context.getResources().getString(R.string.error_server));
            }
            catch (Exception e) {e.printStackTrace();}
        }

        else {

            try {
                JSONObject jsonObject=new JSONObject(result);
                int Type=jsonObject.getInt("Status");
                if(Type==1){
                    JSONArray jsonArray = jsonObject.getJSONArray("Data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                        String Id       = jsonObject2.optString("Id");
                        String ParentId = jsonObject2.optString("ParentId");
                        String QueAns   = jsonObject2.optString("QueAns");

                        if (ParentId.equals("null")){
                            /*it is question*/
                            myQuestion = new Object_Vote(Id,ParentId,QueAns);
                        }else{
                            /*it is one of the answers*/
                            myAnswerList.add(new Object_Vote(Id,ParentId,QueAns));
                        }
                    }

                    delegate.getResult(myAnswerList,myQuestion);
                }
                else {
                    // server said data is incorrect
                    delegate.getError(context.getResources().getString(R.string.error_invalid));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
