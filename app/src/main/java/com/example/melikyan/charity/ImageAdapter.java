package com.example.melikyan.charity;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by melikyan on 10.03.2018.
 */

public class ImageAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<Bitmap> bits;
    public ImageAdapter(Context context,ArrayList<Bitmap> objects){
        this.context=context;
        bits=objects;
    }
    @Override
    public int getCount() {
        return bits.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view= LayoutInflater.from(context).inflate(R.layout.imagefragment,null);
        ImageView image=view.findViewById(R.id.image);
        image.setImageBitmap(bits.get(position));
        container.addView(view);
        return view;
    }
}
