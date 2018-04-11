package com.example.melikyan.charity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by melikyan on 16.03.2018.
 */

public class AnnoucAdapter extends RecyclerView.Adapter<AnnoucAdapter.ViewHolder>{
    ArrayList<UsersAnnotations> users;
    private RecyclerViewClickListener mListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView text,money;
        public ImageView image;
        public ProgressBar bar;
        private RecyclerViewClickListener mListener;

        public ViewHolder(View v,RecyclerViewClickListener listener) {
            super(v);
            text=v.findViewById(R.id.annoucname);
            money=v.findViewById(R.id.money);
            image=v.findViewById(R.id.firstimage);
            bar=v.findViewById(R.id.moneyprogress);
            mListener=listener;
            v.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            mListener.OnClick(v,getAdapterPosition());
        }
    }

    public AnnoucAdapter(ArrayList<UsersAnnotations> users,RecyclerViewClickListener listener){
        this.users=users;
        this.mListener=listener;
    }

    @Override
    public AnnoucAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.users_adapter,parent,false);
        ViewHolder vh=new ViewHolder(v,mListener);
        return vh;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UsersAnnotations user=users.get(position);
        holder.text.setText(user.name);
        holder.bar.setMax(user.moneyneeded);
        holder.bar.setProgress(user.moneyincome);
        holder.money.setText(user.moneyincome+"/"+user.moneyneeded);
        holder.image.setImageBitmap(user.bitmap);
    }

}
