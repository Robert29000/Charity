package com.example.melikyan.charity.MainApplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.melikyan.charity.ImageAdapter;
import com.example.melikyan.charity.MainActivity;
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
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    final private ArrayList<Bitmap> bitmaps=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_of_annoucment);
        int position=getIntent().getIntExtra("POSITION",34);
        UsersAnnotations users=AnnoucmentFragment.users.get(position);
        bitmaps.add(users.bitmap);
        ProgressBar bar=findViewById(R.id.progressbar);
        bar.setMax(users.moneyneeded);
        bar.setProgress(users.moneyincome);
        TextView name=findViewById(R.id.name_of_ann);
        name.setText(users.name);
        TextView text=findViewById(R.id.text_of_ann);
        text.setText(users.text);
        final TextView moneyprogress=findViewById(R.id.mnprogress);
        moneyprogress.setText(users.moneyincome+"/"+users.moneyneeded);
        final ProgressBar waitingBar=findViewById(R.id.waitingbar);
        waitingBar.setVisibility(View.VISIBLE);
        final StorageReference storage=FirebaseStorage.getInstance().getReference();
        if(users.imagescount==1){
            mAdapter=new ImageAdapter(bitmaps);
            mRecyclerView.setAdapter(mAdapter);
        }else{

        }
    }

    public void Donate(View view) {

    }

    class Helper{
        boolean success;
    }
}
