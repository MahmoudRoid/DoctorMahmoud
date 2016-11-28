package ir.unicoce.doctorMahmoud.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import ir.unicoce.doctorMahmoud.AsyncTasks.GetData;
import ir.unicoce.doctorMahmoud.Classes.Internet;
import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.Database.db_details;
import ir.unicoce.doctorMahmoud.Helper.Object_Data;
import ir.unicoce.doctorMahmoud.Interface.IWebservice;
import ir.unicoce.doctorMahmoud.R;

public class AboutUsActivity extends AppCompatActivity implements IWebservice {

    private ArrayList<Object_Data> objectDataArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        init();

    }

    private void init() {

        try {
            db_details db = Select.from(db_details.class).where(Condition.prop("parent_Id").eq(Variables.getAboutUs)).first();

            if(db== null || db.getContent().equals("")){
                // get data from service
                if(Internet.isNetworkAvailable(this)){
                    // call service
                    GetData getdata = new GetData(this,this,Variables.getAboutUs);
                    getdata.execute();
                }
            }
            else {
                showDetails(db.getTitle(),db.getContent(),db.getImageUrl());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showDetails(String title,String content,String imageUrl){

    }

    @Override
    public void getResult(Object result) throws Exception {
        objectDataArrayList = (ArrayList<Object_Data>) result;
        showDetails(objectDataArrayList.get(0).getTitle(),
                    objectDataArrayList.get(0).getContent(),
                    objectDataArrayList.get(0).getImageUrl());
    }

    @Override
    public void getError(String ErrorCodeTitle) throws Exception {
        Toast.makeText(this, "Problem", Toast.LENGTH_SHORT).show();
    }
}
