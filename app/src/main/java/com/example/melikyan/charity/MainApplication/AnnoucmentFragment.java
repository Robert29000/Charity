package com.example.melikyan.charity.MainApplication;




import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;
import android.widget.TextView;


import com.example.melikyan.charity.Adapters.AnnoucAdapter;
import com.example.melikyan.charity.FirebaseManager;
import com.example.melikyan.charity.R;
import com.example.melikyan.charity.RecyclerViewClickListener;
import com.example.melikyan.charity.Data.UsersAnnotations;


import java.util.ArrayList;


public class AnnoucmentFragment extends Fragment  {
    TabLayout tableLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<UsersAnnotations> list;
    public static boolean bottom=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.annoucments,container,false);
        list=getArguments().getParcelableArrayList("USERS");
        mRecyclerView=view.findViewById(R.id.annouclists);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        tableLayout=view.findViewById(R.id.tablelayout);
        RecyclerViewClickListener listener=new RecyclerViewClickListener() {
            @Override
            public void OnClick(View view, int position) {
                Intent intent=new Intent(getContext(),InformationOfAnnoucment.class);
                intent.putExtra("VALUE",list.get(position));
                startActivity(intent);
            }
        };
        if(list !=null) {
            mAdapter = new AnnoucAdapter(list, listener);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if(dy>0){
                            int visibleItemCount = mLayoutManager.getChildCount();
                            int totalItemCount = mLayoutManager.getItemCount();
                            int pastItemCount = mLayoutManager.findFirstVisibleItemPosition();
                            if (visibleItemCount + pastItemCount >= totalItemCount){
                                if(!bottom) {
                                    bottom=true;
                                    FirebaseManager.GettingData(list, mAdapter);
                                }
                            }

                        }
                }
            });
        }else {
            TextView text=view.findViewById(R.id.noavailableann);
            text.setVisibility(View.VISIBLE);
        }
        return view;
    }



}
