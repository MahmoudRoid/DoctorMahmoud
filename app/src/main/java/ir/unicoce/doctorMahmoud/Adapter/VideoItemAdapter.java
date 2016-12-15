package ir.unicoce.doctorMahmoud.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.R;

/**
 * Created by soheil syetem on 12/11/2016.
 */

public class VideoItemAdapter extends RecyclerView.Adapter<VideoItemAdapter.ViewHolder> {

    List<String> titleList;
    Typeface San;
    Context mContext;

    public VideoItemAdapter(List<String> titleList, Typeface San, Context context) {
        this.titleList = titleList;
        this.San = San;
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        CircleImageView img;

        ViewHolder(View itemView) {
            super(itemView);
            txtTitle    = (TextView) itemView.findViewById(R.id.txtTitle);
            img         = (CircleImageView) itemView.findViewById(R.id.imgCircleImage);

            txtTitle.setTypeface(San);

        }// Cunstrator()

    }// end class ViewHolder{}


    @Override
    public int getItemCount() {
        return titleList.size();
    }

    @Override
    public VideoItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.row_recycleview_listdata, viewGroup, false);

        VideoItemAdapter.ViewHolder pvh = new VideoItemAdapter.ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final VideoItemAdapter.ViewHolder RowViewHolder, final int position) {
        RowViewHolder.txtTitle.setText(titleList.get(position));
        Glide.with(mContext).load("")
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.ic_launcher)
                .into(RowViewHolder.img);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}

