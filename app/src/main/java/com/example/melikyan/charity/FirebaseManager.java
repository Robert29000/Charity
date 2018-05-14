package com.example.melikyan.charity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.melikyan.charity.Data.UserInfo;
import com.example.melikyan.charity.Data.UsersAnnotations;
import com.example.melikyan.charity.MainApplication.AnnoucmentFragment;
import com.example.melikyan.charity.MainApplication.ApplicationActivity;
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
import java.util.ArrayList;

/**
 * Created by melikyan on 01.05.2018.
 */

public class FirebaseManager {

    public static void GettingData(final Context context, final Activity activity){
        final ArrayList<UsersAnnotations> annotations=new ArrayList<>();
        final Query myRef= FirebaseDatabase.getInstance().getReference().child("Annoucments").limitToFirst(10);
        final StorageReference storage= FirebaseStorage.getInstance().getReference();
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final FirebaseUser user=mAuth.getCurrentUser();
        final ValueEventListener listener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null){Intent intent=new Intent(context,ApplicationActivity.class);
                    context.startActivity(intent);
                    activity.finish();
                }
                else {
                    for (DataSnapshot postData : dataSnapshot.getChildren()) {
                        String key=postData.getKey().substring(0,postData.getKey().lastIndexOf("-"));
                        if (!key.equals(user.getUid())) {
                            annotations.add(postData.getValue(UsersAnnotations.class));
                            int counter = annotations.size();
                            annotations.get(counter - 1).uid = postData.getKey();
                        }
                    }
                    allAnn=annotations.size();
                    myRef.removeEventListener(this);
                    if(annotations.size()==0){
                        Intent intent = new Intent(context, ApplicationActivity.class);
                        context.startActivity(intent);
                        activity.finish();
                    }
                    for (int i = 0; i < annotations.size(); i++) {
                        final int counter = i;
                        final StorageReference ref = storage.child("images/" + annotations.get(i).uid + "/" + "image-0");
                        try {
                            final File localFile = File.createTempFile("Images"+System.currentTimeMillis(), ".jpg");
                            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                    annotations.get(counter).bimapUri = localFile.getAbsolutePath();

                                    if (counter == annotations.size() - 1) {
                                        Intent intent = new Intent(context, ApplicationActivity.class);
                                        intent.putParcelableArrayListExtra("ANNOUCMENTS",annotations);
                                        context.startActivity(intent);
                                        activity.finish();
                                    }

                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRef.addListenerForSingleValueEvent(listener);
    }

    private static int allAnn;
    public static void GettingData(final ArrayList<UsersAnnotations> list, final RecyclerView.Adapter adapter){
            final ArrayList<UsersAnnotations> annotations = new ArrayList<>();
            final Query myRef = FirebaseDatabase.getInstance().getReference().child("Annoucments").limitToFirst(allAnn + 5);
            final StorageReference storage = FirebaseStorage.getInstance().getReference();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            final FirebaseUser user = mAuth.getCurrentUser();
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    myRef.removeEventListener(this);
                    int countAnn = 1;
                    for (DataSnapshot postData : dataSnapshot.getChildren()) {
                        String key = postData.getKey().substring(0, postData.getKey().lastIndexOf("-"));
                        if (!key.equals(user.getUid())) {
                            if (postData.getKey().equals(list.get(0).uid) || countAnn <= list.size()) {
                                countAnn += 1;
                            } else {
                                annotations.add(postData.getValue(UsersAnnotations.class));
                                int counter = annotations.size();
                                annotations.get(counter - 1).uid = postData.getKey();

                            }
                        }
                    }
                    for (int i = 0; i < annotations.size(); i++) {
                        final int counter = i;
                        final StorageReference ref = storage.child("images/" + annotations.get(i).uid + "/" + "image-0");
                        try {
                            final File localFile = File.createTempFile("Images" + System.currentTimeMillis(), ".jpg");
                            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    annotations.get(counter).bimapUri = localFile.getAbsolutePath();
                                    if (counter == annotations.size() - 1) {
                                        list.addAll(list.size() , annotations);
                                        allAnn = list.size();
                                        adapter.notifyDataSetChanged();
                                        AnnoucmentFragment.bottom=false;
                                    }

                                }
                            });
                        } catch (IOException e) {
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
                        case "age": currentUserInfo.age=(Long)dataSnapshot1.getValue();
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
