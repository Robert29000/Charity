package com.example.melikyan.charity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by melikyan on 16.03.2018.
 */

public class AnnoucAdapter extends ArrayAdapter {
    ArrayList<UsersAnnotations> users;
    public AnnoucAdapter(@NonNull Context context, @NonNull ArrayList<UsersAnnotations> objects) {
        super(context, R.layout.users_adapter,  objects);
        users=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UsersAnnotations user=users.get(position);
        if(convertView==null)
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.users_adapter,null);
        TextView text=convertView.findViewById(R.id.annoucname);
        text.setText(user.name);
        ProgressBar bar=convertView.findViewById(R.id.moneyprogress);
        bar.setMax(user.moneyneeded);
        bar.setProgress(user.moneyincome);
        TextView money=convertView.findViewById(R.id.money);
        money.setText(user.moneyincome+"/"+user.moneyneeded);
        ImageView image=convertView.findViewById(R.id.firstimage);
        image.setImageBitmap(user.bitmap);
        image.setImageBitmap(user.bitmap);
        return convertView;
    }
}
