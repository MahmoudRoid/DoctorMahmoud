package ir.unicoce.doctorMahmoud.AsyncTasks;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
import ir.unicoce.doctorMahmoud.Objects.Object_Data;
import ir.unicoce.doctorMahmoud.Interface.IWebservice;
import ir.unicoce.doctorMahmoud.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetData extends AsyncTask<Void,Void,String> {

    private ArrayList<Object_Data> myObjectArrayList;
    private Context context;
    private IWebservice delegate = null;
    private String WEB_SERVICE_URL;
    private String FACTION;
    private String KEY_ID;
    private SweetAlertDialog pDialog;
    private boolean isFolder;
    StringBuilder stringBuilder;

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
                    // clear data in this FACTION except which in favorites
                    try {
                        List<db_main> list = Select
                                .from(db_main.class)
                                .where(
                                        Condition.prop("parentid").eq(FACTION)
                                        //,Condition.prop("favorite").eq(false)
                                )
                                .list();

                        if(list.size()>0){
                            for(db_main s : list){
                                s.delete();
                            }
                        }
                    } // end try
                    catch (Exception e){e.printStackTrace();}
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
                            String imageUrl = jsonObject2.getString("ImageUrl");

                            Object_Data obj = new Object_Data(id,parentId,title,"",imageUrl,-1,"","","",false);
                            myObjectArrayList.add(obj);

                            // save gotten folder information in database
                            db_main db = new db_main(
                                    parentId,
                                    id,
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
                                         ,Condition.prop("favorite").eq(0)
                                )
                                .list();

                        if(list.size()>0){
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

                            int id = jsonObject2.optInt("Id");
                            int parentId = jsonObject2.optInt("CategoryId",-1);
                            String title = jsonObject2.optString("Title","");
                            String content = jsonObject2.optString("Content","");
                            int seenNumber = jsonObject2.optInt("SeenNumber",0);
                            String dateCreated = jsonObject2.optString("DateCreated","");
                            String dateModified = jsonObject2.optString("DateModified","");
                            String files;
                            stringBuilder = new StringBuilder();
                            // chizhayee k too fileUrl hast
                            JSONArray jsonArray2 = jsonObject2.getJSONArray("Files");
                            for (int j = 0; j < jsonArray2.length(); j++) {
                                JSONObject jsonObject3 = jsonArray2.getJSONObject(j);

                                String url = jsonObject3.optString("Url");
                                String name = jsonObject3.optString("Name");

                                stringBuilder.append(url+","+name+",");
                            }
                            files = stringBuilder.substring(0,stringBuilder.length()-1).toString();
                            // split files
                            String[] tmp = files.split(",");
                            String imageUrl = tmp[0]; //  axe asli

                            Object_Data obj = new Object_Data(id,parentId,title,content,imageUrl
                                    ,seenNumber,dateCreated,dateModified,files,false);
                            myObjectArrayList.add(obj);

                            // save gotten object in database
                            try {
                                db_details db = new db_details(
                                        id,
                                        parentId,
                                        title,
                                        content,
                                        imageUrl,
                                        seenNumber,
                                        dateCreated,
                                        dateModified,
                                        files,
                                        false
                                );
                                db.save();

                            }catch (Exception e) { e.printStackTrace();}

                        }// end for
                        // Check if list is empty or not

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
