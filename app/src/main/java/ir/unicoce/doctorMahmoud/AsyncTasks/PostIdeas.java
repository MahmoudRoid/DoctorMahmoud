package ir.unicoce.doctorMahmoud.AsyncTasks;


import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.unicoce.doctorMahmoud.Classes.URLS;
import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.Interface.IWebservice;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostIdeas extends AsyncTask<Void,Void,String> {

    public Context context;
    private IWebservice delegate = null;
    public String name,title,content,email,phone;
    SweetAlertDialog pDialog ;
    public String Url;
    public String Tag;

    public PostIdeas(Context context,IWebservice delegate,String name,String title,String phone,String email,String content){
        this.context=context;
        this.delegate=delegate;
        this.name=name;
        this.phone=phone;
        this.title=title;
        this.email=email;
        this.content=content;

        this.Url= URLS.PostContactUs;
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
    }

    @Override
    protected void onPreExecute() {
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("در حال ارسال اطلاعات");
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
                        .add("Name",name)
                        .add("Title",title)
                        .add("Message",content)
                        .add("Phone",phone)
                        .add("Email",email)
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
                delegate.getError("NoData");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(!result.startsWith("{")){
            // moshkel dare kollan
            try {
                delegate.getError("problem");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else {

            try {
                JSONObject jsonObject=new JSONObject(result);
                int Type=jsonObject.getInt("Status");
                if(Type==1){
                    delegate.getResult("");
                }
                else {
                    delegate.getError("problem");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

