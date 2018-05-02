package com.example.melikyan.charity.MainApplication;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.melikyan.charity.Adapters.AnnoucAdapter;
import com.example.melikyan.charity.BitmapHelper;
import com.example.melikyan.charity.StartActivities.MainActivity;
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

public class MyAnnoucFragmnet extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar bar;
    private ArrayList<UsersAnnotations> myann=new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView text;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState==null) {
            super.onCreate(savedInstanceState);
            myann.clear();
            GetMyAnnouc();
        }else {
            super.onCreate(savedInstanceState);
            myann=savedInstanceState.getParcelableArrayList("MYANN");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.myannoucments,container,false);
        text=view.findViewById(R.id.noann);
        mSwipeRefreshLayout=view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED);
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
                if (myann.size()==0){
                    bar.setVisibility(View.INVISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    text.setVisibility(View.VISIBLE);

                }
                for(int i=0;i<myann.size();i++) {
                    final int counter=i;
                    StorageReference ref=storage.child("images/" + myann.get(i).uid+ "/" + "image-0");
                    try {
                        final File localFile = File.createTempFile("Images", ".jpg");
                        ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                myann.get(counter).bimapUri= localFile.getAbsolutePath();
                                if(counter==myann.size()-1){
                                    bar.setVisibility(View.INVISIBLE);
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    mAdapter.notifyDataSetChanged();
                                    text.setVisibility(View.INVISIBLE);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("MYANN",myann);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRefresh() {
        myann.clear();
        mAdapter.notifyDataSetChanged();
        GetMyAnnouc();
    }
}
