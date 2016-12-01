package ir.unicoce.doctorMahmoud.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ir.unicoce.doctorMahmoud.Objects.Object_Data;
import ir.unicoce.doctorMahmoud.R;

/**
 * Created by soheil syetem on 12/1/2016.
 */

public class CommonQuestionsAdapter extends RecyclerView.Adapter<CommonQuestionsAdapter.DataObjectHolder> {
    public Context context;
    public ArrayList<Object_Data> myObjectArrayList;
    Typeface San;

    public CommonQuestionsAdapter(Context context,ArrayList<Object_Data> arrayList,Typeface San){
        this.myObjectArrayList=arrayList;
        this.context=context;
        this.San = San;
    }

    public  class DataObjectHolder extends RecyclerView.ViewHolder{

        TextView tv_title;

        public DataObjectHolder(View itemView){
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.txtTitle);
            tv_title.setTypeface(San);
        }

    }

    @Override
    public CommonQuestionsAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.common_questions_item,parent,false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(CommonQuestionsAdapter.DataObjectHolder holder, int position) {
        holder.tv_title.setText(myObjectArrayList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return myObjectArrayList.size();
    }

}