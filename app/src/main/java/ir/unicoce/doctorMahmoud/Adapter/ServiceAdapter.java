package ir.unicoce.doctorMahmoud.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import ir.unicoce.doctorMahmoud.Interface.IService;
import ir.unicoce.doctorMahmoud.Objects.Object_Service;
import ir.unicoce.doctorMahmoud.R;


public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    List<Object_Service> ItemsList;
    Typeface San;
    Context mContext;
    private IService delegate = null;


    public ServiceAdapter(List<Object_Service> row, Typeface San, Context context,IService delegate) {
        this.ItemsList = row;
        this.San = San;
        this.mContext = context;
        this.delegate=delegate;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout applyLinear;
        RelativeLayout mainRelative;
        TextView txtTitle,txtPrice;
        AppCompatCheckBox cb ;
        CircleImageView img;


        ViewHolder(View itemView) {
            super(itemView);
            txtTitle    = (TextView) itemView.findViewById(R.id.item_reservation_txtTitle);
            txtPrice    = (TextView) itemView.findViewById(R.id.item_reservation_price);
            img         = (CircleImageView) itemView.findViewById(R.id.item_reservation_imgCircleImage);
            cb         = (AppCompatCheckBox) itemView.findViewById(R.id.item_reservation_chechbox);

            applyLinear    = (LinearLayout) itemView.findViewById(R.id.item_reservation_apply);
            mainRelative     = (RelativeLayout) itemView.findViewById(R.id.item_reservtion_above_relative);

            txtTitle.setTypeface(San);
            txtPrice.setTypeface(San);
        }// Cunstrator()

    }// end class ViewHolder{}


    @Override
    public int getItemCount() {
        return ItemsList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_reservation, viewGroup, false);

        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder RowViewHolder, final int position) {
        RowViewHolder.txtTitle.setText(ItemsList.get(position).getTitle());
        RowViewHolder.txtPrice.setText(String.valueOf(ItemsList.get(position).getPrice()));
        Glide.with(mContext).load(ItemsList.get(position).getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.mipmap.ic_launcher)
                .into(RowViewHolder.img);

        RowViewHolder.mainRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to show activity
               delegate.starNewActivity(ItemsList.get(position).getTitle(),
                       ItemsList.get(position).getContent(),
                       ItemsList.get(position).getImageUrl());
            }
        });

        RowViewHolder.applyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open a dialog to send a description to server through reservation apply
                // sending service Id to activity
                delegate.iShowDialog(ItemsList.get(position).getId());
            }
        });

        RowViewHolder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(RowViewHolder.cb.isChecked()){
                    ItemsList.get(position).ischeked=true;
                }
                else    ItemsList.get(position).ischeked=false;
            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}

