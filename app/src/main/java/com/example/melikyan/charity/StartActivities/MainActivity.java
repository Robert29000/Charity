package com.example.melikyan.charity.StartActivities;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.melikyan.charity.FirebaseManager;
import com.example.melikyan.charity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


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



