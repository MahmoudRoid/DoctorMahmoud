package ir.unicoce.doctorMahmoud.Activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.unicoce.doctorMahmoud.Adapter.CommonQuestionsAdapter;
import ir.unicoce.doctorMahmoud.AsyncTasks.GetData;
import ir.unicoce.doctorMahmoud.Classes.Internet;
import ir.unicoce.doctorMahmoud.Classes.RecyclerItemClickListener;
import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.Database.db_details;
import ir.unicoce.doctorMahmoud.Interface.IWebservice;
import ir.unicoce.doctorMahmoud.Objects.Object_Data;
import ir.unicoce.doctorMahmoud.R;

public class CommonQuestionsActivity extends AppCompatActivity implements IWebservice {

    TextView bottomDialogTxt;
    @BindView(R.id.common_questions_recycler)
    RecyclerView mRecyclerView;

    private RecyclerView.LayoutManager mLayoutManager;
    CommonQuestionsAdapter mAdapter;
    public ArrayList<Object_Data> myObjectArrayList;
    Typeface San;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_questions);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        define();
        init();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : refresh
                if(Internet.isNetworkAvailable(CommonQuestionsActivity.this)){
                    GetData getdata = new GetData(CommonQuestionsActivity.this,CommonQuestionsActivity.this,Variables.getCommonQuestions, false);
                    getdata.execute();
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.error_internet,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void define() {
        San = Typeface.createFromAsset(getAssets(), "fonts/SansLight.ttf");
        myObjectArrayList = new ArrayList<>();
    }

    private void init() {
        try {
            List<db_details> db = Select
                    .from(db_details.class)
                    .where(Condition.prop("parentid").eq(Variables.getCommonQuestions))
                    .list();

            if (db.size() == 0 || db.get(0).getContent().equals("")) {
                // get data from service
                if (Internet.isNetworkAvailable(this)) {
                    // call service
                    GetData getdata = new GetData(this, this, Variables.getCommonQuestions, false);
                    getdata.execute();
                }
            } else {
                for(int i=0;i<db.size();i++){
                    Object_Data cs = new Object_Data(db.get(i).getsid()
                            ,db.get(i).getparentid()
                            ,db.get(i).getTitle()
                            ,db.get(i).getContent()
                            ,db.get(i).getImageUrl()
                            ,db.get(i).isFavorite());
                    myObjectArrayList.add(cs);
                }
                showList(myObjectArrayList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showContent(String title, String content) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.bottom_dialog_view, null);

        bottomDialogTxt = (TextView) customView.findViewById(R.id.custom_text_view);
        bottomDialogTxt.setText(content);

        new BottomDialog.Builder(this)
                .setCustomView(customView)
                .setTitle(title)
                .show();
    }

    @Override
    public void getResult(Object result) throws Exception {
        showList((ArrayList<Object_Data>) result);
    }

    @Override
    public void getError(String ErrorCodeTitle) throws Exception {
        Toast.makeText(this,R.string.error_server, Toast.LENGTH_SHORT).show();
    }

    public void showList(ArrayList<Object_Data> arrayList){
        this.myObjectArrayList=arrayList;


        mLayoutManager = new LinearLayoutManager(CommonQuestionsActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CommonQuestionsAdapter(CommonQuestionsActivity.this,myObjectArrayList,San);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(CommonQuestionsActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showContent(myObjectArrayList.get(position).getTitle(),myObjectArrayList.get(position).getContent());
            }
        }));
    }
}
