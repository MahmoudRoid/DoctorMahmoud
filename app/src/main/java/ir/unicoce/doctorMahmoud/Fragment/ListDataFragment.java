package ir.unicoce.doctorMahmoud.Fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import ir.unicoce.doctorMahmoud.Adapter.ItemClickSupport;
import ir.unicoce.doctorMahmoud.Adapter.RecycleViewAdapter_FolderData;
import ir.unicoce.doctorMahmoud.Adapter.RecycleViewAdapter_ObjectData;
import ir.unicoce.doctorMahmoud.AsyncTasks.GetData;
import ir.unicoce.doctorMahmoud.Classes.Internet;
import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.Database.db_details;
import ir.unicoce.doctorMahmoud.Database.db_main;
import ir.unicoce.doctorMahmoud.Helper.Object_Data;
import ir.unicoce.doctorMahmoud.Interface.IWebservice;
import ir.unicoce.doctorMahmoud.Interface.onFragmentInteractionListener2;
import ir.unicoce.doctorMahmoud.R;

public class ListDataFragment extends Fragment
        implements
        IWebservice
{

    private ViewGroup layout;

    private static final String ARG_PARAM0 = "PARENT_FACTION";
    private static final String ARG_PARAM1 = "FACTION";
    private static final String ARG_PARAM2 = "KIND";
    private static final String ARG_PARAM3 = "DEPTH_OF_FOLDERS";

    private String PARENT_FACTION;
    private String FACTION;
    private boolean isFolder;
    private int DEPTH_OF_FOLDERS;

    private RecyclerView rv;
    private FloatingActionButton fab;
    private Typeface San;
    private ArrayList<Object_Data> myList = new ArrayList<>();
    private RecycleViewAdapter_ObjectData oAdapter;
    private RecycleViewAdapter_FolderData fAdapter;

    private onFragmentInteractionListener2 mListener;

    public ListDataFragment() {}

    public static ListDataFragment newInstance(String param1, String param2) {
        ListDataFragment fragment = new ListDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        San = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SansLight.ttf");
        if (getArguments() != null) {
            PARENT_FACTION      = getArguments().getString(ARG_PARAM0);
            FACTION             = getArguments().getString(ARG_PARAM1);
            isFolder            = getArguments().getBoolean(ARG_PARAM2);
            DEPTH_OF_FOLDERS    = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (ViewGroup) inflater.inflate(R.layout.fragment_list_data, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab = (FloatingActionButton) layout.findViewById(R.id.fab);
        rv = (RecyclerView) layout.findViewById(R.id.rv);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(lm);
        setFab();
        init();
        onClickListener();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            //mParam1 = bundle.getString("title");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onFragmentInteractionListener2) {
            mListener = (onFragmentInteractionListener2) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    /*handle on views click by interface in Activity which calls the fragment*/
    public void onButtonPressed(int folderDepth,Object_Data ob) {
        if (mListener != null) {
            mListener.onFragmentInteraction(folderDepth,ob);
        }
    }// end onButtonPressed()
    // initialize data
    private void init() {
        myList.clear();
        try {
            if(isFolder){
                // if we want list of folders
                List<db_main> list = Select
                        .from(db_main.class)
                        .where(Condition.prop("parentid").eq(FACTION))
                        .list();

                if(list.size()<=0){
                    // database is empty for this FACTION than lets check Internet
                    askServer();
                } else {
                    // database has some data in this FACTION than lets load them
                    for (int i=0;i<list.size();i++){
                        myList.add(new Object_Data(
                                list.get(i).getsid(),
                                list.get(i).getparentid(),
                                list.get(i).getTitle(),
                                "",
                                list.get(i).getFolderimageurl(),
                                false)
                        );
                    }
                    // set the collected data from database to recycleView
                    refreshAdapter();
                }// end check emptiness of database
            }else{
                // if we want list of objects
                List<db_details> list = Select
                        .from(db_details.class)
                        .where(Condition.prop("parentid").eq(FACTION))
                        .list();

                if(list.size()<=0){
                    // database is empty for this FACTION than lets check Internet
                    askServer();
                } else {
                    // database has some data in this FACTION than lets load them
                    for (int i=0;i<list.size();i++){
                        myList.add(new Object_Data(
                                list.get(i).getsid(),
                                list.get(i).getparentid(),
                                list.get(i).getTitle(),
                                list.get(i).getContent(),
                                list.get(i).getImageUrl(),
                                list.get(i).isFavorite())
                        );
                    }
                    // set the collected data from database to recycleView
                    refreshAdapter();
                }// end check emptiness of database
            }

        } // end try
        catch (Exception e) { e.printStackTrace(); }

    }// end init()
    /*ask server for data*/
    private void askServer() {
        if(Internet.isNetworkAvailable(getActivity())){
            // network available than ask server for data
            GetData getdata = new GetData(getActivity(),this,FACTION,isFolder);
            getdata.execute();
        }else{
            // network isn't available than toast user a message
            Toast.makeText(getActivity(), R.string.error_nework, Toast.LENGTH_SHORT).show();
        }// end check Internet
    }
    /*set RecycleView adapter*/
    private void refreshAdapter(){
        if(isFolder){
            fAdapter = new RecycleViewAdapter_FolderData(myList,San,getActivity());
            rv.setAdapter(fAdapter);
        }else{
            oAdapter = new RecycleViewAdapter_ObjectData(myList,San,getActivity());
            rv.setAdapter(oAdapter);
        }

    }// end refreshAdapter()
    /*set situation of fab*/
    private void setFab() {
        switch (FACTION){

            case Variables.getFavorites:
                fab.setVisibility(View.INVISIBLE);
                break;

            default:
                fab.setVisibility(View.VISIBLE);
                break;

        }// end switch

    }// end setFab()
    /*onClick listener of fragment views*/
    private void onClickListener(){

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askServer();
            }
        });

        ItemClickSupport.addTo(rv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Object_Data ob = myList.get(position);
                onButtonPressed(DEPTH_OF_FOLDERS,ob);
            }// end onItemClicked()
        });

    }//  end onClickListener()

    @Override
    public void getResult(Object result) throws Exception {
        myList.clear();
        myList = (ArrayList<Object_Data>) result;
        refreshAdapter();
    }

    @Override
    public void getError(String ErrorCodeTitle) throws Exception {
        Toast.makeText(getActivity(), ErrorCodeTitle, Toast.LENGTH_SHORT).show();
    }

}// end class