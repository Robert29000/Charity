package com.example.melikyan.charity.MainApplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.melikyan.charity.AnnoucAdapter;
import com.example.melikyan.charity.BitmapHelper;
import com.example.melikyan.charity.MainActivity;
import com.example.melikyan.charity.R;
import com.example.melikyan.charity.RecyclerViewClickListener;
import com.example.melikyan.charity.UsersAnnotations;
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
 * Created by melikyan on 20.02.2018.
 */

public class MyAnnoucFragmnet extends Fragment {
    private RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar bar;
    private static ArrayList<UsersAnnotations> myann=new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("EASY","Oncreate");
        super.onCreate(savedInstanceState);
        myann.clear();
        GetMyAnnouc();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.myannoucments,container,false);
        mRecyclerView=view.findViewById(R.id.myannouclists);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        bar=view.findViewById(R.id.waitbar);
        RecyclerViewClickListener listener=new RecyclerViewClickListener() {
            @Override
            public void OnClick(View view, int position) {

            }
        };
        mAdapter=new AnnoucAdapter(myann,listener);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    public void GetMyAnnouc(){
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final FirebaseUser user=mAuth.getCurrentUser();
        final Query myRef=FirebaseDatabase.getInstance().getReference().child("Annoucments");
        final StorageReference storage= FirebaseStorage.getInstance().getReference();
        final ValueEventListener listener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postData:dataSnapshot.getChildren()){
                    String key=postData.getKey().substring(0,postData.getKey().lastIndexOf("-"));
                    if(key.equals(user.getUid())) {
                        myann.add(postData.getValue(UsersAnnotations.class));
                        int counter = myann.size();
                        myann.get(counter - 1).uid = postData.getKey();
                    }
                    myRef.removeEventListener(this);
                }
                for(int i=0;i<myann.size();i++) {
                    final int counter=i;
                    StorageReference ref=storage.child("images/" + myann.get(i).uid+ "/" + "image-0");
                    try {
                        final File localFile = File.createTempFile("Images", ".jpg");
                        ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                int targetW = MainActivity.targetW;
                                int targetH = MainActivity.targetH;
                                BitmapFactory.Options options=new BitmapFactory.Options();
                                options.inJustDecodeBounds=true;
                                BitmapFactory.decodeFile(localFile.getAbsolutePath(),options);
                                int photoW = options.outWidth;
                                int photoH = options.outHeight;
                                int scaleFactor =Math.min(photoW/targetW,photoH/targetH);
                                options.inJustDecodeBounds=false;
                                options.inSampleSize=scaleFactor;
                                options.inPurgeable=true;
                                myann.get(counter).bitmap= BitmapHelper.modifyOrientation(BitmapFactory.decodeFile(localFile.getAbsolutePath(),options),
                                                    localFile.getAbsolutePath());
                                if(counter==myann.size()-1){
                                    bar.setVisibility(View.INVISIBLE);
                                    mAdapter.notifyDataSetChanged();
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
        };
        myRef.addListenerForSingleValueEvent(listener);
    }




}
