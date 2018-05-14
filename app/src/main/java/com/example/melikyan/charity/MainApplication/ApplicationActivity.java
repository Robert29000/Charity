package com.example.melikyan.charity.MainApplication;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.melikyan.charity.CreatingAnnoucment.AddingAnnoucment;
import com.example.melikyan.charity.R;
import com.example.melikyan.charity.Data.UsersAnnotations;


import java.io.File;
import java.util.ArrayList;

import static com.example.melikyan.charity.R.id.toolbar;

public class ApplicationActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private Button button;
    public  ArrayList<UsersAnnotations> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
        if(savedInstanceState==null || getIntent()!=null) {
            users = getIntent().getParcelableArrayListExtra("ANNOUCMENTS");
        }else users=savedInstanceState.getParcelableArrayList("USERS");
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
                        Bundle bundle=new Bundle();
                        bundle.putParcelableArrayList("USERS",users);
                        fragment.setArguments(bundle);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("USERS",users);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        File[] files=this.getCacheDir().listFiles();
        if(files!=null) {
            for (File f : files) {
                f.delete();
            }
        }
    }
}
