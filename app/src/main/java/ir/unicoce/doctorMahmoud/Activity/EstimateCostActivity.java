package ir.unicoce.doctorMahmoud.Activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.logging.StreamHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.unicoce.doctorMahmoud.AsyncTasks.GetEstimatCost;
import ir.unicoce.doctorMahmoud.Classes.Internet;
import ir.unicoce.doctorMahmoud.Helper.EstimateCost;
import ir.unicoce.doctorMahmoud.Interface.IWebservice;
import ir.unicoce.doctorMahmoud.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EstimateCostActivity extends AppCompatActivity
        implements
        IWebservice
{

    @BindView(R.id.estimate_cost_linear)
    LinearLayout estimateCostLinear;
    @BindView(R.id.estimate_cost_price)
    TextView estimateCostPrice;

    public ArrayList<EstimateCost> estimateCostArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate_cost);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        if(Internet.isNetworkAvailable(EstimateCostActivity.this)){
            GetEstimatCost getdata = new GetEstimatCost(EstimateCostActivity.this,EstimateCostActivity.this);
            getdata.execute();
        }
    }

    @OnClick(R.id.estimate_button)
    public void onClick() {
        // calculate the cost
        double value = 0 ;
        String result="";

        for (int i = 0; i < estimateCostLinear.getChildCount(); i++){
            View v = estimateCostLinear.getChildAt(i);
            if (v instanceof AppCompatCheckBox){
                AppCompatCheckBox cb = (AppCompatCheckBox)v;
                // isChecked should be a boolean indicating if every Checkbox should be checked or not
                if(cb.isChecked()){
                    value+= Double.parseDouble(String.valueOf(cb.getTag()));
                }
            }
        }
        result =String.valueOf(value) + " "+ "ریال";
        estimateCostPrice.setVisibility(View.VISIBLE);
        estimateCostPrice.setText(result);
    }

    private void showData(ArrayList<EstimateCost> estimateCostArrayList) {
        for (int i = 0; i < estimateCostArrayList.size(); i++) {

            AppCompatCheckBox cb = new AppCompatCheckBox(EstimateCostActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0,5, 0,0);
            lp.gravity = Gravity.CENTER_HORIZONTAL;
            cb.setGravity(Gravity.CENTER_VERTICAL);
            cb.setPadding(10, 35, 10, 8);
            cb.setLineSpacing(30,1);
            cb.setText(estimateCostArrayList.get(i).getTitle());
            cb.setTag(estimateCostArrayList.get(i).getCost());
            cb.setBackgroundColor(Color.parseColor("#ffffff"));
            estimateCostLinear.addView(cb, lp);
        }
    }

    @Override
    public void getResult(Object result) throws Exception {
        estimateCostArrayList = (ArrayList<EstimateCost>) result;
        showData(estimateCostArrayList);
    }

    @Override
    public void getError(String ErrorCodeTitle) throws Exception {
        Toast.makeText(this, "An Error Occured", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
