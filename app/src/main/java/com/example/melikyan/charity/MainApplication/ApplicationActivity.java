package com.example.melikyan.charity.MainApplication;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.melikyan.charity.CreatingAnnoucment.AddingAnnoucment;
import com.example.melikyan.charity.R;
import com.example.melikyan.charity.UsersAnnotations;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import static com.example.melikyan.charity.R.id.toolbar;

public class ApplicationActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
        Toolbar mytoolbar=findViewById(toolbar);
        setSupportActionBar(mytoolbar);
        pager=findViewById(R.id.pager);
        pagerAdapter=new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        TabLayout tab=findViewById(R.id.tablelayout);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
        tab.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
        button=findViewById(R.id.addannoucment);
        button.setOnClickListener(this);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:button.setVisibility(View.VISIBLE);
                            break;
                    case 1:button.setVisibility(View.VISIBLE);
                            break;
                    case 2:button.setVisibility(View.INVISIBLE);
                            break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(this,AddingAnnoucment.class);
        startActivity(intent);
    }
    private class MyFragmentPagerAdapter extends FragmentPagerAdapter{

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment=null;
            switch (position){
                case 0:fragment=new AnnoucmentFragment();
                        AnnoucmentFragment.getAb(getSupportActionBar());
                        break;
                case 1:fragment=new MyAnnoucFragmnet();
                        break;
                case 2:fragment=new MyProfileFragment();
                        break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }


    }




}