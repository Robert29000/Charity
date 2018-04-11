package com.example.melikyan.charity.MainApplication;




import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;
import android.widget.ListView;


import com.example.melikyan.charity.AnnoucAdapter;
import com.example.melikyan.charity.R;
import com.example.melikyan.charity.RecyclerViewClickListener;
import com.example.melikyan.charity.UsersAnnotations;


import java.util.ArrayList;


public class AnnoucmentFragment extends Fragment  {
    TabLayout tableLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    static public ArrayList<UsersAnnotations> users=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.annoucments,container,false);
        mRecyclerView=view.findViewById(R.id.annouclists);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        tableLayout=view.findViewById(R.id.tablelayout);
        RecyclerViewClickListener listener=new RecyclerViewClickListener() {
            @Override
            public void OnClick(View view, int position) {
                Intent intent=new Intent(getContext(),InformationOfAnnoucment.class);
                intent.putExtra("POSITION",position);
                startActivity(intent);
            }
        };
        mAdapter=new AnnoucAdapter(users,listener);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }



}
