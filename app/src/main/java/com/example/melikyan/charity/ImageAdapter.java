package com.example.melikyan.charity;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by melikyan on 08.04.2018.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    ArrayList<Bitmap> bits;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.images);
        }
    }
    public ImageAdapter(ArrayList<Bitmap> bits){
        this.bits=bits;
    }

    @Override
    public int getItemCount() {
        return bits.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.image.setImageBitmap(bits.get(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_recycle,parent,false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }
}
