package com.example.melikyan.charity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AnnoucmentsActivity extends AppCompatActivity {
    private FragmentTransaction fTrans;
    private ProfileFragment profFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annoucments);
        profFragment=new ProfileFragment();
    }


    public void SeeProf(View view) {
        ImageView profileim=findViewById(R.id.imageView3);
        profileim.setBackgroundColor(Color.rgb(255,100,0));
        fTrans = getFragmentManager().beginTransaction();
        fTrans.add(R.id.FragmentContainer,profFragment);
        fTrans.commit();
    }

    public void AddAnnounc(View view) {
    }

    public void AllAnnouc(View view) {
    }
}
