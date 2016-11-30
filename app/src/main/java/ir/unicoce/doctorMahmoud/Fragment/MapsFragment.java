package ir.unicoce.doctorMahmoud.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ir.unicoce.doctorMahmoud.Interface.OnFragmentInteractionListener;
import ir.unicoce.doctorMahmoud.R;

public class MapsFragment extends Fragment {

    private ViewGroup layout;

    private static final String ARG_PARAM1 = "Lat";
    private static final String ARG_PARAM2 = "Lng";
    private static final String ARG_PARAM3 = "Title";

    // TODO: Rename and change types of parameters
    private Float Lat1;
    private Float Lng1;
    private String MarkerTitle = "";

    private OnFragmentInteractionListener mListener;

    private GoogleMap mMap;
    public LatLng mLocation1 =  null;

    public MapsFragment() {}

    public static MapsFragment newInstance(Float param1, Float param2, String param3) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putFloat(ARG_PARAM1, param1);
        args.putFloat(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3,param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Lat1        = getArguments().getFloat(ARG_PARAM1);
            Lng1        = getArguments().getFloat(ARG_PARAM2);
            MarkerTitle = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        layout = (ViewGroup) inflater.inflate(R.layout.fragment_map, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLocation1 = new LatLng(Lat1, Lng1);

        try {

            mMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            CameraUpdate cam= CameraUpdateFactory.newLatLngZoom(mLocation1,17);
            mMap.animateCamera(cam);

            mMap.addMarker(new MarkerOptions().position(mLocation1)
                    .title(MarkerTitle)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        } // end try
        catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            //mParam1 = bundle.getString("title");
        }
    }

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

}
