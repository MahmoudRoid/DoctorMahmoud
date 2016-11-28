package ir.unicoce.doctorMahmoud.Fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.Interface.OnFragmentInteractionListener;
import ir.unicoce.doctorMahmoud.R;


public class ListDataFragment extends Fragment {

    private ViewGroup layout;

    private static final String ARG_PARAM1 = "FACTION";
    private static final String ARG_PARAM2 = "KIND";

    private String FACTION;
    private boolean isFolder;

    private RecyclerView rv;
    private FloatingActionButton fab;
    private Typeface San;

    private OnFragmentInteractionListener mListener;

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
            FACTION     = getArguments().getString(ARG_PARAM1);
            isFolder    = getArguments().getBoolean(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        setRecycleView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            //mParam1 = bundle.getString("title");
        }
    }
    /*handle on views click by interface in Activity which calls the fragment*/
    public void onButtonPressed(int tagNumber) {
        if (mListener != null) {
            mListener.onFragmentInteraction(tagNumber);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
    /*set RecycleView adapter*/
    private void refreshAdapter(){

        //mAdapter = new RecycleViewAdapter_aboutus(rvList,San,getActivity());
        //rv.setAdapter(mAdapter);
    }// end refreshAdapter()
    /*set situation of fab*/
    private void setFab() {

        switch (FACTION){

            case Variables.getNews:
                break;

            case Variables.getMagazine:
                break;

            default:
                break;

        }// end switch

    }// end setFab()
    /*set data of RecycleView*/
    private void setRecycleView(){
        /*refresh the adapter*/
        refreshAdapter();
    }// end setRecycleView()

}// end class