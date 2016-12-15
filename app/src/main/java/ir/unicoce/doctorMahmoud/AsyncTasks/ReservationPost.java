package ir.unicoce.doctorMahmoud.AsyncTasks;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.unicoce.doctorMahmoud.Classes.URLS;
import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.Interface.IWebserviceByTag;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by soheil syetem on 12/12/2016.
 */

public class ReservationPost extends AsyncTask<Void,Void,String> {

    public Context context;
    private IWebserviceByTag delegate = null;
    public String serviceId,userId,description;
    SweetAlertDialog pDialog ;
    public String Url;
    public String Tag;

    public ReservationPost(Context context,IWebserviceByTag delegate,String serviceId,String userId,String description,String Tag){
        this.context=context;
        this.delegate=delegate;
        this.serviceId=serviceId;
        this.userId=userId;
        this.description=description;
        this.Tag=Tag;

        this.Url= URLS.PostReservation;
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
    }

    @Override
    protected void onPreExecute() {
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("لطفا صبر کنید");
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
                        .add("UserName",userId)
                        .add("ServiceId",serviceId)
                        .add("Description",description)
                        .build();
                Request request = new Request.Builder()
                        .url(this.Url)
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

        if (result.equals("nothing_got")) {
            try {
                delegate.getError("NoData",this.Tag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(!result.startsWith("{")){
            // moshkel dare kollan
            try {
                delegate.getError("problem",this.Tag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else {

            try {
                JSONObject jsonObject=new JSONObject(result);
                int Type=jsonObject.getInt("Status");
                if(Type==1){
                    int reservaionId = jsonObject.optInt("Data");
                    delegate.getResult(reservaionId,this.Tag);
                }
                else {
                    delegate.getError("error",this.Tag);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                try {
                    delegate.getError("error",this.Tag);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
