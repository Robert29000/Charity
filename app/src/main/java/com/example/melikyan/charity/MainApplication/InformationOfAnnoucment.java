package com.example.melikyan.charity.MainApplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.melikyan.charity.ImageAdapter;
import com.example.melikyan.charity.MainApplication.AnnoucmentFragment;
import com.example.melikyan.charity.R;
import com.example.melikyan.charity.UsersAnnotations;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class InformationOfAnnoucment extends AppCompatActivity {
    private ArrayList<Bitmap> bitmaps;
    private ImageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_of_annoucment);
        final int position=getIntent().getIntExtra("Position",-1);
        /*UsersAnnotations user= AnnoucmentFragment.users.get(position);
        TextView name=findViewById(R.id.name_of_ann);
        name.setText(user.name);
        TextView text=findViewById(R.id.text_of_ann);
        text.setText(user.text);*/
    }
}
