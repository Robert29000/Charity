package com.example.melikyan.charity.MainApplication;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.melikyan.charity.AnnoucAdapter;
import com.example.melikyan.charity.R;
import com.example.melikyan.charity.RecyclerViewClickListener;
import com.example.melikyan.charity.UsersAnnotations;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.ArrayList;

/**
 * Created by melikyan on 20.02.2018.
 */

public class MyAnnoucFragmnet extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar bar;
    static public ArrayList<UsersAnnotations> myann=new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Query myRef= FirebaseDatabase.getInstance().getReference().child("Annoucments");
        final StorageReference storage= FirebaseStorage.getInstance().getReference();
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final FirebaseUser user=mAuth.getCurrentUser();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postData:dataSnapshot.getChildren()){
                    String key=postData.getKey().substring(0,postData.getKey().lastIndexOf("-"));
                    if(key.equals(user.getUid())) {
                        myann.add(postData.getValue(UsersAnnotations.class));
                        int counter = myann.size();
                        myann.get(counter - 1).uid = postData.getKey();
                    }
                }
                for(int i=0;i<myann.size();i++) {
                    final int counter=i;
                    StorageReference ref=storage.child("images/" + myann.get(i).uid + "/" + "image-0");
                    try {
                        final File localFile = File.createTempFile("Images", ".jpg");
                        ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                myann.get(counter).bitmap= BitmapFactory.decodeFile(localFile.getAbsolutePath());
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
        });
    }


}
