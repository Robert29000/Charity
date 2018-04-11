package com.example.melikyan.charity;


import android.app.Activity;
import android.content.Context;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user==null){
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            GetUserInfo(user);
            GettingData(this,this);
        }

    }

    public static void GettingData(final Context context, final Activity activity){
        Query myRef= FirebaseDatabase.getInstance().getReference().child("Annoucments").limitToFirst(15);
        final StorageReference storage= FirebaseStorage.getInstance().getReference();
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final FirebaseUser user=mAuth.getCurrentUser();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postData:dataSnapshot.getChildren()){
                    String key=postData.getKey().substring(0,postData.getKey().lastIndexOf("-"));
                    if(!key.equals(user.getUid())) {
                        AnnoucmentFragment.users.add(postData.getValue(UsersAnnotations.class));
                        int counter = AnnoucmentFragment.users.size();
                        AnnoucmentFragment.users.get(counter - 1).uid = postData.getKey();
                    }
                }
                for(int i=0;i<AnnoucmentFragment.users.size();i++) {
                    final int counter=i;
                    StorageReference ref=storage.child("images/" + AnnoucmentFragment.users.get(i).uid + "/" + "image-0");
                    try {
                        final File localFile = File.createTempFile("Images", ".jpg");
                        ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                AnnoucmentFragment.users.get(counter).bitmap= BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                if(counter==AnnoucmentFragment.users.size()-1){
                                    Intent intent=new Intent(context,ApplicationActivity.class);
                                    context.startActivity(intent);
                                    activity.finish();
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
    public static UserInfo currentUserInfo=new UserInfo();
    public static void GetUserInfo(FirebaseUser user){
        ValueEventListener listener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    Log.d("EASY",dataSnapshot1.getValue()+"");
                    switch (dataSnapshot1.getKey()){
                        case "age": currentUserInfo.age=Integer.parseInt((String)dataSnapshot1.getValue());
                                break;
                        case "email":currentUserInfo.email=(String)dataSnapshot1.getValue();
                                break;
                        case "lastname":currentUserInfo.lastname=(String)dataSnapshot1.getValue();
                            break;
                        case "name":currentUserInfo.name=(String)dataSnapshot1.getValue();
                            break;
                        case "numberOfAnnouc":currentUserInfo.numberOfAnnouc=(long)dataSnapshot1.getValue();
                            break;
                        case "region":currentUserInfo.region=(String)dataSnapshot1.getValue();
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        DatabaseReference storage=FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        storage.addListenerForSingleValueEvent(listener);
    }

}



