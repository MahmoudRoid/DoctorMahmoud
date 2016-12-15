package ir.unicoce.doctorMahmoud.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import ir.unicoce.doctorMahmoud.Objects.Object_Data;
import ir.unicoce.doctorMahmoud.R;

public class RecycleViewAdapter_Images extends RecyclerView.Adapter<RecycleViewAdapter_Images.ViewHolder> {

    List<String> ItemsList;
    Typeface San;
    Context mContext;

    public RecycleViewAdapter_Images(List<String> row, Typeface San, Context context) {
        this.ItemsList = row;
        this.San = San;
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        ViewHolder(View itemView) {
            super(itemView);
            img         = (ImageView) itemView.findViewById(R.id.iv_row);

        }// Cunstrator()

    }// end class ViewHolder{}


    @Override
    public int getItemCount() {
        return ItemsList.size();
    }

    @Override
    public RecycleViewAdapter_Images.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.row_recycleview_adapter_image, viewGroup, false);

        RecycleViewAdapter_Images.ViewHolder pvh = new RecycleViewAdapter_Images.ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final RecycleViewAdapter_Images.ViewHolder RowViewHolder, final int position) {
        Glide.with(mContext).load(ItemsList.get(position))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.ic_launcher)
                .into(RowViewHolder.img);
    }

}


