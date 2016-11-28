package ir.unicoce.doctorMahmoud.AsyncTasks;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.unicoce.doctorMahmoud.Classes.URLs;
import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.Database.db_details;
import ir.unicoce.doctorMahmoud.Helper.Object_Data;
import ir.unicoce.doctorMahmoud.Interface.IWebservice;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetData extends AsyncTask<Void,Void,String> {

    public ArrayList<Object_Data> myObjectArrayList;
    public Context context;
    private IWebservice delegate = null;
    public String Url;
    public String faction;
    public String id,index;
    SweetAlertDialog pDialog;

    public GetData(Context context, IWebservice delegate,String faction){
        this.context    = context;
        this.delegate   = delegate;
        this.faction    = faction;
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        getUrl(faction);
    }

    private void getUrl(String faction){
        switch (faction){
            case Variables.getAboutUs:
                this.Url = URLs.GetItem;
                id = Variables.getAboutUs;
                break;
            case Variables.getNews:
                this.Url = URLs.GetItem;
                id = Variables.getNews;
                break;
        }
    }

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
                            .add("id",this.id)
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
//        pDialog.dismiss();

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

                // pak kardane database ha baraye rikhtane data e jadid
                try {
                    List<db_details> list = Select.from(db_details.class)
                            .where(Condition.prop("parent_Id").eq(faction),Condition.prop("favorite").eq(false)).list();

                    if(list.size()>0){
//                        db_details.deleteAll(db_details.class,"parentId = ?",faction);
//                        db_details.deleteInTx(list);
                        db_details.delete(list);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }



                JSONObject jsonObject = new JSONObject(result);
                int Type=jsonObject.getInt("Status");
                if(Type==1){
                    myObjectArrayList =  new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("Data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                        int id = jsonObject2.getInt("Id");
                        int parentId = Integer.parseInt(faction);
                        String title = jsonObject2.getString("Title");
                        String content = jsonObject2.getString("Content");
                        String imageUrl = jsonObject2.getString("FileUrl");

                        Object_Data obj = new Object_Data(id,parentId,title,content,imageUrl,false);
                        myObjectArrayList.add(obj);

                        db_details db = new db_details(id,parentId,title,content,imageUrl,false);
                        db.save();

                    }// end for

                    // Check if list is empty or not
                    if(myObjectArrayList.size()>0){
                        delegate.getResult(myObjectArrayList);
                    } else{
                        delegate.getError("problem");
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
