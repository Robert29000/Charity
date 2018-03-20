package com.example.melikyan.charity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.melikyan.charity.MainApplication.AnnoucmentFragment;
import com.example.melikyan.charity.MainApplication.ApplicationActivity;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    StorageReference storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Query myRef= FirebaseDatabase.getInstance().getReference().child("Annoucments").limitToFirst(10);
        storage= FirebaseStorage.getInstance().getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postData:dataSnapshot.getChildren()){
                    AnnoucmentFragment.users.add(postData.getValue(UsersAnnotations.class));
                    int counter=AnnoucmentFragment.users.size();
                    AnnoucmentFragment.users.get(counter-1).uid=postData.getKey();
                }
                for(int i=0;i<AnnoucmentFragment.users.size();i++) {
                    final int counter=i;
                    StorageReference ref=storage.child("images/" + AnnoucmentFragment.users.get(i).uid + "/" + "image-0");
                    try {
                        final File localFile = File.createTempFile("Images", "jpg");
                        ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                AnnoucmentFragment.users.get(counter).bitmap= BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                if(counter==AnnoucmentFragment.users.size()-1){
                                    Intent intent=new Intent(MainActivity.this,ApplicationActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}



