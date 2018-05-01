package com.example.melikyan.charity.StartActivities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.melikyan.charity.FirebaseManager;
import com.example.melikyan.charity.MainApplication.AnnoucmentFragment;
import com.example.melikyan.charity.MainApplication.ApplicationActivity;
import com.example.melikyan.charity.R;
import com.example.melikyan.charity.StartActivities.LoginActivity;
import com.example.melikyan.charity.UserInfo;
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
        }else{
            FirebaseManager.GetUserInfo(user);
            FirebaseManager.GettingData(this,this);
        }

    }




}



