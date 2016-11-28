package ir.unicoce.doctorMahmoud.AsyncTasks;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.orm.query.Condition;
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
import ir.unicoce.doctorMahmoud.Database.db_details;
import ir.unicoce.doctorMahmoud.Database.db_main;
import ir.unicoce.doctorMahmoud.Helper.Object_Data;
import ir.unicoce.doctorMahmoud.Interface.IWebservice;
import ir.unicoce.doctorMahmoud.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetData extends AsyncTask<Void,Void,String> {

    public ArrayList<Object_Data> myObjectArrayList;
    public Context context;
    private IWebservice delegate = null;
    public String WEB_SERVICE_URL;
    public String FACTION;
    private String KEY_ID;
    SweetAlertDialog pDialog;
    private boolean isFolder;

    public GetData(Context context, IWebservice delegate,String faction,boolean isFolder){
        this.context    = context;
        this.delegate   = delegate;
        this.FACTION    = faction;
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        this.isFolder = isFolder;
        getUrl();
    }// end GetData()

    private void getUrl(){
        if(isFolder){
            WEB_SERVICE_URL = URLS.GetCategory;
            KEY_ID = Variables.Id;
        }else{
            WEB_SERVICE_URL = URLS.GetItem;
            KEY_ID = Variables.catId;
        }
    }// end getUrl()

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

            for(int i=0;i<=9;i++){
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormBody.Builder()
                            .add("Token", Variables.TOKEN)
                            .add(KEY_ID,FACTION)
                            .build();
                    Request request = new Request.Builder()
                            .url(WEB_SERVICE_URL)
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
    }// end doInBackground()

    @Override
    protected void onPostExecute(String result) {
        pDialog.dismiss();
        Log.i(Variables.Tag,"res: "+result);
        if (result.equals("nothing_got")) {
            // no data to get
            try {
                delegate.getError(context.getResources().getString(R.string.error_empty_server));
            } catch (Exception e) {e.printStackTrace();}
        }
        else if(!result.startsWith("{")){
            // it has a problem completely
            try {
                delegate.getError(context.getResources().getString(R.string.error_server));
            }
            catch (Exception e) {e.printStackTrace();}
        }
        else {
            // data is okay and gotten successfully
            try {

                if(isFolder){
                    // it is folder mood
                    // parse JSON and pour it in array for future use
                    JSONObject jsonObject = new JSONObject(result);
                    int Type=jsonObject.getInt("Status");
                    if(Type==1){
                        // server said data is correct
                        myObjectArrayList =  new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            int id = jsonObject2.getInt("Id");
                            int parentId = jsonObject2.getInt("ParentId");
                            String title = jsonObject2.getString("Name");
                            String content = "";
                            String imageUrl = "";

                            Object_Data obj = new Object_Data(id,parentId,title,content,imageUrl,false);
                            myObjectArrayList.add(obj);

                            // save gotten folder information in database
                            db_main db = new db_main(
                                    id,
                                    parentId,
                                    title,
                                    imageUrl
                            );
                            db.save();

                        }// end for
                        // Check if list is empty or not
                        if(myObjectArrayList.size()>0){
                            delegate.getResult(myObjectArrayList);
                        } else{
                            delegate.getError(context.getResources().getString(R.string.error_empty_server));
                        }// end check size of gotten object list
                    } else{
                        // server said data is incorrect
                        delegate.getError(context.getResources().getString(R.string.error_invalid));
                    }
                }// end in folder mood



                else{
                    // it is object mood
                    // clear data in this FACTION except which in favorites
                    try {
                        List<db_details> list = Select
                                .from(db_details.class)
                                .where(
                                        Condition.prop("parentid").eq(FACTION)
                                        //,Condition.prop("favorite").eq(false)
                                )
                                .list();
                        Log.i(Variables.Tag,"size:" +list.size()+" and faction: "+FACTION);

                        if(list.size()>0){
//                        db_details.deleteAll(db_details.class,"parentId = ?",faction);
//                        db_details.deleteInTx(list);
//                         db_details.delete(list);
                            for(db_details s : list){
                                s.delete();
                            }

                        }
                    } // end try
                    catch (Exception e){e.printStackTrace();}
                    // pour data to objects and add them to list for sending them where it call
                    JSONObject jsonObject = new JSONObject(result);
                    int Type=jsonObject.getInt("Status");
                    if(Type==1){
                        // server said data is correct
                        myObjectArrayList =  new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            int id = jsonObject2.getInt("Id");
                            int parentId = jsonObject2.getInt("CategoryId");
                            String title = jsonObject2.getString("Title");
                            String content = jsonObject2.getString("Content");
                            String imageUrl = jsonObject2.getString("Url");

                            Object_Data obj = new Object_Data(id,parentId,title,content,imageUrl,false);
                            myObjectArrayList.add(obj);

                            // save gotten object in database
                            try {
                                Log.i(Variables.Tag,"id: "+id+" and faction: "+parentId);
                                db_details db = new db_details(
                                        id,
                                        parentId,
                                        title,
                                        content,
                                        imageUrl,
                                        false
                                );
                                db.save();
                            }catch (Exception e) { e.printStackTrace();}

                        }// end for
                        // Check if list is empty or not
                        Log.i(Variables.Tag,"myObjectArrayList size: "+myObjectArrayList.size());
                        if(myObjectArrayList.size()>0){
                            delegate.getResult(myObjectArrayList);
                        } else{
                            delegate.getError(context.getResources().getString(R.string.error_empty_server));
                        }// end check size of gotten object list
                    }else{
                        // server said data is incorrect
                        delegate.getError(context.getResources().getString(R.string.error_invalid));
                    }
                }// end in object mood


            } // end try
            catch (JSONException e) {e.printStackTrace();}
            catch (Exception e) {e.printStackTrace();}
        }// end else
    }// end onPostExecute()

}// end class
