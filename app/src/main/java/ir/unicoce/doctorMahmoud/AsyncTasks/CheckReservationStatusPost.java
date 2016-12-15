package ir.unicoce.doctorMahmoud.AsyncTasks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.unicoce.doctorMahmoud.BroadcastReceiver.AlarmNotification;
import ir.unicoce.doctorMahmoud.Classes.URLS;
import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.Interface.ITest;
import ir.unicoce.doctorMahmoud.Interface.IWebservice2;
import ir.unicoce.doctorMahmoud.R;
import ir.unicoce.doctorMahmoud.Service.ReservationService;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by soheil syetem on 12/12/2016.
 */

public class CheckReservationStatusPost extends AsyncTask<Void,Void,String> {

    public Context context;
    //    private IWebservice2 delegate = null;
    public String id;
    //    SweetAlertDialog pDialog ;
    ITest iTest;
    public String Url;


    public CheckReservationStatusPost(Context context, String id){
        this.context=context;
//        this.delegate=delegate;
        this.id=id;

        iTest = new AlarmNotification();
        this.Url= URLS.PostReservationAnswer;
//        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
    }

    @Override
    protected void onPreExecute() {
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("لطفا صبر کنید");
//        pDialog.setCancelable(true);
//        pDialog.show();
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
                        .add("Id",id)
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

        if (result.equals("nothing_got")) {
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(!result.startsWith("{")){
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else {

            try {
                JSONObject jsonObject=new JSONObject(result);
                int Type=jsonObject.getInt("Status");
                if(Type==1){
                    JSONArray jsonArray = jsonObject.getJSONArray("Data");
                    JSONObject jsonObject2 = jsonArray.getJSONObject(0);

                    // reject request status= 2
                    // verify request status = 3

                    int requestStatus = jsonObject2.optInt("Status");
                    String requestResult = jsonObject2.optString("Description","");
                    if(requestStatus == 2  || requestStatus ==3)   showMessage(requestStatus,requestResult);


                }
                else {
//                    delegate.getError("error");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void showMessage(final int requestStatus, String message){

        String title ="";
        if(requestStatus==2) title = "نوبت شما رد شد";
        if(requestStatus==3) title = "نوبت شما ثبت گردید";
        // show dialog message
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.setTitleText(title);
        dialog.setContentText(message);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setConfirmText("باشه");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                if(requestStatus == 2){
                    // rejected
                    sDialog
                            .setTitleText(":(")
                            .setContentText("میتوانید مجددا درخواست نوبت دهی نمایید")
                            .setConfirmText("باشه")
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                }
              else if(requestStatus == 3){
                    // rejected
                    sDialog
                            .setTitleText("منتظر شما هستیم")
                            .setContentText("")
                            .setConfirmText("باشه")
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                }
            }
        });
        dialog.show();

        // cancel alarm
        cancelAlarm();
    }

    private void cancelAlarm() {
        context.stopService(new Intent(context,ReservationService.class));
        iTest.cancelAlarm();
    }
}

