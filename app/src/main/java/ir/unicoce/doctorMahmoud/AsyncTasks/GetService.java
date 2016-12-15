package ir.unicoce.doctorMahmoud.AsyncTasks;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.font.TextAttribute;
import java.io.IOException;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.unicoce.doctorMahmoud.Classes.URLS;
import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.Interface.IWebservice;
import ir.unicoce.doctorMahmoud.Interface.IWebserviceByTag;
import ir.unicoce.doctorMahmoud.Objects.Object_Service;
import ir.unicoce.doctorMahmoud.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by soheil syetem on 12/11/2016.
 */

public class GetService extends AsyncTask<Void,Void,String> {

    private ArrayList<Object_Service> myObjectArrayList;
    private Context context;
    private IWebserviceByTag delegate = null;
    private String WEB_SERVICE_URL,Tag;
    private SweetAlertDialog pDialog;


    public GetService(Context context, IWebserviceByTag delegate,String Tag) {
        this.context = context;
        this.delegate = delegate;
        this.Tag= Tag;
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);

        WEB_SERVICE_URL = URLS.GetServices;
    }// end GetData()

    @Override
    protected void onPreExecute() {
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("در حال دریافت اطلاعات");
        pDialog.setCancelable(true);
        pDialog.show();
    }// end onPreExecute()

    @Override
    protected String doInBackground(Void... voids) {
        Response response = null;
        String strResponse = "nothing_got";

        for (int i = 0; i <= 9; i++) {
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody body = new FormBody.Builder()
                        .add("Token", Variables.TOKEN)
                        .build();
                Request request = new Request.Builder()
                        .url(WEB_SERVICE_URL)
                        .post(body)
                        .build();

                response = client.newCall(request).execute();
                strResponse = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response != null) break;
        }

        return strResponse;
    }// end doInBackground()

    @Override
    protected void onPostExecute(String result) {
        pDialog.dismiss();
        if (result.equals("nothing_got")) {
            // no data to get
            try {
                delegate.getError(context.getResources().getString(R.string.error_empty_server),Tag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!result.startsWith("{")) {
            // it has a problem completely
            try {
                delegate.getError(context.getResources().getString(R.string.error_server),Tag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // data is okay and gotten successfully
            try {

                    // parse JSON and pour it in array for future use
                    JSONObject jsonObject = new JSONObject(result);
                    int Type = jsonObject.getInt("Status");
                    if (Type == 1) {
                        // server said data is correct
                        myObjectArrayList = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            int id = jsonObject2.optInt("Id");
                            int price = jsonObject2.optInt("Price");
                            String title = jsonObject2.optString("Title");
                            String content = jsonObject2.optString("Content","");
                            String imageUrl="";

                            try {
                                JSONArray jsonArray2 = jsonObject2.getJSONArray("Files");
                                imageUrl= jsonArray2.get(0).toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Object_Service obj = new Object_Service(id,title,content,price,imageUrl,false);
                            myObjectArrayList.add(obj);


                        }// end for
                        // Check if list is empty or not
                        if (myObjectArrayList.size() > 0) {
                            delegate.getResult(myObjectArrayList,Tag);
                        } else {
                            delegate.getError(context.getResources().getString(R.string.error_empty_server),Tag);
                        }// end check size of gotten object list
                    } else {
                        // server said data is incorrect
                        delegate.getError(context.getResources().getString(R.string.error_invalid),Tag);
                    }

            } // end try
            catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }// end else
    }// end onPostExecute()
}
