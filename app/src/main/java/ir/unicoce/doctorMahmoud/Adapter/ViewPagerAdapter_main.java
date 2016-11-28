package ir.unicoce.doctorMahmoud.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.R;

public class ViewPagerAdapter_main extends PagerAdapter {

    private Context context;
    private Drawable[] sttr;
    private List<String> mylist;
    private boolean isCash;

    public ViewPagerAdapter_main(Context context, List<String> myList, boolean isCash) {
        this.context = context;
        this.mylist = myList;
        this.isCash = isCash;
        Log.i(Variables.Tag,"size: "+myList.size());
        if(this.mylist.size()<4){
            this.isCash = true;
        }
        Drawable[] stt = {
                // TODO : Every app has its own sliders
                context.getResources().getDrawable(R.drawable.bs_ic_more),
                context.getResources().getDrawable(R.drawable.bs_ic_more),
                context.getResources().getDrawable(R.drawable.bs_ic_more),
                context.getResources().getDrawable(R.drawable.bs_ic_more)
        };
        sttr = stt;
    }// end Cunstractor()

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.viewpager_layout, collection, false);
        ImageView img = (ImageView) layout.findViewById(R.id.img);
        if(isCash){
            img.setImageDrawable(sttr[position]);
        }else{
            Glide.with(context)
                    .load(mylist.get(position))
                    .override(500,500)
                    .into(img);
        }

        collection.addView(layout);
        return layout;
    }// end instantiateItem()

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return sttr.length;
    }// end getCount()

}// end class

